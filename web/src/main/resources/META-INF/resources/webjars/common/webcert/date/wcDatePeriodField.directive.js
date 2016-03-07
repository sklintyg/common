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

angular.module('common').directive('wcDatePeriodField',
    function($rootScope, $timeout) {
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
                maxDate: '@'
            },
            templateUrl: '/web/webjars/common/webcert/date/wcDatePeriodField.directive.html',
            require:'wcDatePeriodField',
            controller: function($scope) {

                if($scope.format === undefined){
                    $scope.format = 'yyyy-MM-dd';
                }

                if($scope.maxDate === undefined){
                    $scope.maxDate = null;
                } else {
                    $scope.maxDate = '\'' + $scope.maxDate + '\'';
                }

                $scope.isOpen = false;
                $scope.toggleOpen = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $timeout(function() {
                        $scope.isOpen = !$scope.isOpen;
                    });
                };

                this.fieldOptions = {
                    field : $scope.field,
                    index : $scope.index,
                    type : $scope.type
                };
            }
        };
    })
    .directive('wcDatePeriodFieldInput', ['common.DateUtilsService',
        function(dateUtils) {
            'use strict';
            return {
                restrict: 'A',
                require:['ngModel', '^wcDatePeriodField', '^wcDatePeriodValidator'],
                link: function(scope, element, attrs, ctrls) {
                    var ngModel = ctrls[0];
                    dateUtils.addDateParserFormatter(ngModel);

                    var wcDatePeriodField = ctrls[1];
                    var wcDatePeriodValidator = ctrls[2];

                    wcDatePeriodValidator.registerDatePeriod(ngModel, wcDatePeriodField.fieldOptions);
                }
            };
        }]);
