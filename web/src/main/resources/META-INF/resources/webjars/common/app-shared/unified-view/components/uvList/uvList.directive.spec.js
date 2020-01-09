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

describe('uvList Directive', function() {
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

    describe('listKey-less version', function() {
        beforeEach(angular.mock.inject([ '$compile', '$rootScope', function($compile, $rootScope) {
            $scope = $rootScope.$new();

            $scope.configMock = {
                labelKey: 'KV_FKMU_0004.{var}.RBK',
                modelProp: 'listProp'
            };

            element = $compile('<uv-list config="configMock" view-data="viewDataMock"></uv-list>')($scope);

        } ]));

        it('should render modelProp values as list of dynamic key values', function() {
            $scope.viewDataMock = {
                listProp: [ 'KEY0', 'KEY1', 'KEY2']
            };

            $scope.$digest();

            expect($(element).find('li').length).toBe(3);
            expect($(element).find('li').eq(0).text().trim()).toBe('dynamicLabel-KV_FKMU_0004.KEY0.RBK');
            expect($(element).find('li').eq(1).text().trim()).toBe('dynamicLabel-KV_FKMU_0004.KEY1.RBK');
            expect($(element).find('li').eq(2).text().trim()).toBe('dynamicLabel-KV_FKMU_0004.KEY2.RBK');
            expect($(element).find('uv-no-value').length).toBe(0);
        });

        it('should show empty message if no values', function() {
            $scope.viewDataMock = {
                nothing: 'here'
            };

            $scope.$digest();

            expect($(element).find('li').length).toBe(0);
            expect($(element).find('uv-no-value').length).toBe(1);
        });
    });

    describe('listKey version', function() {

        beforeEach(angular.mock.inject([ '$compile', '$rootScope', function($compile, $rootScope) {
            $scope = $rootScope.$new();

            $scope.configMock = {
                labelKey: 'KV_FKMU_0004.{var}.RBK',
                listKey: 'typ',
                modelProp: 'listProp'
            };

            element = $compile('<uv-list config="configMock" view-data="viewDataMock"></uv-list>')($scope);

        } ]));

        it('should render modelProp values as list of dynamic key values', function() {
            $scope.viewDataMock = {
                listProp: [ {
                    typ: 'KEY0'
                }, {
                    typ: 'KEY1'
                }, {
                    typ: 'KEY2'
                } ]
            };

            $scope.$digest();

            expect($(element).find('li').length).toBe(3);
            expect($(element).find('li').eq(0).text().trim()).toBe('dynamicLabel-KV_FKMU_0004.KEY0.RBK');
            expect($(element).find('li').eq(1).text().trim()).toBe('dynamicLabel-KV_FKMU_0004.KEY1.RBK');
            expect($(element).find('li').eq(2).text().trim()).toBe('dynamicLabel-KV_FKMU_0004.KEY2.RBK');
            expect($(element).find('uv-no-value').length).toBe(0);
        });

        it('should show empty message if no values', function() {
            $scope.viewDataMock = {
                nothing: 'here'
            };

            $scope.$digest();

            expect($(element).find('li').length).toBe(0);
            expect($(element).find('uv-no-value').length).toBe(1);
        });

    });
});
