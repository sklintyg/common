/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
    '$log', '$stateParams', '$rootScope', 'common.PatientService',
    function($log, $stateParams, $rootScope, PatientService) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                intyg: '=',
                intygProperties: '=',
                context: '@'
            },
            controller: function($scope) {

                function update(intyg, intygProperties){
                    $scope.patient = PatientService.getPatientDataChanges($scope.context, intyg, intygProperties);
                }

                update($scope.intyg, $scope.intygProperties);
                $scope.$on('intyg.loaded', function(event, intyg){
                    update(intyg, $scope.intygProperties);
                });
            },
            templateUrl: '/web/webjars/common/webcert/components/wcPatientInfoChangeMessage/wcPatientInfoChangeMessage.directive.html'
        };
    }]);
