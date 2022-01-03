/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcUtkastButtonBar', ['$log', '$stateParams', '$timeout', '$window', '$location',
    'common.authorityService', 'common.featureService', 'common.messageService', 'common.UtkastViewStateService',
    'common.dialogService', 'common.IntygHeaderViewState',
    'common.PatientProxy', 'common.statService', 'common.UtkastProxy', 'common.UserModel', 'common.IntygMakulera',
    'common.ResourceLinkService', 'common.IntygViewStateService',
    function($log, $stateParams, $timeout, $window, $location,
        authorityService, featureService, messageService, CommonViewState, dialogService, IntygHeaderViewState,
        PatientProxy, statService, UtkastProxy, UserModel, IntygMakulera, ResourceLinkService, IntygViewState) {
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
                $scope.copyBtnTooltipText = messageService.getProperty('common.copy.utkast.tooltip');
                $scope.deleteBtnTooltipText = messageService.getProperty('common.delete.tooltip');
                $scope.makuleraBtnTooltipText = messageService.getProperty('common.makulera.locked.tooltip');

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

                            function afterDelete() {
                                statService.refreshStat(); // Update statistics to reflect change
                                IntygViewState.deletedDraft = true;
                                if (!authorityService.isAuthorityActive({authority: 'NAVIGERING'})) {
                                    if (CommonViewState.isCreatedFromIntygInSession()) {
                                        CommonViewState.clearUtkastCreatedFrom();
                                        draftDeleteDialog.close({direct: back});
                                    } else {
                                        CommonViewState.deleted = true;
                                        CommonViewState.error.activeErrorMessageKey = 'error';
                                        draftDeleteDialog.close();
                                    }
                                } else {
                                    draftDeleteDialog.close({direct: back});
                                }
                            }

                            UtkastProxy.discardUtkast($stateParams.certificateId, CommonViewState.intyg.type,
                                $scope.viewState.draftModel.version, function() {
                                    dialogModel.acceptprogressdone = true;

                                    afterDelete();
                                }, function(error) {
                                    dialogModel.acceptprogressdone = true;
                                    if (error.errorCode === 'DATA_NOT_FOUND') { // Godtagbart, intyget var redan borta.
                                        afterDelete();
                                    } else if (error.errorCode === 'CONCURRENT_MODIFICATION') {
                                        dialogModel.showerror = true;
                                        var errorMessageId = 'common.error.discard.concurrent_modification';
                                        // In the case of concurrent modification we should have the name of the user making trouble in the message.
                                        dialogModel.errormessage =
                                            messageService.getProperty(errorMessageId, {name: error.message},
                                                errorMessageId);
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
                        button2click: function(modalInstance) {
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

                    var getPrintTarget = function() {
                        var targetIframe = featureService.isFeatureActive(featureService.features.SKRIV_UT_I_IFRAME);
                        return targetIframe ? 'printTargetIFrame' : '_blank';
                    };

                    var onPatientFound = function(patient) {
                        if (!patient.sekretessmarkering) {
                            window.open(CommonViewState.intyg.pdfUrl, getPrintTarget());
                        } else {
                            // Visa infodialog för vanlig utskrift där patienten är sekretessmarkerad.
                            dialogService.showDialog({
                                dialogId: 'print-patient-sekretessmarkerad',
                                titleId: 'common.modal.label.print.sekretessmarkerad.title.draft',
                                templateUrl: '/app/partials/sekretessmarkerad-print-dialog.html',
                                model: {patient: patient},
                                button1click: function(modalInstance) {
                                    window.open(CommonViewState.intyg.pdfUrl, getPrintTarget());
                                    modalInstance.close();
                                },
                                button2click: function(modalInstance) {
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
                    PatientProxy.getPatient($scope.viewState.draftModel.content.grundData.patient.personId,
                        onPatientFound,
                        onNotFoundOrError, onNotFoundOrError);
                };

                $scope.showPrintBtn = function() {
                    return !CommonViewState.isRevoked() &&
                        ResourceLinkService.isLinkTypeExists($scope.viewState.draftModel.links, 'SKRIV_UT_UTKAST');
                };

                $scope.showCopyBtn = function() {
                    return !CommonViewState.isRevoked() &&
                        ResourceLinkService.isLinkTypeExists($scope.viewState.draftModel.links, 'KOPIERA_UTKAST') ||
                        $scope.disabledCopyBtn(); // Button should be shown but disabled if this is true.
                };

                $scope.disabledCopyBtn = function() {
                    if (angular.isFunction($scope.viewState.getLockedDraftAlert) &&
                        $scope.viewState.getLockedDraftAlert().length > 0) {
                        return true;
                    }

                    return false;
                };

                $scope.showDeleteBtn = function() {
                    return ResourceLinkService.isLinkTypeExists($scope.viewState.draftModel.links, 'TA_BORT_UTKAST');
                };

                $scope.showMakuleraBtn = function() {
                    return !CommonViewState.isRevoked() &&
                        ResourceLinkService.isLinkTypeExists($scope.viewState.draftModel.links, 'MAKULERA_UTKAST');
                };

                $scope.copy = function() {
                    var dialogModel = {
                        acceptprogressdone: false,
                        errormessageid: 'Error',
                        showerror: false
                    };

                    var isCopied = CommonViewState.isCopied();

                    var button1text = 'common.copy';
                    var bodytext = 'common.modal.copy.body_new';

                    if (isCopied) {
                        button1text = 'common.copy.resume';
                        bodytext = 'common.modal.copy.body_go';
                    }

                    if (angular.isFunction($scope.viewState.getCopyDraftAlert)) {
                        dialogModel.infoMessage = $scope.viewState.getCopyDraftAlert();
                    }

                    dialogService.showDialog({
                        dialogId: 'confirm-draft-copy',
                        titleId: 'common.modal.copy.title',
                        templateUrl: '/app/partials/copy-dialog.html',
                        bodyTextId: bodytext,
                        button1id: 'confirm-draft-copy-button',
                        model: dialogModel,

                        button1click: function(modalInstance) {
                            if (isCopied) {
                                modalInstance.close();

                                $location.url('/' + CommonViewState.intyg.type + '/' +
                                    $scope.viewState.draftModel.content.textVersion + '/edit/' +
                                    CommonViewState.getCopyUtkastId() + '/', true);
                            } else {
                                dialogModel.acceptprogressdone = false;

                                UtkastProxy.copyUtkast(
                                    $stateParams.certificateId,
                                    CommonViewState.intyg.type,
                                    function(data) {
                                        dialogModel.acceptprogressdone = true;
                                        modalInstance.close();

                                        $location.url('/' + data.intygsTyp + '/' + data.intygTypeVersion + '/edit/' +
                                            data.intygsUtkastId + '/', true);
                                        IntygHeaderViewState.utkastCreatedFrom = $stateParams.certificateId;
                                    },
                                    function(error) {
                                        dialogModel.acceptprogressdone = true;
                                        dialogModel.showerror = true;
                                        dialogModel.errormessage = '';
                                        if (error === '') {
                                            dialogModel.errormessageid = 'common.error.cantconnect';
                                        } else {
                                            dialogModel.errormessageid =
                                                ('common.error.' + error.errorCode).toLowerCase();
                                        }
                                    }
                                );
                            }


                        },
                        button2click: function(modalInstance) {
                            modalInstance.close();
                        },
                        button1text: button1text,
                        button2text: 'common.cancel',
                        autoClose: false
                    });
                };

                $scope.makulera = function() {
                    var confirmationMessage = messageService.getProperty(
                        CommonViewState.intyg.type + '.label.makulera.confirmation', {
                            namn: CommonViewState.__utlatandeJson.content.grundData.patient.fullstandigtNamn,
                            personnummer: CommonViewState.__utlatandeJson.content.grundData.patient.personId
                        });
                    var intyg = {
                        id: $stateParams.certificateId,
                        intygType: CommonViewState.intyg.type,
                        utkast: true,
                        isLocked: CommonViewState.intyg.isLocked
                    };

                    IntygMakulera.makulera(intyg, confirmationMessage, function() {
                        CommonViewState.intyg.isRevoked = true;
                    });
                };
            }
        };
    }]);
