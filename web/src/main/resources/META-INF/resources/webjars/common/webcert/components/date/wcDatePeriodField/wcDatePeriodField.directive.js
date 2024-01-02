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
angular.module('common').directive('wcDatePeriodField', ['$rootScope', '$timeout', 'common.DatePickerOpenService', 'common.DateUtilsService',
    function($rootScope, $timeout, datePickerOpen, dateUtils) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                model: '=',
                field: '@',
                index: '@',
                type: '@',
                format: '@',
                domId: '@',
                maxDate: '@',
                onBlur: '&'
            },
            templateUrl: '/web/webjars/common/webcert/components/date/wcDatePeriodField/wcDatePeriodField.directive.html',
            require:'wcDatePeriodField',
            controller: function($scope) {

                if ($scope.domId === undefined) {
                    $scope.domId = $scope.field + '-' + $scope.index + '-' + $scope.type;
                }

                if($scope.format === undefined){
                    $scope.format = 'yyyy-MM-dd';
                }

                if($scope.maxDate === undefined){
                    $scope.maxDate = null;
                } else {
                    $scope.maxDate = '\'' + $scope.maxDate + '\'';
                }

                var PickerState = {
                    isOpen: false
                };
                $scope.pickerState = PickerState;

                $scope.toggleOpen = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $timeout(function() {
                        $scope.pickerState.isOpen = !$scope.pickerState.isOpen;
                        datePickerOpen.update($scope.pickerState);
                    });
                };

                $scope.dateOptions = {};

                this.onBlur = $scope.onBlur;

                this.fieldOptions = {
                    field : $scope.field,
                    index : $scope.index,
                    type : $scope.type
                };

                $scope.isFocused = false;
                $scope.toggleFocus = function() {
                    if ($scope.isFocused) {
                        $scope.isFocused = false;
                    } else {
                        $scope.isFocused = true;
                    }
                };

                $scope.onInputBlur = function() {
                    $scope.toggleFocus();
                    $scope.onblur();
                };

                $scope.focused = function() {
                    $scope.toggleFocus();
                };
            },
            link: function(scope, element, attrs, ctrl) {
                var waitForModelValue = scope.$watch(watchNode, function(newVal) {
                    if (newVal) {
                        if(dateUtils.isDate(newVal)) {
                            scope.dateOptions.initDate = new Date(newVal);
                        } else {
                            scope.dateOptions.initDate = new Date();
                        }
                        waitForModelValue();
                        
                    }
                });

                function watchNode() {
                    return scope.model[scope.field][scope.index].period[scope.type];
                }
            }
        };
    }])
    .directive('wcDatePeriodFieldInput', ['common.DateUtilsService',
        function(dateUtils) {
            'use strict';
            return {
                priority: 1,
                restrict: 'A',
                require:['ngModel', '^wcDatePeriodField', '^wcDatePeriodManager'],
                link: function(scope, element, attrs, ctrls) {
                    var ngModel = ctrls[0];

                    dateUtils.addLooseDateParser(ngModel);

                    var wcDatePeriodField = ctrls[1];
                    var wcDatePeriodManager = ctrls[2];

                    wcDatePeriodManager.registerDatePeriod(ngModel, wcDatePeriodField.fieldOptions);

                    scope.onblur = function() {
                        wcDatePeriodManager.applyToDateCodes(wcDatePeriodField.fieldOptions.index);
                        if (wcDatePeriodField.onBlur) {
                            wcDatePeriodField.onBlur();
                        }
                    };

                }
            };
        }]);
