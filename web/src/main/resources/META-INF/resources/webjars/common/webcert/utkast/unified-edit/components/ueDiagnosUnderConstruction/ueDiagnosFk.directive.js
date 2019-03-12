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

angular.module('common').directive('ueDiagnosFk', [ '$log', '$timeout', 'common.DiagnosProxy', 'common.fmbViewState',
    'common.fmbService', 'common.srsService', 'common.ObjectHelper', 'common.MonitoringLogService',
    'common.ArendeListViewStateService', 'common.UtkastValidationService', 'common.UtkastViewStateService', 'common.AtticHelper',
    'common.DateUtilsService', 'common.DatePickerOpenService',
    function($log, $timeout, diagnosProxy, fmbViewState, fmbService, srsService, ObjectHelper, monitoringService,
        ArendeListViewState, UtkastValidationService, UtkastViewState, AtticHelper, dateUtils, DatePickerOpenService) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueDiagnosUnderConstruction/ueDiagnosFk.directive.html',
            link: function($scope) {

                AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

                // Clear attic model and destroy watch on scope destroy
                AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);

                $scope.validation = UtkastViewState.validation;

                // TODO: How to manage kompletteringar.
                $scope.hasKomplettering = function() {
                    return ArendeListViewState.hasKompletteringar($scope.config.modelProp);
                };

                $scope.$watch('model.' + $scope.config.modelProp + '[0].diagnosKodSystem', function(newVal, oldVal) {
                    if (newVal) {
                        $scope.model[$scope.config.modelProp].forEach(function(diagnos) {
                            diagnos.diagnosKod = undefined;
                            diagnos.diagnosBeskrivning = undefined;
                        });
                    }
                });

                $scope.$watch('model.'+ $scope.config.modelProp+'[0].diagnosKod', function(newVal, oldVal) {
                    // Clear diagnosArtal if diagnosKod is cleared
                    if (!newVal) {
                        $scope.model[$scope.config.modelProp][0].diagnosArtal = undefined;
                    }
                });

                $scope.$watch('model.'+ $scope.config.modelProp+'[1].diagnosKod', function(newVal, oldVal) {
                    // Clear diagnosArtal if diagnosKod is cleared
                    if (!newVal) {
                        $scope.model[$scope.config.modelProp][1].diagnosArtal = undefined;
                    }
                });

                $scope.$watch('model.'+ $scope.config.modelProp+'[2].diagnosKod', function(newVal, oldVal) {
                    // Clear diagnosArtal if diagnosKod is cleared
                    if (!newVal) {
                        $scope.model[$scope.config.modelProp][2].diagnosArtal = undefined;
                    }
                });

                $scope.$watch('model.'+ $scope.config.modelProp+'[3].diagnosKod', function(newVal, oldVal) {
                    // Clear diagnosArtal if diagnosKod is cleared
                    if (!newVal) {
                        $scope.model[$scope.config.modelProp][3].diagnosArtal = undefined;
                    }
                });

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

                $scope.getValidationErrors = function(index) {
                    return $scope.diagnosValidations[index] || undefined;
                };
            }
        };
    }]);

