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

describe('wcPersonNumber', function() {
  'use strict';

  beforeEach(angular.mock.module('common'));

  var $scope;

  // Create a form to test the validation directive on.
  beforeEach(angular.mock.inject(['$compile', '$rootScope', function($compile, $rootScope) {
    $scope = $rootScope.$new();
    $scope.model = {
      test: null
    };

    var el = angular
    .element('<form name="form"><input ng-model="model.test" name="test" wc-person-number></form>');
    $compile(el)($scope);
    $scope.$digest();
  }]));

  // Pass

  it('should pass with a valid "personnummer" with format "yyyyMMdd-nnnn"', function() {
    $scope.form.test.$setViewValue('19121212-1212');

    expect($scope.model.test).toEqual('19121212-1212');
    expect($scope.form.$valid).toBeTruthy();
  });

  it('should pass with a valid "personnummer" with format "yyyyMMdd-nnnn"', function() {
    $scope.form.test.$setViewValue('19121212-1212');

    expect($scope.model.test).toEqual('19121212-1212');
    expect($scope.form.$valid).toBeTruthy();
  });

  it('should pass with a valid "samordningsnummer" with format "yyyyMMnn-nnnn"', function() {
    $scope.form.test.$setViewValue('19121272-1219');

    expect($scope.model.test).toEqual('19121272-1219');
    expect($scope.form.$valid).toBeTruthy();
  });

  it('should add dash for 8 or 9 chars typed', function() {
    $scope.form.test.$setViewValue('19121212');
    expect($scope.form.test.$viewValue).toEqual('19121212-');

    $scope.form.test.$setViewValue('19121212'); // reset so length differs between oldValue and newValue
    $scope.form.test.$setViewValue('191212121');
    expect($scope.form.test.$viewValue).toEqual('19121212-1');
  });

  // Fail

  it('should fail if "personnummer" has too few digits', function() {
    $scope.form.test.$setViewValue(null);

    expect($scope.model.test).toBeNull();
    expect($scope.form.$valid).toBeFalsy();

    $scope.form.test.$setViewValue(undefined);

    expect($scope.model.test).toBeUndefined();
    expect($scope.form.$valid).toBeFalsy();
  });

  it('should fail if "personnummer" has too few digits', function() {
    $scope.form.test.$setViewValue('121212-1212');

    expect($scope.model.test).toBeUndefined();
    expect($scope.form.$valid).toBeFalsy();
  });

  it('should fail if "personnummer" has invalid check digit', function() {
    $scope.form.test.$setViewValue('19121212-1213');

    expect($scope.model.test).toBeUndefined();
    expect($scope.form.$valid).toBeFalsy();
  });

  it('should fail with if "personnummer" has invalid characters', function() {
    $scope.form.test.$setViewValue('19121212.1212');

    expect($scope.model.test).toBeUndefined();
    expect($scope.form.$valid).toBeFalsy();
  });

  it('should fail if "samordningsnummer" has invalid check digit', function() {
    $scope.form.test.$setViewValue('19121272-1213');

    expect($scope.model.test).toBeUndefined();
    expect($scope.form.$valid).toBeFalsy();
  });

  it('should fail with if "samordningsnummer" has invalid date', function() {
    $scope.form.test.$setViewValue('19121292-1215');

    expect($scope.model.test).toBeUndefined();
    expect($scope.form.$valid).toBeFalsy();
  });

  it('should fail with if "samordningsnummer" has invalid characters', function() {
    $scope.form.test.$setViewValue('19121272.1219');

    expect($scope.model.test).toBeUndefined();
    expect($scope.form.$valid).toBeFalsy();
  });
});
