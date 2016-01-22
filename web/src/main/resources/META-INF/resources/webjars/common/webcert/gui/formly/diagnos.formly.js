angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'diagnos',
        templateUrl: '/web/webjars/common/webcert/gui/formly/diagnos.formly.html',
        controller: ['$scope', '$log', 'sjukersattning.diagnosService', 'sjukersattning.EditCertCtrl.ViewStateService', function($scope, $log, diagnosService, viewState) {

            var formState = $scope.formState;
            formState.diagnosKodSystem = 'ICD_10_SE';
            $scope.$watch('model.' + $scope.options.key + '[0].diagnosKodSystem', function(newVal, oldVal) {
                if (newVal) {
                    formState.diagnosKodSystem = newVal;
                    console.log(formState.diagnosKodSystem);
                }
            });

            $scope.getDiagnoseCodes = function(codeSystem, val) {
                return diagnosService.searchByCode(codeSystem, val)
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
                return diagnosService.searchByDescription(codeSystem, val)
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
                    diagnosKodSystem: undefined,
                    diagnosKod : undefined,
                    diagnosBeskrivning : undefined
                });
            };

            function resetDiagnoses(){
                $scope.model[$scope.options.key].forEach(function(diagnos) {
                    diagnos.diagnosKodSystem = undefined;
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
