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

describe('DatePeriodFieldDirective', function() {
  'use strict';

  var $scope;

  beforeEach(angular.mock.module('htmlTemplates'));
  beforeEach(angular.mock.module('common'), function() {
  });
  beforeEach(angular.mock.inject(['$rootScope', '$compile',
    function($rootScope, $compile) {

      var validatorCtrl = {
        registerDatePeriod: function() {
        }
      };

      var tpl = angular.element(
          '<div ng-form="testForm">' +
          '<span wc-date-period-field model="model" field="test" index="0" type="from" dom-id="test"></span>' +
          '</div>'
      );
      tpl.data('$wcDatePeriodManagerController', validatorCtrl);
      $scope = $rootScope.$new();
      $scope.model = {
        test: [{
          period: {
            from: null
          }
        }]
      };
      $compile(tpl)($scope);
      $scope.$digest();
    }
  ]));

  it('should allow partial dates', function() {
    $scope.model.test[0].period.from = '2016-0';
    $scope.$apply();

    expect($scope.testForm.test.$viewValue).toBe('2016-0');
    expect($scope.testForm.test.$modelValue).toBe('2016-0');
  });

  it('should allow partial dates without converting to a javscript date object', function() {
    $scope.model.test[0].period.from = '2';
    $scope.$apply();

    expect($scope.testForm.test.$viewValue).toBe('2');
    expect($scope.testForm.test.$modelValue).toBe('2');
  });

  it('should allow non date input', function() {
    $scope.model.test[0].period.from = 'tras';
    $scope.$apply();

    expect($scope.testForm.test.$viewValue).toBe('tras');
    expect($scope.testForm.test.$modelValue).toBe('tras');
  });

});