/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

describe('uvKodverk Directive', function() {
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
                replace: true,
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

    beforeEach(angular.mock.inject([ '$compile', '$rootScope', function($compile, $rootScope) {
        $scope = $rootScope.$new();

        $scope.configMock = {
            modelProp: 'data',
            kvModelProps: [ 'data.KEY0', 'data.KEY1', 'data.KEY2' ],
            kvLabelKeys: [ 'KV0.{var}.RBK', 'KV1.{var}.RBK', 'KV2.{var}.RBK' ]
        };

        element = $compile('<uv-kodverk-value config="configMock" view-data="viewDataMock"></uv-kodverk-value>')($scope);

    } ]));

    it('should render modelProps values as resolved dynamic labelkey spans', function() {
        $scope.viewDataMock = {
            data: {
                KEY0: 'VALUE_0',
                KEY1: 'VALUE_1',
                KEY2: 'VALUE_2'
            }
        };

        $scope.$digest();

        var expectedElements = $(element).find('span.value');
        expect(expectedElements.length).toBe(3);
        expect(expectedElements.eq(0).text().trim()).toBe('dynamicLabel-KV0.VALUE_0.RBK');
        expect(expectedElements.eq(1).text().trim()).toBe('dynamicLabel-KV1.VALUE_1.RBK');
        expect(expectedElements.eq(2).text().trim()).toBe('dynamicLabel-KV2.VALUE_2.RBK');

    });

    it('should render "Ej angivet" if no values are resolved', function() {
        $scope.viewDataMock = {};

        $scope.$digest();

        var expectedElements = $(element).find('span.value');
        expect(expectedElements.length).toBe(0);
        expect($(element).find('uv-no-value').length).toBe(1);

    });

});
