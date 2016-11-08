angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'diagnos',
        templateUrl: '/web/webjars/common/webcert/gui/formly/diagnos.formly.html',
        controller: ['$scope', '$log', 'common.DiagnosProxy', 'common.fmb.ViewStateService', 'common.fmbService',
            'common.ObjectHelper', 'common.MonitoringLogService', 'common.ArendeListViewStateService',
            function($scope, $log, diagnosProxy, fmbViewState, fmbService, ObjectHelper, monitoringService, ArendeListViewState) {
                var enableFMB = $scope.options.data.enableFMB;

                var formState = $scope.formState;
                formState.diagnosKodSystem = 'ICD_10_SE';
                $scope.$watch('model.' + $scope.options.key + '[0].diagnosKodSystem', function(newVal, oldVal) {
                    if (newVal) {
                        formState.diagnosKodSystem = newVal;
                        // We only want to log when the diagnoskodverk really changed and not when the value is set in the beginning
                        // of loading the utkast
                        if (oldVal) {
                            monitoringService.diagnoskodverkChanged(formState.viewState.intygModel.id, formState.viewState.common.intyg.type);
                        }
                    }
                });

                $scope.diagnosKodLoading = [];
                $scope.diagnosKodNoResults = [];

                $scope.$watchCollection(
                    'model.diagnoser[0].diagnosKod', function(newValue) {
                        //Reset fmb if we no longer have a valid diagnoseCode to work with
                        if (ObjectHelper.isEmpty(newValue) || newValue.length < 3) {
                            fmbViewState.reset();
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

            $scope.onDiagnoseCodeSelect = function($index, $item) {
                if (isShortPsykiskDiagnos($item.value)) {
                    $scope.model[$scope.options.key][$index].diagnosKod = undefined;
                    return;
                }
                $scope.model[$scope.options.key][$index].diagnosBeskrivning = $item.beskrivning;
                $scope.model[$scope.options.key][$index].diagnosKodSystem = formState.diagnosKodSystem;
                $scope.form.$setDirty();
                if ($index === 0) {
                    $scope.updateFmbText();
                }
            };

            $scope.onDiagnoseDescriptionSelect = function($index, $item) {
                if (isShortPsykiskDiagnos($item.value)) {
                    $scope.model[$scope.options.key][$index].diagnosKod = undefined;
                    $scope.model[$scope.options.key][$index].diagnosBeskrivning = undefined;
                    return;
                }
                $scope.model[$scope.options.key][$index].diagnosKod = $item.value;
                $scope.model[$scope.options.key][$index].diagnosBeskrivning = $item.beskrivning;
                $scope.model[$scope.options.key][$index].diagnosKodSystem = formState.diagnosKodSystem;
                $scope.form.$setDirty();
                if ($index === 0 || $item.value.length === 0) {
                    $scope.updateFmbText();
                }
            };

            $scope.onDiagnoseCodeChanged = function(index) {
                if (!$scope.form['diagnoseCode' + index].$viewValue) {
                    $scope.model[$scope.options.key][index].diagnosBeskrivning = undefined;
                }
            };

            $scope.onDiagnoseDescriptionChanged = function(index) {
                if (!$scope.model[$scope.options.key][index].diagnosBeskrivning) {
                    $scope.model[$scope.options.key][index].diagnosKod = undefined;
                }
            };

            $scope.onChangeKodverk = function() {
                resetDiagnoses();
                setAlldiagnosKodSystem(formState.diagnosKodSystem);
            };

            $scope.addDiagnos = function() {
                $scope.model[$scope.options.key].push({
                    diagnosKodSystem: formState.diagnosKodSystem,
                    diagnosKod : undefined,
                    diagnosBeskrivning : undefined
                });
            };

            $scope.removeDiagnos = function(index) {
                $scope.model[$scope.options.key].splice(index, 1);
                $scope.form.$setDirty();
                $scope.updateFmbText();

            };

            function resetDiagnoses(){
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
                if (index === 0) {
                    $scope.updateFmbText();
                }
            };

            $scope.updateFmbText = function() {
                if (!enableFMB) {
                    return;
                }

                var diagnoseCode = $scope.model[$scope.options.key][0].diagnosKod;

                if (ObjectHelper.isEmpty(diagnoseCode)) {
                    fmbViewState.reset();
                } else if (fmbViewState.state.diagnosKod !== diagnoseCode) {

                    fmbService.getFMBHelpTextsByCode(diagnoseCode).then(function(formData) {
                        fmbViewState.setState(formData, formData.icd10Code, formData.icd10Description, diagnoseCode);
                    }, function fmbReject(data) {
                        $log.debug('Error searching fmb help text');
                        fmbViewState.reset();
                        return [];
                    });
                }
            };

            $scope.hasKomplettering = function() {
                return ArendeListViewState.hasKompletteringar($scope.options.key);
            };

            $scope.hasValidationError = function(field, index) {
                return $scope.formState.viewState.common.validationMessagesByField &&
                    !!$scope.formState.viewState.common.validationMessagesByField['diagnoser.' + index + '.' + field];
            };

            $scope.$watch('formState.viewState.common.validationMessagesByField', function() {
                $scope.diagnosValidations = [];
                angular.forEach($scope.formState.viewState.common.validationMessagesByField, function(validations, key) {
                    if (key.substr(0, $scope.options.key.length) === $scope.options.key.toLowerCase()) {
                        $scope.diagnosValidations = $scope.diagnosValidations.concat(validations);
                    }
                });
            });

        }]
    });

});
