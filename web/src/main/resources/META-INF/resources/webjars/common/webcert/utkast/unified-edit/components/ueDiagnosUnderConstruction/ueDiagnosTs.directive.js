/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

angular.module('common').directive('ueDiagnosTs', [ '$log', '$timeout', 'common.DiagnosProxy', 'common.ObjectHelper',
    'common.MonitoringLogService', 'common.UtkastValidationService', 'common.UtkastViewStateService', 'common.AtticHelper',
    function($log, $timeout, diagnosProxy, ObjectHelper, monitoringService, UtkastValidationService, UtkastViewState, AtticHelper) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueDiagnosUnderConstruction/ueDiagnosTs.directive.html',
            link: function($scope) {

                AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

                // Clear attic model and destroy watch on scope destroy
                AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);

                $scope.validation = UtkastViewState.validation;

                // TODO: denna skall bort.
                $scope.$watch('model.'+ $scope.config.modelProp, function(newVal, oldVal) {
                    clearDiagnosArtal(3, newVal);
                });

                // Add listeners to diagnosKod changes
                $scope.$watch('model.'+ $scope.config.modelProp+'[0].diagnosKod', function(newVal) {
                    clearDiagnosArtal(0, newVal);
                });

                $scope.$watch('model.'+ $scope.config.modelProp+'[1].diagnosKod', function(newVal) {
                    clearDiagnosArtal(1, newVal);
                });

                $scope.$watch('model.'+ $scope.config.modelProp+'[2].diagnosKod', function(newVal) {
                    clearDiagnosArtal(2, newVal);
                });

                $scope.$watch('model.'+ $scope.config.modelProp+'[3].diagnosKod', function(newVal) {
                    clearDiagnosArtal(3, newVal);
                });

                function clearDiagnosArtal(index, newVal) {
                    if (newVal && $scope.model[$scope.config.modelProp]) {
                        $scope.model[$scope.config.modelProp][index].diagnosArtal = undefined;
                    }
                }

                // Split validations on different rows
                $scope.$watch('validation.messagesByField', function() {
                    $scope.diagnosValidations = [];
                    angular.forEach($scope.validation.messagesByField,
                        function(validations, key) {
                            if (key.substr(0, $scope.config.modelProp.length + 1) === $scope.config.modelProp.toLowerCase() + '[') {
                                var index = parseInt(key.substr($scope.config.modelProp.length + 1, 1), 10);
                                if(typeof $scope.diagnosValidations[index] === 'undefined') {
                                    $scope.diagnosValidations[index] = [];
                                }
                                $scope.diagnosValidations[index].push(validations[0]);
                            }
                        });
                });

                // Return validation errors for the specific row (previously splitted)
                $scope.getValidationErrors = function(index) {
                    return $scope.diagnosValidations[index] || undefined;
                };
            }
        };
    }]);

