/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

/**
 * wcField directive. Used to abstract common layout for full-layout form fields in cert modules
 */
angular.module('common').directive('wcField',
    [ 'common.messageService', 'common.dynamicLabelService',
        function(messageService, dynamicMessageService) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                templateUrl: '/web/webjars/common/webcert/utkast/wcField.directive.html',
                scope: {
                    fieldLabel: '@',
                    fieldDynamicLabel: '@',
                    fieldNumber: '@?',
                    fieldHelpText: '@',
                    fieldDynamicHelpText: '@',
                    fieldHasErrors: '=',
                    fieldTooltipPlacement: '@',
                    filled: '@?'
                },
                controller: function($scope) {

                    if ($scope.filled === undefined) {
                        $scope.filled = 'true';
                    }

                    if ($scope.fieldNumber === null) {
                        $scope.fieldNumber = undefined;
                    }

                    if ($scope.fieldTooltipPlacement === undefined) {
                        $scope.placement = 'right';
                    } else {
                        $scope.placement = $scope.fieldTooltipPlacement;
                    }

                    $scope.getMessage = function(key) {
                        return messageService.getProperty(key);
                    };

                    $scope.getDynamicText = function(key) {
                        return dynamicMessageService.getProperty(key);
                    };
                }
            };
        }]);
