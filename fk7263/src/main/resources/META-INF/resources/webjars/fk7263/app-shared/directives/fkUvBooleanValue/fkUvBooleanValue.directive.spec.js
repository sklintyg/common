/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
describe('fkUvBooelanValue Directive', function() {
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

            $filterProvider.register('uvBoolFilter', function() {
                return function(value) { return value ? 'Ja' : 'Nej';};
            });
        })
    );

    beforeEach(angular.mock.inject([ '$compile', '$rootScope', function($compile, $rootScope) {
        $scope = $rootScope.$new();

        $scope.viewDataMock = {
            property1:  true,
            property2:  false
        };

        $scope.configMock = {
            labelKey: 'DUMMY.1.RBK',
            value: function(cert) {
                if (cert.property1) {
                    return false;
                }

                if (cert.property2) {
                    return true;
                }

                return null;
            }
        };

        element = $compile('<fk-uv-boolean-value config="configMock" view-data="viewDataMock"></fk-uv-boolean-value>')($scope);

    } ]));

    it('should display "Nej" when property1 is true', function() {
        $scope.$digest();
        expect(element.isolateScope().getValue()).toEqual('Nej');
        expect($(element).find('span').text()).toContain('Nej');
    });

    it('should display "Ja" when property2 is true', function() {
        $scope.viewDataMock.property1 = false;
        $scope.viewDataMock.property2 = true;

        $scope.$digest();
        expect(element.isolateScope().getValue()).toEqual('Ja');
        expect($(element).find('span').text()).toContain('Ja');
    });

    it('should by default display "Ej angivet" when value is undefined', function() {
        $scope.viewDataMock.property1 = false;
        $scope.viewDataMock.property2 = false;

        $scope.$digest();
        expect(element.isolateScope().hasValue()).toBeFalsy();
        expect($(element).find('uv-no-value').length).toBe(1);
    });

});
