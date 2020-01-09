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

describe('uvKategori Directive', function() {
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

    beforeEach(angular.mock.inject([ '$compile', '$rootScope', function($compile, $rootScope) {
        $scope = $rootScope.$new();

        $scope.viewDataMock = {};

        $scope.configMock = {
            labelKey: 'DUMMY.1.RBK',
            components: [ {
                type: 'uv-fraga',
                labelKey: 'FRG_26.RBK'
            } ]
        };

        element = $compile('<uv-kategori config="configMock" view-data="viewDataMock"></uv-kategori>')($scope);

    } ]));

    it('should display title when labelKey is configured', function() {
        $scope.$digest();
        expect($(element).find('h2').text()).toBe('dynamicLabel-DUMMY.1.RBK');
    });


    it('should render child components', function() {
        $scope.$digest();
        expect($(element).find('uv-fraga').length).toBe(1);
        expect($(element).find('h3').text()).toBe('dynamicLabel-FRG_26.RBK');
    });


});
