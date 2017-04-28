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
    ['$rootScope', '$scope', '$log', '$state', '$stateParams',
        'common.authorityService', 'common.featureService', 'common.messageService',
        'common.moduleService', 'common.IntygCopyRequestModel', 'common.IntygFornyaRequestModel', 'common.IntygErsattRequestModel', 'common.User', 'common.UserModel',
        'common.IntygSend', 'common.IntygCopyActions', 'common.IntygMakulera', 'common.IntygViewStateService',

        function($rootScope, $scope, $log, $state, $stateParams,
            authorityService, featureService, messageService, moduleService, IntygCopyRequestModel,
            IntygFornyaRequestModel, IntygErsattRequestModel, User, UserModel, IntygSend, IntygCopyActions, IntygMakulera, CommonViewState) {

            'use strict';

            var intygType = $state.current.data.intygType;
            var _intygActionDialog = null;

            $scope.user = UserModel;
            $scope.intygstyp = intygType;
            $scope.intygsnamn = moduleService.getModuleName(intygType);
            // get print features
            $scope.utskrift = authorityService.isAuthorityActive({ feature: featureService.features.UTSKRIFT, intygstyp: intygType });
            $scope.arbetsgivarUtskrift = authorityService.isAuthorityActive({ feature: featureService.features.ARBETSGIVARUTSKRIFT, intygstyp: intygType });

            $scope.copyBtnTooltipText = messageService.getProperty('common.copy.tooltip');
            $scope.fornyaBtnTooltipText = messageService.getProperty('common.fornya.tooltip');
            $scope.ersattBtnTooltipText = messageService.getProperty('common.ersatt.tooltip');
            $scope.employerPrintBtnTooltipText = messageService.getProperty('common.button.save.as.pdf.mininmal.title');

            $scope.makuleratIntyg = function(){
                return $scope.viewState.common.intygProperties.isRevoked || $scope.viewState.common.isIntygOnRevokeQueue;
            };
            $scope.isReplaced = function(){
                return  angular.isObject($scope.viewState.common.intygProperties.replacedByRelation);
            };

            $scope.isComplemented = function() {
                return angular.isObject($scope.viewState.common.intygProperties.complementedByRelation)
            }

            $scope.isPatientDeceased = function() {
                return $scope.viewState.common.intygProperties.isPatientDeceased;
            };

            $scope.isSentIntyg = function(){
                return $scope.viewState.common.intygProperties.isSent ||
                    $scope.viewState.common.isIntygOnSendQueue;
            };

            $scope.showSkickaButton = function(){
                return !$scope.isSentIntyg() && !$scope.makuleratIntyg() && !$scope.isReplaced();
            };

            $scope.showPrintBtn = function() {
                if ($scope.showEmployerPrintBtn()) {
                    return false;
                }
                return $scope.utskrift;
            };

            $scope.showEmployerPrintBtn = function() {
                return $scope.arbetsgivarUtskrift && !$scope.makuleratIntyg();
            };

            $scope.showKopieraButton = function() {
                return !$scope.makuleratIntyg() &&
                    !$scope.viewState.common.common.sekretessmarkering &&
                    !$scope.isPatientDeceased() && !$scope.isReplaced() && !$scope.isComplemented() &&
                    !($scope.user.user.parameters !== undefined && $scope.user.user.parameters.inactiveUnit) &&
                    ($scope.user.user.parameters === undefined || $scope.user.user.parameters.copyOk);
            };

            $scope.showErsattButton = function() {
                return !$scope.makuleratIntyg() && !$scope.isReplaced() &&
                        !$scope.isComplemented() &&
                    !$scope.viewState.common.common.sekretessmarkering &&
                    !$scope.isPatientDeceased() &&
                    !UserModel.getIntegrationParam('inactiveUnit') &&
                    $scope.viewState.intygModel.grundData !== undefined &&
                    User.getValdVardgivare().id === $scope.viewState.intygModel.grundData.skapadAv.vardenhet.vardgivare.vardgivarid;
            };

            $scope.showFornyaButton = function() {
                return $scope.intygstyp === 'fk7263' && $scope.showKopieraButton();
            };

            $scope.send = function() {
                IntygSend.send($scope.viewState.intygModel.id, intygType, CommonViewState.defaultRecipient,
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

            function intygCopyAction (intyg, intygServiceMethod, buildIntygRequestModel) {
                if (intyg === undefined || intyg.grundData === undefined) {
                    $log.debug('intyg or intyg.grundData is undefined. Aborting fornya.');
                    return;
                }
                var isOtherCareUnit = User.getValdVardenhet().id !== intyg.grundData.skapadAv.vardenhet.enhetsid;
                _intygActionDialog = intygServiceMethod($scope.viewState,
                    buildIntygRequestModel({
                        intygId: intyg.id,
                        intygType: intygType,
                        patientPersonnummer: intyg.grundData.patient.personId
                    }),
                    isOtherCareUnit
                );
            }

            $scope.fornya = function(intyg) {
                return intygCopyAction(intyg, IntygCopyActions.fornya, IntygFornyaRequestModel.build);
            };

            $scope.copy = function(intyg) {
                return intygCopyAction(intyg, IntygCopyActions.copy, IntygCopyRequestModel.build);
            };

            $scope.ersatt = function(intyg) {
                return intygCopyAction(intyg, IntygCopyActions.ersatt, IntygErsattRequestModel.build);
            };

            $scope.print = function(intyg, isEmployeeCopy) {
                if (isEmployeeCopy) {
                    window.open($scope.pdfUrl + '/arbetsgivarutskrift', '_blank');
                } else {
                    window.open($scope.pdfUrl, '_blank');
                }
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
