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

describe('DateIsAfterValidatorDirective', function() {
  'use strict';

  var $scope;

  beforeEach(angular.mock.module('htmlTemplates'));
  beforeEach(angular.mock.module('common'), function() {
  });
  beforeEach(angular.mock.inject(['$rootScope', '$compile',
    function($rootScope, $compile) {

      var tpl = angular.element(
          '<div ng-form="testForm">' +
          '<input type="text" name="testDate1" ng-model="model.testDate1" dom-id="testDate1">' +
          '<input type="text" name="testDate2" ng-model="model.testDate2" dom-id="testDate2" wc-date-is-after-validator="testDate1">' +
          '</div>'
      );
      $scope = $rootScope.$new();
      $scope.model = {
        testDate1: '',
        testDate2: ''
      };
      $compile(tpl)($scope);
      $scope.$digest();
    }
  ]));

  it('should pass validation if date1 is before date2 ', function() {
    $scope.model.testDate1 = '2016-01-01';
    $scope.model.testDate2 = '2016-01-02';
    $scope.$apply();

    expect($scope.testForm.$invalid).toBe(false);
  });

  it('should pass validation if date1 is equals to date2 ', function() {
    $scope.model.testDate1 = '2016-01-01';
    $scope.model.testDate2 = '2016-01-01';
    $scope.$apply();

    expect($scope.testForm.$invalid).toBe(false);
  });

  it('should pass validation if date1 is empty', function() {
    $scope.model.testDate1 = '';
    $scope.model.testDate2 = '2016-01-01';
    $scope.$apply();

    expect($scope.testForm.$invalid).toBe(false);
  });

  it('should pass validation if date2 is empty', function() {
    $scope.model.testDate1 = '2016-01-01';
    $scope.model.testDate2 = '';
    $scope.$apply();

    expect($scope.testForm.$invalid).toBe(false);
  });

  it('should pass validation if date1 is not a date', function() {
    $scope.model.testDate1 = '2016-01-';
    $scope.model.testDate2 = '2015-01-01';
    $scope.$apply();

    expect($scope.testForm.$invalid).toBe(false);
  });

  it('should pass validation if date2 is not a date', function() {
    $scope.model.testDate1 = '2016-01-01';
    $scope.model.testDate2 = '2015-01-';
    $scope.$apply();

    expect($scope.testForm.$invalid).toBe(false);
  });

  it('should fail validation if date1 is after date2', function() {
    $scope.model.testDate1 = '2016-10-01';
    $scope.model.testDate2 = '2016-01-02';
    $scope.$apply();

    expect($scope.testForm.$invalid).toBe(true);
  });

});