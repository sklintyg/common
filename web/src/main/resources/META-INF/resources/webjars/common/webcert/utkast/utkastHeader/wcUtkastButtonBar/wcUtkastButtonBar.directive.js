/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcUtkastButtonBar', [ '$rootScope', '$log', '$stateParams', '$timeout', '$window',
    'common.authorityService', 'common.featureService', 'common.messageService', 'common.UtkastViewStateService', 'common.dialogService',
    'common.PatientProxy', 'common.statService', 'common.UtkastProxy',
    function($rootScope, $log, $stateParams, $timeout, $window,
        authorityService, featureService, messageService, CommonViewState, dialogService,
        PatientProxy, statService, UtkastProxy) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            viewState: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/utkastHeader/wcUtkastButtonBar/wcUtkastButtonBar.directive.html',
        link: function($scope) {

            $scope.CommonViewState = CommonViewState;

            $scope.printBtnTooltipText = messageService.getProperty('common.button.save.as.pdf.utkast.tooltip');
            $scope.deleteBtnTooltipText = messageService.getProperty('common.delete.tooltip');

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
                    templateUrl: '/app/partials/discard-dialog.html',
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

                var onPatientFound = function(patient) {
                    if (!patient.sekretessmarkering) {
                        window.open(CommonViewState.intyg.pdfUrl, '_self');
                    } else {
                        // Visa infodialog för vanlig utskrift där patienten är sekretessmarkerad.
                        dialogService.showDialog({
                            dialogId: 'print-patient-sekretessmarkerad',
                            titleId: 'common.modal.label.print.sekretessmarkerad.title',
                            templateUrl: '/app/partials/sekretessmarkerad-print-dialog.html',
                            model: {patient: patient},
                            button1click: function (modalInstance) {
                                window.open(CommonViewState.intyg.pdfUrl, '_self');
                                modalInstance.close();
                            },
                            button2click: function(modalInstance){
                                modalInstance.close();
                            },
                            button1text: 'common.modal.label.print.sekretessmarkerad.yes',
                            button2text: 'common.cancel',
                            autoClose: false
                        });
                    }
                };

                var onNotFoundOrError = function() {
                    // If patient couldn't be looked up in PU-service, show modal with common message.
                    var errorMsg = messageService.getProperty('common.error_could_not_print_draft_no_pu');
                    dialogService.showErrorMessageDialog(errorMsg);
                };

                // INTYG-4086: Before printing, we must make sure the PU-service is available
                PatientProxy.getPatient($scope.viewState.draftModel.content.grundData.patient.personId, onPatientFound,
                    onNotFoundOrError, onNotFoundOrError);
            };
        }
    };
} ]);
