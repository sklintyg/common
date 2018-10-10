/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

angular.module('common').directive('ueIcf', [ 'ueUtil', '$window', '$http',
    function(ueUtil, $window, $http) {
    'use strict';
    return {
        restrict: 'E',
        scope: {
            form: '=',
            config: '=',
            model: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueIcf/ueIcf.directive.html',
        link: function(scope) {
            ueUtil.standardSetup(scope);

            scope.icfFunktioner = {};
            scope.selectedFunktioner = [];
            
            scope.hasICFDiagnos = function() {
                var hasICFDiagnoser = false;
                if (!angular.equals(scope.icfFunktioner, {}) &&
                    (!angular.equals(scope.icfFunktioner.gemensamma, {}) || 
                    !angular.equals(scope.icfFunktioner.unika, []))) {
                        hasICFDiagnoser = true;
                }
                return hasICFDiagnoser;
            };

           scope.$watch('model.diagnoser', function(newVal) {
                if (newVal) {
                    if(scope.model.diagnoser && scope.model.diagnoser.length > 0) {
                        var icdKod = 0;
                        var diagnoser = scope.model.diagnoser.filter(function(v){
                            return !!v.diagnosKod;
                        }).map(function(v){
                            icdKod++;
                            return 'icfCode' + icdKod + '=' + v.diagnosKod;
                        }).join('&');
                        if (diagnoser) {
                            scope.getIcf(diagnoser);
                        }
                    }
                }
            }, true);

            scope.getIcf = function(diagnoser) {
                var restPath = '/api/icf?' + diagnoser;
                $http.get(restPath).then(function(response) {
                    if (response && response.statusText === 'OK') {
                        scope.icfFunktioner = response.data;
                    }
                }, function(response) {
                    console.log('error ' + response.statusText);
                });
            };

            scope.diagnosBeskrivningen = function(kod) {
                if (angular.isArray(kod)) {
                    return scope.model.diagnoser.filter(function(v){
                        return kod.indexOf(v.diagnosKod) > -1;
                    }).map(function(v) {
                        return v.diagnosBeskrivning;
                    }).join(', ');
                }
                var icdKod = scope.model.diagnoser.filter(function(v){
                        return v.diagnosKod === kod;
                });
                if (icdKod.length === 1) {
                    return icdKod[0].diagnosBeskrivning;
                }
            };

            scope.getKodTyp = function () {
                return scope.config.kategoriProp === 'funktionsKategorier' ? 
                'funktionsNedsattningsKoder' : 'aktivitetsBegransningsKoder';
            };

            scope.openPlate = function() {
                $window.document.addEventListener('click', onDocumentClick, true);
                scope.funktionsDropdown = true;
            };

            scope.closePlate = function() {
                scope.funktionsDropdown = false;
                $window.document.removeEventListener('click', onDocumentClick, true);
            };

            scope.openFunktionsDropdown = function() {
                if (scope.hasICFDiagnos()) {
                    setupChoices();
                    if (!scope.funktionsDropdown) {
                        scope.openPlate();
                    } else {
                        scope.closePlate();
                    }
                }
            };

            scope.rensa = function(option) {
                iterateFunktioner(function (v) {
                    v.vald = false;
                });
                scope.model[scope.config.kategoriProp] = [];
                scope.closePlate();
            };

            scope.add = function(arr) {
                scope.model[scope.config.kategoriProp] = [];
                iterateFunktioner(function (v) {
                    if (v.vald) {
                        scope.model[scope.config.kategoriProp].push(v.kod);
                    }
                });
                scope.closePlate();
            };

            function iterateFunktioner(fun) {
                if (scope.icfFunktioner.gemensamma[scope.getKodTyp()]) {
                    scope.icfFunktioner.gemensamma[scope.getKodTyp()].centralaKoder.forEach(fun);
                    scope.icfFunktioner.gemensamma[scope.getKodTyp()].kompletterandeKoder.forEach(fun);
                }
                scope.icfFunktioner.unika.forEach(function(v) {
                    v[scope.getKodTyp()].centralaKoder.forEach(fun);
                    v[scope.getKodTyp()].kompletterandeKoder.forEach(fun);
                });
            }

            function onDocumentClick(e) {
                if (scope.funktionsDropdown) {
                    var plateElement = $window.document.getElementById(scope.model.diagnoser[0].diagnosKod +
                        '-'+scope.config.modelProp+'-plate');
                    var dropElement = $window.document.getElementById(scope.model.diagnoser[0].diagnosKod +
                        '-'+scope.config.modelProp+'-dropdown');
                    if (!plateElement.contains(e.target) && !dropElement.contains(e.target)) {
                        scope.closePlate();
                        scope.$digest();
                    }
                }
            }
            
            function setVald(i) {
                return function(v) {
                    if (v.kod === scope.model[scope.config.kategoriProp][i]) {
                        v.vald = true;
                    }
                };
            }

            function setupChoices() {
                if (scope.model[scope.config.kategoriProp].length > 0) {
                    for (var i = 0; i < scope.model[scope.config.kategoriProp].length; i++) {
                        iterateFunktioner(setVald(i));
                    }
                }
            }

            scope.getPlaceHolder = function () {
                return scope.config.modelProp === 'funktionsnedsattning' ?
                    'Hur har du kommit fram till ovanstående? På vilket sätt och i vilken utsträckning är patienten påverkad?' :
                    'Hur begränsar ovanstående patientens sysselsättning och i vilken utsträckning?';
            };

            scope.getPopoverText = function () {
                if (!scope.model.diagnoser || scope.model.diagnoser[0].diagnosKod === undefined) {
                    return 'Ange minst en diagnos för att få ICF-stöd.';
                }
                if (scope.model.diagnoser) {
                    for (var i = 0; i < scope.model.diagnoser.length; i++) {
                        var diagnos = scope.model.diagnoser[i];
                        if (diagnos.diagnosKod === 'F322' || diagnos.diagnosKod === 'M751') {
                            return '';
                        }
                    }
                    if (scope.model.diagnoser[1].diagnosKod === undefined) {
                        return 'För den angivna diagnosen finns för tillfället inget ICF-stöd.';
                    } else {
                        return 'För de angivna diagnoserna finns för tillfället inget ICF-stöd.';
                    }
                }
            };

            scope.isInteractionDisabled = function() {
                var shouldDisable = true;
                iterateFunktioner(function(v) {
                    if (v.vald) {
                        shouldDisable = false;
                    }
                });
                if (!shouldDisable) {
                    return false;
                }
                if (scope.model[scope.config.kategoriProp].length === 0) {
                    return true;
                }
            };
        }
    };

}]);
