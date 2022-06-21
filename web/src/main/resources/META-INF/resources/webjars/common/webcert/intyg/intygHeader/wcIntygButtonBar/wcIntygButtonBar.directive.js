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
angular.module('common').directive('wcIntygButtonBar', ['$rootScope', '$timeout',
    'common.authorityService', 'common.featureService', 'common.messageService', 'common.moduleService',
    'common.IntygViewStateService', 'common.IntygHeaderService', 'common.IntygHeaderViewState',
    'common.UserModel', 'common.IntygSend', 'common.dialogService', 'common.PatientProxy', 'common.IntygMakulera',
    'common.IntygCopyActions', 'common.IntygFornyaRequestModel', 'common.IntygCopyRequestModel',
    'common.IntygErsattRequestModel',
    'common.ArendeListViewStateService',
    'common.ResourceLinkService',
    function($rootScope, $timeout,
        authorityService, featureService, messageService, moduleService,
        CommonIntygViewState, IntygHeaderService, IntygHeaderViewState,
        UserModel, IntygSend, DialogService, PatientProxy, IntygMakulera,
        IntygCopyActions, IntygFornyaRequestModel, IntygCopyRequestModel, IntygErsattRequestModel,
        ArendeListViewStateService, ResourceLinkService) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                viewState: '='
            },
            templateUrl: '/web/webjars/common/webcert/intyg/intygHeader/wcIntygButtonBar/wcIntygButtonBar.directive.html',
            link: function($scope) {
                $scope.IntygHeaderService = IntygHeaderService;
                $scope.CommonIntygViewState = CommonIntygViewState;

                var intygType = IntygHeaderViewState.intygType; // get type from state so we dont have to wait for intyg.load

                // get print features
                $scope.utskrift = authorityService.isAuthorityActive(
                    {feature: featureService.features.UTSKRIFT, intygstyp: intygType});
                $scope.arbetsgivarUtskrift = authorityService.isAuthorityActive(
                    {feature: featureService.features.ARBETSGIVARUTSKRIFT, intygstyp: intygType});

                $scope.copyBtnTooltipText = messageService.getProperty('common.copy.tooltip');
                $scope.fornyaBtnTooltipText = messageService.getProperty('common.fornya.tooltip');
                $scope.ersattBtnTooltipText = messageService.getProperty('common.ersatt.tooltip');
                $scope.makuleraBtnTooltipText = messageService.getProperty('common.makulera.tooltip');
                $scope.normalPrintBtnTooltipText =
                    messageService.getProperty('common.button.save.as.pdf.intyg.tooltip');
                $scope.selectPrintBtnTooltipText =
                    messageService.getProperty('common.button.save.as.pdf.intyg.select.tooltip');
                $scope.normalPrintBtnTooltipText =
                    messageService.getProperty('common.button.save.as.pdf.intyg.tooltip');
                $scope.sendBtnTooltipText = messageService.getProperty('common.send.tooltip', {
                    'recipient': messageService.getProperty(
                        'common.recipient.' + moduleService.getModule(intygType).defaultRecipient.toLowerCase())
                });

                $scope.ersattButtonDisabled = false;

                function _updateErsattButton() {
                    if (ArendeListViewStateService.getUnhandledKompletteringCount() > 0) {
                        $scope.ersattButtonDisabled = true;
                        $scope.ersattBtnTooltipText =
                            messageService.getProperty('common.ersatt.unhandledkomplettering.tooltip');
                    } else {
                        $scope.ersattButtonDisabled = false;
                        $scope.ersattBtnTooltipText =
                            messageService.getProperty('common.ersatt.tooltip');

                    }
                }

                _updateErsattButton();
                $scope.$on('arenden.updated', _updateErsattButton);

                $scope.intygType = intygType;

                $scope.showValjMottagareButton = function() {
                    return IntygHeaderViewState.intygLoaded && !CommonIntygViewState.isRevoked() &&
                        ResourceLinkService.isLinkTypeExists(CommonIntygViewState.getLinks(), 'GODKANNA_MOTTAGARE');
                };

                $scope.showSkickaButton = function() {
                    return IntygHeaderViewState.intygLoaded && !CommonIntygViewState.isSentIntyg() &&
                        ResourceLinkService.isLinkTypeExists(CommonIntygViewState.getLinks(), 'SKICKA_INTYG') &&
                        !CommonIntygViewState.isRevoked() && !CommonIntygViewState.isReplaced();
                };

                $scope.showPrintBtn = function() {
                    if ($scope.showEmployerPrintBtn()) {
                        return false;
                    }
                    return IntygHeaderViewState.intygLoaded &&
                        ResourceLinkService.isLinkTypeExists(CommonIntygViewState.getLinks(), 'SKRIV_UT_INTYG') &&
                        !CommonIntygViewState.isRevoked();
                };

                $scope.showEmployerPrintBtn = function() {
                    // Default true, if showEmployerPrintBtn exists use that value
                    // function added for special case for smittskydd that shouldn't allow to print minimal. INTYG-
                    if (angular.isFunction($scope.viewState.showEmployerPrintBtn) &&
                        !$scope.viewState.showEmployerPrintBtn()) {
                        return false;
                    }

                    return IntygHeaderViewState.intygLoaded && $scope.arbetsgivarUtskrift &&
                        ResourceLinkService.isLinkTypeExists(CommonIntygViewState.getLinks(), 'SKRIV_UT_INTYG') &&
                        !CommonIntygViewState.isRevoked();
                };

                $scope.showFornyaButton = function() {
                    return IntygHeaderViewState.intygLoaded &&
                        ResourceLinkService.isLinkTypeExists(CommonIntygViewState.getLinks(), 'FORNYA_INTYG') &&
                        !CommonIntygViewState.isRevoked() &&
                        !CommonIntygViewState.isReplaced() &&
                        !CommonIntygViewState.isComplementedByIntyg();
                };

                $scope.showErsattButton = function() {
                    return IntygHeaderViewState.intygLoaded &&
                        ResourceLinkService.isLinkTypeExists(CommonIntygViewState.getLinks(), 'ERSATT_INTYG') &&
                        !CommonIntygViewState.isRevoked() && !CommonIntygViewState.isReplaced() &&
                        !CommonIntygViewState.isComplementedByIntyg();
                };

                $scope.showMakuleraButton = function() {
                    return IntygHeaderViewState.intygLoaded && !CommonIntygViewState.isRevoked() &&
                        ResourceLinkService.isLinkTypeExists(CommonIntygViewState.getLinks(), 'MAKULERA_INTYG');
                };

                $scope.send = function() {
                    var onPatientFound = function() {
                        var recipient = moduleService.getModule(intygType).defaultRecipient;
                        var sendContentModel = $scope.viewState.getSendContent &&
                            $scope.viewState.getSendContent(intygType);

                        IntygSend.send($scope.viewState.intygModel, $scope.viewState.intygModel.id, intygType,
                            recipient,
                            sendContentModel, function() {
                                // After a send request we shouldn't reload right away due to async reasons.
                                CommonIntygViewState.intygProperties.isSent = true;
                                CommonIntygViewState.intygProperties.sentTimestamp = new Date();
                                angular.forEach($scope.viewState.relations, function(relation) {
                                    if (relation.intygsId === $scope.viewState.intygModel.id) {
                                        relation.status = 'sent';
                                    }
                                });
                                $rootScope.$broadcast('intygstatus.updated');
                            });
                    };

                    var onNotFoundOrError = function() {
                        // If patient couldn't be looked up in PU-service, show modal with intygstyp-specific error message.
                        var errorMsg = messageService.getProperty(intygType + '.error_could_not_send_cert_no_pu');
                        DialogService
                            .showErrorMessageDialog(errorMsg);
                    };

                    // INTYG-4086, we must not send the intyg if the patient cannot be found in the PU-service.
                    PatientProxy.getPatient($scope.viewState.intygModel.grundData.patient.personId, onPatientFound,
                        onNotFoundOrError,
                        onNotFoundOrError);
                };

                $scope.makulera = function(intyg) {
                    var confirmationMessage = messageService.getProperty(intygType + '.label.makulera.confirmation', {
                        namn: intyg.grundData.patient.fullstandigtNamn,
                        personnummer: intyg.grundData.patient.personId
                    });
                    intyg.intygType = intygType;
                    IntygMakulera.makulera(intyg, confirmationMessage, function() {
                        CommonIntygViewState.isIntygOnRevokeQueue = true;
                        CommonIntygViewState.intygProperties.isRevoked = true;
                        CommonIntygViewState.intygProperties.revokedTimestamp = new Date();
                        angular.forEach($scope.viewState.relations, function(relation) {
                            if (relation.intygsId === intyg.id) {
                                relation.status = 'cancelled';
                            }
                        });
                        $rootScope.$emit('ViewCertCtrl.load', intyg, CommonIntygViewState.intygProperties);
                        $rootScope.$broadcast('intygstatus.updated');
                    });
                };

                $scope.fornya = function(intyg) {
                    return IntygHeaderService.intygCopyAction(intyg, IntygCopyActions.fornya,
                        IntygFornyaRequestModel.build);
                };

                $scope.copy = function(intyg) {
                    return IntygHeaderService.intygCopyAction(intyg, IntygCopyActions.copy,
                        IntygCopyRequestModel.build);
                };

                $scope.ersatt = function(intyg) {
                    return IntygHeaderService.intygCopyAction(intyg, IntygCopyActions.ersatt,
                        IntygErsattRequestModel.build);
                };

                $scope.print = function(intyg, isEmployeeCopy) {

                    var getPrintTarget = function() {
                        var targetIframe = featureService.isFeatureActive(featureService.features.SKRIV_UT_I_IFRAME);
                        return targetIframe ? 'printTargetIFrame' : '_blank';
                    };

                    var printOrDownload = function() {
                        var isIEBrowser = /(?:Trident\/\d+)|(?:MSIE \d+)/.test(window.navigator.userAgent);
                        var urlExtension = isEmployeeCopy ? '/arbetsgivarutskrift' : '';
                        if (isIEBrowser) {
                            window.open(CommonIntygViewState.intygProperties.pdfUrl + urlExtension, getPrintTarget());
                        } else {
                            var iframe = document.getElementById('printTargetIFrame');
                            iframe.onload = function() {
                                setTimeout(function() {
                                    iframe.focus();
                                    iframe.contentWindow.print();
                                }, 1);
                            };
                            iframe.src = CommonIntygViewState.intygProperties.pdfUrl + urlExtension;
                        }
                    };

                    var onPatientFound = function(patient) {
                        if (isEmployeeCopy) {
                            DialogService.showDialog({
                                dialogId: 'print-employee-copy',
                                titleId: 'common.modal.label.employee.title',
                                templateUrl: '/app/partials/employee-print-dialog.html',
                                model: {patient: patient},
                                button1click: function(modalInstance) {
                                    printOrDownload();
                                    modalInstance.close();
                                },
                                button2click: function(modalInstance) {
                                    modalInstance.close();
                                },
                                button1text: 'common.modal.label.employee.yes',
                                button2text: 'common.cancel',
                                bodyText: 'common.modal.label.employee.body',
                                autoClose: false
                            });
                        } else if (patient.sekretessmarkering) {
                            // Visa infodialog för vanlig utskrift där patienten är sekretessmarkerad.
                            DialogService.showDialog({
                                dialogId: 'print-patient-sekretessmarkerad',
                                titleId: 'common.modal.label.print.sekretessmarkerad.title',
                                templateUrl: '/app/partials/sekretessmarkerad-print-dialog.html',
                                model: {patient: patient},
                                button1click: function(modalInstance) {
                                    printOrDownload();
                                    modalInstance.close();
                                },
                                button2click: function(modalInstance) {
                                    modalInstance.close();
                                },
                                button1text: 'common.modal.label.print.sekretessmarkerad.yes',
                                button2text: 'common.cancel',
                                autoClose: false
                            });
                        } else {
                            printOrDownload();
                        }
                    };



                    var onNotFoundOrError = function() {
                        // If patient couldn't be looked up in PU-service, show modal with intygstyp-specific message.
                        var errorMsg = messageService.getProperty(intyg.typ + '.error_could_not_print_cert_no_pu');
                        DialogService
                            .showErrorMessageDialog(errorMsg);
                    };

                    // INTYG-4086: Before printing, we must make sure the PU-service is available
                    PatientProxy.getPatient(intyg.grundData.patient.personId, onPatientFound, onNotFoundOrError,
                        onNotFoundOrError);
                };
            }
        };
    }]);
