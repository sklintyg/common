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

angular.module('common').directive('ueIcf', [ 'ueUtil', 'common.icf', '$window',
    function(ueUtil, icf, $window) {
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

            scope.hasICFDiagnos = function() {
               if(scope.model.diagnoser) {
                   for(var i = 0; i < scope.model.diagnoser.length; i++) {
                       var diagnos = scope.model.diagnoser[i];
                       if (diagnos.diagnosKod === 'F322' || diagnos.diagnosKod === 'M751') {
                           return diagnos;
                       }
                   }
               }
               return false;
           };

           scope.icfFunktioner = [];

           scope.$watch('model.diagnoser', function(newVal) {
                if (newVal) {
                    if(scope.model.diagnoser && scope.model.diagnoser.length > 0) {
                        for (var i = 0; i < scope.model.diagnoser.length; i++) {
                            var diagnos = scope.model.diagnoser[i];
                            if (diagnos.diagnosKod === 'F322' || diagnos.diagnosKod === 'M751') {
                                if (scope.config.modelProp === 'funktionsnedsattning') {
                                    scope.icfFunktioner = angular.copy(icf[diagnos.diagnosKod.toLowerCase()].funktion);
                                } else {
                                    scope.icfFunktioner = angular.copy(icf[diagnos.diagnosKod.toLowerCase()].aktivitet);
                                }
                            }
                        }
                    }
                }
            }, true);

            scope.diagnosBeskrivningen = function() {
                return scope.hasICFDiagnos().diagnosBeskrivning;
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
                for (var i = 0; i < scope.icfFunktioner.length; i++) {
                    scope.icfFunktioner[i].vald = false;
                }
                scope.model[scope.config.kategoriProp] = [];
                scope.closePlate();
            };

            scope.add = function(arr) {
                scope.model[scope.config.kategoriProp] = [];
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].vald) {
                        scope.model[scope.config.kategoriProp].push(arr[i].namn);
                    }
                }
                scope.closePlate();
            };

            scope.toggle = function(kategori) {
                kategori.vald = !kategori.vald;
            };

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

            function setupChoices() {
                if (scope.model[scope.config.kategoriProp].length > 0) {
                    for (var i = 0; i < scope.model[scope.config.kategoriProp].length; i++) {
                        for (var f = 0; f < scope.icfFunktioner.length; f++) {
                            if (scope.icfFunktioner[f].namn === scope.model[scope.config.kategoriProp][i]) {
                                scope.icfFunktioner[f].vald = true;
                            }
                        }
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
                for (var i = 0; i < scope.icfFunktioner.length; i++) {
                    if (scope.icfFunktioner[i].vald) {
                        return false;
                    }
                }
                if (scope.model[scope.config.kategoriProp].length === 0) {
                    return true;
                }
            };
        }
    };

}]);
