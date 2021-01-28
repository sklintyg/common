/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcDateIsAfterValidator',
    [
        function() {
            'use strict';

            return {
                restrict: 'A',
                require: '^form',
                link: function(scope, elm, attrs, form) {

                    scope.date1Ctrl = form[attrs.domId];
                    scope.date2Ctrl = form[attrs.wcDateIsAfterValidator];

                    scope.date1Ctrl.$validators.isBefore = function() {
                        var date1 = scope.date1Ctrl.$modelValue;
                        var date2 = scope.date2Ctrl.$modelValue;
                        if (scope.date1Ctrl.$isEmpty(date1) || scope.date2Ctrl.$isEmpty(date2)) {
                            // consider empty models to be valid
                            return true;
                        }

                        var mdate1 = moment(date1, 'YYYY-MM-DD', true);
                        var mdate2 = moment(date2, 'YYYY-MM-DD', true);

                        if(!mdate1.isValid() || !mdate2.isValid()) {
                            return true;
                        }

                        return !moment(mdate2).isAfter(mdate1);
                    };

                    // Trigger validation of this field when other field changes
                    scope.$watch('date2Ctrl.$modelValue', function() {
                        scope.date1Ctrl.$validate();
                    });
                }
            };
        }
    ]
);