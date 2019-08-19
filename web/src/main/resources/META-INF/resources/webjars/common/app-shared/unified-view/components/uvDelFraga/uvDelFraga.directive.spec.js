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

describe('uvDelFraga Directive', function() {
  'use strict';

  var $scope;
  var element;

  beforeEach(angular.mock.module('htmlTemplates'));

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
      prop: {
        subprop: true
      },
      another: 'test'
    };

    $scope.configMock = {
      labelKey: 'DELFRAGA1.1.RBK',
      components: [{
        type: 'uv-boolean-value',
        labelKey: 'FRG_26.RBK',
        modelProp: 'prop.subprop'
      }, {
        type: 'uv-simple-value',
        labelKey: 'FRG_33.RBK',
        modelProp: 'another'
      }]
    };

    element = $compile('<uv-del-fraga config="configMock" view-data="viewDataMock"></uv-del-fraga>')($scope);

  }]));

  it('should display title when labelKey is configured', function() {
    $scope.$digest();
    expect($(element).find('> div > div > h4').text()).toBe('dynamicLabel-DELFRAGA1.1.RBK');
  });

  it('should render child components', function() {
    $scope.$digest();

    expect($(element).find('uv-boolean-value').length).toBe(1);
    expect($(element).find('uv-boolean-value h4').text()).toBe('dynamicLabel-FRG_26.RBK');

    expect($(element).find('uv-simple-value').length).toBe(1);
    expect($(element).find('uv-simple-value h4').text()).toBe('dynamicLabel-FRG_33.RBK');
  });

});
