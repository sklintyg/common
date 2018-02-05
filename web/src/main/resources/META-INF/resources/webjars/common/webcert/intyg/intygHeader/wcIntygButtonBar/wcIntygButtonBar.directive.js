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
angular.module('common').directive('wcIntygButtonBar', [ '$rootScope',
    'common.authorityService', 'common.featureService', 'common.messageService', 'common.moduleService',
    'common.IntygViewStateService', 'common.IntygHeaderService', 'common.IntygHeaderViewState',
    'common.UserModel', 'common.IntygSend', 'common.dialogService', 'common.PatientProxy', 'common.IntygMakulera',
    'common.IntygCopyActions', 'common.IntygFornyaRequestModel', 'common.IntygCopyRequestModel', 'common.IntygErsattRequestModel',
    function($rootScope,
        authorityService, featureService, messageService, moduleService,
        CommonIntygViewState, IntygHeaderService, IntygHeaderViewState,
        UserModel, IntygSend, DialogService, PatientProxy, IntygMakulera,
        IntygCopyActions, IntygFornyaRequestModel, IntygCopyRequestModel, IntygErsattRequestModel) {
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

            // get print features
            $scope.utskrift = authorityService.isAuthorityActive({ feature: featureService.features.UTSKRIFT, intygstyp: intygType });
            $scope.arbetsgivarUtskrift = authorityService.isAuthorityActive({ feature: featureService.features.ARBETSGIVARUTSKRIFT, intygstyp: intygType });

            // Förnya feature
            $scope.fornya = authorityService.isAuthorityActive({ authority: featureService.features.FORNYA_INTYG, intygstyp: intygType });

            $scope.copyBtnTooltipText = messageService.getProperty('common.copy.tooltip');
            $scope.fornyaBtnTooltipText = messageService.getProperty('common.fornya.tooltip');
            $scope.ersattBtnTooltipText = messageService.getProperty('common.ersatt.tooltip');
            $scope.employerPrintBtnTooltipText = messageService.getProperty('common.button.save.as.pdf.mininmal.title');

            var intygType = IntygHeaderViewState.intygType; // get type from state so we dont have to wait for intyg.load
            $scope.intygType = intygType;

            $scope.showSkickaButton = function(){
                return !CommonIntygViewState.isSentIntyg() && !CommonIntygViewState.isRevoked() && !CommonIntygViewState.isReplaced();
            };

            $scope.showPrintBtn = function() {
                if ($scope.showEmployerPrintBtn()) {
                    return false;
                }
                return $scope.utskrift;
            };

            $scope.showEmployerPrintBtn = function() {
                return $scope.arbetsgivarUtskrift && !CommonIntygViewState.isRevoked();
            };

            $scope.showFornyaButton = function() {
                return $scope.fornya &&
                    !CommonIntygViewState.isRevoked() &&
                    !CommonIntygViewState.isPatientDeceased() && !CommonIntygViewState.isReplaced() && !CommonIntygViewState.isComplemented() &&
                    !(UserModel.user.parameters !== undefined && UserModel.user.parameters.inactiveUnit) &&
                    (UserModel.user.parameters === undefined || UserModel.user.parameters.copyOk);
            };

            $scope.showErsattButton = function() {
                return !CommonIntygViewState.isRevoked() && !CommonIntygViewState.isReplaced() &&
                    !CommonIntygViewState.isComplemented() &&
                    (authorityService.isAuthorityActive({ feature: featureService.features.HANTERA_INTYGSUTKAST_AVLIDEN, intygstyp: intygType }) || !CommonIntygViewState.isPatientDeceased()) &&
                    !UserModel.getIntegrationParam('inactiveUnit');
            };

            $scope.send = function() {
                var onPatientFound = function() {
                    var recipient = moduleService.getModule(intygType).defaultRecipient;
                    IntygSend.send($scope.viewState.intygModel.id, intygType, recipient,
                        intygType+'.label.send', intygType+'.label.send.body', function() {
                            // After a send request we shouldn't reload right away due to async reasons.
                            // Instead, we show an info message stating 'Intyget has skickats till mottagaren'
                            CommonIntygViewState.isIntygOnSendQueue = true;
                            angular.forEach($scope.viewState.relations, function(relation) {
                                if(relation.intygsId === $scope.viewState.intygModel.id) {
                                    relation.status = 'sent';
                                }
                            });
                        });
                };

                var onNotFoundOrError = function() {
                    // If patient couldn't be looked up in PU-service, show modal with intygstyp-specific error message.
                    var errorMsg = messageService.getProperty(intygType + '.error_could_not_send_cert_no_pu');
                    DialogService
                        .showErrorMessageDialog(errorMsg);
                };

                // INTYG-4086, we must not send the intyg if the patient cannot be found in the PU-service.
                PatientProxy.getPatient($scope.viewState.intygModel.grundData.patient.personId, onPatientFound, onNotFoundOrError,
                    onNotFoundOrError);
            };

            $scope.makulera = function(intyg) {
                var confirmationMessage = messageService.getProperty(intygType+'.label.makulera.confirmation', {
                    namn: intyg.grundData.patient.fullstandigtNamn,
                    personnummer: intyg.grundData.patient.personId
                });
                intyg.intygType = intygType;
                IntygMakulera.makulera( intyg, confirmationMessage, function() {
                    CommonIntygViewState.isIntygOnRevokeQueue = true;
                    CommonIntygViewState.intygProperties.isRevoked = true;
                    angular.forEach($scope.viewState.relations, function(relation) {
                        if(relation.intygsId === intyg.id) {
                            relation.status = 'cancelled';
                        }
                    });
                    $rootScope.$emit('ViewCertCtrl.load', intyg, CommonIntygViewState.intygProperties);
                });
            };

            $scope.fornya = function(intyg) {
                return IntygHeaderService.intygCopyAction(intyg, IntygCopyActions.fornya, IntygFornyaRequestModel.build);
            };

            $scope.copy = function(intyg) {
                return IntygHeaderService.intygCopyAction(intyg, IntygCopyActions.copy, IntygCopyRequestModel.build);
            };

            $scope.ersatt = function(intyg) {
                return IntygHeaderService.intygCopyAction(intyg, IntygCopyActions.ersatt, IntygErsattRequestModel.build);
            };

            $scope.print = function(intyg, isEmployeeCopy) {

                var onPatientFound = function(patient) {
                    if (isEmployeeCopy) {
                        DialogService.showDialog({
                            dialogId: 'print-employee-copy',
                            titleId: 'common.modal.label.employee.title',
                            templateUrl: '/app/partials/employee-print-dialog.html',
                            model: {patient: patient},
                            button1click: function (modalInstance) {
                                window.open($scope.pdfUrl + '/arbetsgivarutskrift', '_blank');
                                modalInstance.close();
                            },
                            button2click: function(modalInstance){
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
                            button1click: function (modalInstance) {
                                window.open($scope.pdfUrl, '_blank');
                                modalInstance.close();
                            },
                            button2click: function(modalInstance){
                                modalInstance.close();
                            },
                            button1text: 'common.modal.label.print.sekretessmarkerad.yes',
                            button2text: 'common.cancel',
                            bodyText: 'common.alert.sekretessmarkering.print',
                            autoClose: false
                        });
                    } else {
                        // Om patienten ej är sekretessmarkerad, skriv ut direkt.
                        window.open($scope.pdfUrl, '_blank');
                    }
                };
                var onNotFoundOrError = function() {
                    // If patient couldn't be looked up in PU-service, show modal with intygstyp-specific message.
                    var errorMsg = messageService.getProperty(intyg.typ + '.error_could_not_print_cert_no_pu');
                    DialogService
                        .showErrorMessageDialog(errorMsg);
                };

                // INTYG-4086: Before printing, we must make sure the PU-service is available
                PatientProxy.getPatient(intyg.grundData.patient.personId, onPatientFound, onNotFoundOrError, onNotFoundOrError);
            };
        }
    };
} ]);
