/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
angular.module('common').directive('uvEnumValue', [ 'uvUtil', 'common.messageService', 'common.dynamicLabelService',
    function(uvUtil, messageService, dynamicLabelService) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            viewData: '='
        },
        templateUrl: '/web/webjars/common/app-shared/unified-view/components/uvEnumValue/uvEnumValue.directive.html',
        link: function($scope) {

            $scope.value = undefined;

            var key = uvUtil.getValue($scope.viewData, $scope.config.modelProp);

            if ($scope.config.values[key]) {
                var translationKey = $scope.config.values[key];
                // Try to find the key in the messageService first
                var result = messageService.propertyExists(translationKey);

                if (!result) {
                    result = dynamicLabelService.getProperty(translationKey);

                    $scope.$on('dynamicLabels.updated', function() {
                        $scope.value = dynamicLabelService.getProperty(translationKey);
                    });
                }

                $scope.value = result;
            }

            $scope.hasValue = function() {
                return uvUtil.isValidValue($scope.value);
            };

        }
    };
} ]);
