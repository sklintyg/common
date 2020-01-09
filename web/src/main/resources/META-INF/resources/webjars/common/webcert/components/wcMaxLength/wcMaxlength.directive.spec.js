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

describe('wcMaxlength', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var $scope, form;

    // Create a form to test the validation directive on.
    beforeEach(angular.mock.inject(function($compile, $rootScope) {
        $scope = $rootScope;
        $scope.model = {
            test: ''
        };

        var el = angular.element('<form name="form"><textarea wc-maxlength maxlength="40" rows="13" ng-model="model.test" name="test" id="test-{{1+1}}"></textarea></form>');

        form = $compile(el)($scope);
        $scope.$digest();
    }));

    it('should work with ngModel', function() {
        $scope.form.test.$setViewValue('This is a test');
        expect($scope.model.test).toEqual('This is a test');
    });
    it('should append number of characters left', function() {
        expect(form.html()).toContain('Tecken kvar: 40');
    });
    it('should update number of characters left', function() {
        $scope.form.test.$setViewValue('13 characters');
        $scope.$digest();
        expect(form.html()).toContain('Tecken kvar: 27'); // 40 - 13
    });
    it('should limit model to set limit', function() {
        $scope.form.test.$setViewValue('this text is very long, extremely long even!'); // 44 characters
        $scope.$digest();
        expect(form.html()).toContain('Tecken kvar: 0'); // 40 - 44 ~ 0
        expect($scope.model.test).toEqual('this text is very long, extremely long e'); // Break after limit
    });
    it('should accept stupid input', function() {
        $scope.form.test.$setViewValue('\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n');
        $scope.$digest();
        expect(form.html()).toContain('Tecken kvar: 0');
    });
    it('should accept unprintable input', function() {
        $scope.form.test.$setViewValue('\0\0\0\0');
        $scope.$digest();
        expect(form.html()).toContain('Tecken kvar: 36'); // 40 - 4
    });
});
