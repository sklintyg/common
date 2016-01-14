describe('dynamicLabelService', function() {
    'use strict';

    var dynamicLabelService;
    var $rootScope;

    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.inject(['common.dynamicLabelService', '$rootScope',
        function(_dynamicLabelService_, _$rootScope_) {
            dynamicLabelService = _dynamicLabelService_;
            $rootScope = _$rootScope_;
        }
    ]));

    describe('#getProperty', function() {
        var testDataDynamicLabelInline = {
            'texter': {
                'text':
                    [
                        {
                            'id': 'KAT_2.RBK', // Kategori
                            'text': '2. Intyg är baserat på'
                        },
                        {
                            'id': 'KAT_2.HLP',
                            'text': 'Om underlaget är baserat utifrån annan anledning, ange det under \'Övrigt\', se kategori 8.'
                        },
                        { 'id': 'FRG_10.RBK' }, // Fråga - Rubrik
                        { 'id': 'FRG_10.HLP' }, // Fråga - Hjälptext
                        { 'id': 'DFR_10.1.RBK' }, // Delfråga ...
                        { 'id': 'DFR_10.2.HLP' }, // Delfråga ...
                        {
                            'id': 'KV_FKMU_0001.1', // ?? Svarsalt
                            'text': 'Min undersökning av patienten'
                        },
                        {
                            'id': 'KV_FKMU_0001.2',
                            'text': 'Min telefonkontakt med patienten'
                        },
                        {
                            'id': 'DFR_1.2.RBK',
                            'text': 'Ange yrke och nuvarande arbetsuppgifter'
                        },
                    ],
                'tillagg': {
                    'tillaggsfraga': {
                        'id': 'TFG_1',
                        'text': [
                            { 'id': 'TFG_1.RBK' },
                            { 'id': 'TFG_1.HLP' }
                        ]
                    }
                }
            }
        };

        beforeEach(function() {
            dynamicLabelService.addLabels(testDataDynamicLabelInline);
        });

        it('should return a question label string when given id', function() {
            var rootProp = 'texter'; // rootprop is only used by mock test in prototype, will not be present later
            var aProp = dynamicLabelService.getProperty('KAT_2.RBK', rootProp);
            expect(aProp).toEqual('2. Intyg är baserat på');
        });

        it('should return a help text string when given id', function() {
            var rootProp = 'texter';
            var aProp = dynamicLabelService.getProperty('KAT_2.HLP', rootProp);
            expect(aProp).toEqual('Om underlaget är baserat utifrån annan anledning, ange det under \'Övrigt\', se kategori 8.');
        });

        it('should return a partial question string when given id', function() {
            var rootProp = 'texter';
            var aProp = dynamicLabelService.getProperty('DFR_1.2.RBK', rootProp);
            expect(aProp).toEqual('Ange yrke och nuvarande arbetsuppgifter');
        });

        it('should return a answer alternative string when given id', function() {
            var rootProp = 'texter';
            var aProp = dynamicLabelService.getProperty('KV_FKMU_0001.1', rootProp);
            expect(aProp).toEqual('Min undersökning av patienten');
        });

        it('required text type should return a Error string when id is missing', function() {
            var rootProp = 'texter';
            var aProp = dynamicLabelService.getProperty('KAT_999X.RBK', rootProp); // KAT_999X.RBK does not exist
            expect(aProp).toContain('[MISSING TEXT ERROR');
            expect(aProp).toContain('KAT_999X.RBK');
        });

        it('Optional text type shoud return a MISSING LABEL string when id is missing', function() {
            var rootProp = 'texter';
            var aProp = dynamicLabelService.getProperty('KAT_999X.HLP', rootProp);
            expect(aProp).toContain('[Missing optional');
            expect(aProp).toContain('KAT_999X.HLP');
        });

        /* it('should return an empty string if the message is an empty string', function() {
            // todo
        });

        it('should return the correct value if a language is set in the root scope', function() {
            // todo
        });

        it('should return the correct value if a default language is set', function() {
            // todo
        });

        it('should return the default value if the key is not present in the resources', function() {
            // todo
        });

        it('should return the missing key if the key is not present in the resources', function() {
            // todo
        });

        it('should return missing language if no language is set', function() {
            // todo
        });

        it('should return string with expanded string if variable available', function() {
            // todo
        }); */

    });
});
