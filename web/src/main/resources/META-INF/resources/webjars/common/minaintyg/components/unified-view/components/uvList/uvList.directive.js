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

angular.module('common').directive('uvList', [ 'uvUtil', function(uvUtil) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            viewData: '='
        },
        templateUrl: '/web/webjars/common/minaintyg/components/unified-view/components/uvList/uvList.directive.html',
        link: function($scope) {
            $scope.getValue = function() {

                var listData = uvUtil.getValue($scope.viewData, $scope.config.modelProp);

                var finalListData = [];

                // Use labelKey as pattern if a {var} is present, otherwise just use the list as is
                if ($scope.config.labelKey.indexOf('{var}') !== -1 && listData) {
                    finalListData = listData.map(function(item) {
                        var result = $scope.config.labelKey;
                        return result.replace('{var}', item[$scope.config.listKey]);
                    });
                } else {
                    finalListData = listData;
                }

                return finalListData;

            };
        }

    };
}]);
