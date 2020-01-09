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
describe('ueFilterLatin1 Directive', function() {
    'use strict';

    var $scope;
    var element;

    beforeEach(angular.mock.inject(function($compile, $rootScope) {
        $scope = $rootScope.$new();

        $scope.model = '';

        element = $compile(
            '<textarea ng-model="model" ue-filter-latin1></textarea>'
        )($scope);
    }));

    it('Should allow all non control character latin1 characters', function() {
        var testValue = '!"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~';
        testValue += '¡¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ';

        $(element).val(testValue).trigger('input');
        $scope.$digest();
        expect($scope.model).toBe(testValue);
    });

    it('Should not allow control characters or other unicode characters', function() {
        var testValue = 'AB👽C\n123'; // jshint ignore:line

        $(element).val(testValue).trigger('input');
        $scope.$digest();
        expect($scope.model).toBe('ABC\n123');
    });

});
