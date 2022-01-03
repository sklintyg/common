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
angular.module('common').directive('ueGrid', [ 'common.UtkastValidationViewState', '$parse',
    function(UtkastValidationViewState, $parse) {
    'use strict';
    return {
        restrict: 'E',
        scope: {
            form: '=',
            config: '=',
            model: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/containers/ueGrid/ueGrid.directive.html',
        link: function($scope) {
            var firstRowError =  [{key: $scope.config.firstRequiredRowKey, type: 'ue-grid'}];
            $scope.validation = UtkastValidationViewState;
            $scope.getColSize = function(row, $index) {
                if ($scope.config.colSizes) {
                    return $scope.config.colSizes[$index];
                } else {
                    return 12 / row.length;
                }
            };

            $scope.useRowValidation = !!$scope.config.validationRows;
            $scope.getRowValidationKeys = function(rowIndex, colIndex) {
                if (!validOrder() && rowIndex === $scope.config.firstRequiredRow) {
                    return firstRowError;
                } else {
                    return $scope.config.validationRows[colIndex];
                }
            };

            function validOrder() {
                if (angular.isDefined($scope.config.firstRequiredRow) && angular.isArray($scope.validation.messagesByField)) {
                    if ($scope.validation.messagesByField[$scope.config.firstRequiredRowKey.toLowerCase()]) {
                        return false;
                    }
                }
                return true;
            }

            if ($scope.config.validationContext) {
                $scope.validationKeys = [];
                $scope.validationKeys.push({key: $scope.config.validationContext.key.toLowerCase(), type: $scope.config.validationContext.type});
            }

            $scope.getFirstRowValidations = function(index) {
                if (index === $scope.config.firstRequiredRow && $scope.config.modelProp) {
                    if (angular.isDefined($scope.config.firstRequiredRow) && angular.isArray($scope.validation.messagesByField)) {
                        var required = $scope.validation.messagesByField[$scope.config.firstRequiredRowKey.toLowerCase()];
                        return required || $scope.validation.messagesByField[$scope.config.modelProp.toLowerCase()];
                    }
                }
            };
            
        }
    };

}]);