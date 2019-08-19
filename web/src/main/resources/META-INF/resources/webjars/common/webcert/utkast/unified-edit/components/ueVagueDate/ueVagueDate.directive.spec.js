/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

describe('vagueDate', function() {
  'use strict';

  var $scope;
  var element;

  beforeEach(angular.mock.module('htmlTemplates'));

  beforeEach(angular.mock.module('common'));

  beforeEach(inject(function($compile, $rootScope) {
    $scope = $rootScope.$new();
    $scope.model = {
      date: undefined,
      clear: function() {
      }
    };
    $scope.config = {
      type: 'ue-vague-date',
      modelProp: 'date'
    };
    element =
        angular.element('<form><ue-vague-date model="model" config="config" form="form"></ue-vague-date></form>');
    $compile(element)($scope);
    $scope.$digest();
    $scope = element.find('ue-vague-date').isolateScope();
  }));

  it('Initial load should set year and month from model', function() {

    $scope.model.date = '2017-01-00';
    $scope.$apply();

    expect($scope.years.length).toBe(3);
    expect($scope.vagueDateModel.year).toBe('2017');
    expect($scope.vagueDateModel.month).toBe('01');
    expect($scope.vagueDateModel.monthEnabled).toBeTruthy();
  });

  it('If a year is selected month should be selectable', function() {
    expect($scope.years.length).toBe(4);
    expect($scope.vagueDateModel.monthEnabled).toBeFalsy();

    $scope.vagueDateModel.year = (new Date()).getFullYear();
    $scope.$apply();

    expect($scope.model.date).toBe((new Date()).getFullYear() + '--00');
    expect($scope.years.length).toBe(3);
    expect($scope.vagueDateModel.monthEnabled).toBeTruthy();
  });

  it('If year "0000" is selected month should be set to "00" and not selectable', function() {
    expect($scope.vagueDateModel.monthEnabled).toBeFalsy();

    $scope.vagueDateModel.year = '0000';
    $scope.$apply();

    expect($scope.model.date).toBe('0000-00-00');
    expect($scope.vagueDateModel.month).toBe('00');
    expect($scope.vagueDateModel.monthEnabled).toBeFalsy();
  });

  it('If year and month is selected "Ange år" and "Ange månad" should no longer be available', function() {
    expect($scope.years.length).toBe(4);   // "Ange år", "0000", this year and last year
    expect($scope.months.length).toBe(14); // "Ange månad", "00" and 01-12

    $scope.vagueDateModel.year = (new Date()).getFullYear();
    $scope.$apply();
    $scope.vagueDateModel.month = '06';
    $scope.$apply();

    expect($scope.model.date).toBe((new Date()).getFullYear() + '-06-00');
    expect($scope.years.length).toBe(3);
    expect($scope.months.length).toBe(13);
  });

  it('If year is first set to 0000 and then changed to current year: month should reset back to "Ange månad"', function() {
    expect($scope.years.length).toBe(4);   // "Ange år", "0000", this year and last year
    expect($scope.months.length).toBe(14); // "Ange månad", "00" and 01-12

    $scope.vagueDateModel.year = '0000';
    $scope.$apply();

    expect($scope.months.length).toBe(13); // "Ange månad" should be gone
    expect($scope.vagueDateModel.month).toBe('00');

    $scope.vagueDateModel.year = (new Date()).getFullYear();
    $scope.$apply();

    expect($scope.months.length).toBe(14); // "Ange månad" should be back
    expect($scope.vagueDateModel.month).toBe('');
  });

  it('If year is first set to last year and then changed to current year: month should not reset back to "Ange månad"', function() {
    expect($scope.years.length).toBe(4);   // "Ange år", "0000", this year and last year
    expect($scope.months.length).toBe(14); // "Ange månad", "00" and 01-12

    $scope.vagueDateModel.year = (new Date()).getFullYear();
    $scope.$apply();
    $scope.vagueDateModel.month = '02';
    $scope.$apply();

    expect($scope.months.length).toBe(13); // "Ange månad" should be gone
    expect($scope.vagueDateModel.month).toBe('02');

    $scope.vagueDateModel.year = (new Date()).getFullYear() - 1;
    $scope.$apply();

    expect($scope.months.length).toBe(13); // "Ange månad" should be gone
    expect($scope.vagueDateModel.month).toBe('02');
  });

});

