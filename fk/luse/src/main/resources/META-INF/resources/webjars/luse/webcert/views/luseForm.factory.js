angular.module('luse').factory('luse.FormFactory', [
    'luse.FormFactoryHelper', 'common.UserModel', 'common.FactoryTemplatesHelper', 'common.DateUtilsService',
    function(FactoryHelper, UserModel, FactoryTemplates, dateUtils) {
        'use strict';

        var categoryNames = {
            1: 'grundformu',
            2: 'underlag',
            3: 'sjukdomsforlopp',
            4: 'diagnos',
            5: 'funktionsnedsattning',
            6: 'aktivitetsbegransning',
            7: 'medicinskabehandlingar',
            8: 'medicinskaforutsattningarforarbete',
            9: 'ovrigt',
            10: 'kontakt'
        };

        var kategori = FactoryTemplates.kategori;
        var fraga = FactoryTemplates.fraga;

        var formFields = [
            FactoryTemplates.adress,
            kategori(1, categoryNames[1], [
                fraga(1, [
                    {
                        type: 'headline',
                        templateOptions: {id: 'FRG_1', label: 'FRG_1', level: 4, noH5After: true, required: true}
                    },
                    FactoryTemplates.grundForMU,
                    FactoryTemplates.annatGrundForMUBeskrivning,
                    {
                        key: 'motiveringTillInteBaseratPaUndersokning',
                        type: 'multi-text',
                        hideExpression: 'model.undersokningAvPatienten || !( model.journaluppgifter || model.anhorigsBeskrivningAvPatienten || model.annatGrundForMU)',
                        templateOptions: {
                            bold: 'bold',
                            forceHeadingTypeLabel: true,
                            staticLabelId: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning',
                            subTextId: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning.info',
                            subTextDynId: 'FRG_25',
                            hideWhenEmpty: true,
                            required: true
                        }
                    }
                ]),
                fraga(2, [
                    {key: 'kannedomOmPatient', type: 'singleDate', templateOptions: {label: 'FRG_2', required: true}}
                ]),
                fraga(3, [
                    {key: 'underlagFinns', type: 'boolean', templateOptions: {label: 'FRG_3', required: true}},
                    {
                        key: 'underlag',
                        type: 'underlag',
                        hideExpression: '!model.underlagFinns',
                        templateOptions: {
                            underlagsTyper: [
                                'NEUROPSYKIATRISKT',
                                'HABILITERING',
                                'ARBETSTERAPEUT',
                                'FYSIOTERAPEUT',
                                'LOGOPED',
                                'PSYKOLOG',
                                'FORETAGSHALSOVARD',
                                'SPECIALISTKLINIK',
                                'VARD_UTOMLANDS',
                                'OVRIGT_UTLATANDE'
                            ],
                            typLabel: 'FRG_4',
                            datumLabel: 'DFR_4.2',
                            hamtasFranLabel: 'DFR_4.3'
                        },
                        watcher: {
                            expression: 'model.underlagFinns',
                            listener: FactoryHelper.underlagListener
                        }
                    }
                ])
            ]),
            kategori(4, categoryNames[4], [
                fraga(6, [
                    {type: 'headline', templateOptions: {label: 'FRG_6', level: 4, noH5After: false, required: true}},
                    {
                        key: 'diagnoser',
                        type: 'diagnos',
                        templateOptions: {diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2'}
                    },
                    {key: 'diagnosgrund', type: 'multi-text', templateOptions: {label: 'DFR_7.1', required: true}},
                    {
                        key: 'nyBedomningDiagnosgrund',
                        type: 'boolean',
                        templateOptions: {label: 'FRG_45', required: true, hideKompletteringText: true}
                    },
                    {
                        key: 'diagnosForNyBedomning',
                        type: 'multi-text',
                        templateOptions: {
                            label: 'DFR_45.2',
                            required: true,
                            hideWhenEmpty: true,
                            kompletteringKey: 'nyBedomningDiagnosgrund'
                        },
                        hideExpression: '!model.nyBedomningDiagnosgrund'
                    }
                ])
            ]),
            kategori(3, categoryNames[3], [
                fraga(5, [
                    {key: 'sjukdomsforlopp', type: 'multi-text', templateOptions: {label: 'DFR_5.1', required: true}}
                ])
            ]),
            kategori(5, categoryNames[5], [
                {
                    wrapper: 'validationGroup',
                    templateOptions: {type: 'text-group', validationGroup: 'funktionsnedsattning'},
                    fieldGroup: [
                        fraga(8, [
                            {
                                key: 'funktionsnedsattningIntellektuell',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '8'}
                            }
                        ]),
                        fraga(9, [
                            {
                                key: 'funktionsnedsattningKommunikation',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '9'}
                            }
                        ]),
                        fraga(10, [
                            {
                                key: 'funktionsnedsattningKoncentration',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '10'}
                            }
                        ]),
                        fraga(11, [
                            {
                                key: 'funktionsnedsattningPsykisk',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '11'}
                            }
                        ]),
                        fraga(12, [
                            {
                                key: 'funktionsnedsattningSynHorselTal',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '12'}
                            }
                        ]),
                        fraga(13, [
                            {
                                key: 'funktionsnedsattningBalansKoordination',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '13'}
                            }
                        ]),
                        fraga(14, [
                            {
                                key: 'funktionsnedsattningAnnan',
                                type: 'check-multi-text',
                                templateOptions: {frgId: '14'}
                            }
                        ])
                    ]
                }
            ], true),
            kategori(6, categoryNames[6], [
                fraga(17, [
                    {
                        key: 'aktivitetsbegransning',
                        type: 'multi-text',
                        templateOptions: {label: 'FRG_17', required: true}
                    }
                ])
            ]),
            kategori(7, categoryNames[7], [
                fraga(18, [
                    {key: 'avslutadBehandling', type: 'multi-text', templateOptions: {label: 'DFR_18.1'}}
                ]),
                fraga(19, [
                    {key: 'pagaendeBehandling', type: 'multi-text', templateOptions: {label: 'DFR_19.1'}}
                ]),
                fraga(20, [
                    {key: 'planeradBehandling', type: 'multi-text', templateOptions: {label: 'DFR_20.1'}}
                ]),
                fraga(21, [
                    {key: 'substansintag', type: 'multi-text', templateOptions: {label: 'DFR_21.1'}}
                ])
            ]),
            kategori(8, categoryNames[8], [
                fraga(22, [
                    {
                        key: 'medicinskaForutsattningarForArbete',
                        type: 'multi-text',
                        templateOptions: {label: 'FRG_22', required: true}
                    }
                ]),
                fraga(23, [
                    {key: 'formagaTrotsBegransning', type: 'multi-text', templateOptions: {label: 'FRG_23'}}
                ])
            ]),
            kategori(9, categoryNames[9], [
                fraga(25, [
                    {key: 'ovrigt', type: 'multi-text', templateOptions: {label: 'DFR_25.1'}}
                ])
            ]),
            kategori(10, categoryNames[10], [
                fraga(26, [
                    {
                        key: 'kontaktMedFk',
                        type: 'checkbox-inline',
                        templateOptions: {label: 'DFR_26.1', hideKompletteringText: true}
                    },
                    {
                        key: 'anledningTillKontakt',
                        type: 'multi-text',
                        hideExpression: '!model.kontaktMedFk',
                        templateOptions: {label: 'DFR_26.2', hideWhenEmpty: true, kompletteringKey: 'kontaktMedFk'}
                    }
                ])
            ]),
            FactoryTemplates.vardenhet
        ];

        return {
            getFormFields: function() {
                return angular.copy(formFields);
            },
            getCategoryNames: function() {
                return angular.copy(categoryNames);
            }
        };
    }])
;
