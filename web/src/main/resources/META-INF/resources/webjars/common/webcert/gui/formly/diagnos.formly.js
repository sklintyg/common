angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'diagnos',
        templateUrl: '/web/webjars/common/webcert/gui/formly/diagnos.formly.html',
        controller: ['$scope', '$log', 'common.DiagnosProxy', function($scope, $log, diagnosProxy) {

            var formState = $scope.formState;
            formState.diagnosKodSystem = 'ICD_10_SE';
            $scope.$watch('model.' + $scope.options.key + '[0].diagnosKodSystem', function(newVal, oldVal) {
                if (newVal) {
                    formState.diagnosKodSystem = newVal;
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
            };

            $scope.onDiagnoseDescriptionSelect = function($index, $item) {
                $scope.model[$scope.options.key][$index].diagnosKod = $item.value;
                $scope.model[$scope.options.key][$index].diagnosBeskrivning = $item.beskrivning;
                $scope.model[$scope.options.key][$index].diagnosKodSystem = formState.diagnosKodSystem;
                $scope.form.$setDirty();
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

            function resetDiagnoses(){
                $scope.model[$scope.options.key].forEach(function(diagnos) {
                    diagnos.diagnosKodSystem = formState.diagnosKodSystem;
                    diagnos.diagnosKod = undefined;
                    diagnos.diagnosBeskrivning = undefined;
                });
            }

            function setAlldiagnosKodSystem(val){
                formState.diagnosKodSystem = val;
                $scope.model[$scope.options.key].forEach(function(diagnos) {
                    diagnos.diagnosKodSystem = val;
                });
            }
        }]
    });

});
