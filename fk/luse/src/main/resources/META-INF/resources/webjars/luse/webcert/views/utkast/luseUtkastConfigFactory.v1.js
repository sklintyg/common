/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('luse').factory('luse.UtkastConfigFactory.v1',
    ['common.ueFactoryTemplatesHelper',
        function(ueFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                return {
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
            }

            function _getConfig(viewState) {
                var categoryIds = _getCategoryIds();

                var underlagChoices = [
                    {label: 'common.label.choose', id: null},
                    {label: 'KV_FKMU_0005.NEUROPSYKIATRISKT.RBK', id: 'NEUROPSYKIATRISKT'},
                    {label: 'KV_FKMU_0005.HABILITERING.RBK', id: 'HABILITERING'},
                    {label: 'KV_FKMU_0005.ARBETSTERAPEUT.RBK', id: 'ARBETSTERAPEUT'},
                    {label: 'KV_FKMU_0005.FYSIOTERAPEUT.RBK', id: 'FYSIOTERAPEUT'},
                    {label: 'KV_FKMU_0005.LOGOPED.RBK', id: 'LOGOPED'},
                    {label: 'KV_FKMU_0005.PSYKOLOG.RBK', id: 'PSYKOLOG'},
                    {label: 'KV_FKMU_0005.FORETAGSHALSOVARD.RBK', id: 'FORETAGSHALSOVARD'},
                    {label: 'KV_FKMU_0005.SPECIALISTKLINIK.RBK', id: 'SPECIALISTKLINIK'},
                    {label: 'KV_FKMU_0005.VARD_UTOMLANDS.RBK', id: 'VARD_UTOMLANDS'},
                    {label: 'KV_FKMU_0005.OVRIGT_UTLATANDE.RBK', id: 'OVRIGT_UTLATANDE'}
                ];

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var today = moment().format('YYYY-MM-DD');

                var isLocked = viewState.common.intyg.isLocked;
                var lockedExpression = isLocked ? '&& model.motiveringTillInteBaseratPaUndersokning' : '';
                var motiveringBaseratHideExpression = '!(!model.undersokningAvPatienten && (model.anhorigsBeskrivningAvPatienten || ' + 
                'model.journaluppgifter || model.annatGrundForMU)' + lockedExpression + ')';

                var isKompletteringsUtkast = false;
                if(viewState.relations !== undefined && viewState.relations.parent !== undefined && viewState.relations.parent.relationKod !== undefined){
                    isKompletteringsUtkast = viewState.relations.parent.relationKod === 'KOMPLT';
                }

                var buildUnderlagConfigRow = function(row) {
                    return [ {
                        type: 'ue-dropdown',
                        modelProp: 'underlag[' + row + '].typ',
                        choices: underlagChoices,
                        skipAttic: (row > 0)

                    }, {
                        type: 'ue-date',
                        maxDate: today,
                        modelProp: 'underlag[' + row + '].datum',
                        skipAttic: true
                    }, {
                        type: 'ue-textfield',
                        modelProp: 'underlag[' + row + '].hamtasFran',
                        size: 'full-width',
                        htmlMaxlength: 53,
                        skipAttic: true
                    } ];

                };

                function funktionsnedsattningar() {
                    return ['funktionsnedsattningIntellektuell','funktionsnedsattningKommunikation', 'funktionsnedsattningKoncentration',
                        'funktionsnedsattningPsykisk', 'funktionsnedsattningSynHorselTal', 'funktionsnedsattningBalansKoordination', 'funktionsnedsattningAnnan'];
                }

                var config = [
                    kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', {signingDoctor: true}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', {
                                validationContext: {
                                    key: 'baseratPa',
                                    type: 'ue-checkgroup'
                                },
                                required: true,
                                requiredProp: ['undersokningAvPatienten', 'journaluppgifter', 'anhorigsBeskrivningAvPatienten', 'annatGrundForMU']
                            },
                            [{
                                type: 'ue-grid',
                                independentRowValidation: true,
                                components: [
                                    // Row 1
                                    [{
                                        label: {
                                            key: 'KV_FKMU_0001.UNDERSOKNING.RBK',
                                            helpKey: 'KV_FKMU_0001.UNDERSOKNING.HLP'
                                        },
                                        type: 'ue-checkbox-date',
                                        modelProp: 'undersokningAvPatienten',
                                        maxDate: today
                                    }],
                                    // Row 2
                                    [{
                                        label: {
                                            key: 'KV_FKMU_0001.JOURNALUPPGIFTER.RBK',
                                            helpKey: 'KV_FKMU_0001.JOURNALUPPGIFTER.HLP'
                                        },
                                        type: 'ue-checkbox-date',
                                        modelProp: 'journaluppgifter',
                                        maxDate: today
                                    }],
                                    // Row 3
                                    [{
                                        label: {
                                            key: 'KV_FKMU_0001.ANHORIG.RBK',
                                            helpKey: 'KV_FKMU_0001.ANHORIG.HLP'
                                        },
                                        type: 'ue-checkbox-date',
                                        modelProp: 'anhorigsBeskrivningAvPatienten',
                                        maxDate: today
                                    }],
                                    // Row 4
                                    [{
                                        label: {
                                            key: 'KV_FKMU_0001.ANNAT.RBK',
                                            helpKey: 'KV_FKMU_0001.ANNAT.HLP'
                                        },
                                        type: 'ue-checkbox-date',
                                        modelProp: 'annatGrundForMU',
                                        maxDate: today
                                    }],
                                    [{
                                        label: {
                                            key: 'DFR_1.3.RBK',
                                            helpKey: 'DFR_1.3.HLP',
                                            required: true,
                                            requiredProp: 'annatGrundForMUBeskrivning'
                                        },
                                        type: 'ue-textfield',
                                        hideExpression: '!model.annatGrundForMU',
                                        modelProp: 'annatGrundForMUBeskrivning'
                                    }]
                                ]
                            }]
                        ),
                        fraga(1, '', '', { hideExpression: motiveringBaseratHideExpression }, [{
                            type: 'ue-textarea',
                            htmlMaxlength: 150,
                            modelProp: 'motiveringTillInteBaseratPaUndersokning',
                            label: {
                                bold: 'bold',
                                key: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning',
                                materialIcon: 'lightbulb_outline',
                                isLocked: isLocked,
                                helpKey: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning.help',
                                variableLabelKey: 'FRG_25.RBK'
                            }
                        }]),
                        fraga(2, 'FRG_2.RBK', 'FRG_2.HLP', {required: true, requiredProp: 'kannedomOmPatient'}, [{
                            type: 'ue-date',
                            maxDate: today,
                            modelProp: 'kannedomOmPatient'

                        }]),
                        fraga(3, 'FRG_3.RBK', 'FRG_3.HLP', {required: true, requiredProp: 'underlagFinns'}, [{
                            type: 'ue-radio',
                            modelProp: 'underlagFinns',
                            paddingBottom: true
                        },
                        {
                            type: 'ue-grid',
                            hideExpression: '!model.underlagFinns',
                            colSizes: [3,3,6],
                            independentRowValidation: true,
                            modelProp: 'underlag',
                            firstRequiredRow: 1,
                            firstRequiredRowKey: 'underlag[0]',
                            components: [
                                // Row 1
                                [{
                                    type: 'ue-form-label',
                                    required: true,
                                    key: 'FRG_4.RBK',
                                    helpKey: 'FRG_4.RBK.HLP',
                                    requiredProp: 'underlag[0].typ'
                                },{
                                    type: 'ue-form-label',
                                    required: true,
                                    key: 'common.label.date',
                                    requiredProp: 'underlag[0].datum'
                                },{
                                    type: 'ue-form-label',
                                    required: true,
                                    key: 'DFR_4.3.RBK',
                                    helpKey: 'DFR_4.3.HLP',
                                    requiredProp: 'underlag[0].hamtasFran'
                                }],
                                // Row 2-4
                                buildUnderlagConfigRow(0),
                                buildUnderlagConfigRow(1),
                                buildUnderlagConfigRow(2)
                            ]

                        }])
                    ]),

                    kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', {}, [
                        fraga(6, 'FRG_6.RBK', 'FRG_6.HLP', { required: true, requiredProp: 'diagnoser[0].diagnosKod'}, [{
                            type: 'ue-diagnos',
                            modelProp: 'diagnoser',
                            descriptionMaxLength: 81,
                            defaultKodSystem: 'ICD_10_SE',
                            diagnosBeskrivningLabel: 'DFR_6.1.RBK',
                            diagnosBeskrivningHelp: 'DFR_6.1.HLP',
                            diagnosKodLabel: 'DFR_6.2.RBK',
                            diagnosKodHelp: 'DFR_6.2.HLP'
                        }]),
                        fraga(7, 'FRG_7.RBK', 'FRG_7.HLP', { required: true, requiredProp: 'diagnosgrund'}, [{
                            type: 'ue-textarea',
                            modelProp: 'diagnosgrund'
                        }]),
                        fraga(45, 'FRG_45.RBK', 'FRG_45.HLP', { required: true, requiredProp: 'nyBedomningDiagnosgrund'}, [{
                            type: 'ue-radio',
                            modelProp: 'nyBedomningDiagnosgrund'
                        },{
                            type: 'ue-textarea',
                            modelProp: 'diagnosForNyBedomning',
                            hideExpression: '!model.nyBedomningDiagnosgrund',
                            label: {
                                key: 'DFR_45.2.RBK',
                                required: true,
                                requiredProp: 'diagnosForNyBedomning'
                            }
                        } ])
                    ]),

                    kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', {}, [
                        fraga(5, 'FRG_5.RBK', 'FRG_5.HLP', { required: true, requiredProp: 'sjukdomsforlopp'}, [{
                            type: 'ue-textarea',
                            modelProp: 'sjukdomsforlopp'
                        }])
                    ]),

                    kategori(categoryIds[5], 'KAT_5.RBK', 'KAT_5.HLP', { required: true , requiredProp: funktionsnedsattningar() }, [
                        fraga(8, '', '', {}, [{
                            type: 'ue-component-toggler',
                            modelPropToWatch: 'funktionsnedsattningIntellektuell',
                            id: 'check-funktionsnedsattningIntellektuell',
                            validationContext: {key: 'funktionsnedsattning' },
                            label: {
                                labelType: 'h4',
                                key: 'FRG_8.RBK',
                                helpKey: 'FRG_8.HLP'
                            },
                            components: [ {
                                type: 'ue-textarea',
                                modelProp: 'funktionsnedsattningIntellektuell',
                                label: {
                                    key: 'DFR_8.1.RBK',
                                    helpKey: 'DFR_8.1.HLP'
                                }
                            }]
                        }]), fraga(9, '', '', {}, [ {
                            type: 'ue-component-toggler',
                            modelPropToWatch: 'funktionsnedsattningKommunikation',
                            id: 'check-funktionsnedsattningKommunikation',
                            validationContext: {key: 'funktionsnedsattning' },
                            label: {
                                labelType: 'h4',
                                key: 'FRG_9.RBK',
                                helpKey: 'FRG_9.HLP'
                            },
                            components: [ {
                                type: 'ue-textarea',
                                modelProp: 'funktionsnedsattningKommunikation',
                                label: {
                                    key: 'DFR_9.1.RBK',
                                    helpKey: 'DFR_9.1.HLP'
                                }
                            }]
                        }]), fraga(10, '', '', {}, [ {
                            type: 'ue-component-toggler',
                            modelPropToWatch: 'funktionsnedsattningKoncentration',
                            id: 'check-funktionsnedsattningKoncentration',
                            validationContext: {key: 'funktionsnedsattning' },
                            label: {
                                labelType: 'h4',
                                key: 'FRG_10.RBK',
                                helpKey: 'FRG_10.HLP'
                            },
                            components: [ {
                                type: 'ue-textarea',
                                modelProp: 'funktionsnedsattningKoncentration',
                                label: {
                                    key: 'DFR_10.1.RBK',
                                    helpKey: 'DFR_10.1.HLP'
                                }
                            }]
                        }]), fraga(11, '', '', {}, [{
                            type: 'ue-component-toggler',
                            modelPropToWatch: 'funktionsnedsattningPsykisk',
                            id: 'check-funktionsnedsattningPsykisk',
                            validationContext: {key: 'funktionsnedsattning' },
                            label: {
                                labelType: 'h4',
                                key: 'FRG_11.RBK',
                                helpKey: 'FRG_11.HLP'
                            },
                            components: [ {
                                type: 'ue-textarea',
                                modelProp: 'funktionsnedsattningPsykisk',
                                label: {
                                    key: 'DFR_11.1.RBK',
                                    helpKey: 'DFR_11.1.HLP'
                                }
                            }]
                        }]), fraga(12, '', '', {}, [ {
                            type: 'ue-component-toggler',
                            modelPropToWatch: 'funktionsnedsattningSynHorselTal',
                            id: 'check-funktionsnedsattningSynHorselTal',
                            validationContext: {key: 'funktionsnedsattning' },
                            label: {
                                labelType: 'h4',
                                key: 'FRG_12.RBK',
                                helpKey: 'FRG_12.HLP'
                            },
                            components: [ {
                                type: 'ue-textarea',
                                modelProp: 'funktionsnedsattningSynHorselTal',
                                label: {
                                    key: 'DFR_12.1.RBK',
                                    helpKey: 'DFR_12.1.HLP'
                                }
                            }]
                        }]), fraga(13, '', '', {}, [ {
                            type: 'ue-component-toggler',
                            modelPropToWatch: 'funktionsnedsattningBalansKoordination',
                            id: 'check-funktionsnedsattningBalansKoordination',
                            validationContext: {key: 'funktionsnedsattning' },
                            label: {
                                labelType: 'h4',
                                key: 'FRG_13.RBK',
                                helpKey: 'FRG_13.HLP'
                            },
                            components: [ {
                                type: 'ue-textarea',
                                modelProp: 'funktionsnedsattningBalansKoordination',
                                label: {
                                    key: 'DFR_13.1.RBK',
                                    helpKey: 'DFR_13.1.HLP'
                                }
                            }]
                        }]),fraga(14, '', '', {}, [ {
                            type: 'ue-component-toggler',
                            modelPropToWatch: 'funktionsnedsattningAnnan',
                            id: 'check-funktionsnedsattningAnnan',
                            validationContext: {key: 'funktionsnedsattning' },
                            label: {
                                labelType: 'h4',
                                key: 'FRG_14.RBK',
                                helpKey: 'FRG_14.HLP'
                            },
                            components: [ {
                                type: 'ue-textarea',
                                modelProp: 'funktionsnedsattningAnnan',
                                label: {
                                    key: 'DFR_14.1.RBK',
                                    helpKey: 'DFR_14.1.HLP'
                                }
                            }]
                        }]),
                        fraga(null, '', '', { validationContext: {key: 'funktionsnedsattning', type: 'ue-checkgroup'} })
                    ]),

                    kategori(categoryIds[6], 'KAT_6.RBK', 'KAT_6.HLP', { }, [
                        fraga(17, 'FRG_17.RBK', 'FRG_17.HLP', { }, [{
                            type: 'ue-textarea',
                            modelProp: 'aktivitetsbegransning',
                            label: {
                                key: 'DFR_17.1.RBK',
                                helpKey: 'DFR_17.1.HLP',
                                required: true,
                                requiredProp: 'aktivitetsbegransning'
                            }
                        }])
                    ]),


                    kategori(categoryIds[7], 'KAT_7.RBK', 'KAT_7.HLP', { }, [
                        fraga(18, 'FRG_18.RBK', 'FRG_18.HLP', { }, [{
                            type: 'ue-textarea',
                            modelProp: 'avslutadBehandling',
                            label: { key: 'DFR_18.1.RBK', helpKey: 'DFR_18.1.HLP' }
                        }]),
                        fraga(19, 'FRG_19.RBK', 'FRG_19.HLP', { }, [{
                            type: 'ue-textarea',
                            modelProp: 'pagaendeBehandling',
                            label: { key: 'DFR_19.1.RBK', helpKey: 'DFR_19.1.HLP' }
                        }]),
                        fraga(20, 'FRG_20.RBK', 'FRG_20.HLP', { }, [{
                            type: 'ue-textarea',
                            modelProp: 'planeradBehandling',
                            label: { key: 'DFR_20.1.RBK', helpKey: 'DFR_20.1.HLP' }
                        }]),
                        fraga(21, 'FRG_21.RBK', 'FRG_21.HLP', { }, [{
                            type: 'ue-textarea',
                            modelProp: 'substansintag',
                            label: { key: 'DFR_21.1.RBK', helpKey: 'DFR_21.1.HLP' }
                        }])
                    ]),


                    kategori(categoryIds[8], 'KAT_8.RBK', 'KAT_8.HLP', { }, [
                        fraga(22, 'FRG_22.RBK', 'FRG_22.HLP', { required: true, requiredProp: 'medicinskaForutsattningarForArbete'}, [{
                            type: 'ue-textarea',
                            modelProp: 'medicinskaForutsattningarForArbete'
                        }]),
                        fraga(23, 'FRG_23.RBK', 'FRG_23.HLP', { }, [{
                            type: 'ue-textarea',
                            modelProp: 'formagaTrotsBegransning'
                        }])
                    ]),

                    kategori(categoryIds[9], 'KAT_9.RBK', 'KAT_9.HLP', { }, [
                        fraga(25, 'FRG_25.RBK', 'FRG_25.HLP', { }, [{
                            type: 'ue-textarea',
                            modelProp: 'ovrigt',
                            htmlMaxlength: isKompletteringsUtkast? 3446 : 2850
                        }])
                    ]),

                    kategori(categoryIds[10], 'KAT_10.RBK', 'KAT_10.HLP', { }, [
                        fraga(26, 'FRG_26.RBK', 'FRG_26.HLP', { }, [{
                            type: 'ue-checkbox',
                            modelProp: 'kontaktMedFk',
                            paddingBottom: true,
                            label: {
                                key: 'DFR_26.1.RBK',
                                helpKey: 'DFR_26.1.HLP'
                            }
                        },{
                            type: 'ue-textarea',
                            modelProp: 'anledningTillKontakt',
                            hideExpression: '!model.kontaktMedFk',
                            label: {
                                labelType: 'h5',
                                key: 'DFR_26.2.RBK',
                                helpKey: 'DFR_26.2.HLP'
                            }
                        }])
                    ]),

                    {
                        type: 'ue-tillaggsfragor'
                    },

                    ueFactoryTemplates.vardenhet
                ];

                return config;
            }


            return {
                getConfig: _getConfig,
                getCategoryIds: _getCategoryIds
            };
        }]);
