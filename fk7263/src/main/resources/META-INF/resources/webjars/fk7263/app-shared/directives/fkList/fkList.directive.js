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
angular.module('fk7263').directive('fk7263List', [ 'uvUtil',
    function(uvUtil) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            viewData: '='
        },
        templateUrl: '/web/webjars/fk7263/app-shared/directives/fkList/fkList.directive.html',
        link: function($scope) {

            $scope.values = [];

            angular.forEach($scope.config.modelProps, function(data) {
                var value = uvUtil.getValue($scope.viewData, data.modelProp);

                if (value) {
                    $scope.values.push({
                        modelProp: data.modelProp,
                        key: data.label,
                        text: data.showValue ? value : null
                    });
                }
            });

            $scope.hasValue = function() {
                return $scope.values.length > 0;
            };

        }
    };
} ]);
