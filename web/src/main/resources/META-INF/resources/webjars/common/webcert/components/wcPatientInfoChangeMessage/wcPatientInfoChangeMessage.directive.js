/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
/**
 * Show info if patient info changed from djupintegration compared to the shown info.
 */
angular.module('common').directive('wcPatientInfoChangeMessage', [
    '$log', '$stateParams', '$rootScope', '$uibModal', 'common.PatientService', 'common.messageService',
    function($log, $stateParams, $rootScope, $uibModal, PatientService, messageService) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                intyg: '=',
                intygProperties: '=',
                isIntyg: '='
            },
            controller: function($scope) {

                function update(intyg, intygProperties){
                    $scope.patient = PatientService.getPatientDataChanges($scope.isIntyg, intyg, intygProperties);
                }

                update($scope.intyg, $scope.intygProperties);
                $scope.$on('intyg.loaded', function(event, intyg){
                    update(intyg, $scope.intygProperties);
                });

                $scope.openModal = function(key) {
                    var modalInstance = $uibModal.open({
                        templateUrl: '/web/webjars/common/webcert/components/wcPatientInfoChangeMessage/wcPatientInfoChangeMessageModal.template.html',
                        size: 'md',
                        controller: function($scope) {
                            $scope.header = messageService.getProperty(key + '.modalheader');
                            $scope.body = messageService.getProperty(key + '.modalbody');
                        }
                    });
                    //angular > 1.5 warns if promise rejection is not handled (e.g backdrop-click == rejection)
                    modalInstance.result.catch(function () {}); //jshint ignore:line
                };
            },
            templateUrl: '/web/webjars/common/webcert/components/wcPatientInfoChangeMessage/wcPatientInfoChangeMessage.directive.html'
        };
    }]);
