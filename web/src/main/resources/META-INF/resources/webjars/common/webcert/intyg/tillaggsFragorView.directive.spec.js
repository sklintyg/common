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

describe('tillaggsFragorView Directive', function() {
    'use strict';

    var $scope;
    var element;
    var $compileProvider;
    var dynamicLabelProxy;
    var dynamicLabelService;

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('networkConfig', {});
    }));

    beforeEach(module(function(_$compileProvider_) {
        $compileProvider = _$compileProvider_;
    }));

    beforeEach(angular.mock.inject(['$compile', '$rootScope', 'common.DynamicLabelProxy', 'common.dynamicLabelService',
        function($compile, $rootScope, _dynamicLabelProxy_, _dynamicLabelService_) {
            dynamicLabelProxy = _dynamicLabelProxy_;
            dynamicLabelService = _dynamicLabelService_;

            $scope = $rootScope.$new();
            element =
                $compile('<tillaggs-fragor-view tillaggs-fragor="tillaggsfragor"></tillaggs-fragor-view>')($scope);

            $scope.$digest();

        }]));


    it('should display svarstext if present', function() {
        $scope.tillaggsfragor = [{
            'id': 'TFG_1',
            'svar': 'Ett svar'
        }];

        $scope.$apply();
        expect(element.html()).not.toEqual('');
        expect(element.hasClass('ng-hide')).toBe(false);
        var svarsText = element.find('#tillaggsfraga-TFG_1');
        expect(svarsText.html()).toContain('Ett svar');

    });

    it('should display \'Ej Angivet\' if svar not present', function() {
        $scope.tillaggsfragor = [
            {
                'id': 'TFG_1'
            },
            {
                'id': 'TFG_2',
                'svar': null
            },
            {
                'id': 'TFG_3',
                'svar': ''
            }
        ];

        $scope.$apply();
        expect(element.html()).not.toEqual('');
        expect(element.hasClass('ng-hide')).toBe(false);
        expect(element.find('#tillaggsfraga-TFG_1').html()).toContain('Ej angivet');
        expect(element.find('#tillaggsfraga-TFG_2').html()).toContain('Ej angivet');
        expect(element.find('#tillaggsfraga-TFG_3').html()).toContain('Ej angivet');

    });

    it('should not display anything if no fragor present ', function() {
        $scope.tillaggsfragor = undefined;

        $scope.$apply();
        expect(element.hasClass('ng-hide')).toBe(true);

    });


});
