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
describe('fk7263List Directive', function() {
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
                        }
                    };
                };
            });

            $filterProvider.register('uvDomIdFilter', function() {
                return function(value) { return value;};
            });
        })
    );

    beforeEach(angular.mock.inject(function($compile, $rootScope) {
        $scope = $rootScope.$new();

        $scope.viewDataMock ={
            property1: 'elvan',
            property2: 'Tolvan'
        };

        $scope.configMock = {
            noValueId: 'id-no-value',
            modelProps: [{
                modelProp: 'property1',
                label: 'translationKey'
            },
                {
                    modelProp: 'property2',
                    label: 'translationKey',
                    showValue: true
                }]
        };

        element = $compile(
            '<fk7263-list config="configMock" view-data="viewDataMock"></fk7263-list>'
        )($scope);
    }));

    it('should display label when value exists', function() {
        $scope.$digest();

        expect(element.isolateScope().values.length).toBe(2);
        expect(element.isolateScope().hasValue()).toBeTruthy();
        expect($(element).find('#property2-text').text()).toContain('Tolvan');
        expect($(element).find('uv-no-value').length).toBe(0);
    });

    it('should display "uv-no-value" when no value exists', function() {
        $scope.viewDataMock.property1 = null;
        $scope.viewDataMock.property2 = null;
        $scope.$digest();

        expect(element.isolateScope().values.length).toBe(0);
        expect(element.isolateScope().hasValue()).toBeFalsy();
        expect($(element).find('uv-no-value').length).toBe(1);
        expect($(element).find('#id-no-value').length).toBe(1);
    });

});
