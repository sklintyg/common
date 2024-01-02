/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('common').directive('selectableFieldWrapper', ['$parse', 'common.ObjectHelper', 'common.messageService', function($parse, objectHelper, messageService) {
    'use strict';

    return {
        restrict: 'A',
        transclude: true,
        scope: {
            labelKey: '@',
            fieldConfig: '=',
            fieldId: '=',
            certModel: '=',
            summary: '=',
            showDeselected: '='
        },
        templateUrl: '/web/webjars/common/minaintyg/components/customize-pdf/selectableFieldWrapper.directive.html',
        controller: function($scope) {

            $scope.fc = null;

            var _parseSingleValue = function (expression) {
                if (angular.isFunction(expression)) {
                    return objectHelper.isModelValue(expression($scope.certModel));
                } else {
                    var value = $parse(expression)($scope.certModel);
                    return objectHelper.isModelValue(value);
                }
            };

            /**
             * Determines if this field should be rendered or not, depending on wether
             * it has a value, and/or if it's in summary mode.
             */
            $scope.showContent = function() {
                //cert may not have been loaded yet, so we can't check if it has value..
                if (!$scope.certModel) {
                    return false;
                }

                function findIndexById(fieldId) {
                    var result = null;
                    angular.forEach($scope.fieldConfig, function(fc) {
                        if (fc.id === fieldId) {
                            result = fc;
                        }
                    });
                    return result;
                }

                if (angular.isDefined($scope.fieldId)) {
                    $scope.fc = findIndexById($scope.fieldId);
                } else {
                    $scope.fc = $scope.fieldConfig;
                }

                if (!$scope.fc) {
                    return false;
                }

                var fieldHasValue = false;

                angular.forEach($scope.fc.fields, function(value) {
                    fieldHasValue = fieldHasValue || _parseSingleValue(value);
                });
                //store findings here
                $scope.fc.hasValue = fieldHasValue;

                if (!$scope.summary) {
                    //if not summary, only rule is that all fields with value should be shown
                    return fieldHasValue;
                } else {
                    //summary mode: are we in selected section or the unselected section (2 lists)
                    if ($scope.showDeselected) {
                        //rule for lists of de-selected items
                        return fieldHasValue && !$scope.fc.selected && !$scope.fc.mandatory;
                    } else {
                        return fieldHasValue && ($scope.fc.selected || $scope.fc.mandatory);
                    }
                }

            };

        }
    };
} ]);
