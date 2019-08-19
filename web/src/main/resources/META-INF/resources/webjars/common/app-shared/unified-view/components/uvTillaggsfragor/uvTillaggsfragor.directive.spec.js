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

    var expectedConfig = {
        type: 'uv-kategori',
        labelKey: 'TILLAGGSFRAGARUBRIK.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'DFR_id.1.1.RBK',
            components: [ {
                type: 'uv-del-fraga',
                contentUrl: null,
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'tillaggsfragor[0].svar',
                    id: 'tillaggsfragor-id.1'
                } ]
            } ]
        }, {
            type: 'uv-fraga',
            labelKey: 'DFR_id.2.1.RBK',
            components: [ {
                type: 'uv-del-fraga',
                contentUrl: null,
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'tillaggsfragor[1].svar',
                    id: 'tillaggsfragor-id.2'
                } ]
            } ]
        } ]
    };

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

        $scope.viewDataMock = {
            tillaggsfragor: [ {
                id: 'id.1',
                svar: 'svar-1'
            }, {
                id: 'id.2',
                svar: 'svar-2'
            } ]
        };

        $scope.configMock = {
            labelKey: 'TILLAGGSFRAGARUBRIK.RBK',
            modelProp: 'tillaggsfragor'
        };

        element = $compile('<uv-tillaggsfragor config="configMock" view-data="viewDataMock"></uv-tillaggsfragor>')($scope);

    } ]));

    it('should return correct tillagsconfig for tillaggsfragor in viewData', function() {
        $scope.$digest();
        expect(angular.toJson(element.isolateScope().tillaggsConfig)).toBe(angular.toJson(expectedConfig));
    });

    it('should render child components', function() {
        $scope.$digest();

        expect($(element).find('uv-simple-value').length).toBe(2);
        expect($(element).find('uv-simple-value .multiline').first().text()).toBe('svar-1');
        expect($(element).find('uv-simple-value .multiline').last().text()).toBe('svar-2');
    });

});
