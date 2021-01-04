/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
describe('fkUvTable Directive', function() {
    'use strict';

    var $scope;
    var element;

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('fk7263'));

    beforeEach(
        module(function ($provide, $filterProvider) {
            $provide.provider('uvUtil', function () {
                this.$get = function($parse) {
                    return {
                        getValue: function(obj, pathExpression) {
                            return $parse(pathExpression)(obj);
                        },
                        isValidValue: function(key) {
                            return !!key;
                        },
                        getTextFromConfig: function(key) {
                            return key;
                        }
                    };
                };
            });

            $filterProvider.register('uvDomIdFilter', function() {
                return function(value) { return value;};
            });
        })
    );

    beforeEach(angular.mock.inject([ '$compile', '$rootScope', function($compile, $rootScope) {
        $scope = $rootScope.$new();

        $scope.viewDataMock = {
            property1:  {
                from: 123,
                tom: 456
            }
        };

        $scope.configMock = {
            headers: ['label', 'from', 'tom'],
            rows: [{
                label: 'row1',
                key: 'property1'
            },
            {
                label: 'row2',
                key: 'property2'
            }]
        };

        element = $compile('<fk-uv-table config="configMock" view-data="viewDataMock"></fk-uv-table>')($scope);

    } ]));

    it('should display "123" on row1', function() {
        $scope.$digest();
        expect(element.isolateScope().viewModel.rows.length).toBe(1);
        expect($(element).find('#property1-row-col1').text()).toContain('123');
    });

    it('should by display "Ej angivet" when no rows', function() {
        $scope.viewDataMock.property1 = null;

        $scope.$digest();
        expect($(element).find('uv-no-value').length).toBe(1);
    });

});
