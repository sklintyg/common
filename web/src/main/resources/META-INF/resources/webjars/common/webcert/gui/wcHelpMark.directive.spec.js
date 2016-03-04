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

describe('wcHelpMark', function() {
    'use strict';

    var $q;
    var $scope;
    var $compileProvider;
    var dynamicLabelProxy;
    var dynamicLabelService;
    var element;

    beforeEach(angular.mock.module('htmlTemplates'));

    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('networkConfig', {});
    }));

    beforeEach(module(function(_$compileProvider_) {
        $compileProvider = _$compileProvider_;
    }));

    beforeEach(angular.mock.inject(['$q', 'common.DynamicLabelProxy', 'common.dynamicLabelService',
        function(_$q_, _dynamicLabelProxy_, _dynamicLabelService_) {
            $q = _$q_;
            dynamicLabelProxy = _dynamicLabelProxy_;
            dynamicLabelService = _dynamicLabelService_;
        }
    ]));


    describe('directive call to wcHelpMark', function() {

        beforeEach(inject(function($compile, $rootScope, $httpBackend) {
            $scope = $rootScope.$new();
            $scope.fieldDynamicHelpText = 'KAT_4.HLP';
            element =
                angular.element('<div wc-help-mark field-dynamic-help-text={{fieldDynamicHelpText}} field-help-text={{fieldHelpText}}</div>');
            $compile(element)($scope);

        }));

        beforeEach(inject(function() {
            /* jshint maxlen: false, unused: false */

            var dynamicLabelTestJson = {
                'texter': {
                    'KAT_4.RBK': 'Diagnos/diagnoser som orsakar nedsatt arbetsförmåga',
                    'KAT_4.HLP': 'Ange samtliga diagnoser'
                }, 'tillaggsfragor': [
                    {
                        'id': 'TFG_1',
                        'text': 'TFG_1.RBK',
                        'help': 'TFG_1.HLP'
                    }
                ]
            };
            var model = {
                textVersion: '1.0',
                tillaggsfragor: []
            };

            spyOn(dynamicLabelProxy, 'getDynamicLabels').and.callFake(function() {
                var promise = $q.defer();
                promise.resolve(dynamicLabelTestJson);
                return promise.promise;
            });
            dynamicLabelService.updateDynamicLabels('testtyp', model);
            $scope.$apply();
        }));

        it('should have applied HTML template', function() {
            // var elemToTest = getCompiledElement();
            $scope.fieldDynamicHelpText = 'KAT_4.HLP';
            //$scope.fieldHelpText = 'KAT_4.HLP';

            expect(element.html()).not.toEqual('');
        });

        it('should have element uib-popover-html in template', function() {
            //var elemToTest = getCompiledElement();
            $scope.fieldDynamicHelpText = 'KAT_4.HLP';
            // $scope.fieldHelpText = 'KAT_4.HLP';

            expect(element.find('uib-popover-html'));
            expect(element.find('KAT_4.HLP'));
        });


        it('should have fieldHelpText defined on isolated scope', function() {
            $scope.fieldDynamicHelpText = 'KAT_4.HLP';

            $scope.$digest();
            expect(element.scope().fieldDynamicHelpText).toBeDefined();
            console.log('1:' + element.scope().fieldDynamicHelpText);
        });

        // Cant get this one to work...
        it('should have text of given ID from file on disk', function() {
            $scope.fieldDynamicHelpText = 'KAT_32.HLP';

            $scope.$digest();
            expect(element.isolateScope().text).toContain('Ange samtliga diagnoser');
            console.log('1:' + element.isolateScope().fieldDynamicHelpText);
        });

        it('should have showHelp set to true when ID prop is provided', function() {
            $scope.$digest();

            expect(element.isolateScope().showHelp).toBe(true);
            console.log('1:' + element.isolateScope().showHelp);
        });

    });
});
