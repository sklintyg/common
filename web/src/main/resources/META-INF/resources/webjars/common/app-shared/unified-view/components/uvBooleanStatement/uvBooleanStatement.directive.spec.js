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

describe('uvBooleanStatement Directive', function() {
  'use strict';

  var $scope;
  var element;

  beforeEach(angular.mock.module('htmlTemplates'));
  beforeEach(angular.mock.module('common'));

  beforeEach(module('common', function($compileProvider) {
    // Create a mocked version of the dynamic label directive for easier and more
    // focused unittesting of THIS directive.
    // (That the dynamic-label directive works should be tested by that directive)
    $compileProvider.directive('dynamicLabel', function() {
      return {
        priority: 100,
        terminal: true,
        restrict: 'A',
        scope: {
          key: '@'
        },
        template: '<span></span>',
        link: function($scope, $element) {
          $element.append('dynamicLabel-' + $scope.key);
        }
      };
    });
  }));

  beforeEach(angular.mock.inject(['$compile', '$rootScope', function($compile, $rootScope) {
    $scope = $rootScope.$new();

    $scope.viewDataMock = {
      property1: {
        isRelevant: true
      }
    };

    $scope.configMock = {
      labelKey: 'DUMMY.1.RBK',
      modelProp: 'property1.isRelevant'
    };

    element = $compile('<uv-boolean-statement config="configMock" view-data="viewDataMock"></uv-boolean-statement>')($scope);

  }]));

  it('should display title when labelKey is configured', function() {
    $scope.$digest();
    expect($(element).find('h4').text()).toBe('dynamicLabel-DUMMY.1.RBK');
  });
  it('should not display title when labelKey is not configured', function() {
    $scope.configMock.labelKey = undefined;
    $scope.$digest();
    expect($(element).find('h4').length).toBe(0);
  });

  it('should display "Ja" when value is true', function() {
    $scope.$digest();
    expect(element.isolateScope().getValue()).toEqual('Ja');
    expect($(element).find('span').text()).toContain('Ja');
  });

  it('should display "Ej angivet" when value is false', function() {
    $scope.viewDataMock.property1.isRelevant = false;

    $scope.$digest();
    expect(element.isolateScope().getValue()).toEqual('Ej angivet');
    expect($(element).find('span').text()).toContain('Ej angivet');
  });

  it('should by default display "Ej angivet" when value is undefined', function() {
    $scope.viewDataMock.property1 = undefined;

    $scope.$digest();
    expect(element.isolateScope().getValue()).toEqual('Ej angivet');
    expect($(element).find('span').text()).toContain('Ej angivet');
  });

});
