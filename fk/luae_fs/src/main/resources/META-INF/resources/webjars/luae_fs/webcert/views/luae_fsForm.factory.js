/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('luae_fs').factory('luae_fs.FormFactory',
    ['luae_fs.FormFactoryHelper', 'common.UserModel', 'common.FactoryTemplatesHelper',
        function(FactoryHelper, UserModel, FactoryTemplates) {
            'use strict';


            var categoryNames = {
                1: 'grundformu',
                2: 'underlag',
                3: 'diagnos',
                4: 'funktionsnedsattning',
                5: 'ovrigt',
                6: 'kontakt'
            };
            var kategori = FactoryTemplates.kategori;
            var fraga = FactoryTemplates.fraga;

            var formFields = [
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
                                required: true
                            }
                        }
                    ]),
                    fraga(2, [
                        {
                            key: 'kannedomOmPatient',
                            type: 'singleDate',
                            templateOptions: {label: 'FRG_2', required: true}
                        }
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
                                    'SKOLHALSOVARD',
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
                kategori(3, categoryNames[3], [
                    fraga(6, [
                        {
                            type: 'headline',
                            templateOptions: {label: 'FRG_6', level: 4, noH5After: false, required: true}
                        },
                        {
                            key: 'diagnoser',
                            type: 'diagnos',
                            templateOptions: {diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2'}
                        }])
                ]),
                kategori(4, categoryNames[4], [
                    fraga(15, [
                        {
                            key: 'funktionsnedsattningDebut',
                            type: 'multi-text',
                            templateOptions: {label: 'FRG_15', required: true}
                        }]),
                    fraga(16, [{
                        key: 'funktionsnedsattningPaverkan',
                        type: 'multi-text',
                        templateOptions: {label: 'FRG_16', required: true}
                    }])
                ]),
                kategori(5, categoryNames[5], [
                    fraga(25, [
                        {
                            key: 'ovrigt',
                            type: 'multi-text',
                            templateOptions: {label: 'FRG_25'},
                            ngModelElAttrs: {
                                'wc-focus-on': 'focusOvrigt'
                            }
                        }
                    ])
                ]),
                kategori(6, categoryNames[6], [
                    fraga(25, [
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
        }]);
