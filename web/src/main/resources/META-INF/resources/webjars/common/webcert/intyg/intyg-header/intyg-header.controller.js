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
    ['$scope', '$log', '$state', '$stateParams', 'common.messageService', 'common.PrintService',
    'common.IntygCopyRequestModel', 'common.IntygFornyaRequestModel', 'common.User', 'common.UserModel',
    'common.IntygService', 'common.IntygViewStateService', 'common.statService',
        function($scope, $log, $state, $stateParams, messageService, PrintService, IntygCopyRequestModel,
            IntygFornyaRequestModel, User, UserModel, IntygService, CommonViewState, statService) {
            'use strict';

            var intygType = $state.current.data.intygType;

            $scope.user = UserModel;
            $scope.intygstyp = intygType;
            $scope.copyBtnTooltipText = messageService.getProperty($scope.intygstyp+'.label.kopiera.text');
            $scope.fornyaBtnTooltipText = messageService.getProperty($scope.intygstyp+'.label.fornya.text');

            $scope.visaSkickaKnappen = function(){
                return !$scope.viewState.common.intygProperties.isSent &&
                  !$scope.viewState.common.isIntygOnSendQueue &&
                  !$scope.viewState.common.intygProperties.isRevoked &&
                  !$scope.viewState.common.isIntygOnRevokeQueue;
            };

            $scope.send = function() {
                IntygService.send($scope.viewState.intygModel.id, intygType, CommonViewState.defaultRecipient,
                        intygType+'.label.send', intygType+'.label.send.body', function() {
                        // After a send request we shouldn't reload right away due to async reasons.
                        // Instead, we show an info message stating 'Intyget has skickats till mottagaren'
                        $scope.viewState.common.isIntygOnSendQueue = true;
                });
            };

            $scope.makulera = function(cert) {
                var confirmationMessage = messageService.getProperty(intygType+'.label.makulera.confirmation', {
                    namn: cert.grundData.patient.fullstandigtNamn,
                    personnummer: cert.grundData.patient.personId
                });
                cert.intygType = intygType;
                IntygService.makulera( cert, confirmationMessage, function() {
                    $scope.viewState.common.isIntygOnRevokeQueue = true;
                });
            };

            function fornyaOrCopy (cert, intygServiceMethod, buildIntygRequestModel) {
                if (cert === undefined || cert.grundData === undefined) {
                    $log.debug('cert or cert.grundData is undefined. Aborting fornya.');
                    return;
                }
                var isOtherCareUnit = User.getValdVardenhet().id !== cert.grundData.skapadAv.vardenhet.enhetsid;
                intygServiceMethod($scope.viewState,
                    buildIntygRequestModel({
                        intygId: cert.id,
                        intygType: intygType,
                        patientPersonnummer: cert.grundData.patient.personId,
                        nyttPatientPersonnummer: $stateParams.patientId
                    }),
                    isOtherCareUnit
                );
            }

            $scope.fornya = function(cert) {
                return fornyaOrCopy(cert, IntygService.fornya, IntygFornyaRequestModel.build);
            };

            $scope.copy = function(cert) {
                return fornyaOrCopy(cert, IntygService.copy, IntygCopyRequestModel.build);
            };

            $scope.print = function(cert, isEmployeeCopy) {
                if (CommonViewState.intygProperties.isRevoked) {
                    var customHeader = cert.grundData.patient.fullstandigtNamn + ' - ' + cert.grundData.patient.personId;
                    PrintService.printWebPageWithCustomTitle(cert.id, intygType, customHeader);
                } else if (isEmployeeCopy) {
                    window.open($scope.pdfUrl + '/arbetsgivarutskrift', '_blank');
                } else {
                    window.open($scope.pdfUrl, '_blank');
                }
            };
        }
    ]
);
