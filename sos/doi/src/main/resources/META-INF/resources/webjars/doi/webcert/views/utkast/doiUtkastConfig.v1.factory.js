/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

angular.module('doi').factory('doi.UtkastConfigFactory.v1',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ueFactoryTemplatesHelper', 'common.ueSOSFactoryTemplatesHelper',
        function($log, $timeout, DateUtils, ueFactoryTemplates, ueSOSFactoryTemplates) {
            'use strict';

            var today = moment().format('YYYY-MM-DD');
            var beginningOfLastYear = moment()
                .subtract(1, 'year')
                .dayOfYear(1)
                .format('YYYY-MM-DD');

            function _getCategoryIds() {
                // Validation category names matched with backend message strings from InternalDraftValidator
                return {
                    1: 'personuppgifter',
                    2: 'dodsdatumochdodsplats',
                    3: 'barnsomavlidit',
                    7: 'utlatandeorsak',
                    8: 'operation',
                    9: 'forgiftning',
                    10: 'dodsorsakgrund'
                };
            }

            function _getConfig(viewState) {
                var categoryIds = _getCategoryIds();

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var patient = ueSOSFactoryTemplates.patient(viewState);

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
                        fraga(8, 'FRG_8.RBK', 'FRG_8.HLP', {}, [{
                            modelProp: 'terminalDodsorsak',
                            type: 'ue-dodsorsak-foljd', // R9
                            maxDate: today,
                            orsaksTyper: [
                                'PLOTSLIG',
                                'KRONISK',
                                'UPPGIFT_SAKNAS'
                            ],
                            letter: 'A',
                            htmlMaxlength: 72,
                            beskrivningLabel: 'DELAT_TEXT.BESKRIVNING',
                            datumLabel: 'DELAT_TEXT.DEBUT',
                            orsakLabel: 'DELAT_TEXT.SPECIFIKATION',
                            watcher: {
                                expression: 'model.dodsdatumSakert ? model.dodsdatum : model.antraffatDodDatum',
                                listener: function _terminalMaxDateListener(newValue, oldValue, scope) {
                                    var maxDate = (newValue ? newValue : today);
                                    scope.config.maxDate = maxDate;
                                    scope.config.foljd.maxDate = maxDate;
                                }
                            },
                            foljd: {
                                modelProp: 'foljd',
                                maxDate: today,
                                label: {
                                    key: 'FRG_9.RBK',
                                    helpKey: 'FRG_9.HLP'
                                },
                                htmlMaxlength: 24,
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
                                helpKey: 'KAT_7.2.HLP',
                                bold: true
                            },
                            {
                                modelProp: 'bidragandeSjukdomar',
                                type: 'ue-dodsorsak-multi', // R11
                                maxRows: 8,
                                htmlMaxlength: 28,
                                maxDate: today,
                                orsaksTyper: [
                                    'PLOTSLIG',
                                    'KRONISK',
                                    'UPPGIFT_SAKNAS'
                                ],
                                beskrivningLabel: 'DELAT_TEXT.BESKRIVNING',
                                datumLabel: 'DELAT_TEXT.DEBUT',
                                orsakLabel: 'DELAT_TEXT.SPECIFIKATION',
                                watcher: {
                                    expression: 'model.dodsdatumSakert ? model.dodsdatum : model.antraffatDodDatum',
                                    listener: function _bidragandeMaxDateListener(newValue, oldValue, scope) {
                                        scope.config.maxDate = newValue ? newValue : today;
                                    }
                                }
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
                                minDate: beginningOfLastYear,
                                maxDate: today,
                                watcher: {
                                    expression: 'model.dodsdatumSakert ? model.dodsdatum : model.antraffatDodDatum',
                                    listener: function _operationsMinMaxDateListener(newValue, oldValue, scope) {
                                        var minDate = (scope.model.dodsdatum ? moment(scope.model.dodsdatum)
                                            .subtract(4, 'week').format('YYYY-MM-DD') : beginningOfLastYear);
                                        scope.config.minDate = minDate;
                                        scope.config.maxDate = newValue ? newValue : today;
                                    }
                                },
                                label: {key: 'DFR_11.2.RBK', required: true, requiredProp: 'operationDatum'}
                            }]),
                        fraga(11, '', '', { hideExpression: 'model.operation !== "JA"' /* R13 */ }, [
                            {
                                modelProp: 'operationAnledning',
                                type: 'ue-textfield',
                                label: {key: 'DFR_11.3.RBK', required: true, requiredProp: 'operationAnledning'}, htmlMaxlength: 31
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
                                    required: true,
                                    requiredProp: 'forgiftning'
                                },
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK'
                            }]),
                        fraga(12, '', '', { hideExpression: 'model.forgiftning !== true' /* R14 */ }, [
                            {
                                modelProp: 'forgiftningOrsak',
                                type: 'ue-radiogroup', // R15
                                label: {
                                    key: 'DFR_12.2.RBK',
                                    helpKey: 'DFR_12.2.HLP',
                                    required: true,
                                    requiredProp: 'forgiftningOrsak'
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
                                maxDate: today,
                                label: {
                                    key: 'DFR_12.3.RBK',
                                    required: true,
                                    requiredProp: 'forgiftningDatum'
                                },
                                watcher: {
                                    expression: 'model.dodsdatumSakert ? model.dodsdatum : model.antraffatDodDatum',
                                    listener: function _forgiftningMaxDateListener(newValue, oldValue, scope) {
                                        scope.config.maxDate = newValue ? newValue : today;
                                    }
                                }
                            }]),
                        fraga(12, '', '', { hideExpression: 'model.forgiftning !== true' /* R17 */ }, [
                            {
                                modelProp: 'forgiftningUppkommelse',
                                type: 'ue-textarea',
                                label: {
                                    key: 'DFR_12.4.RBK',
                                    helpKey: 'DFR_12.4.HLP',
                                    required: true,
                                    requiredProp: 'forgiftningUppkommelse'
                                }, htmlMaxlength: 150
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
                                    required: true,
                                    requiredProp: ['grunder.UNDERSOKNING_FORE_DODEN', 'grunder.UNDERSOKNING_EFTER_DODEN',
                                        'grunder.KLINISK_OBDUKTION', 'grunder.RATTSMEDICINSK_OBDUKTION', 'grunder.RATTSMEDICINSK_BESIKTNING' ]
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

                return config;
            }


            return {
                getConfig: _getConfig,
                getCategoryIds: _getCategoryIds
            };
        }]);
