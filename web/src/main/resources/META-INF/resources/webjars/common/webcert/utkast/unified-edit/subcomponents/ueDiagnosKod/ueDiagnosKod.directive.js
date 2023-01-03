/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('common').directive('ueDiagnosKod', ['$log', '$timeout', 'common.DiagnosProxy', 'common.fmbViewState',
    'common.fmbService', 'common.srsService', 'common.ObjectHelper', 'common.MonitoringLogService',
    'common.ArendeListViewStateService', 'common.UtkastValidationService', 'common.UtkastViewStateService',
    function($log, $timeout, diagnosProxy, fmbViewState, fmbService, srsService, ObjectHelper, monitoringService,
        ArendeListViewState, UtkastValidationService, UtkastViewState) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                form: '=',
                config: '=',
                model: '=',
                index: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/subcomponents/ueDiagnosKod/ueDiagnosKod.directive.html',
            link: function($scope) {

                $scope.validation = UtkastViewState.validation;

                $scope.diagnosKodLoading = false;
                $scope.diagnosKodNoResults = undefined;

                // descriptionMaxLength defaults to 250 if not set in config
                $scope.descriptionMaxLength = $scope.config.descriptionMaxLength ? $scope.config.descriptionMaxLength : 250;

                function isShortPsykiskDiagnos(kod) {
                    // Från kravsidan:
                    // För LUSE,  LISU, LUAE NA och LUAE FS måste psykiska diagnoser anges med minst fyra positioner
                    // Med psykisk diagnos avses alla diagnoser som börjar med Z73 eller med F (dvs. som tillhör F-kapitlet i ICD-10)
                    var psykiskDiagnos = kod.substr(0, 3) === 'Z73' || kod.substr(0, 1) === 'F';
                    var shortPsykiskDiagnos = kod.length < 4 && psykiskDiagnos;

                    return shortPsykiskDiagnos;
                }

                $scope.getDiagnoseCodes = function(codeSystem, val) {
                    return diagnosProxy.searchByCode(codeSystem, val)
                        .then(function(response) {
                            if (response && response.data && response.data.resultat === 'OK') {
                                var result = response.data.diagnoser.map(function(item) {
                                    return {
                                        value: item.kod,
                                        beskrivning: item.beskrivning,
                                        label: item.kod + ' | ' + item.beskrivning,
                                        shortPsykiskDiagnos: isShortPsykiskDiagnos(item.kod)
                                    };
                                });
                                if (result.length > 0) {
                                    result[0].moreResults = response.data.moreResults;
                                }
                                return result;
                            } else {
                                return [];
                            }
                        }, function(response) {
                            $log.debug('Error searching diagnose code');
                            $log.debug(response);
                            return [];
                        });
                };

                $scope.searchDiagnoseByDescription = function(codeSystem, val) {
                    return diagnosProxy.searchByDescription(codeSystem, val)
                        .then(function(response) {
                            if (response && response.data && response.data.resultat === 'OK') {
                                var result = response.data.diagnoser.map(function(item) {
                                    return {
                                        value: item.kod,
                                        beskrivning: item.beskrivning,
                                        label: item.kod + ' | ' + item.beskrivning,
                                        shortPsykiskDiagnos: isShortPsykiskDiagnos(item.kod)
                                    };
                                });
                                if (result.length > 0) {
                                    result[0].moreResults = response.data.moreResults;
                                }
                                return result;
                            } else {
                                return [];
                            }
                        }, function(response) {
                            $log.debug('Error searching diagnose code');
                            $log.debug(response);
                            return [];
                        });
                };

                $scope.onDiagnoseCodeSelect = function($item) {
                    var diagnoseModel = $scope.model[$scope.config.modelProp][$scope.index];
                    if (isShortPsykiskDiagnos($item.value)) {
                        diagnoseModel.diagnosKod = undefined;
                        return;
                    }
                    diagnoseModel.diagnosBeskrivning = $item.beskrivning;
                    $scope.diagnosKodNoResults = undefined;
                    $scope.form.$setDirty();
                };

                $scope.onDiagnoseDescriptionSelect = function($item) {
                    var diagnoseModel = $scope.model[$scope.config.modelProp][$scope.index];
                    if (isShortPsykiskDiagnos($item.value)) {
                        diagnoseModel.diagnosKod = undefined;
                        diagnoseModel.diagnosBeskrivning = undefined;
                        return;
                    }
                    diagnoseModel.diagnosKod = $item.value;
                    diagnoseModel.diagnosBeskrivning = $item.beskrivning;
                    $scope.diagnosKodNoResults = undefined;
                    $scope.form.$setDirty();
                };

                $scope.onDiagnoseCodeChanged = function() {
                    if (!$scope.form['diagnoseCode' + $scope.index].$viewValue) {
                        $scope.model[$scope.config.modelProp][$scope.index].diagnosBeskrivning = undefined;
                    }
                };

                $scope.onDiagnoseDescriptionChanged = function() {
                    if (!$scope.model[$scope.config.modelProp][$scope.index].diagnosBeskrivning) {
                        $scope.model[$scope.config.modelProp][$scope.index].diagnosKod = undefined;
                    }
                };

                $scope.onBlurDiagnoseCodeField = function() {
                    if (!$scope.model[$scope.config.modelProp][$scope.index].diagnosKod) {
                        // Clear diagnoskod in all cases when not selected from the typeahead
                        $scope.form['diagnoseCode' + $scope.index].$setViewValue();
                        $scope.form['diagnoseCode' + $scope.index].$render();
                    }
                };

                $scope.hasValidationError = function(field) {
                    if (field === 'diagnosbeskrivning') {
                        field = 'diagnoskod';
                    }
                    var modelPropLowerCase = $scope.config.modelProp.toLowerCase();
                    return $scope.validation.messagesByField &&
                        (!!$scope.validation.messagesByField[modelPropLowerCase + '[' + $scope.index + '].' + field] ||
                            !!$scope.validation.messagesByField[modelPropLowerCase + '[' + $scope.index + '].row'] ||
                            // If no diagnose has been entered the first row should be marked with validation-error
                            ($scope.index === 0 && !!$scope.validation.messagesByField[modelPropLowerCase]));
                };

                $scope.validate = function() {
                    //The timeout here allows the model to be updated (via typeahead selection) before sending it for validation
                    $timeout(function() {
                        UtkastValidationService.validate($scope.model);
                    }, 100);
                };
            }
        };
    }]);
