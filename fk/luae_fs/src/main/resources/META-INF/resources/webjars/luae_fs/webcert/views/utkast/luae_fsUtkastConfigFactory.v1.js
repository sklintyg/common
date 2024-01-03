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
angular.module('luae_fs').factory('luae_fs.UtkastConfigFactory.v1',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ueFactoryTemplatesHelper',
        function($log, $timeout, DateUtils, ueFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                return {
                    1: 'grundformu',
                    2: 'underlag',
                    3: 'diagnos',
                    4: 'funktionsnedsattning',
                    5: 'ovrigt',
                    6: 'kontakt'
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
                    {label: 'KV_FKMU_0005.SKOLHALSOVARD.RBK', id: 'SKOLHALSOVARD'},
                    {label: 'KV_FKMU_0005.SPECIALISTKLINIK.RBK', id: 'SPECIALISTKLINIK'},
                    {label: 'KV_FKMU_0005.VARD_UTOMLANDS.RBK', id: 'VARD_UTOMLANDS'},
                    {label: 'KV_FKMU_0005.OVRIGT_UTLATANDE.RBK', id: 'OVRIGT_UTLATANDE'}
                ];

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var today = moment().format('YYYY-MM-DD');

                var isLocked = viewState.common.intyg.isLocked;
                var lockedExpression = viewState.common.intyg.isLocked ? '&& model.motiveringTillInteBaseratPaUndersokning' : '';
                var motiveringBaseratHideExpression = '!(!model.undersokningAvPatienten && (model.anhorigsBeskrivningAvPatienten || ' + 
                'model.journaluppgifter || model.annatGrundForMU)' + lockedExpression + ')';

                var isKompletteringsUtkast = false;
                if(viewState.relations !== undefined && viewState.relations.parent !== undefined && viewState.relations.parent.relationKod !== undefined){
                    isKompletteringsUtkast=viewState.relations.parent.relationKod === 'KOMPLT';
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

                var config = [
                    kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', {signingDoctor: true}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', {
                            validationContext: {
                                key: 'baseratPa',
                                type: 'ue-checkgroup'
                            },
                            required: true,
                            requiredProp: ['undersokningAvPatienten', 'journaluppgifter', 'anhorigsBeskrivningAvPatienten', 'annatGrundForMU'] },
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
                            modelProp: 'motiveringTillInteBaseratPaUndersokning',
                            htmlMaxlength: 150,
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
                        fraga(3, 'FRG_3.RBK', 'FRG_3.HLP', { required: true, requiredProp: 'underlagFinns'}, [{
                            type: 'ue-radio',
                            modelProp: 'underlagFinns',
                            paddingBottom: true
                        },{
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

                    kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', {}, [
                        fraga(6, 'FRG_6.RBK', 'FRG_6.HLP', { required: true, requiredProp: 'diagnoser[0].diagnosKod'}, [{
                            type: 'ue-diagnos',
                            modelProp: 'diagnoser',
                            descriptionMaxLength: 81,
                            defaultKodSystem: 'ICD_10_SE',
                            diagnosBeskrivningLabel: 'DFR_6.1.RBK',
                            diagnosBeskrivningHelp: 'DFR_6.1.HLP',
                            diagnosKodLabel: 'DFR_6.2.RBK',
                            diagnosKodHelp: 'DFR_6.2.HLP'
                        }])
                    ]),

                    kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', {}, [
                        fraga(15, 'FRG_15.RBK', 'FRG_15.HLP', { required: true, requiredProp: 'funktionsnedsattningDebut'}, [{
                            type: 'ue-textarea',
                            modelProp: 'funktionsnedsattningDebut'
                        }]),
                        fraga(16, 'FRG_16.RBK', 'FRG_16.HLP', { required: true, requiredProp: 'funktionsnedsattningPaverkan'}, [{
                            type: 'ue-textarea',
                            modelProp: 'funktionsnedsattningPaverkan'
                        }])
                    ]),

                    kategori(categoryIds[5], 'KAT_5.RBK', 'KAT_5.HLP', {}, [
                        fraga(25, 'FRG_25.RBK', 'FRG_25.HLP', { }, [{
                            type: 'ue-textarea',
                            modelProp: 'ovrigt',
                            htmlMaxlength: isKompletteringsUtkast ? 3446 : 2850
                        }])
                    ]),

                    kategori(categoryIds[6], 'KAT_6.RBK', 'KAT_6.HLP', { }, [
                        fraga(26, 'FRG_26.RBK', 'FRG_26.HLP', { }, [{
                            type: 'ue-checkbox',
                            modelProp: 'kontaktMedFk',
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
