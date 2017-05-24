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

angular.module('common').directive('uvKodverkValue', [ 'uvUtil', function(uvUtil) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            viewData: '='
        },
        templateUrl: '/web/webjars/common/minaintyg/components/unified-view/components/uvKodverkValue/uvKodverkValue.directive.html',
        link: function($scope) {

            $scope.labelKeys = _buildLabelKeys();

            function _buildLabelKeys() {
                var labelKeys = [];
                for (var i = 0; i < $scope.config.modelProp.length; i++) {
                    var modelValue = uvUtil.getValue($scope.viewData, $scope.config.modelProp[i]);
                    if (modelValue) {
                        var resolvedKey = $scope.config.labelKey[i].replace('{var}', modelValue);
                        labelKeys.push({
                            key: resolvedKey,
                            modelProp: $scope.config.modelProp[i]
                        });
                    }
                }
                return labelKeys;
            }
        }
    };
} ]);
