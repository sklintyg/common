angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'diagnos',
        templateUrl: '/web/webjars/common/webcert/gui/formly/diagnos.formly.html',
        controller: ['$scope', '$log', 'common.DiagnosProxy', 'common.fmb.ViewStateService', 'common.fmbService', 'common.ObjectHelper',
            function($scope, $log, diagnosProxy, fmbViewState, fmbService, ObjectHelper) {
                var enableFMB = $scope.options.data.enableFMB;

                var formState = $scope.formState;
                formState.diagnosKodSystem = 'ICD_10_SE';
                $scope.$watch('model.' + $scope.options.key + '[0].diagnosKodSystem', function(newVal, oldVal) {
                    if (newVal) {
                        formState.diagnosKodSystem = newVal;
                    }
                });

                $scope.$watchCollection(
                    'model.diagnoser[0].diagnosKod', function(newValue) {
                        //Reset fmb if we no longer have a valid diagnoseCode to work with
                        if (ObjectHelper.isEmpty(newValue) || newValue.length < 3) {
                            fmbViewState.reset();
                        }
                    });

                $scope.getDiagnoseCodes = function(codeSystem, val) {
                    return diagnosProxy.searchByCode(codeSystem, val)
                        .then(function(response) {
                            if (response && response.data && response.data.resultat === 'OK') {
                                var result = response.data.diagnoser.map(function(item) {
                                    return {
                                        value: item.kod,
                                        beskrivning: item.beskrivning,
                                        label: item.kod + ' | ' + item.beskrivning
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
                                    label: item.kod + ' | ' + item.beskrivning
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
                $scope.model[$scope.options.key][$index].diagnosBeskrivning = $item.beskrivning;
                $scope.model[$scope.options.key][$index].diagnosKodSystem = formState.diagnosKodSystem;
                $scope.form.$setDirty();
                if ($index === 0) {
                    $scope.updateFmbText();
                }

            };

            $scope.onDiagnoseDescriptionSelect = function($index, $item) {
                $scope.model[$scope.options.key][$index].diagnosKod = $item.value;
                $scope.model[$scope.options.key][$index].diagnosBeskrivning = $item.beskrivning;
                $scope.model[$scope.options.key][$index].diagnosKodSystem = formState.diagnosKodSystem;
                $scope.form.$setDirty();
                if ($index === 0 || $item.value.length === 0) {
                    $scope.updateFmbText();
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
                        fmbViewState.setState(formData, formData.icd10Code);
                    }, function fmbReject(data) {
                        $log.debug('Error searching fmb help text');
                        fmbViewState.reset();
                        return [];
                    });
                }
            };

        }]
    });

});
