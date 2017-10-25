/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

angular.module('doi').factory('doi.FormFactory',
    ['$log', '$timeout',
        'common.ObjectHelper', 'common.UserModel',
        'common.FactoryTemplatesHelper', 'common.SOSFactoryTemplatesHelper',
        function($log, $timeout,
            ObjectHelper, UserModel, FactoryTemplates, SOSFactoryTemplates) {
            'use strict';

            // Validation category names matched with backend message strings from InternalDraftValidator
            var categoryNames = {
                1: 'personuppgifter',
                2: 'dodsdatumochdodsplats',
                3: 'barnsomavlidit',
                7: 'utlatandeorsak',
                8: 'operation',
                9: 'forgiftning',
                10: 'dodsorsakgrund'
            };

            var kategori = FactoryTemplates.kategori;
            var fraga = FactoryTemplates.fraga;

            var formFields = [
                FactoryTemplates.adress,
                SOSFactoryTemplates.identitet(categoryNames[1], true),
                SOSFactoryTemplates.dodsDatum(categoryNames[2]),
                SOSFactoryTemplates.barn(categoryNames[3]),
                kategori(7, categoryNames[7], [
                    fraga(8, [
                        {
                            type: 'headline',
                            templateOptions: {id: 'KAT_7.1', label: 'KAT_7.1'}
                        },
                        {
                            key: 'terminalDodsorsak',
                            type: 'dodsorsakTerminalFoljd', // R9
                            templateOptions: {
                                label: 'FRG_8',
                                orsaksTyper: [
                                    'UPPGIFT_SAKNAS',
                                    'KRONISK',
                                    'PLOTSLIG'
                                ],
                                letter: 'A',
                                required: false,
                                maxlength: 140,
                                beskrivningLabel: 'DELAT_TEXT.BESKRIVNING',
                                datumLabel: 'DELAT_TEXT.DEBUT',
                                orsakLabel: 'DELAT_TEXT.SPECIFIKATION',
                                foljd: {
                                    key: 'foljd',
                                    maxRows: 3,
                                    required: false,
                                    maxlength: 140,
                                    letter: ['B', 'C', 'D'],
                                    label: 'FRG_9',
                                    beskrivningLabel: 'DELAT_TEXT.BESKRIVNING',
                                    datumLabel: 'DELAT_TEXT.DEBUT',
                                    orsakLabel: 'DELAT_TEXT.SPECIFIKATION',
                                    orsaksTyper: [
                                        'UPPGIFT_SAKNAS',
                                        'KRONISK',
                                        'PLOTSLIG'
                                    ]
                                }
                            }
                        }
                    ]),
                    fraga(10, [
                        {
                            type: 'headline',
                            templateOptions: {id: 'KAT_7.2', label: 'KAT_7.2', bold: true}
                        },
                        {
                            key: 'bidragandeSjukdomar',
                            type: 'dodsorsakMulti', // R11
                            templateOptions: {
                                maxRows: 8,
                                maxlength: 45,
                                orsaksTyper: [
                                    'UPPGIFT_SAKNAS',
                                    'KRONISK',
                                    'PLOTSLIG'
                                ],
                                beskrivningLabel: 'DELAT_TEXT.BESKRIVNING',
                                datumLabel: 'DELAT_TEXT.DEBUT',
                                orsakLabel: 'DELAT_TEXT.SPECIFIKATION'
                            }
                        }
                    ])
                ]),
                kategori(8, categoryNames[8], [
                    fraga(11, [
                        {
                            key: 'operation',
                            type: 'radio-group', // R12
                            templateOptions: {
                                label: 'DFR_11.1',
                                code: 'DFR_11.1',
                                choices: [
                                    'JA',
                                    'NEJ',
                                    'UPPGIFT_SAKNAS'
                                ],
                                required: true
                            }
                        },
                        {
                            key: 'operationDatum',
                            type: 'singleDate',
                            hideExpression: 'model.operation !== "JA"', // R13
                            templateOptions: {label: 'DFR_11.2', required: true}
                        },
                        {
                            key: 'operationAnledning',
                            type: 'single-text-vertical',
                            hideExpression: 'model.operation !== "JA"', // R13
                            templateOptions: {label: 'DFR_11.3', required: true, maxlength: 100}
                        }
                    ])
                ]),
                kategori(9, categoryNames[9], [
                    fraga(12, [
                        {
                            key: 'forgiftning',
                            type: 'boolean',
                            templateOptions: {
                                label: 'DFR_12.1',
                                required: true
                            }
                        },
                        {
                            key: 'forgiftningOrsak',
                            type: 'radio-group', // R15
                            hideExpression: 'model.forgiftning !== true', // R14
                            templateOptions: {
                                label: 'DFR_12.2',
                                code: 'ORSAK',
                                choices: [
                                    'OLYCKSFALL',
                                    'SJALVMORD',
                                    'AVSIKTLIGT_VALLAD',
                                    'OKLART'
                                ],
                                required: true
                            }
                        },
                        {
                            key: 'forgiftningDatum',
                            type: 'singleDate',
                            hideExpression: 'model.forgiftning !== true', // R16
                            templateOptions: {label: 'DFR_12.3', required: true}
                        },
                        {
                            key: 'forgiftningUppkommelse',
                            type: 'multi-text',
                            hideExpression: 'model.forgiftning !== true', // R17
                            templateOptions: {label: 'DFR_12.4', required: true, maxlength: 400}
                        }
                    ])
                ]),
                kategori(10, categoryNames[10], [
                    fraga(13, [
                        {
                            key: 'grunder',
                            type: 'check-group', // R18
                            templateOptions: {
                                label: 'DFR_13.1',
                                required: true,
                                descLabel: 'DFR_13.1',
                                code: 'DODSORSAKSUPPGIFTER',
                                choices: [
                                    'UNDERSOKNING_FORE_DODEN',
                                    'UNDERSOKNING_EFTER_DODEN',
                                    'KLINISK_OBDUKTION',
                                    'RATTSMEDICINSK_OBDUKTION',
                                    'RATTSMEDICINSK_BESIKTNING'
                                ]
                            }
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
