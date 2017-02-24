angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'diagnos',
        templateUrl: '/web/webjars/common/webcert/gui/formly/diagnos.formly.html',
        controller: ['$scope', '$log', 'common.DiagnosProxy', 'common.fmbViewState', 'common.fmbService',
            'common.ObjectHelper', 'common.MonitoringLogService', 'common.ArendeListViewStateService', 'common.UtkastValidationService',
            function($scope, $log, diagnosProxy, fmbViewState, fmbService, ObjectHelper, monitoringService,
                ArendeListViewState, UtkastValidationService) {

                var formState = $scope.formState;
                formState.diagnosKodSystem = 'ICD_10_SE';
                $scope.$watch('model.' + $scope.options.key + '[0].diagnosKodSystem', function(newVal, oldVal) {
                    if (newVal) {
                        formState.diagnosKodSystem = newVal;
                        // We only want to log when the diagnoskodverk really changed and not when the value is set in the beginning
                        // of loading the utkast
                        if (oldVal) {
                            monitoringService.diagnoskodverkChanged(formState.viewState.intygModel.id,
                                formState.viewState.common.intyg.type);
                        }
                    }
                });

                $scope.diagnosKodLoading = [];
                $scope.diagnosKodNoResults = [];

                $scope.$watch(
                    'model.diagnoser[0].diagnosKod', function(newValue) {
                        //Reset fmb if we no longer have a valid diagnoseCode to work with
                        if (ObjectHelper.isEmpty(newValue) || newValue.length < 3) {
                            fmbViewState.reset(0);
                        }
                    });
                $scope.$watch(
                    'model.diagnoser[1].diagnosKod', function(newValue) {
                        //Reset fmb if we no longer have a valid diagnoseCode to work with
                        if (ObjectHelper.isEmpty(newValue) || newValue.length < 3) {
                            fmbViewState.reset(1);
                        }
                    });
                $scope.$watch(
                    'model.diagnoser[2].diagnosKod', function(newValue) {
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
                    var diagnoseModel = $scope.model[$scope.options.key][index];
                    if (isShortPsykiskDiagnos($item.value)) {
                        diagnoseModel.diagnosKod = undefined;
                        return;
                    }
                    diagnoseModel.diagnosBeskrivning = $item.beskrivning;
                    diagnoseModel.diagnosKodSystem = formState.diagnosKodSystem;
                    $scope.form.$setDirty();
                    fmbService.updateFmbText(index, $item.value);
                };

                $scope.onDiagnoseDescriptionSelect = function(index, $item) {
                    var diagnoseModel = $scope.model[$scope.options.key][index];
                    if (isShortPsykiskDiagnos($item.value)) {
                        diagnoseModel.diagnosKod = undefined;
                        diagnoseModel.diagnosBeskrivning = undefined;
                        return;
                    }
                    diagnoseModel.diagnosKod = $item.value;
                    diagnoseModel.diagnosBeskrivning = $item.beskrivning;
                    diagnoseModel.diagnosKodSystem = formState.diagnosKodSystem;
                    $scope.diagnosKodNoResults = [];
                    $scope.form.$setDirty();
                    fmbService.updateFmbText(index, $item.value);
                };

                $scope.onDiagnoseCodeChanged = function(index) {
                    if (!$scope.form['diagnoseCode' + index].$viewValue) {
                        $scope.model[$scope.options.key][index].diagnosBeskrivning = undefined;
                        fmbService.updateFmbText(index, null);
                    }
                };

                $scope.onDiagnoseDescriptionChanged = function(index) {
                    if (!$scope.model[$scope.options.key][index].diagnosBeskrivning) {
                        $scope.model[$scope.options.key][index].diagnosKod = undefined;
                        fmbService.updateFmbText(index, null);
                    }
                };

                $scope.onChangeKodverk = function() {
                    resetDiagnoses();
                    setAlldiagnosKodSystem(formState.diagnosKodSystem);
                };

                $scope.addDiagnos = function() {
                    $scope.model[$scope.options.key].push({
                        diagnosKodSystem: formState.diagnosKodSystem,
                        diagnosKod: undefined,
                        diagnosBeskrivning: undefined
                    });
                };

                function resetDiagnoses() {
                    $scope.model[$scope.options.key].forEach(function(diagnos) {
                        diagnos.diagnosKodSystem = formState.diagnosKodSystem;
                        diagnos.diagnosKod = undefined;
                        diagnos.diagnosBeskrivning = undefined;
                    });
                }

                function setAlldiagnosKodSystem(val) {
                    formState.diagnosKodSystem = val;
                    $scope.model[$scope.options.key].forEach(function(diagnos) {
                        diagnos.diagnosKodSystem = val;
                    });
                }

                $scope.onBlurDiagnoseCodeField = function(index) {
                    if (!$scope.model[$scope.options.key][index].diagnosKod) {
                        // Clear diagnoskod in all cases when not selected from the typeahead
                        $scope.form['diagnoseCode' + index].$setViewValue();
                        $scope.form['diagnoseCode' + index].$render();
                    }
                    fmbService.updateFmbText(index, $scope.model[$scope.options.key][index].diagnosKod);
                };

                $scope.hasValidationError = function(field, index) {
                    return $scope.formState.viewState.common.validation.messagesByField &&
                        !!$scope.formState.viewState.common.validation.messagesByField['diagnoser.' + index + '.' +
                        field];
                };


                $scope.hasKomplettering = function() {
                    return ArendeListViewState.hasKompletteringar($scope.options.key);
                };

                $scope.$watch('formState.viewState.common.validation.messagesByField', function() {
                    $scope.diagnosValidations = [];
                    angular.forEach($scope.formState.viewState.common.validation.messagesByField,
                        function(validations, key) {
                            if (key.substr(0, $scope.options.key.length) === $scope.options.key.toLowerCase()) {
                                $scope.diagnosValidations = $scope.diagnosValidations.concat(validations);
                            }
                        });
                });

                $scope.validate = function() {
                    UtkastValidationService.validate($scope.model);
                };
            }
        ]
    });
});
