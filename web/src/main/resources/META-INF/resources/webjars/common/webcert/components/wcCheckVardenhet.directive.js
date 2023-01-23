/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
/**
 * Det här direktivet används för att verifiera att användaren är inloggad på en specifik vårdenhet. Om användaren inte
 * är inloggad på den vårdenheten så döljs elementet.
 */
angular.module('common').directive('wcCheckVardenhet',
    ['common.User', function(User) {
        'use strict';

        return {
            restrict: 'A',
            scope: {
                vardenhet: '@'
            },
            link: function(scope, element, attrs) {
                attrs.$observe('vardenhet', function() {
                    var found = false;

                    // If vardenhet is set but set to a blank id, ignore the check.
                    if (scope.vardenhet === '') {
                        found = true;
                    }
                    var vardenheter = User.getVardenhetFilterList();
                    for (var i = 0; i < vardenheter.length && !found; i++) {
                        if (vardenheter[i].id === scope.vardenhet) {
                            found = true;
                        }
                    }
                    element.css('display', found ? '' : 'none');
                });
            }
        };
    }]);
