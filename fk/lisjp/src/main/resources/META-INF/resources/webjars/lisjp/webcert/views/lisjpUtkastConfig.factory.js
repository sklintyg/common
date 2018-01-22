
angular.module('lisjp').factory('lisjp.UtkastConfigFactory',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ObjectHelper', 'common.UserModel',
        'common.FactoryTemplatesHelper',
        function($log, $timeout,
            DateUtils, ObjectHelper, UserModel, FactoryTemplates) {
            'use strict';

            var categoryNames = {
                1: 'grundformu',
                2: 'sysselsattning',
                3: 'diagnos',
                4: 'funktionsnedsattning',
                5: 'medicinskaBehandlingar',
                6: 'bedomning',
                7: 'atgarder',
                8: 'ovrigt',
                9: 'kontakt',
                10: 'smittbararpenning'
            };

            var kategori = FactoryTemplates.kategori;
            var fraga = FactoryTemplates.fraga;

            var config = [
                {
                    type: 'ue-kategori',
                    category: 10,
                    components: [ {
                        type: 'ue-fraga',
                        frageId: 27,
                        components: [ {
                            label: {
                                bold: true,
                                key: 'FRG_27.RBK',
                                helpKey: 'FRG_27.HLP'
                            },
                            type: 'ue-checkbox',
                            modelProp: 'avstangningSmittskydd'
                        } ]
                    } ]
                },{
                    type: 'ue-kategori',
                    category: 1,
                    hideExpression: 'model.avstangningSmittskydd',
                    components: [{
                        type: 'ue-fraga',
                        frageId: 1,
                        label: {
                            key: 'FRG_1.RBK',
                            helpKey: 'FRG_1.HLP',
                            type: 'h4',
                            required: true
                        },
                        components: [{
                            label: {
                                key: 'KV_FKMU_0001.UNDERSOKNING.RBK',
                                helpKey: 'KV_FKMU_0001.UNDERSOKNING.HLP'
                            },
                            type: 'ue-checkbox-date',
                            modelProp: 'undersokningAvPatienten'
                        }, {
                            label: {
                                key: 'KV_FKMU_0001.TELEFONKONTAKT.RBK',
                                helpKey: 'KV_FKMU_0001.TELEFONKONTAKT.HLP'
                            },
                            type: 'ue-checkbox-date',
                            modelProp: 'telefonkontaktMedPatienten'
                        }, {
                            label: {
                                key: 'KV_FKMU_0001.JOURNALUPPGIFTER.RBK',
                                helpKey: 'KV_FKMU_0001.JOURNALUPPGIFTER.HLP'
                            },
                            type: 'ue-checkbox-date',
                            modelProp: 'journaluppgifter'
                        }, {
                            label: {
                                key: 'KV_FKMU_0001.ANNAT.RBK',
                                helpKey: 'KV_FKMU_0001.ANNAT.HLP'
                            },
                            type: 'ue-checkbox-date',
                            modelProp: 'annatGrundForMU'
                        }, {
                            label: {
                                key: 'DFR_1.3.RBK',
                                helpKey: 'DFR_1.3.HLP',
                                required: true
                            },
                            type: 'ue-textfield',
                            hideExpression: '!model.annatGrundForMU',
                            modelProp: 'annatGrundForMUBeskrivning'
                        }]
                    }, {
                        type: 'ue-fraga',
                        hideExpression: 'model.undersokningAvPatienten || !(model.telefonkontaktMedPatienten || model.journaluppgifter || model.annatGrundForMU)',
                        components: [ {
                            type: 'ue-textarea',
                            label: {
                                bold: 'bold',
                                key: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning',
                                required: true,
                                type: 'label'
                            },
                            modelProp: 'motiveringTillInteBaseratPaUndersokning'
                        }, {
                            type: 'ue-text',
                            key: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning.info',
                            variableLabelKey: 'FRG_25.RBK'
                        } ]
                    } ]
                }, {
                    type:'ue-kategori',
                    category: 2,
                    hideExpression: 'model.avstangningSmittskydd',
                    components: [{
                        type: 'ue-fraga',
                        frageId: 28,
                        label: {
                            key: 'FRG_28.RBK',
                            helpKey: 'FRG_28.HLP',
                            type: 'h4',
                            required: true
                        },
                        components: [{
                            type: 'ue-checkgroup',
                            modelProp: 'sysselsattning',
                            code: 'KV_FKMU_0002',
                            choices: ['NUVARANDE_ARBETE',
                                'ARBETSSOKANDE',
                                'FORALDRALEDIG',
                                'STUDIER'
                            ]
                        }]
                    }, {
                        type: 'ue-fraga',
                        frageId: 29,
                        label: {
                            key: 'FRG_29.RBK',
                            helpKey: 'FRG_29.HLP',
                            type: 'h4',
                            required: true
                        },
                        hideExpression: '!model.sysselsattning["NUVARANDE_ARBETE"]',
                        components: [{
                            type: 'ue-textarea',
                            modelProp: 'nuvarandeArbete'
                        }]
                    }]
                }, {
                    type:'ue-kategori',
                    category: 3,
                    components: [{
                        type: 'ue-fraga',
                        frageId: 6,
                        label: {
                            key: 'FRG_6.RBK',
                            helpKey: 'FRG_6.HLP',
                            type: 'h4',
                            required: true
                        },
                        components: [{
                            type: 'ue-diagnos',
                            modelProp: 'diagnoser',
                            diagnosBeskrivningLabel: 'DFR_6.1.RBK',
                            diagnosBeskrivningHelp: 'DFR_6.1.HLP',
                            diagnosKodLabel: 'DFR_6.2.RBK',
                            diagnosKodHelp: 'DFR_6.2.HLP'
                        }]
                    }]
                }, {
                    type: 'ue-kategori',
                    category: 4,
                    components: [{
                        type: 'ue-fraga',
                        frageId: 35,
                        label: {
                            key: 'FRG_35.RBK',
                            helpKey: 'FRG_35.HLP',
                            type: 'h4',
                            required: true
                        },
                        components: [{
                            type: 'ue-textarea',
                            modelProp: 'funktionsnedsattning',
                            label: {
                                key: 'DFR_35.1.RBK',
                                helpKey: 'DFR_35.1.HLP'
                            }
                        }]
                    }, {
                        type: 'ue-fraga',
                        frageId: 17,
                        label: {
                            key: 'FRG_17.RBK',
                            helpKey: 'FRG_17.HLP',
                            type: 'h4',
                            required: true
                        },
                        components: [{
                            type: 'ue-textarea',
                            modelProp: 'aktivitetsbegransning',
                            label: {
                                key: 'DFR_17.1.RBK',
                                helpKey: 'DFR_17.1.HLP'
                            }
                        }]
                    }]
                }, {
                    type: 'ue-kategori',
                    category: 5,
                    components: [{
                        type: 'ue-fraga',
                        frageId: 19,
                        label: {
                            key: 'FRG_19.RBK',
                            helpKey: 'FRG_19.HLP',
                            type: 'h4'
                        },
                        components: [{
                            type: 'ue-textarea',
                            modelProp: 'pagaendeBehandling',
                            label: {
                                key: 'DFR_19.1.RBK',
                                helpKey: 'DFR_19.1.HLP'
                            }
                        }]
                    }, {
                        type: 'ue-fraga',
                        frageId: 20,
                        label: {
                            key: 'FRG_20.RBK',
                            helpKey: 'FRG_20.HLP',
                            type: 'h4'
                        },
                        components: [{
                            type: 'ue-textarea',
                            modelProp: 'planeradBehandling',
                            label: {
                                key: 'DFR_20.1.RBK',
                                helpKey: 'DFR_20.1.HLP'
                            }
                        }]
                    }]
                }, {
                    type: 'ue-kategori',
                    category: 6,
                    components: [{
                        type: 'ue-fraga',
                        frageId: 32,
                        label: {
                            key: 'FRG_32.RBK',
                            helpKey: 'FRG_32.HLP',
                            type: 'h4',
                            required: true
                        },
                        components: [{
                            type: 'ue-sjukskrivningar',
                            modelProp: 'sjukskrivningar',
                            code: 'KV_FKMU_0003',
                            fields: [
                                'EN_FJARDEDEL',
                                'HALFTEN',
                                'TRE_FJARDEDEL',
                                'HELT_NEDSATT'
                            ]
                        }]
                    }, {
                        type: 'ue-fraga',
                        hideExpression: function(scope) {
                            var hide = true;
                            var warnings = scope.validation.warningMessagesByField;
                            if (warnings) {
                                angular.forEach(warnings.sjukskrivningar, function(w) {
                                    if (w.message ===
                                        'lisjp.validation.bedomning.sjukskrivningar.tidigtstartdatum') {
                                        hide = false;
                                    }
                                });
                            }
                            return hide;
                        },
                        components: [ {
                            type: 'ue-textarea',
                            label: {
                                bold: 'bold',
                                key: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering',
                                helpKey: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering.help',
                                required: true,
                                type: 'label'
                            },
                            modelProp: 'motiveringTillTidigtStartdatumForSjukskrivning'
                        }, {
                            type: 'ue-text',
                            key: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering.info',
                            variableLabelKey: 'FRG_25.RBK'
                        } ]
                    }, {
                        type: 'ue-fraga',
                        frageId: 37,
                        hideExpression: 'model.avstangningSmittskydd',
                        label: {
                            key: 'FRG_37.RBK',
                            helpKey: 'FRG_37.HLP',
                            type: 'h4'
                        },
                        components: [{
                            type: 'ue-textarea',
                            modelProp: 'forsakringsmedicinsktBeslutsstod'
                        }]
                    }, {
                        type: 'ue-fraga',
                        frageId: 33,
                        components: [{
                            type: 'ue-radio',
                            modelProp: 'arbetstidsforlaggning',
                            label: {
                                key: 'FRG_33.RBK',
                                helpKey: 'FRG_33.HLP',
                                type: 'h4'
                            },
                            hideExpression: function(scope) {

                                if (scope.model.avstangningSmittskydd) {
                                    return true;
                                }

                                var sjukskrivningar = scope.model.sjukskrivningar;

                                var nedsatt75under = false;
                                angular.forEach(sjukskrivningar, function(item, key) {
                                    if (!nedsatt75under && key !== 'HELT_NEDSATT') {
                                        if (item.period && DateUtils.isDate(item.period.from) &&
                                            DateUtils.isDate(item.period.tom)) {
                                            nedsatt75under = true;
                                        }
                                    }
                                });

                                return !nedsatt75under;
                            }
                        }, {
                            type: 'ue-textarea',
                            modelProp: 'arbetstidsforlaggningMotivering',
                            label: {
                                key: 'DFR_33.2.RBK',
                                helpKey: 'DFR_33.2.HLP',
                                type: 'h4',
                                required: true
                            },
                            hideExpression: function(scope) {
                                if (scope.model.avstangningSmittskydd) {
                                    return true;
                                }
                                return scope.model.arbetstidsforlaggning !== true;
                            }
                        }]
                    }, {
                        type: 'ue-fraga',
                        frageId: 34,
                        hideExpression: 'model.avstangningSmittskydd',
                        components: [{
                            type: 'ue-checkbox',
                            modelProp: 'arbetsresor',
                            label: {
                                key: 'FRG_34.RBK',
                                helpKey: 'FRG_34.HLP'
                            }
                        }]
                    }, {
                        type: 'ue-fraga',
                        frageId: 39,
                        label: {
                            key: 'FRG_39.RBK',
                            helpKey: 'FRG_39.HLP',
                            type: 'h4',
                            required: true
                        },
                        components: [{
                            type: 'ue-prognos',
                            modelProp: 'prognos',
                            code: 'KV_FKMU_0006',
                            choices: [{id: 'STOR_SANNOLIKHET', showDropDown: false},
                                {id: 'ATER_X_ANTAL_DGR', showDropDown: true},
                                {id: 'PROGNOS_OKLAR', showDropDown: false},
                                {id: 'SANNOLIKT_INTE', showDropDown: false}
                            ],
                            prognosDagarTillArbeteCode: 'KV_FKMU_0007',
                            prognosDagarTillArbeteTyper: ['TRETTIO_DGR',
                                'SEXTIO_DGR',
                                'NITTIO_DGR',
                                'HUNDRAATTIO_DAGAR',
                                'TREHUNDRASEXTIOFEM_DAGAR'
                            ]
                        }]
                    }]
                }, {
                    type: 'ue-kategori',
                    category: 7,
                    components: [{
                        type: 'ue-fraga',
                        frageId: 40,
                        label: {
                            key: 'FRG_40.RBK',
                            helpKey: 'FRG_40.HLP',
                            type: 'h4',
                            required: true
                        },
                        components: [{
                            type: 'ue-checkgroup',
                            modelProp: 'arbetslivsinriktadeAtgarder',
                            code: 'KV_FKMU_0004',
                            choices: ['EJ_AKTUELLT',
                                'ARBETSTRANING',
                                'ARBETSANPASSNING',
                                'SOKA_NYTT_ARBETE',
                                'BESOK_ARBETSPLATS',
                                'ERGONOMISK',
                                'HJALPMEDEL',
                                'KONFLIKTHANTERING',
                                'KONTAKT_FHV',
                                'OMFORDELNING',
                                'OVRIGA_ATGARDER'
                            ],
                            watcher: [{
                                type: '$watch',
                                watchDeep: true,
                                expression: 'model.arbetslivsinriktadeAtgarder',
                                listener: function(newValue, oldValue, scope) {
                                    if (oldValue && newValue !== oldValue) {
                                        if (newValue.EJ_AKTUELLT !== oldValue.EJ_AKTUELLT) {
                                            if (newValue.EJ_AKTUELLT) {
                                                angular.forEach(scope.model.arbetslivsinriktadeAtgarder,
                                                    function(atgard, key) {
                                                        if (key !== 'EJ_AKTUELLT') {
                                                            scope.model.arbetslivsinriktadeAtgarder[key] = undefined;
                                                        }
                                                    });
                                            }
                                        } else {
                                            angular.forEach(scope.model.arbetslivsinriktadeAtgarder,
                                                function(atgard, key) {
                                                    if (key !== 'EJ_AKTUELLT' && atgard) {
                                                        scope.model.arbetslivsinriktadeAtgarder.EJ_AKTUELLT = undefined;
                                                    }
                                                });
                                        }
                                    }
                                }
                            }]
                        }]
                    }]
                },{
                    type: 'ue-kategori',
                    category: 8,
                    components: [
                        {
                            type: 'ue-fraga',
                            frageId: 25,
                            components: [
                                {
                                    modelProp: 'ovrigt',
                                    type: 'ue-textarea'
                                }
                            ]
                        }
                    ]
                },
                {
                    type: 'ue-kategori',
                    category: 9,
                    hideExpression: 'model.avstangningSmittskydd',
                    components: [{
                        type: 'ue-fraga',
                        frageId: 26,
                        label: {
                            key: 'FRG_26.RBK',
                            helpKey: 'FRG_26.HLP',
                            type: 'h4',
                            required: true
                        },
                        components: [
                            {
                                type: 'ue-checkbox',
                                modelProp: 'kontaktMedFk',
                                label: {
                                    key: 'DFR_26.1.RBK',
                                    helpKey: 'DFR_26.1.HLP'
                                }
                            },
                            {
                                type: 'ue-textarea',
                                modelProp: 'anledningTillKontakt',
                                hideExpression: '!model.kontaktMedFk',
                                label: {
                                    type: 'h5',
                                    key: 'DFR_26.2.RBK',
                                    helpKey: 'DFR_26.2.HLP'
                                }
                            }
                        ]
                    }
                ]}//,
                //FactoryTemplates.vardenhet
            ];

            var formFields = [
                kategori(10, categoryNames[10], [
                    fraga(27, [
                        {
                            key: 'avstangningSmittskydd',
                            type: 'checkbox-inline',
                            templateOptions: {label: 'FRG_27', bold: true}
                        }
                    ])
                ]),
                kategori(1, categoryNames[1], [
                    fraga(1, [
                        {
                            type: 'headline',
                            templateOptions: {id: 'FRG_1', label: 'FRG_1', level: 4, noH5: false, required: true}
                        },
                        {
                            type: 'headline',
                            className: 'col-md-6',
                            templateOptions: {label: 'DFR_1.1'}
                        },
                        {
                            type: 'headline',
                            className: 'col-md-6',
                            templateOptions: {label: 'DFR_1.2'}
                        },
                        {
                            wrapper: 'validationGroup',
                            templateOptions: {
                                type: 'check-group',
                                validationGroup: 'baserasPa',
                                kompletteringGroup: 'baseratPa'
                            },
                            fieldGroup: [
                                {
                                    key: 'undersokningAvPatienten', type: 'date', className: 'small-gap',
                                    templateOptions: {
                                        label: 'KV_FKMU_0001.UNDERSOKNING',
                                        maxDate: DateUtils.todayAsYYYYMMDD()
                                    }
                                },
                                {
                                    key: 'telefonkontaktMedPatienten', type: 'date', className: 'small-gap',
                                    templateOptions: {
                                        label: 'KV_FKMU_0001.TELEFONKONTAKT',
                                        maxDate: DateUtils.todayAsYYYYMMDD()
                                    }
                                },
                                {
                                    key: 'journaluppgifter', type: 'date', className: 'small-gap',
                                    templateOptions: {
                                        label: 'KV_FKMU_0001.JOURNALUPPGIFTER',
                                        maxDate: DateUtils.todayAsYYYYMMDD()
                                    }
                                },
                                {
                                    key: 'annatGrundForMU', type: 'date',
                                    templateOptions: {
                                        label: 'KV_FKMU_0001.ANNAT',
                                        maxDate: DateUtils.todayAsYYYYMMDD()
                                    }
                                }
                            ]
                        },
                        FactoryTemplates.annatGrundForMUBeskrivningNoLine
                    ]),
                    fraga(25, [
                        {
                            key: 'motiveringTillInteBaseratPaUndersokning',
                            type: 'multi-text',
                            hideExpression: 'model.undersokningAvPatienten || !(model.telefonkontaktMedPatienten || model.journaluppgifter || model.annatGrundForMU)',
                            templateOptions: {
                                bold: 'bold',
                                forceHeadingTypeLabel: true,
                                staticLabelId: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning',
                                subTextId: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning.info',
                                subTextDynId: 'FRG_25',
                                required: true
                            }
                        }
                    ])
                ], {required: false, hideExpression: 'model.avstangningSmittskydd'}),
                kategori(2, categoryNames[2], [
                    fraga(28, [
                        {
                            key: 'sysselsattning', type: 'check-group',
                            templateOptions: {
                                label: 'FRG_28',
                                code: 'KV_FKMU_0002',
                                choices: ['NUVARANDE_ARBETE',
                                    'ARBETSSOKANDE',
                                    'FORALDRALEDIG',
                                    'STUDIER'
                                ],
                                required: true
                            }
                        }
                    ]),
                    fraga(29, [
                        {
                            key: 'nuvarandeArbete', type: 'multi-text',
                            hideExpression: '!model.sysselsattning["NUVARANDE_ARBETE"]',
                            templateOptions: {label: 'FRG_29', required: true}
                        }
                    ])
                ], {hideExpression: 'model.avstangningSmittskydd'}),
                kategori(3, categoryNames[3], [
                    fraga(6, [
                        {
                            wrapper: 'fmb-wrapper',
                            templateOptions: {
                                relatedFormId: categoryNames[3],
                                fieldName: 'DIAGNOS',
                                srsId: '2',
                                panelClass: 'sit-fmb-medium'
                            },
                            fieldGroup: [
                                {
                                    type: 'headline',
                                    templateOptions: {label: 'FRG_6', level: 4, noH5: false, required: true}
                                },
                                {
                                    key: 'diagnoser',
                                    type: 'diagnos',
                                    data: {enableFMB: true},
                                    templateOptions: {diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2'}
                                }
                            ]
                        }
                    ])
                ]),
                kategori(4, categoryNames[4], [
                    fraga(35, [
                        {
                            wrapper: 'fmb-wrapper',
                            templateOptions: {
                                relatedFormId: categoryNames[4],
                                fieldName: 'FUNKTIONSNEDSATTNING',
                                panelClass: 'sit-fmb-small'
                            },
                            fieldGroup: [
                                {
                                    key: 'funktionsnedsattning', type: 'multi-text', templateOptions: {
                                    label: 'DFR_35.1',
                                    required: 'FRG_ONLY'
                                }
                                }
                            ]
                        }
                    ]),
                    fraga(17, [
                        {
                            wrapper: 'fmb-wrapper',
                            templateOptions: {
                                relatedFormId: 'aktivitetsbegransning',
                                fieldName: 'AKTIVITETSBEGRANSNING',
                                panelClass: 'sit-fmb-large'
                            },
                            fieldGroup: [
                                {
                                    key: 'aktivitetsbegransning', type: 'multi-text', templateOptions: {
                                    label: 'DFR_17.1',
                                    required: 'FRG_ONLY'
                                }
                                }
                            ]
                        }
                    ])
                ], {hideExpression: 'model.avstangningSmittskydd'}),
                kategori(5, categoryNames[5], [
                    fraga(19, [
                        {
                            key: 'pagaendeBehandling', type: 'multi-text',
                            templateOptions: {
                                label: 'DFR_19.1'
                            }
                        }
                    ]),
                    fraga(20, [
                        {key: 'planeradBehandling', type: 'multi-text', templateOptions: {label: 'DFR_20.1'}}
                    ])
                ], {hideExpression: 'model.avstangningSmittskydd'}),
                kategori(6, categoryNames[6], [
                    fraga(32, [
                        {
                            wrapper: 'fmb-wrapper',
                            templateOptions: {
                                relatedFormId: categoryNames[6],
                                fieldName: 'ARBETSFORMAGA',
                                srsId: '3',
                                panelClass: 'sit-fmb-large'
                            },
                            fieldGroup: [
                                {
                                    key: 'sjukskrivningar', type: 'sjukskrivningar',
                                    templateOptions: {
                                        label: 'FRG_32',
                                        code: 'KV_FKMU_0003',
                                        fields: [
                                            'EN_FJARDEDEL',
                                            'HALFTEN',
                                            'TRE_FJARDEDEL',
                                            'HELT_NEDSATT'
                                        ],
                                        required: true
                                    }
                                }
                            ]
                        }
                    ]),
                    fraga(25, [
                        {
                            key: 'motiveringTillTidigtStartdatumForSjukskrivning',
                            type: 'multi-text',
                            hideExpression: function($viewValue, $modelValue, scope) {
                                var hide = true;
                                var warnings = scope.options.formState.viewState.common.validation.warningMessagesByField;
                                if (warnings) {
                                    angular.forEach(warnings.sjukskrivningar, function(w) {
                                        if (w.message ===
                                            'lisjp.validation.bedomning.sjukskrivningar.tidigtstartdatum') {
                                            hide = false;
                                        }
                                    });
                                }
                                return hide;
                            },
                            templateOptions: {
                                bold: 'bold',
                                forceHeadingTypeLabel: true,
                                staticLabelId: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering',
                                staticHelpId: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering.help',
                                subTextId: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering.info',
                                subTextDynId: 'FRG_25'
                            }
                        }
                    ]),
                    fraga(37, [
                        {
                            key: 'forsakringsmedicinsktBeslutsstod',
                            type: 'multi-text',
                            templateOptions: {
                                label: 'FRG_37'
                            },
                            hideExpression: 'model.avstangningSmittskydd'
                        }
                    ]),
                    fraga(33, [
                        {
                            key: 'arbetstidsforlaggning', type: 'boolean',
                            hideExpression: function($viewValue, $modelValue, scope) {

                                if (scope.model.avstangningSmittskydd) {
                                    return true;
                                }

                                var sjukskrivningar = scope.model.sjukskrivningar;

                                var nedsatt75under = false;
                                angular.forEach(sjukskrivningar, function(item, key) {
                                    if (!nedsatt75under && key !== 'HELT_NEDSATT') {
                                        if (item.period && DateUtils.isDate(item.period.from) &&
                                            DateUtils.isDate(item.period.tom)) {
                                            nedsatt75under = true;
                                        }
                                    }
                                });

                                return !nedsatt75under;
                            },
                            templateOptions: {label: 'FRG_33', required: true}
                        },
                        {
                            key: 'arbetstidsforlaggningMotivering', type: 'multi-text',
                            hideExpression: function($viewValue, $modelValue, scope) {
                                if (scope.model.avstangningSmittskydd) {
                                    return true;
                                }
                                return scope.model.arbetstidsforlaggning !== true;
                            },
                            templateOptions: {
                                label: 'DFR_33.2',
                                required: true
                            }
                        }
                    ]),
                    fraga(34, [
                        {
                            key: 'arbetsresor', type: 'checkbox-inline',
                            templateOptions: {
                                label: 'DFR_34.1',
                                bold: true
                            },
                            hideExpression: 'model.avstangningSmittskydd'
                        }
                    ]),
                    fraga(39, [
                        {
                            key: 'prognos', type: 'prognos',
                            hideExpression: 'model.avstangningSmittskydd',
                            templateOptions: {
                                label: 'FRG_39',
                                required: true,
                                code: 'KV_FKMU_0006',
                                choices: [{id: 'STOR_SANNOLIKHET', showDropDown: false},
                                    {id: 'ATER_X_ANTAL_DGR', showDropDown: true},
                                    {id: 'PROGNOS_OKLAR', showDropDown: false},
                                    {id: 'SANNOLIKT_INTE', showDropDown: false}
                                ],
                                prognosDagarTillArbeteCode: 'KV_FKMU_0007',
                                prognosDagarTillArbeteTyper: ['TRETTIO_DGR',
                                    'SEXTIO_DGR',
                                    'NITTIO_DGR',
                                    'HUNDRAATTIO_DAGAR',
                                    'TREHUNDRASEXTIOFEM_DAGAR'
                                ]
                            },
                            watcher: {
                                expression: 'model.prognos.typ',
                                listener: function _prognosTypListener(field, newValue, oldValue, scope, stopWatching) {
                                    var model = scope.model;
                                    if (newValue === 'ATER_X_ANTAL_DGR') {
                                        model.restoreFromAttic('prognos.dagarTillArbete');
                                    } else {
                                        if (oldValue === 'ATER_X_ANTAL_DGR') {
                                            model.updateToAttic('prognos.dagarTillArbete');
                                        }

                                        model.clear('prognos.dagarTillArbete');
                                    }
                                }
                            }
                        }
                    ])
                ]),
                kategori(7, categoryNames[7], [
                    fraga(40, [
                        {
                            wrapper: 'fmb-wrapper',
                            key: 'arbetslivsinriktadeAtgarder',
                            type: 'check-group',
                            templateOptions: {
                                label: 'FRG_40',
                                srsId: '4',
                                required: true,
                                descLabel: 'DFR_40.2',
                                code: 'KV_FKMU_0004',
                                choices: ['EJ_AKTUELLT',
                                    'ARBETSTRANING',
                                    'ARBETSANPASSNING',
                                    'SOKA_NYTT_ARBETE',
                                    'BESOK_ARBETSPLATS',
                                    'ERGONOMISK',
                                    'HJALPMEDEL',
                                    'KONFLIKTHANTERING',
                                    'KONTAKT_FHV',
                                    'OMFORDELNING',
                                    'OVRIGA_ATGARDER'
                                ]
                            },
                            watcher: [{
                                type: '$watch',
                                watchDeep: true,
                                expression: 'model.arbetslivsinriktadeAtgarder',
                                listener: function(field, newValue, oldValue, scope) {
                                    if (oldValue && newValue !== oldValue) {
                                        if (newValue.EJ_AKTUELLT !== oldValue.EJ_AKTUELLT) {
                                            if (newValue.EJ_AKTUELLT) {
                                                angular.forEach(scope.model.arbetslivsinriktadeAtgarder,
                                                    function(atgard, key) {
                                                        if (key !== 'EJ_AKTUELLT') {
                                                            scope.model.arbetslivsinriktadeAtgarder[key] = undefined;
                                                        }
                                                    });
                                            }
                                        } else {
                                            angular.forEach(scope.model.arbetslivsinriktadeAtgarder,
                                                function(atgard, key) {
                                                    if (key !== 'EJ_AKTUELLT' && atgard) {
                                                        scope.model.arbetslivsinriktadeAtgarder.EJ_AKTUELLT = undefined;
                                                    }
                                                });
                                        }
                                    }
                                }
                            }]
                        }
                    ]),
                    fraga(44, [
                        {
                            key: 'arbetslivsinriktadeAtgarderBeskrivning',
                            type: 'multi-text',
                            hideExpression: function($viewValue, $modelValue, scope) {
                                var hide = true;
                                angular.forEach(scope.model.arbetslivsinriktadeAtgarder, function(atgard, key) {
                                    if (atgard === true && key !== 'EJ_AKTUELLT') {
                                        hide = false;
                                        return;
                                    }
                                });
                                return hide;
                            },
                            templateOptions: {label: 'FRG_44', required: false}
                        }
                    ])
                ], {hideExpression: 'model.avstangningSmittskydd'}),
                kategori(8, categoryNames[8], [
                    fraga(25, [
                        {
                            key: 'ovrigt',
                            type: 'multi-text',
                            templateOptions: {label: 'DFR_25.1'},
                            ngModelElAttrs: {
                                'wc-focus-on': 'focusOvrigt'
                            }
                        }
                    ])
                ]),
                kategori(9, categoryNames[9], [
                    fraga(26, [
                        {
                            key: 'kontaktMedFk',
                            type: 'checkbox-inline',
                            templateOptions: {label: 'DFR_26.1'}
                        },
                        {
                            key: 'anledningTillKontakt',
                            type: 'multi-text',
                            hideExpression: '!model.kontaktMedFk',
                            templateOptions: {label: 'DFR_26.2'}
                        }
                    ])
                ], {hideExpression: 'model.avstangningSmittskydd'}),
                FactoryTemplates.vardenhet
            ];

            return {
                getConfig: function() {
                    return angular.copy(config);
                },
                getCategoryNames: function() {
                    return angular.copy(categoryNames);
                }
            };
        }]);
