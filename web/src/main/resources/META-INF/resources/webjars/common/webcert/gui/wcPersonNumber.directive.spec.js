/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

describe('wcPersonNumber', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var $scope;
    var personIdValidatorService;

    // Create a form to test the validation directive on.
    beforeEach(angular.mock.inject(['$compile', '$rootScope', 'common.PersonIdValidatorService', function($compile, $rootScope, _personIdValidatorService_) {
        $scope = $rootScope.$new();
        $scope.model = {
            test: null
        };

        personIdValidatorService = _personIdValidatorService_;

        var el = angular
            .element('<form name="form"><input ng-model="model.test" name="test" wc-person-number></form>');
        $compile(el)($scope);
        $scope.$digest();
    }]));

    // Pass

    it('should pass with a valid "personnummer" with format "yyyyMMdd-nnnn"', function() {
        $scope.form.test.$setViewValue('19121212-1212');

        expect($scope.model.test).toEqual('19121212-1212');
        expect($scope.form.$valid).toBeTruthy();
    });

    it('should pass with a valid "personnummer" with format "yyyyMMddnnnn"', function() {
        $scope.form.test.$setViewValue('191212121212');

        expect($scope.model.test).toEqual('19121212-1212');
        expect($scope.form.$valid).toBeTruthy();
    });

    it('should pass with a valid "personnummer" with format yyMMdd-nnnn', function() {
        $scope.form.test.$setViewValue('121212-1212');

        expect($scope.model.test).toEqual('20121212-1212');
        expect($scope.form.$valid).toBeTruthy();
    });

    it('should pass with a valid "personnummer" with format yyMMdd+nnnn', function() {
        $scope.form.test.$setViewValue('121212+1212');

        expect($scope.model.test).toEqual('19121212-1212');
        expect($scope.form.$valid).toBeTruthy();
    });

    it('should pass with a valid "personnummer" with format yyMMddnnnn', function() {
        $scope.form.test.$setViewValue('1212121212');

        expect($scope.model.test).toEqual('20121212-1212');
        expect($scope.form.$valid).toBeTruthy();
    });

    it('should pass with a valid "samordningsnummer" with format "yyyyMMnn-nnnn"', function() {
        $scope.form.test.$setViewValue('19121272-1219');

        expect($scope.model.test).toEqual('19121272-1219');
        expect($scope.form.$valid).toBeTruthy();
    });

    it('should pass with a valid "samordningsnummer" with format "yyyyMMnnnnnn"', function() {
        $scope.form.test.$setViewValue('191212721219');

        expect($scope.model.test).toEqual('19121272-1219');
        expect($scope.form.$valid).toBeTruthy();
    });

    it('should pass with a valid "samordningsnummer" with format "yyMMnn-nnnn"', function() {
        $scope.form.test.$setViewValue('121272-1219');

        expect($scope.model.test).toEqual('20121272-1219');
        expect($scope.form.$valid).toBeTruthy();
    });

    it('should pass with a valid "samordningsnummer" with format "yyMMnnnnnn"', function() {
        $scope.form.test.$setViewValue('1212721219');

        expect($scope.model.test).toEqual('20121272-1219');
        expect($scope.form.$valid).toBeTruthy();
    });

    // Fail

    it('should fail if "personnummer" has invalid check digit', function() {
        $scope.form.test.$setViewValue('121212-1213');

        expect($scope.model.test).toBeUndefined();
        expect($scope.form.$valid).toBeFalsy();
    });

    it('should fail if "personnummer" has invalid date', function() {
        $scope.form.test.$setViewValue('121212-1213');

        expect($scope.model.test).toBeUndefined();
        expect($scope.form.$valid).toBeFalsy();
    });

    it('should fail with if "personnummer" has invalid characters', function() {
        $scope.form.test.$setViewValue('121212.1213');

        expect($scope.model.test).toBeUndefined();
        expect($scope.form.$valid).toBeFalsy();
    });

    it('should fail if "samordningsnummer" has invalid check digit', function() {
        $scope.form.test.$setViewValue('121272-1213');

        expect($scope.model.test).toBeUndefined();
        expect($scope.form.$valid).toBeFalsy();
    });

    it('should fail with if "samordningsnummer" has invalid date', function() {
        $scope.form.test.$setViewValue('121292-1215');

        expect($scope.model.test).toBeUndefined();
        expect($scope.form.$valid).toBeFalsy();
    });

    it('should fail with if "samordningsnummer" has invalid characters', function() {
        $scope.form.test.$setViewValue('121272.1219');

        expect($scope.model.test).toBeUndefined();
        expect($scope.form.$valid).toBeFalsy();
    });
});
