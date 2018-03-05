angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        'extends': 'validation-on-blur',
        name: 'multi-text',
        defaultOptions: {
            className: 'fold-animation'
        },
        templateUrl: '/web/webjars/common/webcert/gui/formly/multiText.formly.html',
        controller: ['$scope', 'common.ObjectHelper', 'common.AtticHelper', '$window', 'common.icf', function($scope, ObjectHelper, AtticHelper, $window, icf) {
            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.options.key);

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key);
            if($scope.options.key === 'funktionsnedsattning' || $scope.options.key === 'aktivitetsbegransning') {
                $scope.icfFunktioner = [];

                $scope.$watch('model.diagnoser', function(newVal) {
                    if (newVal) {
                        if($scope.model.diagnoser && $scope.model.diagnoser.length > 0) {
                            for (var i = 0; i < $scope.model.diagnoser.length; i++) {
                                var diagnos = $scope.model.diagnoser[i];
                                if (diagnos.diagnosKod === 'F322' || diagnos.diagnosKod === 'M751') {
                                    if ($scope.options.key === 'funktionsnedsattning') {
                                        $scope.grader = ['Välj gradering','Inget problem','Lätt problem','Måttligt problem','Svårt problem','Totalt problem'];
                                        $scope.icfFunktioner = angular.copy(icf[diagnos.diagnosKod.toLowerCase()].funktion);
                                    } else {
                                        $scope.grader = ['Välj gradering','Ingen svårighet','Lätt svårighet','Måttlig svårighet','Stor svårighet','Total svårighet'];
                                        $scope.icfFunktioner = angular.copy(icf[diagnos.diagnosKod.toLowerCase()].aktivitet);
                                    }
                                }
                            }
                        }
                    }
                }, true);

                $scope.hasICFDiagnos = function() {
                    if($scope.model.diagnoser) {
                        for(var i = 0; i < $scope.model.diagnoser.length; i++) {
                            var diagnos = $scope.model.diagnoser[i];
                            if (diagnos.diagnosKod === 'F322' || diagnos.diagnosKod === 'M751') {
                                return diagnos;
                            }
                        }
                    }
                    return false;
                };

                $scope.diagnosBeskrivningen = function() {
                    return $scope.hasICFDiagnos().diagnosBeskrivning;
                };
                

                // --------  DROPDOWN
                $scope.openPlate = function() {
                    $window.document.addEventListener('click', onDocumentClick, true);
                    $scope.funktionsDropdown = true;
                };

                $scope.closePlate = function() {
                    $scope.funktionsDropdown = false;
                    $window.document.removeEventListener('click', onDocumentClick, true);
                };

                $scope.openFunktionsDropdown = function() {
                    if ($scope.hasICFDiagnos()) {
                        if (!$scope.funktionsDropdown) {
                            $scope.openPlate();
                        } else {
                            $scope.closePlate();
                        }
                    }
                };
                // -------- DROPDOWN:END

                $scope.funktioner = [];

                $scope.rensa = function(option) {
                    var model;
                    for (var i = 0; i < $scope.funktioner.length; i++) {
                        $scope.funktioner.splice(i, 1);
                    }
                    if ($scope.model[$scope.options.key]) {
                        model = JSON.parse($scope.model[$scope.options.key]);
                    }
                    if(model && model.funktioner.length > 0) {
                        for(var j=0;j<model.funktioner.length;j++) {
                            model.funktioner.splice(j ,1);
                        }
                        $scope.model[$scope.options.key] = JSON.stringify(model);
                    }
                    $scope.closePlate();
                };

                $scope.add = function(arr) {
                    $scope.funktioner = [];
                    for (var i = 0; i < arr.length; i++) {
                        if (arr[i].vald) {
                            $scope.funktioner.push(arr[i]);
                        }
                    }
                    $scope.closePlate();
                };

                $scope.tabortFunktion = function(index, option) {
                    var model;
                    $scope.funktioner.splice(index, 1);
                    $scope.icfFunktioner.splice(index, 1);
                    if ($scope.model[$scope.options.key]) {
                        model = JSON.parse($scope.model[$scope.options.key]);
                    }
                    if(model && model.funktioner[index] !== undefined) {
                        model.funktioner.splice(index, 1);
                        $scope.model[$scope.options.key] = JSON.stringify(model);
                    }
                };

                $scope.friText = {value: undefined};
                if ($scope.model[$scope.options.key] && JSON.parse($scope.model[$scope.options.key]).freeText) {
                    $scope.friText.value = JSON.parse($scope.model[$scope.options.key]).freeText;
                }

                $scope.$watch('model.' + $scope.options.key, function(newVal, oldVal) {
                    if (newVal) {
                        var i;
                        if($scope.funktioner.length === 0) {
                            for (i = 0; i < $scope.icfFunktioner.length; i++) {
                                var valdFunktion = isValdFunktion($scope.icfFunktioner[i].namn);
                                if(valdFunktion) {
                                    $scope.icfFunktioner[i].vald = true;
                                    $scope.icfFunktioner[i].freeText = valdFunktion.freeText;
                                    $scope.icfFunktioner[i].begransning = valdFunktion.grad;
                                    $scope.funktioner.push($scope.icfFunktioner[i]);
                                }
                            }
                        }
                        
                        var model = JSON.parse($scope.model[$scope.options.key]);
                        $scope.friText.value = model.freeText;
                    }
                });

                $scope.$watch('friText.value', function(newVal, oldVal) {
                    if (newVal && newVal !== oldVal) {
                        if ($scope.model[$scope.options.key]) {
                            var model = JSON.parse($scope.model[$scope.options.key]);
                            model.freeText = newVal;
                            $scope.model[$scope.options.key] = JSON.stringify(model);
                        } else {
                            $scope.model[$scope.options.key] = '{"freeText":"' + newVal + '"}';
                        }
                    }
                });

                $scope.update = function(funktion) {
                    if ($scope.model[$scope.options.key]) {
                        var model = JSON.parse($scope.model[$scope.options.key]);
                        var found = false;
                        if (model.funktioner && model.funktioner.length > 0) {
                            for (var i = 0; i < model.funktioner.length; i++) {
                                if (model.funktioner[i].namn === funktion.namn) {
                                    model.funktioner[i].freeText = funktion.freeText;
                                    model.funktioner[i].grad = funktion.begransning;
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            if (!model.funktioner) {
                                model.funktioner = [];
                            }
                            model.funktioner.push(
                                {namn: funktion.namn, grad: funktion.begransning, freeText: funktion.freeText});
                        }
                        $scope.model[$scope.options.key] = JSON.stringify(model);
                    } else {
                        $scope.model[$scope.options.key] =
                            '{"funktioner":[{"namn": "' + funktion.namn + '", "freeText":"' + funktion.freeText + '", "grad":"' +
                            funktion.begransning + '"}]}';
                    }
                };
            }

            function isValdFunktion(namn) {
                if ($scope.model[$scope.options.key]) {
                    var initialModel = JSON.parse($scope.model[$scope.options.key]);
                    if (initialModel.funktioner && initialModel.funktioner.length > 0) {
                        for (var i = 0; i < initialModel.funktioner.length; i++) {
                            if (namn === initialModel.funktioner[i].namn) {
                                return initialModel.funktioner[i];
                            }
                        }
                    }
                }
                return undefined;
            }
            function onDocumentClick(e) {
                if ($scope.funktionsDropdown) {
                    var plateElement = $window.document.getElementById($scope.model.diagnoser[0].diagnosKod +
                        '-'+$scope.options.key+'-plate');
                    var dropElement = $window.document.getElementById($scope.model.diagnoser[0].diagnosKod +
                        '-'+$scope.options.key+'-dropdown');
                    if (!plateElement.contains(e.target) && !dropElement.contains(e.target)) {
                        $scope.closePlate();
                        $scope.$digest();
                    }
                }
            }
        }]
    });
});
