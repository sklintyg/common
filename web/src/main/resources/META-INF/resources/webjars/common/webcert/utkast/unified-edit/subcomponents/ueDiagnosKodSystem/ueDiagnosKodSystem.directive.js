/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

angular.module('common').directive('ueDiagnosKodSystem', [ '$timeout', 'common.UtkastValidationService',
    function($timeout, UtkastValidationService) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            model: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/subcomponents/ueDiagnosKodSystem/ueDiagnosKodSystem.directive.html',
        link: function($scope) {

            var diagnosViewState = $scope.diagnosViewState = {
                diagnosKodSystem: $scope.config.defaultKodSystem
            };

            $scope.onChangeKodverk = function() {
                resetDiagnoses();
            };

            $scope.$watch('model.' + $scope.config.modelProp + '[0].diagnosKodSystem', function(newVal) {
                //För att uppdatera diagnosKodSystem från Attic
                if (newVal !== diagnosViewState.diagnosKodSystem) {
                    diagnosViewState.diagnosKodSystem = newVal;
                }
            });

            function resetDiagnoses() {
                if ($scope.model[$scope.config.modelProp]) {
                    $scope.model[$scope.config.modelProp].forEach(function(diagnos) {
                        diagnos.diagnosKodSystem = diagnosViewState.diagnosKodSystem;
                        diagnos.diagnosKod = undefined;
                        diagnos.diagnosBeskrivning = undefined;
                    });
                }
            }

            $scope.validate = function() {
                //The timeout here allows the model to be updated (via typeahead selection) before sending it for validation
                $timeout(function() {
                    UtkastValidationService.validate($scope.model);
                }, 100);
            };

        }
    };

}]);
