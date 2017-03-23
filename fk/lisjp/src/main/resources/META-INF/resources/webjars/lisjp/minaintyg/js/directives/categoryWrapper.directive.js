/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

angular.module('lisjp').directive('categoryWrapper',
    function() {
        'use strict';

        var categoryNames = {
            1: 'grundformu',
            2: 'sysselsattning',
            3: 'diagnos',
            4: 'funktionsnedsattning',
            5: 'medicinskaBehandlingar',
            6: 'bedomning',
            7: 'atgarder',
            8: 'ovrigt',
            9: 'kontakt',
            10: 'smittbararpenning',
            9999: 'tillaggsfragor'
        };

        return {
            restrict: 'E',
            transclude: true,
            scope: {
                categoryKey: '@',
                intygModel: '='
            },
            template: '<div ng-if="showCategory()"><div ng-transclude></div></div>',
            link: function(scope) {

                scope.showCategory = function() {
                    var smittskydd = scope.intygModel && scope.intygModel.avstangningSmittskydd === true;

                    if (smittskydd === true) {
                        switch (scope.getCategoryName(scope.categoryKey)) {
                        case 'smittbararpenning':
                        case 'diagnos':
                        case 'bedomning':
                        case 'ovrigt':
                            return true;
                        case 'tillaggsfragor':
                            return scope.intygModel.tillaggsfragor && scope.intygModel.tillaggsfragor.length > 0;
                        }

                        return false;
                    }

                    return true;
                };

                scope.getCategoryName = function() {
                    var name = categoryNames[scope.categoryKey];
                    return name || 'unknown';
                };

            }

        };

    });
