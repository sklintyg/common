/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
angular.module('common').directive('ueYearPicker', ['ueUtil', '$timeout', 'common.DateUtilsService', 'common.DatePickerOpenService',
  function(ueUtil, $timeout, dateUtils, DatePickerOpenService) {
    'use strict';

    return {
      restrict: 'E',
      scope: {
        form: '=',
        config: '=',
        model: '='
      },
      templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueYearPicker/ueYearPicker.directive.html',
      link: function($scope) {
        ueUtil.standardSetup($scope);

        //configure
        var currentYear = new Date().getFullYear();
        $scope.format = 'yyyy';
        $scope.datePickerOptions = {
          datepickerMode: 'year',
          maxMode: 'year',
          minMode: 'year',
          yearRows: 3,
          yearColumns: 4,
          customClass: function(data) {
            if (currentYear === data.date.getFullYear()) {
              return 'year current-year';
            }
          }

        };

        if (dateUtils.isYear($scope.config.minYear)) {
          // IE11 Date uses timezone in a strange way, casusing parsing a string with only year resolution eg. "2017" to actually be 2016-12-31T23:00:00.
          // To work around this, we make sure the data the yearpicker works with always is a bit into the year.
          $scope.datePickerOptions.minDate = new Date($scope.config.minYear + '-01-10');
        }

        if (dateUtils.isYear($scope.config.maxYear)) {
          $scope.datePickerOptions.maxDate = new Date($scope.config.maxYear + '-01-10');
        }

        $scope.pickerState = {
          isOpen: false
        };

        $scope.toggleOpen = function($event) {
          $event.preventDefault();
          $event.stopPropagation();
          $timeout(function() {
            $scope.pickerState.isOpen = !$scope.pickerState.isOpen;
            DatePickerOpenService.update($scope.pickerState);
          });
        };

        $scope.isFocused = false;
        $scope.toggleFocus = function() {
          $scope.isFocused = !$scope.isFocused;
        };

        $scope.onDatepickerInputFieldBlur = function() {
          $scope.toggleFocus();
        };

        $scope.focused = function() {
          $scope.toggleFocus();
        };

        $scope.$watch('modelGetterSetter()', function(newVal, oldVal) {
          if (newVal || newVal !== oldVal) {
            if (dateUtils.isYear(newVal)) {
              $scope.datePickerOptions.initDate = new Date(newVal + '-01-10');
            } else {
              $scope.datePickerOptions.initDate = new Date();
            }

          }
        });
      }
    };
  }]).directive('ueYearOnlyParser', ['$log', 'common.DateUtilsService',
  function($log, dateUtils) {
    'use strict';
    return {
      priority: 1,
      restrict: 'A',
      require: 'ngModel',
      link: function(scope, element, attrs, ngModel) {
        //We should allow invalid/incomplete date values only consisting of year
        dateUtils.addLooseDateParser(ngModel);

      }
    };
  }]);
