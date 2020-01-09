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

describe('dynamicLabelService', function() {
    'use strict';

    var dynamicLabelProxy;
    var dynamicLabelService;
    var $rootScope;
    var $q;

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('networkConfig', {});
    }));

    beforeEach(angular.mock.inject(['common.DynamicLabelProxy', 'common.dynamicLabelService', '$rootScope', '$q',
        function(_dynamicLabelProxy_, _dynamicLabelService_, _$rootScope_, _$q_) {
            dynamicLabelProxy = _dynamicLabelProxy_;
            dynamicLabelService = _dynamicLabelService_;
            $rootScope = _$rootScope_;
            $q = _$q_;
        }
    ]));


    describe('updateTillaggsfragorToModel', function() {

        /* jshint maxlen: false, unused: false */
        var dynamicLabelJson = {
            'texter': {
            },
            'tillaggsfragor': [
                {
                    'id': '9001',
                    'text': 'Blah',
                    'help': '9001'
                },
                {
                    'id': '9003',
                    'text': 'Blah',
                    'help': '9003'
                }
            ]
        };

        beforeEach(function() {
            spyOn(dynamicLabelProxy, 'getDynamicLabels').and.callFake(function() {
                var promise = $q.defer();
                promise.resolve(dynamicLabelJson);
                return promise.promise;
            });
        });

        it('should add all fragor to model in order if model is empty', function() {
            var model = {
                tillaggsfragor: []
            };

            dynamicLabelService.updateDynamicLabels('testtyp', model);

            $rootScope.$apply();

            expect(model.tillaggsfragor.length).toBe(2);
            expect(model.tillaggsfragor[0].id).toBe('9001');
            expect(model.tillaggsfragor[1].id).toBe('9003');
        });

        it('should only add missing fragor to model in order if model already has values (insert)', function() {
            var model = {
                tillaggsfragor: [
                    {
                        id: '9003',
                        svar: 'yeehaw'
                    }
                ]
            };

            dynamicLabelService.updateDynamicLabels('testtyp', model);

            $rootScope.$apply();

            expect(model.tillaggsfragor.length).toBe(2);
            expect(model.tillaggsfragor[0].id).toBe('9001');
            expect(model.tillaggsfragor[1].id).toBe('9003');
        });

        it('should only add missing fragor to model in order if model already has values (push)', function() {
            var model = {
                tillaggsfragor: [
                    {
                        id: '9001',
                        svar: 'yeehaw'
                    }
                ]
            };

            dynamicLabelService.updateDynamicLabels('testtyp', model);

            $rootScope.$apply();

            expect(model.tillaggsfragor.length).toBe(2);
            expect(model.tillaggsfragor[0].id).toBe('9001');
            expect(model.tillaggsfragor[1].id).toBe('9003');
        });
    });

    describe('#getProperty', function() {
        var testDataDynamicLabelInline = {
            'texter': {
                    'KAT_2.RBK': '2. Intyg är baserat på'
            },
            'tillaggsfragor': [
                {
                    'id': 'TFG_1',
                    'text': 'TFG_1.RBK',
                    'help': 'TFG_1.HLP'
                }
            ]
        };

        beforeEach(function() {
            spyOn(dynamicLabelProxy, 'getDynamicLabels').and.callFake(function() {
                var promise = $q.defer();
                promise.resolve(testDataDynamicLabelInline);
                return promise.promise;
            });
            var model = {
                tillaggsfragor: []
            };
            dynamicLabelService.updateDynamicLabels('testtyp', model);
            $rootScope.$apply();
        });

        it('should return a question label string when given id', function() {
            var rootProp = 'texter'; // rootprop is only used by mock test in prototype, will not be present later
            var aProp = dynamicLabelService.getProperty('KAT_2.RBK', rootProp);
            expect(aProp).toEqual('2. Intyg är baserat på');
        });

        it('required text type should return a Error string when id is missing', function() {
            var rootProp = 'texter';
            var aProp = dynamicLabelService.getProperty('KAT_999X.RBK', rootProp); // KAT_999X.RBK does not exist
            expect(aProp).toBeUndefined();
        });

        it('Optional text type shoud return a MISSING LABEL string when id is missing', function() {
            var rootProp = 'texter';
            var aProp = dynamicLabelService.getProperty('KAT_999X.HLP', rootProp);
            expect(aProp).toBeUndefined();
        });

    });
});
