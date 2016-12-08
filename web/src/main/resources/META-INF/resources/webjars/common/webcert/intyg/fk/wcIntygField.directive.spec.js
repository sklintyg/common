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

describe('wcIntygField Directive', function() {
    'use strict';

    var $scope;
    var element;

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.inject(['$compile', '$rootScope',
        function($compile, $rootScope) {
            $scope = $rootScope.$new();

            $scope.intygModel = { testValue: 'text' };
            $scope.field = {templateOptions:{}};

            element =
                $compile('<div wc-intyg-field field="field" intyg-model="intygModel"></div>')($scope);

            $scope.$digest();
            $scope = element.isolateScope();
        }]));


    it('should display svarstext if present', function() {
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{}})).toBeTruthy();
        expect($scope.showFieldLine({type:'info',templateOptions:{label:'FRG_1'}},{templateOptions:{}})).toBeFalsy();
        expect($scope.showFieldLine({type:'info',templateOptions:{label:'FRG_1'}},{templateOptions:{}})).toBeFalsy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'KV_001_002'}},{templateOptions:{}})).toBeFalsy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_1.1'}})).toBeTruthy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_1.2'}})).toBeFalsy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_1.10'}})).toBeFalsy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_1.11'}})).toBeFalsy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_10.1'}})).toBeTruthy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_10.2'}})).toBeFalsy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_10.3'}})).toBeFalsy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_10.4'}})).toBeFalsy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_10.10'}})).toBeFalsy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_10.11'}})).toBeFalsy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_10.12'}})).toBeFalsy();
        expect($scope.showFieldLine({type:'date',templateOptions:{label:'FRG_1'}},{templateOptions:{label:'DFR_10.99'}})).toBeFalsy();
    });

    it('should display svarstext depending on show options', function() {
        expect($scope.showField({key: 'testValue', type:'date',templateOptions:{label:'FRG_1'}})).toBeTruthy();
        expect($scope.showField({key: 'testValue', type:'date',templateOptions:{label:'FRG_1'}})).toBeTruthy();

        $scope.intygModel = { testValue: null };
        expect($scope.showField({key: 'testValue', type:'date',templateOptions:{label:'FRG_1', hideFromSigned:false}})).toBeTruthy();
        expect($scope.showField({key: 'testValue', type:'date',templateOptions:{label:'FRG_1', hideFromSigned:true}})).toBeFalsy();
        expect($scope.showField({key: 'testValue', type:'date',templateOptions:{label:'FRG_1', hideFromSigned:true, hideWhenEmpty:true}})).toBeFalsy();
        expect($scope.showField({type:'date',templateOptions:{label:'FRG_1', hideWhenEmpty:true}})).toBeFalsy();

        $scope.intygModel = { testValue: 'real text' };
        expect($scope.showField({key: 'testValue', type:'date',templateOptions:{label:'FRG_1', hideWhenEmpty:true}})).toBeTruthy();
    });

});
