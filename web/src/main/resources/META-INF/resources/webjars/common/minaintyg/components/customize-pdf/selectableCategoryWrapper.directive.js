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
angular.module('common').directive('selectableCategoryWrapper',['$parse', 'common.ObjectHelper', function($parse, objectHelper) {
    'use strict';

    return {
        restrict: 'E',
        transclude: true,
        scope: {
            categoryLabel: '@',
            fieldConfigs: '=',
            certModel: '=',
            summary: '='
        },
        templateUrl: '/web/webjars/common/minaintyg/components/customize-pdf/selectableCategoryWrapper.directive.html',
        controller: function($scope) {

            var _parseSingleValue = function (expression) {
                var result = null;
                if (angular.isFunction(expression)) {
                    result =  objectHelper.isModelValue(expression($scope.certModel));
                } else {
                    var value = $parse(expression)($scope.certModel);
                    result = objectHelper.isModelValue(value);
                }
                return result;
            };

            var _parseSingleFieldConfig = function (fieldConfig) {
                var anyValidValue = false;
                angular.forEach(fieldConfig.fields, function(fieldValuePath) {
                    anyValidValue = anyValidValue || _parseSingleValue(fieldValuePath);
                });
                return anyValidValue;

            };

            $scope.haveContent = function() {
                //cert may not have been loaded yet..
                if (!$scope.certModel) {
                    return false;
                }

                //Iterate through all configured fieldconfigs to determine if any field has a value in the
                // model (we should only show those that have a value)
                var anyValidValue = false;
                angular.forEach($scope.fieldConfigs, function(fieldConfig) {
                    if (angular.isArray(fieldConfig)) {
                        angular.forEach(fieldConfig, function(nestedFieldConfig) {
                            anyValidValue = anyValidValue || _parseSingleFieldConfig(nestedFieldConfig);
                        });
                    } else {
                        anyValidValue = anyValidValue || _parseSingleFieldConfig(fieldConfig);
                    }

                });
                return anyValidValue;

            };


        }


    };
}]);
