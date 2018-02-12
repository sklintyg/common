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

angular.module('db').factory('db.UtkastConfigFactory',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ueFactoryTemplatesHelper', 'common.ueSOSFactoryTemplatesHelper',
        function($log, $timeout, DateUtils, ueFactoryTemplates, ueSOSFactoryTemplates) {
            'use strict';

            // Validation category names matched with backend message strings from InternalDraftValidator
            var categoryIds = {
                1: 'personuppgifter',
                2: 'dodsdatumochdodsplats',
                3: 'barnsomavlidit',
                7: 'utlatandeorsak',
                8: 'operation',
                9: 'forgiftning',
                10: 'dodsorsakgrund'
            };

            var kategori = ueFactoryTemplates.kategori;
            var fraga = ueFactoryTemplates.fraga;

            var config = [

                ueFactoryTemplates.patient,

                ueSOSFactoryTemplates.identitet(categoryIds[1], false),
                ueSOSFactoryTemplates.dodsDatum(categoryIds[2]),
                ueSOSFactoryTemplates.barn(categoryIds[3]),
                /*kategori(7, categoryIds[7], [
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
                                    'PLOTSLIG',
                                    'KRONISK',
                                    'UPPGIFT_SAKNAS'
                                ],
                                letter: 'A',
                                required: false,
                                htmlMaxlength: 140,
                                beskrivningLabel: 'DELAT_TEXT.BESKRIVNING',
                                datumLabel: 'DELAT_TEXT.DEBUT',
                                orsakLabel: 'DELAT_TEXT.SPECIFIKATION',
                                foljd: {
                                    key: 'foljd',
                                    maxRows: 3,
                                    required: false,
                                    htmlMaxlength: 140,
                                    letter: ['B', 'C', 'D'],
                                    label: 'FRG_9',
                                    beskrivningLabel: 'DELAT_TEXT.BESKRIVNING',
                                    datumLabel: 'DELAT_TEXT.DEBUT',
                                    orsakLabel: 'DELAT_TEXT.SPECIFIKATION',
                                    orsaksTyper: [
                                        'PLOTSLIG',
                                        'KRONISK',
                                        'UPPGIFT_SAKNAS'
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
                                htmlMaxlength: 45,
                                orsaksTyper: [
                                    'PLOTSLIG',
                                    'KRONISK',
                                    'UPPGIFT_SAKNAS'
                                ],
                                beskrivningLabel: 'DELAT_TEXT.BESKRIVNING',
                                datumLabel: 'DELAT_TEXT.DEBUT',
                                orsakLabel: 'DELAT_TEXT.SPECIFIKATION'
                            }
                        }
                    ])
                ]),*/
  /*              kategori(8, categoryIds[8], [
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
                            templateOptions: {label: 'DFR_11.3', required: true, htmlMaxlength: 100}
                        }
                    ])
                ]),
                kategori(9, categoryIds[9], [
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
                            templateOptions: {label: 'DFR_12.4', required: true, htmlMaxlength: 400}
                        }
                    ])
                ]),
                kategori(10, categoryIds[10], [
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
*/
                ueFactoryTemplates.vardenhet
            ];

            return {
                getConfig: function() {
                    return angular.copy(config);
                },
                getcategoryIds: function() {
                    return angular.copy(categoryIds);
                }
            };
        }]);
