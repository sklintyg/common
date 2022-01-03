/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('common').directive('uvSimpleValue', [ 'uvUtil', function(uvUtil) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            viewData: '='
        },
        templateUrl: '/web/webjars/common/app-shared/unified-view/components/uvSimpleValue/uvSimpleValue.directive.html',
        link: function($scope) {

            // use template and variable approach if available
            if(angular.isDefined($scope.config.variables) && angular.isDefined($scope.config.template)){
                $scope.value = $scope.config.template;
                for(var i = 0; i <  $scope.config.variables.length; i++) {
                    var modelProp = uvUtil.getValue($scope.viewData, $scope.config.modelProp + '.' + $scope.config.variables[i]);
                    $scope.value = $scope.value.replace('{' + i + '}', modelProp);
                }
            } else {
                // otherwise process normally
                $scope.value =  uvUtil.getValue($scope.viewData, $scope.config.modelProp);
            }

            $scope.hasValue = function() {
                return uvUtil.isValidValue($scope.value);
            };

        }
    };
} ]);
