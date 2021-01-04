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
angular.module('common').directive('ueDiagnosAr', ['$log', '$timeout', 'common.DiagnosProxy', 'common.ObjectHelper',
    'common.MonitoringLogService', 'common.UtkastValidationService', 'common.UtkastViewStateService',
    'common.AtticHelper',
    function($log, $timeout, diagnosProxy, ObjectHelper, monitoringService, UtkastValidationService, UtkastViewState,
        AtticHelper) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueDiagnosAr/ueDiagnosAr.directive.html',
            link: function($scope) {

                AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

                // Clear attic model and destroy watch on scope destroy
                AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);

                // If data have been put in attic and user reloads browser, then the data is lost
                // and an empty array need to be recreated.
                if (typeof $scope.model[$scope.config.modelProp] === 'undefined') {
                    $scope.model[$scope.config.modelProp] = [];
                    addEmptyDiagnoses($scope.model[$scope.config.modelProp]);
                }

                $scope.validation = UtkastViewState.validation;

                // Add listeners to diagnosKod changes
                $scope.$watch('model.' + $scope.config.modelProp + '[0].diagnosKod', function(newVal) {
                    clearDiagnosArtal(0, newVal);
                });

                $scope.$watch('model.' + $scope.config.modelProp + '[1].diagnosKod', function(newVal) {
                    clearDiagnosArtal(1, newVal);
                });

                $scope.$watch('model.' + $scope.config.modelProp + '[2].diagnosKod', function(newVal) {
                    clearDiagnosArtal(2, newVal);
                });

                $scope.$watch('model.' + $scope.config.modelProp + '[3].diagnosKod', function(newVal) {
                    clearDiagnosArtal(3, newVal);
                });

                function clearDiagnosArtal(index, newVal) {
                    if (ObjectHelper.isEmpty(newVal) && ObjectHelper.isDefined($scope.model[$scope.config.modelProp])) {
                        $scope.model[$scope.config.modelProp][index].diagnosArtal = undefined;
                    }
                }

                function addEmptyDiagnoses(diagnosArray) {
                    for (var i = 0; diagnosArray.length < 4; i++) {
                        diagnosArray.push({
                            diagnosKodSystem: 'ICD_10_SE',
                            diagnosKod: undefined,
                            diagnosBeskrivning: undefined,
                            diagnosArtal: undefined
                        });
                    }

                    return diagnosArray;
                }

                // Split validations on different rows
                $scope.$watch('validation.messagesByField', function() {
                    $scope.diagnosValidations = [];
                    var addValidationForYear = false;
                    angular.forEach($scope.validation.messagesByField,
                        function(validations, key) {
                            if (key.substr(0, $scope.config.modelProp.length + 1) ===
                                $scope.config.modelProp.toLowerCase() + '[') {
                                var index = parseInt(key.substr($scope.config.modelProp.length + 1, 1), 10);
                                if (typeof $scope.diagnosValidations[index] === 'undefined') {
                                    $scope.diagnosValidations[index] = [];
                                }
                                $scope.diagnosValidations[index].push(validations[0]);

                                // If first row is missing data when other rows has, then add validation for year.
                                if (key === $scope.config.modelProp.toLowerCase() + '[0].diagnoskod' &&
                                    validations[0].type === 'INCORRECT_COMBINATION') {
                                    addValidationForYear = true;
                                }
                                // If diagnose component is completely missing data, then add validation for year.
                            } else if (key === $scope.config.modelProp.toLowerCase()) {
                                addValidationForYear = true;
                            }
                        });

                    // Add validation.messageByField for diagnosArtal on first index to make the ueYearPicker present a red border.
                    // Only do this if first row is missing a diagnose.
                    if (addValidationForYear &&
                        ObjectHelper.isEmpty($scope.model[$scope.config.modelProp][0].diagnosArtal)) {
                        addYearToValidation();
                    }
                });

                function addYearToValidation() {
                    var validation = [{
                        field: $scope.config.yearConfig[0].modelProp.toLowerCase()
                    }];
                    $scope.validation.messagesByField[$scope.config.yearConfig[0].modelProp.toLowerCase()] = validation;
                }

                // Return validation errors for the specific row (previously splitted)
                $scope.getValidationErrors = function(index) {
                    return $scope.diagnosValidations[index] || undefined;
                };
            }
        };
    }]);

