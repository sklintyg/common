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

describe('uvAlertValue Directive', function() {
  'use strict';

  var $scope;
  var isolateScope;
  var element;

  beforeEach(angular.mock.module('htmlTemplates'));
  beforeEach(angular.mock.module('common'));

  beforeEach(angular.mock.inject(['$compile', '$rootScope', function($compile, $rootScope) {
    $scope = $rootScope.$new();

    $scope.viewDataMock = {};

    $scope.configMock = {
      labelKey: 'FRG_1.RBK'
    };

    element = $compile(
        '<uv-alert-value config="configMock" view-data="viewDataMock"></uv-alert-value>'
    )($scope);

    $scope.$digest();
    isolateScope = element.isolateScope();

  }]));

  it('should display message by default', function() {
    $scope.configMock.showExpression = undefined;
    expect(isolateScope.showMessage()).toBeTruthy();
  });

  it('should not display message if showexpression returns false', function() {
    var fakeExpression = jasmine.createSpy('fakeexpression').and.returnValue(false);
    $scope.configMock.showExpression = fakeExpression;
    $scope.$digest();

    expect(fakeExpression).toHaveBeenCalled();
    expect(isolateScope.showMessage()).toBeFalsy();
  });

  it('should display message if showexpression returns true', function() {
    var fakeExpression = jasmine.createSpy('fakeexpression').and.returnValue(true);
    $scope.configMock.showExpression = fakeExpression;
    $scope.$digest();

    expect(fakeExpression).toHaveBeenCalled();
    expect(isolateScope.showMessage()).toBeTruthy();
  });

});
