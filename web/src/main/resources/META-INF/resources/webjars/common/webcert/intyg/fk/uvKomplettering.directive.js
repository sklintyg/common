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

angular.module('common').directive('uvKomplettering',
    [ 'common.IntygViewStateService',
        function(IntygViewStateService) {
            'use strict';

            return {
                restrict: 'AE',
                templateUrl: '/web/webjars/common/webcert/gui/wcUtkastKomplettering.directive.html',
                scope: {
                    fieldKey: '=',
                    marginTop: '=',
                    marginBottom: '='
                },
                link: function(scope) {
                    scope.hasKomplettering = function() {
                        // lookup if there's a field having a status PENDING_INTERNAL_ACTION
                        return IntygViewStateService.getFieldStatus(scope.fieldKey) === 'PENDING_INTERNAL_ACTION';
                    };
                }
            };
        }]);
