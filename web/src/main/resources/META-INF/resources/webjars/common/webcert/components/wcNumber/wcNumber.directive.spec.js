/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

describe('wcNumber', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var $scope;

    // Create a form to test the validation directive on.
    beforeEach(angular.mock.inject(['$compile', '$rootScope', function($compile, $rootScope) {
        $scope = $rootScope.$new();
        $scope.model = {
            test: null
        };

        var el = angular
            .element('<form name="form"><input ng-model="model.test" name="test" wc-number="true"></form>');
        $compile(el)($scope);
        $scope.$digest();
    }]));

    // Pass

    it('should pass with a value containing only numbers and spaces', function() {
        $scope.form.test.$setViewValue('123456');
        expect($scope.model.test).toEqual('123456');

        $scope.form.test.$setViewValue('123 56');
        expect($scope.model.test).toEqual('123 56');

        $scope.form.test.$setViewValue('123');
        $scope.form.test.$setViewValue('123/');
        expect($scope.model.test).not.toEqual('123/');
    });

    it('should ignore all symbols other than space and numbers', function() {
        $scope.form.test.$setViewValue('1');
        expect($scope.model.test).toEqual('1');

        $scope.form.test.$setViewValue('1_');
        expect($scope.model.test).toEqual('1');

        $scope.form.test.$setViewValue('1%');
        expect($scope.model.test).toEqual('1');

        $scope.form.test.$setViewValue('1 ');
        expect($scope.model.test).toEqual('1 ');

        $scope.form.test.$setViewValue('1 a');
        expect($scope.model.test).not.toEqual('1 a');
    });
});
