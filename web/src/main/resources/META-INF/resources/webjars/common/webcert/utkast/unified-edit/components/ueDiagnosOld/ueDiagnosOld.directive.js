/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('common').directive('ueDiagnosOld', [ '$log', '$timeout', 'common.DiagnosProxy', 'common.fmbViewState',
    'common.fmbService', 'common.srsService', 'common.ObjectHelper', 'common.MonitoringLogService',
    'common.ArendeListViewStateService', 'common.UtkastValidationService', 'common.UtkastViewStateService', 'common.AtticHelper',
    function($log, $timeout, diagnosProxy, fmbViewState, fmbService, srsService, ObjectHelper, monitoringService,
        ArendeListViewState, UtkastValidationService, UtkastViewState, AtticHelper) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            model: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueDiagnosOld/ueDiagnosOld.directive.html',
        link: function($scope) {

            AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);

            $scope.validation = UtkastViewState.validation;
            $scope.diagnosValidations = [];

            var diagnosViewState = $scope.diagnosViewState = {
                diagnosKodSystem: 'ICD_10_SE'
            };

            $scope.$watch('model.' + $scope.config.modelProp + '[0].diagnosKodSystem', function(newVal, oldVal) {
                if (newVal) {
                    diagnosViewState.diagnosKodSystem = newVal;
                    // We only want to log when the diagnoskodverk really changed and not when the value is set in the beginning
                    // of loading the utkast
                    fmbService.updateKodverk(newVal);

                    if (oldVal) {
                        monitoringService.diagnoskodverkChanged($scope.model.id, $scope.model.typ);
                    }
                }
            });

            $scope.diagnosKodLoading = [];
            $scope.diagnosKodNoResults = [];

            $scope.$watch(
                'model.' + $scope.config.modelProp + '[0].diagnosKod', function(newValue) {
                    //Reset fmb if we no longer have a valid diagnoseCode to work with
                    if (ObjectHelper.isEmpty(newValue) || newValue.length < 3) {
                        fmbViewState.reset(0);
                    }
                    else{
                        srsService.updateDiagnosKod(newValue);
                        var diagnoseModel = $scope.model[$scope.config.modelProp][0];
                        srsService.updateDiagnosBeskrivning(diagnoseModel.diagnosBeskrivning);
                    }
                });
            $scope.$watch(
                'model.' + $scope.config.modelProp + '[1].diagnosKod', function(newValue) {
                    //Reset fmb if we no longer have a valid diagnoseCode to work with
                    if (ObjectHelper.isEmpty(newValue) || newValue.length < 3) {
                        fmbViewState.reset(1);
                    }
                });
            $scope.$watch(
                'model.' + $scope.config.modelProp + '[2].diagnosKod', function(newValue) {
                    //Reset fmb if we no longer have a valid diagnoseCode to work with
                    if (ObjectHelper.isEmpty(newValue) || newValue.length < 3) {
                        fmbViewState.reset(2);
                    }
                });

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
                        }
                        else {
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
                        }
                        else {
                            return [];
                        }
                    }, function(response) {
                        $log.debug('Error searching diagnose code');
                        $log.debug(response);
                        return [];
                    });
            };

            $scope.onDiagnoseCodeSelect = function(index, $item) {
                var diagnoseModel = $scope.model[$scope.config.modelProp][index];
                if (isShortPsykiskDiagnos($item.value)) {
                    diagnoseModel.diagnosKod = undefined;
                    return;
                }
                diagnoseModel.diagnosBeskrivning = $item.beskrivning;
                diagnoseModel.diagnosKodSystem = diagnosViewState.diagnosKodSystem;
                $scope.diagnosForm.$setDirty();
                fmbService.updateFmbText(index, $item.value, diagnoseModel.diagnosKodSystem, $item.beskrivning);
            };

            $scope.onDiagnoseDescriptionSelect = function(index, $item) {
                var diagnoseModel = $scope.model[$scope.config.modelProp][index];
                if (isShortPsykiskDiagnos($item.value)) {
                    diagnoseModel.diagnosKod = undefined;
                    diagnoseModel.diagnosBeskrivning = undefined;
                    return;
                }
                diagnoseModel.diagnosKod = $item.value;
                diagnoseModel.diagnosBeskrivning = $item.beskrivning;
                diagnoseModel.diagnosKodSystem = diagnosViewState.diagnosKodSystem;
                $scope.diagnosKodNoResults = [];
                $scope.diagnosForm.$setDirty();
                fmbService.updateFmbText(index, $item.value, diagnoseModel.diagnosKodSystem, $item.beskrivning);
            };

            $scope.onDiagnoseCodeChanged = function(index) {
                if($scope.diagnosForm['diagnoseCode' + index].$viewValue === ''){
                    srsService.updateDiagnosKod($scope.model.diagnosKod);
                }
                if (!$scope.diagnosForm['diagnoseCode' + index].$viewValue) {
                    $scope.model[$scope.config.modelProp][index].diagnosBeskrivning = undefined;
                    fmbService.updateFmbText(index, null);
                }
            };

            $scope.onDiagnoseDescriptionChanged = function(index) {
                if (!$scope.model[$scope.config.modelProp][index].diagnosBeskrivning) {
                    $scope.model[$scope.config.modelProp][index].diagnosKod = undefined;
                    fmbService.updateFmbText(index, null);
                }
            };

            $scope.onChangeKodverk = function() {
                resetDiagnoses();
                setAlldiagnosKodSystem(diagnosViewState.diagnosKodSystem);
            };

            $scope.addDiagnos = function() {
                $scope.model[$scope.config.modelProp].push({
                    diagnosKodSystem: diagnosViewState.diagnosKodSystem,
                    diagnosKod: undefined,
                    diagnosBeskrivning: undefined
                });
            };

            function resetDiagnoses() {
                if ($scope.model[$scope.config.modelProp]) {
                    $scope.model[$scope.config.modelProp].forEach(function(diagnos) {
                        diagnos.diagnosKodSystem = diagnosViewState.diagnosKodSystem;
                        diagnos.diagnosKod = undefined;
                        diagnos.diagnosBeskrivning = undefined;
                    });
                }
            }

            function setAlldiagnosKodSystem(val) {
                diagnosViewState.diagnosKodSystem = val;
                $scope.model[$scope.config.modelProp].forEach(function(diagnos) {
                    diagnos.diagnosKodSystem = val;
                });
            }

            $scope.onBlurDiagnoseCodeField = function(index) {
                if (!$scope.model[$scope.config.modelProp][index].diagnosKod) {
                    // Clear diagnoskod in all cases when not selected from the typeahead
                    $scope.diagnosForm['diagnoseCode' + index].$setViewValue();
                    $scope.diagnosForm['diagnoseCode' + index].$render();
                }
                var diagnos = $scope.model[$scope.config.modelProp][index];

                fmbService.updateFmbText(index, diagnos.diagnosKod, diagnos.diagnosKodSystem, diagnos.diagnosBeskrivning);
            };

            $scope.hasValidationError = function(field, index) {
                return $scope.validation.messagesByField &&
                    (!!$scope.validation.messagesByField['diagnoser[' + index + '].' + field] ||
                        !!$scope.validation.messagesByField['diagnoser[' + index + '].row'] ||
                        // If no diagnose has been entered the first row should be marked with validation-error
                        (index === 0 && !!$scope.validation.messagesByField.diagnoser));
            };

            $scope.hasKomplettering = function() {
                return ArendeListViewState.hasKompletteringar($scope.config.modelProp);
            };

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

            $scope.validate = function() {
                //The timeout here allows the model to be updated (via typeahead selection) before sending it for validation
                $timeout(function() {
                    UtkastValidationService.validate($scope.model);
                }, 100);
            };
        }
    };

}]);
