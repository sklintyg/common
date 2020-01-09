/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

angular.module('common').directive('ueIcf', [ 'ueUtil', '$window', 'common.IcfProxy', 'common.fmbViewState', 'common.AtticHelper',
    function(ueUtil, $window, IcfProxy, fmbViewState, AtticHelper) {
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

            AtticHelper.restoreFromAttic(scope.model, scope.config.kategoriProp);
            AtticHelper.updateToAttic(scope, scope.model, scope.config.kategoriProp);

            scope.kategorier = {};
            scope.diagnoser = [];

            scope.hasICFDiagnos = function() {
                return (!angular.equals(scope.kategorier, {}) &&
                    (!angular.equals(scope.kategorier.gemensamma, {}) ||
                    !angular.equals(scope.kategorier.unika, [])));
            };

           scope.$watch('model.diagnoser', function(newVal) {
                if (newVal) {
                    if(scope.model.diagnoser && scope.model.diagnoser.length > 0) {
                        var unikaDiagnoser = [];
                        var diagnoser = scope.model.diagnoser.filter(function(v) {
                            if (v.diagnosKod && unikaDiagnoser.indexOf(v.diagnosKod) === -1) {
                                unikaDiagnoser.push(v.diagnosKod);
                                return true;
                            }
                        });
                        if (diagnoser.length > 0 && !angular.equals(diagnoser, scope.diagnoser)) {
                            IcfProxy.getIcf(diagnoser, function(kategorier) {
                                scope.kategorier = kategorier;
                                if (!scope.hasICFDiagnos()) {
                                    scope.model[scope.config.kategoriProp] = [];
                                }
                            }, function() {
                                scope.kategorier = {};
                            });
                        } else if (diagnoser.length === 0) {
                            scope.kategorier = {};
                            scope.model[scope.config.kategoriProp] = [];
                        }
                        scope.diagnoser = angular.copy(diagnoser);
                    }
                }
            }, true);

            scope.diagnosBeskrivningen = function(kod) {
                var beskrivning;
                if (angular.isArray(kod)) {
                    var beskrivningar = [];
                    angular.forEach(fmbViewState.diagnoses, function(d) {
                        if (kod.indexOf(d.diagnosKod) > -1) {
                            beskrivning = d.diagnosBeskrivning;
                            if (d.originalDiagnosKod !== d.diagnosKod) {
                                beskrivning = popoverBeskrivning(d);
                            }
                            beskrivningar.push(beskrivning);
                        }
                    });
                    return beskrivningar.join(' | ');
                }
                angular.forEach(fmbViewState.diagnoses, function(d) {
                    if (kod === d.diagnosKod) {
                        if (d.originalDiagnosKod !== d.diagnosKod) {
                            beskrivning = popoverBeskrivning(d);
                        } else {
                            beskrivning =  d.diagnosBeskrivning;
                        }
                    }
                });
                return beskrivning;
            };

            function popoverBeskrivning(d) {
                var beskrivning = scope.diagnoser.filter(function(k) {
                    return k.diagnosKod === d.originalDiagnosKod;
                })[0].diagnosBeskrivning;
                beskrivning += ' <i class="material-icons md-18" uib-popover="' + 
                'Det ICF-stöd som visas är för koden ' + d.diagnosKod + ' - ' +
                d.diagnosBeskrivning +
                '" popover-placement="auto top">info_outline</i>';
                return beskrivning;
            }

            scope.getKodTyp = function () {
                return scope.config.kategoriProp === 'funktionsKategorier' ?
                'funktionsNedsattningsKoder' : 'aktivitetsBegransningsKoder';
            };

            scope.openPlate = function() {
                $window.document.addEventListener('mousedown', onDocumentClick, true);
                scope.kategoriDropdown = true;
            };

            scope.closePlate = function() {
                scope.kategoriDropdown = false;
                $window.document.removeEventListener('mousedown', onDocumentClick, true);
            };

            scope.openKategoriDropdown = function() {
                if (scope.hasICFDiagnos()) {
                    setupChoices();
                    if (!scope.kategoriDropdown) {
                        scope.openPlate();
                    } else {
                        scope.closePlate();
                    }
                }
            };

            scope.rensa = function() {
                itereraKategorier(function (v) {
                    v.vald = false;
                });
                scope.closePlate();
            };

            scope.add = function() {
                scope.model[scope.config.kategoriProp] = [];
                itereraKategorier(function (v) {
                    if (v.vald) {
                        scope.model[scope.config.kategoriProp].push(v.benamning);
                    }
                });
                scope.closePlate();
                scope.form.$setDirty();
            };

            function itereraKategorier(fun) {
                if (scope.kategorier.gemensamma[scope.getKodTyp()] && 
                    scope.kategorier.gemensamma[scope.getKodTyp()].icfKoder) {
                    scope.kategorier.gemensamma[scope.getKodTyp()].icfKoder.forEach(fun);
                }
                scope.kategorier.unika.forEach(function(v) {
                    if (v[scope.getKodTyp()] && v[scope.getKodTyp()].icfKoder) {
                        v[scope.getKodTyp()].icfKoder.forEach(fun);
                    }
                });
            }

            function onDocumentClick(e) {
                if (scope.kategoriDropdown) {
                    var plateElement = $window.document.getElementById('icf-' + scope.config.modelProp + '-plate');
                    var dropElement = $window.document.getElementById('icf-' + scope.config.modelProp + '-dropdown');
                    if (!plateElement.contains(e.target) && !dropElement.contains(e.target)) {
                        scope.rensa();
                        scope.$digest();
                    }
                }
            }

            function setVald(i) {
                return function(v) {
                    if (v.benamning === scope.model[scope.config.kategoriProp][i]) {
                        v.vald = true;
                    }
                };
            }

            function setupChoices() {
                if (scope.model[scope.config.kategoriProp].length > 0) {
                    for (var i = 0; i < scope.model[scope.config.kategoriProp].length; i++) {
                        itereraKategorier(setVald(i));
                    }
                }
            }

            scope.getPlaceHolder = function () {
                return scope.config.modelProp === 'funktionsnedsattning' ?
                    'Vad grundar sig bedömningen på? På vilket sätt och i vilken utsträckning är patienten påverkad?' :
                    'Hur begränsar ovanstående patientens sysselsättning och i vilken utsträckning?';
            };

            scope.getPopoverText = function () {
                if (!scope.model.diagnoser || (scope.model.diagnoser[0].diagnosKod === undefined && 
                    scope.model.diagnoser[1].diagnosKod === undefined && 
                    scope.model.diagnoser[2].diagnosKod === undefined) ) {
                    return 'Ange minst en diagnos för att få ICF-stöd.';
                }
                if (scope.model.diagnoser) {
                    if (scope.hasICFDiagnos()) {
                        return '';
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
                itereraKategorier(function(v) {
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
