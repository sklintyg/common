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

angular.module('common').directive('uvWcKategori',
    ['common.IntygViewStateService',
    function(IntygViewStateService) {
        'use strict';

        function _hasCategoryKomplettering(category) {
            var index = -1;
            if (Array.isArray(category)) {
                category.some(function (item, i) {
                    return item.status === 'PENDING_INTERNAL_ACTION' ? (index = i, true) : false;
                });
            }
            return index > -1 ? true : false;
        }

        return {
            restrict: 'E',
            scope: {
                config: '=',
                viewData: '='
            },
            templateUrl: '/web/webjars/common/webcert/intyg/unified-view/uvWcKategori/uvWcKategori.directive.html',
            link: function($scope) {

                $scope.hasKomplettering = function() {
                    return _hasCategoryKomplettering(IntygViewStateService.getCategory($scope.config.labelKey));
                };
            }
        };
    }]);
