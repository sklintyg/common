/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('common').controller('common.UtkastHeader',
    ['$scope', '$log', '$stateParams', '$timeout', '$window',
        'common.messageService', 'common.moduleService', 'common.UtkastProxy', 'common.statService',
        'common.dialogService', 'common.UtkastViewStateService', 'common.authorityService', 'common.UtkastService', 'common.PatientProxy',
        function($scope, $log, $stateParams, $timeout, $window,
            messageService, moduleService, UtkastProxy, statService, dialogService, CommonViewState, authorityService, UtkastService, PatientProxy) {
            'use strict';

            $scope.intygsnamn = moduleService.getModuleName(CommonViewState.intyg.type);

            /**
             * Toggle header part ('Dölj meny'-knapp)
             */
            $scope.toggleHeader = function() {
                CommonViewState.toggleCollapsedHeader();
            };

            /**
             * Action to discard the certificate draft and return to WebCert again.
             */
            $scope.discard = function() {
                var bodyText = 'När du raderar utkastet tas det bort från Webcert.';
                var dialogModel = {
                    acceptprogressdone: false,
                    errormessageid: 'Error',
                    showerror: false
                };

                var draftDeleteDialog = {};
                draftDeleteDialog = dialogService.showDialog({
                    dialogId: 'confirm-draft-delete',
                    titleId: 'common.modal.label.discard_draft',
                    bodyText: bodyText,
                    button1id: 'confirm-draft-delete-button',
                    model: dialogModel,

                    button1click: function() {
                        $log.debug('delete draft ');
                        dialogModel.acceptprogressdone = false;
                        var back = function() {
                            // IE9 infinite digest workaround
                            $timeout(function() {
                                $window.history.back();
                            });
                        };
                        UtkastProxy.discardUtkast($stateParams.certificateId, CommonViewState.intyg.type, $scope.viewState.draftModel.version, function() {
                            dialogModel.acceptprogressdone = true;
                            statService.refreshStat(); // Update statistics to reflect change

                            if (!authorityService.isAuthorityActive({authority: 'NAVIGERING'})) {
                                CommonViewState.deleted = true;
                                CommonViewState.error.activeErrorMessageKey = 'error';
                                draftDeleteDialog.close();
                            } else {
                                draftDeleteDialog.close({direct:back});
                            }
                        }, function(error) {
                            dialogModel.acceptprogressdone = true;
                            if (error.errorCode === 'DATA_NOT_FOUND') { // Godtagbart, intyget var redan borta.
                                statService.refreshStat(); // Update statistics to reflect change
                                draftDeleteDialog.close({direct:back});
                            } else if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                                dialogModel.showerror = true;
                                var errorMessageId = 'common.error.discard.concurrent_modification';
                                // In the case of concurrent modification we should have the name of the user making trouble in the message.
                                dialogModel.errormessage = messageService.getProperty(errorMessageId, {name: error.message}, errorMessageId);
                                dialogModel.errormessageid = '';
                            } else {
                                dialogModel.showerror = true;
                                dialogModel.errormessage = '';
                                if (error === '') {
                                    dialogModel.errormessageid = 'common.error.cantconnect';
                                } else {
                                    dialogModel.errormessageid =
                                        ('common.error.' + error.errorCode).toLowerCase();
                                }
                            }
                        });
                    },
                    button2click: function(modalInstance){
                        modalInstance.close();
                    },
                    button1text: 'common.delete',
                    button2text: 'common.cancel',
                    autoClose: false
                });
            };

            /**
             * Print draft.
             */
            $scope.print = function() {

                var onNotFoundOrError = function() {
                    // If patient couldn't be looked up in PU-service, show modal with common message.
                    var errorMsg = messageService.getProperty('common.error_could_not_print_draft_no_pu');
                    dialogService.showErrorMessageDialog(errorMsg);
                };

                // INTYG-4086: Before printing, we must make sure the PU-service is available
                PatientProxy.getPatient($scope.viewState.draftModel.content.grundData.patient.personId, function() {
                    window.open($scope.pdfUrl, '_self');
                }, onNotFoundOrError, onNotFoundOrError);
            };


            $window.onbeforeunload = function(event) {
                if ($scope.certForm.$dirty) {
                    // Trigger a save now. If the user responds with "Leave the page" we may not have time to save
                    // before the page is closed. We could use an ajax request with async:false this will force the
                    // browser to "hang" the page until the request is complete. But using async:false is deprecated
                    // and will be removed in future browsers.
                    UtkastService.save();
                    var message = 'Om du väljer "Lämna sidan" kan ändringar försvinna. Om du väljer "Stanna kvar på sidan" autosparas ändringarna.';
                    if (typeof event === 'undefined') {
                        event = $window.event;
                    }
                    if (event) {
                        event.returnValue = message;
                    }
                    return message;
                }
            };

            $scope.$on('$destroy', function() {
                $window.onbeforeunload = null;
            });
        }
    ]
);
