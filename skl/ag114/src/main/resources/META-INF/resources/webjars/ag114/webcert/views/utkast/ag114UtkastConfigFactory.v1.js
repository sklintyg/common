/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('ag114').factory('ag114.UtkastConfigFactory.v1',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ueFactoryTemplatesHelper',
        function($log, $timeout, DateUtils, ueFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                return {
                    1: 'sysselsattning',
                    2: 'diagnos',
                    3: 'arbetsformaga',
                    4: 'bedomning',
                    5: 'ovrigt',
                    6: 'kontakt',
                    7: 'grundformu'
                };
            }

            function _getConfig() {
                var categoryIds = _getCategoryIds();

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var today = moment().format('YYYY-MM-DD');

                var config = [
                    kategori(categoryIds[7], 'KAT_7.RBK', 'KAT_7.HLP', {signingDoctor: true}, [
                        fraga(1, 'FRG_10.RBK', 'FRG_10.HLP',
                            {
                                validationContext: {
                                    key: 'baseratPa',
                                    type: 'ue-checkgroup'
                                },
                                required: true,
                                requiredProp: ['undersokningAvPatienten', 'telefonkontaktMedPatienten', 'journaluppgifter', 'annatGrundForMU']
                            },
                            [{
                                type: 'ue-grid',
                                independentRowValidation: true,
                                components: [[{
                                    label: {
                                        key: 'KV_FKMU_0001.UNDERSOKNING.RBK',
                                        helpKey: 'KV_FKMU_0001.UNDERSOKNING.HLP'
                                    },
                                    type: 'ue-checkbox-date',
                                    modelProp: 'undersokningAvPatienten',
                                    maxDate: today
                                }], [{
                                    label: {
                                        key: 'KV_FKMU_0001.TELEFONKONTAKT.RBK',
                                        helpKey: 'KV_FKMU_0001.TELEFONKONTAKT.HLP'
                                    },
                                    type: 'ue-checkbox-date',
                                    modelProp: 'telefonkontaktMedPatienten',
                                    maxDate: today
                                }], [{
                                    label: {
                                        key: 'KV_FKMU_0001.JOURNALUPPGIFTER.RBK',
                                        helpKey: 'KV_FKMU_0001.JOURNALUPPGIFTER.HLP'
                                    },
                                    type: 'ue-checkbox-date',
                                    modelProp: 'journaluppgifter',
                                    maxDate: today
                                }], [{
                                    label: {
                                        key: 'KV_FKMU_0001.ANNAT.RBK',
                                        helpKey: 'KV_FKMU_0001.ANNAT.HLP'
                                    },
                                    type: 'ue-checkbox-date',
                                    modelProp: 'annatGrundForMU',
                                    maxDate: today
                                }], [{
                                    label: {
                                        key: 'DFR_10.3.RBK',
                                        helpKey: 'DFR_10.3.HLP',
                                        required: true,
                                        requiredProp: 'annatGrundForMUBeskrivning'
                                    },
                                    type: 'ue-textarea',
                                    hideExpression: '!model.annatGrundForMU',
                                    modelProp: 'annatGrundForMUBeskrivning'
                                }]]
                            }]
                        )
                    ]),
                    // Sysselsättning
                    kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', {}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', { required: true, requiredProp: ['sysselsattning["NUVARANDE_ARBETE"]']}, [{
                            type: 'ue-checkgroup',
                            disabled: true,
                            modelProp: 'sysselsattning',
                            code: 'KV_FKMU_0002',
                            choices: ['NUVARANDE_ARBETE']
                        }]), fraga(2, 'FRG_2.RBK', 'FRG_2.HLP', { required: true, requiredProp: 'nuvarandeArbete'}, [{
                            type: 'ue-textarea',
                            modelProp: 'nuvarandeArbete'
                        }])
                    ]),

                    // Diagnos
                    //  - Önskar förmedla 3.1
                    //  - Typ av diagnos
                    //     - Diagnostext 4.1
                    //     - Diagnoskod 4.2
                    kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', {}, [
                        fraga(3, 'FRG_3.RBK', 'FRG_3.HLP', { required:true, requiredProp: 'onskarFormedlaDiagnos' }, [{
                            type: 'ue-radio',
                            modelProp: 'onskarFormedlaDiagnos'
                        }]),
                        fraga(4, 'FRG_4.RBK', 'FRG_4.HLP', { required: true, requiredProp: 'diagnoser[0].diagnosKod',
                            hideExpression: '!model.onskarFormedlaDiagnos'}, [{
                            type: 'ue-diagnos',
                            modelProp: 'diagnoser',
                            defaultKodSystem: 'ICD_10_SE',
                            diagnosBeskrivningLabel: 'DFR_4.2.RBK',
                            diagnosBeskrivningHelp: 'DFR_4.2.HLP',
                            diagnosKodLabel: 'DFR_4.1.RBK',
                            diagnosKodHelp: 'DFR_4.1.HLP'
                        }])
                    ]),

                    // Arbetsförmåga
                    //  - nedsättning 5.1
                    //  - förmåga trots sjukdom 6.1
                    kategori(categoryIds[3], 'KAT_3.RBK', '', {}, [
                        fraga(5, 'FRG_5.RBK', 'FRG_5.HLP', { required: true, requiredProp: 'nedsattArbetsformaga' }, [{
                            type: 'ue-textarea',
                            modelProp: 'nedsattArbetsformaga'
                        }]),
                        fraga(6, 'FRG_6.RBK', 'FRG_6.HLP', { required: true, requiredProp: 'arbetsformagaTrotsSjukdom' }, [{
                            type: 'ue-radio',
                            modelProp: 'arbetsformagaTrotsSjukdom'
                        }, {
                            type: 'ue-textarea',
                            modelProp: 'arbetsformagaTrotsSjukdomBeskrivning',
                            hideExpression: '!model.arbetsformagaTrotsSjukdom',
                            label: {
                                key: 'DFR_6.2.RBK',
                                requiredProp: 'arbetsformagaTrotsSjukdomBeskrivning',
                                required: true
                            }
                        }])
                    ]),

                    // Bedömning
                    kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', {}, [

                        fraga(7, 'FRG_7.RBK', 'FRG_7.HLP', {}, [
                            {
                                type: 'ue-percent-field',
                                modelProp: 'sjukskrivningsgrad',
                                numbersOnly: true,
                                independentRowValidation: true,
                                label: {
                                    key: 'DFR_7.1.RBK',
                                    helpKey: 'DFR_7.1.HLP',
                                    required: true,
                                    requiredProp: 'sjukskrivningsgrad'
                                }
                            },
                            {
                                type: 'ue-form-label',
                                key: 'DFR_7.2.RBK',
                                required: true,
                                requiredMode: 'AND',
                                requiredProp: ['sjukskrivningsperiod.from', 'sjukskrivningsperiod.tom']
                            },
                            {
                                type: 'ue-grid',
                                colSizes: [1,2,1,1,2],
                                independentRowValidation: true,
                                firstRequiredRow: 0,
                                firstRequiredRowKey: 'sjukskrivningsperiod.period',
                                modelProp: 'sjukskrivningsperiod',
                                components: [
                                    // Row 1
                                    [{
                                        type: 'ue-form-label',
                                        key: 'FROM'
                                    },{
                                        type: 'ue-date',
                                        modelProp: 'sjukskrivningsperiod.from'
                                    }, {
                                    //dummy for spacing
                                    }, {
                                        type: 'ue-form-label',
                                        key: 'TOM'
                                    },{
                                        type: 'ue-date',
                                        modelProp: 'sjukskrivningsperiod.tom',
                                        fromModelProp: 'sjukskrivningsperiod.from'
                                    }]
                                ]
                            }, {
                                type: 'ue-alert',
                                key: 'SKL-001.ALERT',
                                alertType: 'warning',
                                hideExpression: function(scope) {
                                    // Pure javascript function to determine if message shold be displayed.
                                    // This is because we want to use the same expression in pdf uv-config which doesn't
                                    // support any 3:rd party functionality, i.e moment.js / angular etc.
                                    if (scope.model.sjukskrivningsperiod && scope.model.sjukskrivningsperiod.from && scope.model.sjukskrivningsperiod.tom) {

                                        var msPerDay = 86400000;

                                        // Convert both dates to milliseconds
                                        var fromMs = new Date(scope.model.sjukskrivningsperiod.from).getTime();
                                        var toMs = new Date(scope.model.sjukskrivningsperiod.tom).getTime();

                                        // Calculate the difference in milliseconds
                                        var differenceMs = toMs - fromMs;
                                        // Convert back to days
                                        var daysBetween = Math.round(differenceMs / msPerDay) + 1;

                                        return isNaN(daysBetween) || daysBetween <= 14;
                                    }
                                    return true;
                                }
                            }])
                        ]),

                        // Övrigt
                        kategori(categoryIds[5], 'KAT_5.RBK', '', {}, [
                            fraga(8, 'FRG_8.RBK', 'FRG_8.HLP', {}, [{
                                type: 'ue-textarea',
                                modelProp: 'ovrigaUpplysningar'
                            }])
                        ]),

                        // Kontakt
                        kategori(categoryIds[6], 'KAT_6.RBK', 'KAT_6.HLP', { }, [
                        fraga(9, '', '', { }, [{
                            type: 'ue-checkbox',
                            modelProp: 'kontaktMedArbetsgivaren',
                            label: {
                                key: 'FRG_9.RBK'
                            }
                        }, {
                            type: 'ue-textarea',
                            modelProp: 'anledningTillKontakt',
                            hideExpression: '!model.kontaktMedArbetsgivaren',
                            label: {
                                key: 'DFR_9.2.RBK',
                                helpKey: 'DFR_9.2.HLP',
                                required: true,
                                requiredProp: 'anledningTillKontakt'
                            }
                        }])
                    ]),
                    // Kategori vårdenhet
                    ueFactoryTemplates.vardenhet
                ];
                return config;
            }


            return {
                getConfig: _getConfig,
                getCategoryIds: _getCategoryIds
            };
        }]);
