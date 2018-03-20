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

angular.module('doi').factory('doi.UtkastConfigFactory',
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
            var patient = ueSOSFactoryTemplates.patient;

            var config = [

                patient,

                ueSOSFactoryTemplates.identitet(categoryIds[1], true),
                ueSOSFactoryTemplates.dodsDatum(categoryIds[2]),
                ueSOSFactoryTemplates.barn(categoryIds[3]),
                kategori(categoryIds[7], 'KAT_7.RBK', 'KAT_7.HLP', {}, [
                    fraga(null, '', '', {}, [{
                        type: 'ue-form-label',
                        labelType: 'h4',
                        key: 'KAT_7.1.RBK'
                    }]),
                    fraga(null, '', '', {}, [{
                        modelProp: 'terminalDodsorsak',
                        type: 'ue-dodsorsak-foljd', // R9
                        label: {
                            modelProp: 'FRG_8.RBK',
                            helpmodelProp: 'FRG_8.HLP',
                            required: false
                        },
                        orsaksTyper: [
                            'PLOTSLIG',
                            'KRONISK',
                            'UPPGIFT_SAKNAS'
                        ],
                        letter: 'A',
                        htmlMaxlength: 140,
                        beskrivningLabel: 'DELAT_TEXT.BESKRIVNING',
                        datumLabel: 'DELAT_TEXT.DEBUT',
                        orsakLabel: 'DELAT_TEXT.SPECIFIKATION',
                        foljd: {
                            modelProp: 'foljd',
                            label: {
                                modelProp: 'FRG_9.RBK',
                                helpmodelProp: 'FRG_9.RBK',
                                required: false
                            },
                            htmlMaxlength: 140,
                            letter: ['B', 'C', 'D'],
                            maxRows: 3,
                            beskrivningLabel: 'DELAT_TEXT.BESKRIVNING',
                            datumLabel: 'DELAT_TEXT.DEBUT',
                            orsakLabel: 'DELAT_TEXT.SPECIFIKATION',
                            orsaksTyper: [
                                'PLOTSLIG',
                                'KRONISK',
                                'UPPGIFT_SAKNAS'
                            ]
                        }
                    }]),
                    fraga(null, '', '', {}, [
                        {
                            type: 'ue-form-label',
                            key: 'KAT_7.2.RBK',
                            bold: true
                        },
                        {
                            modelProp: 'bidragandeSjukdomar',
                            type: 'ue-dodsorsak-multi', // R11
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
                    ])
                ]),
                kategori(categoryIds[8], 'KAT_8.RBK', 'KAT_8.HLP', {}, [
                    fraga(11, '', '', {}, [
                        {
                            modelProp: 'operation',
                            type: 'ue-radiogroup', // R12
                            label: {
                                key: 'DFR_11.1.RBK',
                                required: true,
                                requiredProp: 'operation'
                            },
                            choices: [
                                {label: 'DFR_11.1.JA.RBK', id: 'JA'},
                                {label: 'DFR_11.1.NEJ.RBK', id: 'NEJ'},
                                {label: 'DFR_11.1.UPPGIFT_SAKNAS.RBK', id: 'UPPGIFT_SAKNAS'}
                            ]
                        }]),
                    fraga(11, '', '', { hideExpression: 'model.operation !== "JA"' /* R13 */ }, [
                        {
                            modelProp: 'operationDatum',
                            type: 'ue-date',
                            label: {key: 'DFR_11.2.RBK', required: true, requiredProp: 'operationDatum'}
                        }]),
                    fraga(11, '', '', { hideExpression: 'model.operation !== "JA"' /* R13 */ }, [
                        {
                            modelProp: 'operationAnledning',
                            type: 'ue-textfield',
                            label: {key: 'DFR_11.3.RBK', required: true, requiredProp: 'operationAnledning'}, htmlMaxlength: 100
                        }
                    ])
                ]),
                kategori(categoryIds[9], 'KAT_9.RBK', 'KAT_9.HLP', {}, [
                    fraga(12, '', '', {}, [
                        {
                            modelProp: 'forgiftning',
                            type: 'ue-radio',
                            label: {
                                key: 'DFR_12.1.RBK',
                                required: true
                            }
                        }]),
                    fraga(12, '', '', { hideExpression: 'model.forgiftning !== true' /* R14 */ }, [
                        {
                            modelProp: 'forgiftningOrsak',
                            type: 'ue-radiogroup', // R15
                            label: {
                                key: 'DFR_12.2.RBK',
                                required: true
                            },
                            choices: [
                                { label:'ORSAK.OLYCKSFALL.RBK', id: 'OLYCKSFALL'},
                                { label:'ORSAK.SJALVMORD.RBK', id: 'SJALVMORD'},
                                { label:'ORSAK.AVSIKTLIGT_VALLAD.RBK', id: 'AVSIKTLIGT_VALLAD'},
                                { label:'ORSAK.OKLART.RBK', id: 'OKLART'}
                            ]
                        }]),
                    fraga(12, '', '', { hideExpression: 'model.forgiftning !== true' /* R16 */ }, [
                        {
                            modelProp: 'forgiftningDatum',
                            type: 'ue-date',
                            label: {key: 'DFR_12.3.RBK', required: true}
                        }]),
                    fraga(12, '', '', { hideExpression: 'model.forgiftning !== true' /* R17 */ }, [
                        {
                            modelProp: 'forgiftningUppkommelse',
                            type: 'ue-textarea',
                            label: {key: 'DFR_12.4.RBK', required: true}, htmlMaxlength: 400
                        }
                    ])
                ]),
                kategori(categoryIds[10], 'KAT_10.RBK', 'KAT_10.HLP', {}, [
                    fraga(13, '', '', {}, [
                        {
                            modelProp: 'grunder',
                            type: 'ue-checkgroup', // R18
                            label: {
                                key: 'DFR_13.1.RBK',
                                required: true
                            },
                            code: 'DODSORSAKSUPPGIFTER',
                            choices: [
                                'UNDERSOKNING_FORE_DODEN',
                                'UNDERSOKNING_EFTER_DODEN',
                                'KLINISK_OBDUKTION',
                                'RATTSMEDICINSK_OBDUKTION',
                                'RATTSMEDICINSK_BESIKTNING'
                            ]
                        }
                    ])
                ]),
                ueFactoryTemplates.vardenhet
            ];

            return {
                getConfig: function() {
                    return angular.copy(config);
                },
                getCategoryIds: function() {
                    return angular.copy(categoryIds);
                }
            };
        }]);
