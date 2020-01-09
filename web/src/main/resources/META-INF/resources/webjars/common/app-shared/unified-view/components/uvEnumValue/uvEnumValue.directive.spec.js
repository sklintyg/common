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

describe('uvEnumValue Directive', function() {
    'use strict';

    var $scope;
    var element;

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));

    beforeEach(function () {
        module(function ($provide) {
            $provide.value('common.messageService', {
                addResources: function() {

                },
                propertyExists: function(key) {
                    return key;
                },
                getProperty: function() {

                }
            });
        });
    });

    beforeEach(angular.mock.inject(['$compile', '$rootScope', function($compile, $rootScope) {
        $scope = $rootScope.$new();

        $scope.viewDataMock ={
            property1: {
                name: 'tolvan'
            }
        };

        $scope.configMock = {
            modelProp: 'property1.name',
            values: {
                'tolvan': 'translationKey',
                'elvan': 'otherkey'
            }
        };

        element = $compile(
            '<uv-enum-value config="configMock" view-data="viewDataMock"></uv-enum-value>'
        )($scope);

    }]));

    it('should display model value when value exists', function() {
        $scope.$digest();

        expect(element.isolateScope().value).toBe('translationKey');
        expect(element.isolateScope().hasValue()).toBeTruthy();
        expect($(element).find('span').text()).toContain('translationKey');
        expect($(element).find('uv-no-value').length).toBe(0);
    });

    it('should display "uv-no-value" when no value exists', function() {
        $scope.viewDataMock.property1.name = 'missing';
        $scope.$digest();

        expect(element.isolateScope().value).toBeUndefined();
        expect(element.isolateScope().hasValue()).toBeFalsy();
        expect($(element).find('uv-no-value').length).toBe(1);
    });

});
