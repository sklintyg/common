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

describe('wcIntygPatientNamnPersonnummerDirective', function() {
  'use strict';

  var $scope;
  var element;

  beforeEach(angular.mock.module('htmlTemplates'));
  beforeEach(angular.mock.module('common'));

  beforeEach(angular.mock.inject(['$compile', '$rootScope', function($compile, $rootScope) {
    $scope = $rootScope.$new();

    $rootScope.lang = 'sv';

    $scope.patientName = '';
    $scope.personId = '';
    $scope.oldPersonId = '';

    element = $compile(
        '<div wc-intyg-patient-namn-personnummer patient-name="patientName" person-id="personId" old-person-id="oldPersonId"></div>'
    )($scope);

  }]));

  it('should display name and personId', function() {
    $scope.patientName = 'Name';
    $scope.personId = '123456-7890';
    $scope.$digest();

    expect($(element).find('#patientNamnPersonnummer').text()).toContain('Name - 123456-7890');
    expect($(element).find('.old-person-id').length).toBe(0);
  });

  it('should display name, personId and oldPersonId is the same', function() {
    $scope.patientName = 'Name';
    $scope.personId = '123456-7890';
    $scope.oldPersonId = '123456-7890';
    $scope.$digest();

    expect($(element).find('#patientNamnPersonnummer').text()).toContain('Name - 123456-7890');
    expect($(element).find('.old-person-id').length).toBe(0);
  });

  it('should display name, personId and oldPersonId is different', function() {
    $scope.patientName = 'Name';
    $scope.personId = '123456-7890';
    $scope.oldPersonId = '223456-7890';
    $scope.$digest();

    expect($(element).find('#patientNamnPersonnummer').text()).toContain('Name - 123456-7890');
    expect($(element).find('.old-person-id').text()).toContain('f.d. 223456-7890');
  });

});
