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

describe('wcDecimal', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var $scope;

    // Create a form to test the validation directive on.
    beforeEach(angular.mock.inject(function($compile, $rootScope) {
        $scope = $rootScope;
        $scope.model = {
            test: null
        };

        var el = angular
            .element('<form name="form"><input id="test" name="test" type="text" class="small-decimal" ' +
                'wc-decimal-number wc-decimal-max-numbers="2" ng-model="model.test"></form>');
        $compile(el)($scope);
        $scope.$digest();
    }));

    // valid inputs
    it('should pass with a valid decimal number with format "n,n"', function() {
        $scope.form.test.$setViewValue('1');
        expect($scope.model.test).toEqual(1.0);
    });
    it('should pass with a valid decimal number with format "n,"', function() {
        $scope.form.test.$setViewValue('1,');
        expect($scope.model.test).toEqual(1.0);
    });
    it('should pass with a valid decimal number with format "n."', function() {
        $scope.form.test.$setViewValue('1.');
        expect($scope.model.test).toEqual(1.0);
    });
    it('should pass with a valid decimal number with format ","', function() {
        $scope.form.test.$setViewValue(',');
        expect($scope.model.test).toEqual(0.0);
    });
    it('should pass with a valid decimal number with format ",1"', function() {
        $scope.form.test.$setViewValue(',1');
        expect($scope.model.test).toEqual(0.1);
    });
    it('should pass with a valid decimal number with format "145"', function() {
        $scope.form.test.$setViewValue('145');
        expect($scope.model.test).toEqual(1.4);
    });
    it('should pass with a single-digit number "0"', function() {
        $scope.form.test.$setViewValue('0');
        expect($scope.model.test).toEqual(0.0);
    });
    it('should pass with a single-digit number "1"', function() {
        $scope.form.test.$setViewValue('1');
        expect($scope.model.test).toEqual(1.0);
    });
    it('should pass with a single-digit number "2"', function() {
        $scope.form.test.$setViewValue('2');
        expect($scope.model.test).toEqual(2.0);
    });

    // invalid formats resulting in null
    it('should pass with a valid decimal number with format ""', function() {
        $scope.form.test.$setViewValue('');
        expect($scope.model.test).toEqual(null);
    });
    it('should pass with a valid decimal number with format "asdf"', function() {
        $scope.form.test.$setViewValue('asdf');
        expect($scope.model.test).toEqual(null);
    });







    // valid view values based on initial values of model.

    it('should pass with a model valid decimal number with format "n,"', function() {
        $scope.model.test = 1.0;
        $scope.$digest();
        expect($scope.form.test.$viewValue).toEqual('1,0');
    });

    it('should pass with a model decimal number with format ",1"', function() {
        $scope.model.test = 0.1;
        $scope.$digest();
        expect($scope.form.test.$viewValue).toEqual('0,1');
    });
    it('should pass with a model decimal number with format "1.45"', function() {
        $scope.model.test = 1.45;
        $scope.$digest();
        expect($scope.form.test.$viewValue).toEqual('1,4');
    });
    it('should pass with a model single-digit number (0) with format "n"', function() {
        $scope.model.test = 0; // Set the model value
        $scope.$digest();
        expect($scope.form.test.$viewValue).toEqual('0,0');
    });
    it('should pass with a valid single-digit number (1) with format "n"', function() {
        $scope.model.test = 1; // Set the model value
        $scope.$digest();
        expect($scope.form.test.$viewValue).toEqual('1,0');
    });
    it('should pass with a valid single-digit number (2) with format "n"', function() {
        $scope.model.test = 2; // Set the model value
        $scope.$digest();
        expect($scope.form.test.$viewValue).toEqual('2,0');
    });


    // Reformatting, comment in when we have separated wcDecimal from syn-specific use.
    //it('should pass with a valid negative decimal number ', function() {
    //    $scope.model.test = -1.2; // Set the model value
    //    $scope.$digest();
    //    expect($scope.form.test.$viewValue).toEqual('-1,2');
    //});
});
