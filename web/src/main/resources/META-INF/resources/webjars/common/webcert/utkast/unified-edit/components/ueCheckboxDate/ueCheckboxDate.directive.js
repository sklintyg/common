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
angular.module('common').directive('ueCheckboxDate', [ '$timeout', 'common.DateUtilsService', 'common.dynamicLabelService',
    'common.AtticHelper', 'common.UtkastValidationService', 'common.UtkastViewStateService',
    function($timeout, dateUtils, dynamicLabelService, AtticHelper, UtkastValidationService, UtkastViewState) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueCheckboxDate/ueCheckboxDate.directive.html',
            link: function($scope) {

                $scope.validation = UtkastViewState.validation;

                // Restore data model value form attic if exists
                AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

                // Clear attic model and destroy watch on scope destroy
                AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);

                $scope.checkbox = {};

                $scope.$watch('model.' + $scope.config.modelProp, function(newVal, oldVal) {
                    if (newVal) {
                        $scope.checkbox.checked = true;
                    } else {
                        $scope.checkbox.checked = false;
                    }
                });

                $scope.$watch('checkbox.checked', function(newVal, oldVal) {
                    if (newVal) {
                        if (!$scope.model[$scope.config.modelProp]) {
                            $scope.model[$scope.config.modelProp] = dateUtils.todayAsYYYYMMDD();
                            $scope.validate();
                        }
                    } else if (oldVal !== undefined) {
                        // Clear date if check is unchecked
                        if ($scope.model[$scope.config.modelProp] !== undefined) {
                            $scope.model[$scope.config.modelProp] = undefined;
                            $scope.validate();
                        }
                    }
                });

                $scope.getDynamicText = function(key) {
                    return dynamicLabelService.getProperty(key);
                };

                $scope.validate = function() {
                    // When a date is selected from the date popup a blur event is sent.
                    // In the current version of Angular UI this blur event is sent before utkast model is updated
                    // This timeout ensures we get the new value in $scope.model
                    $timeout(function() {
                        UtkastValidationService.validate($scope.model);
                    });
                };
            }
        };
    }]);

