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

angular.module('common').directive('wcIntygFieldKomplettering',
    [ 'common.IntygViewStateService',
        function(IntygViewStateService) {
            'use strict';

            function _hasFieldKomplettering(category, fieldId) {
                var index = -1;
                if (Array.isArray(category)) {
                    category.some(function (item, i) {
                        return item.id === fieldId && item.status === 'PENDING_INTERNAL_ACTION' ? (index = i, true) : false;
                    });
                }
                return index > -1 ? true : false;
            }

            return {
                restrict: 'AE',
                templateUrl: '/web/webjars/common/webcert/intyg/fk/wcIntygFieldKomplettering.directive.html',
                scope: {
                    categoryKey: '@',
                    fieldKey: '@'
                },
                link: function(scope) {
                    console.log('- - - - - - - - - - - - - - ');
                    console.log(scope.categoryKey, scope.fieldKey);
                    scope.hasFieldKomplettering = function() {
                        // lookup if there's a field having a status PENDING_INTERNAL_ACTION
                        var category = IntygViewStateService.getCategory(scope.categoryKey);
                        return _hasFieldKomplettering(category, scope.fieldKey);
                    };
                }
            };
        }]);
