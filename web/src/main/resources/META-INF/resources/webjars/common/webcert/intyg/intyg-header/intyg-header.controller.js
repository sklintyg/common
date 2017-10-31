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

angular.module('common').controller('common.IntygHeader',
    ['$rootScope', '$scope', '$log', '$state', '$stateParams', 'common.authorityService', 'common.featureService',
        'common.messageService', 'common.moduleService', 'common.IntygCopyRequestModel', 'common.IntygFornyaRequestModel',
        'common.IntygErsattRequestModel', 'common.User', 'common.UserModel', 'common.IntygSend', 'common.IntygCopyActions',
        'common.IntygMakulera', 'common.IntygViewStateService', 'common.dialogService', 'common.PatientProxy',

        function($rootScope, $scope, $log, $state, $stateParams, authorityService, featureService, messageService,
            moduleService, IntygCopyRequestModel, IntygFornyaRequestModel, IntygErsattRequestModel, User, UserModel,
            IntygSend, IntygCopyActions, IntygMakulera, CommonViewState, DialogService, PatientProxy) {

            'use strict';
            $scope.createFromTemplateConfig = {
                'db': {
                    'moduleId': 'doi',
                    'name': 'dödsorsaksintyg'
                }
            };

            var intygType = $state.current.data.intygType;
            var _intygActionDialog = null;

            $scope.user = UserModel;
            $scope.intygstyp = intygType;
            $scope.intygsnamn = moduleService.getModuleName(intygType);
            // get print features
            $scope.utskrift = authorityService.isAuthorityActive({ feature: featureService.features.UTSKRIFT, intygstyp: intygType });
            $scope.arbetsgivarUtskrift = authorityService.isAuthorityActive({ feature: featureService.features.ARBETSGIVARUTSKRIFT, intygstyp: intygType });

            // Förnya feature
            $scope.fornya = authorityService.isAuthorityActive({ authority: featureService.features.FORNYA_INTYG, intygstyp: intygType });

            $scope.copyBtnTooltipText = messageService.getProperty('common.copy.tooltip');
            $scope.fornyaBtnTooltipText = messageService.getProperty('common.fornya.tooltip');
            $scope.ersattBtnTooltipText = messageService.getProperty('common.ersatt.tooltip');
            $scope.employerPrintBtnTooltipText = messageService.getProperty('common.button.save.as.pdf.mininmal.title');


            $scope.generateSentText = function () {

                var sentText = '';

                var recipientId = moduleService.getModule(intygType).defaultRecipient;

                function getRecipientNameFromId(recipientId){
                    switch (recipientId){
                        case 'FKASSA': return 'Försäkringskassan';
                        case 'TRANSP': return 'Transportstyrelsen';
                        case 'SKV': return 'Skatteverket';
                        case 'SOS': return 'Socialstyrelsen';
                    }
                }

                if($scope.viewState.common.intygProperties.isSent){
                    sentText = messageService.getProperty('common.label.status.recieved', {'recipient': getRecipientNameFromId(recipientId) });

                    if(!$scope.isPatientDeceased()) {
                        sentText += messageService.getProperty('common.label.status.recieved.available-mi');
                    }
                }
                return sentText;
            };

            $scope.isRevoked = function(){
                return $scope.viewState.common.intygProperties.isRevoked || $scope.viewState.common.isIntygOnRevokeQueue;
            };
            $scope.isReplaced = function(){
                return angular.isObject($scope.viewState.common.intygProperties.latestChildRelations) &&
                    angular.isObject($scope.viewState.common.intygProperties.latestChildRelations.replacedByIntyg);
            };

            $scope.isComplemented = function() {
                return angular.isObject($scope.viewState.common.intygProperties.latestChildRelations) &&
                    angular.isObject($scope.viewState.common.intygProperties.latestChildRelations.complementedByIntyg);
            };

            $scope.isPatientDeceased = function() {
                return $scope.viewState.common.intygProperties.isPatientDeceased;
            };

            $scope.isSentIntyg = function(){
                return $scope.viewState.common.intygProperties.isSent ||
                    $scope.viewState.common.isIntygOnSendQueue;
            };

            $scope.showSkickaButton = function(){
                return !$scope.isSentIntyg() && !$scope.isRevoked() && !$scope.isReplaced();
            };

            $scope.showPrintBtn = function() {
                if ($scope.showEmployerPrintBtn()) {
                    return false;
                }
                return $scope.utskrift;
            };

            $scope.showEmployerPrintBtn = function() {
                return $scope.arbetsgivarUtskrift && !$scope.isRevoked();
            };

            $scope.showFornyaButton = function() {
                return $scope.fornya &&
                    !$scope.isRevoked() &&
                    !$scope.isPatientDeceased() && !$scope.isReplaced() && !$scope.isComplemented() &&
                    !($scope.user.user.parameters !== undefined && $scope.user.user.parameters.inactiveUnit) &&
                    ($scope.user.user.parameters === undefined || $scope.user.user.parameters.copyOk);
            };

            $scope.showErsattButton = function() {
                return !$scope.isRevoked() && !$scope.isReplaced() &&
                    !$scope.isComplemented() &&
                    (authorityService.isAuthorityActive({ feature: featureService.features.HANTERA_INTYGSUTKAST_AVLIDEN, intygstyp: intygType }) || !$scope.isPatientDeceased()) &&
                    !UserModel.getIntegrationParam('inactiveUnit');
            };

            $scope.showCreateFromTemplate = function() {
                return $scope.createFromTemplateConfig[$scope.intygstyp] !== undefined && !$scope.isRevoked() && !$scope.isReplaced() &&
                    !$scope.isComplemented() && !UserModel.getIntegrationParam('inactiveUnit');
            };

            $scope.send = function() {
                var onPatientFound = function() {
                    var recipient = moduleService.getModule(intygType).defaultRecipient;
                    IntygSend.send($scope.viewState.intygModel.id, intygType, recipient,
                        intygType+'.label.send', intygType+'.label.send.body', function() {
                            // After a send request we shouldn't reload right away due to async reasons.
                            // Instead, we show an info message stating 'Intyget has skickats till mottagaren'
                            $scope.viewState.common.isIntygOnSendQueue = true;
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
                    $scope.viewState.common.isIntygOnRevokeQueue = true;
                    $scope.viewState.common.intygProperties.isRevoked = true;
                    angular.forEach($scope.viewState.relations, function(relation) {
                        if(relation.intygsId === intyg.id) {
                            relation.status = 'cancelled';
                        }
                    });
                    $rootScope.$emit('ViewCertCtrl.load', intyg, $scope.viewState.common.intygProperties);
                });
            };

            function intygCopyAction (intyg, intygServiceMethod, buildIntygRequestModel, newIntygType) {
                if (intyg === undefined || intyg.grundData === undefined) {
                    $log.debug('intyg or intyg.grundData is undefined. Aborting fornya.');
                    return;
                }
                var isOtherCareUnit = User.getValdVardenhet().id !== intyg.grundData.skapadAv.vardenhet.enhetsid;
                _intygActionDialog = intygServiceMethod($scope.viewState,
                    buildIntygRequestModel({
                        intygId: intyg.id,
                        intygType: intygType,
                        newIntygType: newIntygType || intygType,
                        patientPersonnummer: intyg.grundData.patient.personId
                    }),
                    isOtherCareUnit
                );
            }

            $scope.fornya = function(intyg) {
                return intygCopyAction(intyg, IntygCopyActions.fornya, IntygFornyaRequestModel.build);
            };

            $scope.createFromTemplate = function(intyg, newIntygType) {
                return intygCopyAction(intyg, IntygCopyActions.createFromTemplate, IntygFornyaRequestModel.build, $scope.createFromTemplateConfig[$scope.intygstyp].moduleId);
            };

            $scope.copy = function(intyg) {
                return intygCopyAction(intyg, IntygCopyActions.copy, IntygCopyRequestModel.build);
            };

            $scope.ersatt = function(intyg) {
                return intygCopyAction(intyg, IntygCopyActions.ersatt, IntygErsattRequestModel.build);
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

            //Potentially we are showing a copy/forny/ersatt dialog when exiting (clicked back etc)
            // - make sure it's closed properly
            $scope.$on('$destroy', function() {
                if (_intygActionDialog) {
                    _intygActionDialog.close();
                    _intygActionDialog = undefined;
                }
            });

        }
    ]
);
