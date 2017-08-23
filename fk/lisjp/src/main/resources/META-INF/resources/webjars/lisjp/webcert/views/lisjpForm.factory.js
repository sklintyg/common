angular.module('lisjp').factory('lisjp.FormFactory',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ObjectHelper', 'common.UserModel',
        'common.FactoryTemplatesHelper', 'common.DateUtilsService',
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

            var formFields = [
                FactoryTemplates.adress,
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
                            className: 'col-md-6 no-space-left',
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
                                panelClass: 'sit-fmb-large'
                            },
                            fieldGroup: [
                                {
                                    key: 'sjukskrivningar', type: 'sjukskrivningar',
                                    templateOptions: {
                                        label: 'DFR_32.1',
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
                            key: 'arbetslivsinriktadeAtgarder',
                            type: 'check-group',
                            templateOptions: {
                                label: 'FRG_40',
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
                        {key: 'ovrigt', type: 'multi-text', templateOptions: {label: 'DFR_25.1'}}
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
                getFormFields: function() {
                    return angular.copy(formFields);
                },
                getCategoryNames: function() {
                    return angular.copy(categoryNames);
                }
            };
        }]);
