/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

angular.module('common').directive('sjukskrivningView',
    function() {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                fieldModel: '='
            },
            templateUrl: '/web/webjars/common/minaintyg/intyg/sjukskrivningView.directive.html',
            link: function(scope) {
                scope.grader = [0,1,2,3];

                scope.getSjuksrivningsgrad = function(grad) {
                    if (scope.hasSjukskrivning(grad)) {
                        return scope.fieldModel.sjukskrivningar[grad].sjukskrivningsgrad;
                    }

                    return 'UNKNOWN';
                };

                scope.getSjuksrivningFrom = function(grad) {
                    if (scope.hasSjukskrivning(grad)) {
                        return scope.fieldModel.sjukskrivningar[grad].period.from;
                    }
                };

                scope.getSjuksrivningTom = function(grad) {
                    if (scope.hasSjukskrivning(grad)) {
                        return scope.fieldModel.sjukskrivningar[grad].period.tom;
                    }
                };

                scope.hasSjukskrivning = function(grad) {
                    if (scope.fieldModel.sjukskrivningar && scope.fieldModel.sjukskrivningar[grad]) {
                        return true;
                    }
                    return false;
                };

            }
        };
    });
