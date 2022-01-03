/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('lisjp').factory('lisjp.viewConfigFactory.v1', [ 'uvUtil', function(uvUtil) {
    'use strict';

    var viewConfig = [ {
        type: 'uv-kategori',
        labelKey: 'KAT_10.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'FRG_27.RBK',
            components: [ {
                type: 'uv-del-fraga',
                components: [ {
                    type: 'uv-boolean-statement',
                    modelProp: 'avstangningSmittskydd'
                } ]
            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_1.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'FRG_1.RBK',
            components: [ {
                type: 'uv-del-fraga',
                labelKey: 'KV_FKMU_0001.UNDERSOKNING.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'undersokningAvPatienten'
                } ]
            }, {
                type: 'uv-del-fraga',
                labelKey: 'KV_FKMU_0001.TELEFONKONTAKT.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'telefonkontaktMedPatienten'
                } ]
            }, {
                type: 'uv-del-fraga',
                labelKey: 'KV_FKMU_0001.JOURNALUPPGIFTER.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'journaluppgifter'
                } ]
            }, {
                type: 'uv-del-fraga',
                labelKey: 'KV_FKMU_0001.ANNAT.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'annatGrundForMU'
                } ]

            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_1.3.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'annatGrundForMUBeskrivning'
                } ]

            }, {
                type: 'uv-del-fraga',
                labelKey: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning',
                hideExpression: '!motiveringTillInteBaseratPaUndersokning',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'motiveringTillInteBaseratPaUndersokning'
                } ]
            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_2.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_28.RBK',
                components: [ {
                    type: 'uv-list',
                    labelKey: 'KV_FKMU_0002.{var}.RBK', // {var} is a placeholder for sysselsattning.typ values
                    listKey: 'typ', // name of property on modelProp to use on each row
                    modelProp: 'sysselsattning'
                } ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_29.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'nuvarandeArbete'
                } ]
            }]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_3.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'FRG_6.RBK',
            components: [ {
                type: 'uv-table',
                headers: ['DFR_6.2.RBK', ''], // labels for th cells
                valueProps: ['diagnosKod', 'diagnosBeskrivning'], // properties on diagnoser entries to use in each rows cells
                modelProp: 'diagnoser'
            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_4.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_35.RBK',
                components: [ {
                    type: 'uv-del-fraga',
                    labelKey: 'DFR_35.1.RBK',
                    components: [ {
                        type: 'uv-icf',
                        modelProp: 'funktionsnedsattning',
                        kategoriProp: 'funktionsKategorier'
                    }]
                } ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_17.RBK',
                components: [ {
                    type: 'uv-del-fraga',
                    labelKey: 'DFR_17.1.RBK',
                    components: [ {
                        type: 'uv-icf',
                        modelProp: 'aktivitetsbegransning',
                        kategoriProp: 'aktivitetsKategorier'
                    }]
                } ]
            }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_5.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_19.RBK',
                components: [ {
                    type: 'uv-del-fraga',
                    labelKey: 'DFR_19.1.RBK',
                    components: [ {
                        type: 'uv-simple-value',
                        modelProp: 'pagaendeBehandling'
                    }]
                } ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_20.RBK',
                components: [ {
                    type: 'uv-del-fraga',
                    labelKey: 'DFR_20.1.RBK',
                    components: [ {
                        type: 'uv-simple-value',
                        modelProp: 'planeradBehandling'
                    }]
                } ]
            }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_6.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_32.RBK',
                components: [
                    {
                        type: 'uv-table',
                        headers: ['Nedsättningsgrad', 'Från och med', 'Till och med'],
                        valueProps: [
                            'KV_FKMU_0003.{sjukskrivningsgrad}.RBK',
                            'period.from',
                            'period.tom'
                        ],
                        modelProp: 'sjukskrivningar'
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'lisjp.label.sjukskrivningar.tidigtstartdatum.motivering',
                        hideExpression: '!motiveringTillTidigtStartdatumForSjukskrivning',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'motiveringTillTidigtStartdatumForSjukskrivning'
                        } ]
                    }
                ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_37.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'forsakringsmedicinsktBeslutsstod'
                } ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_33.RBK',
                components: [
                    {
                        type: 'uv-boolean-value',
                        modelProp: 'arbetstidsforlaggning'
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_33.2.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'arbetstidsforlaggningMotivering'
                        }]
                    }
                ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_34.RBK',
                components: [ {
                    type: 'uv-boolean-statement',
                    modelProp: 'arbetsresor'
                } ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_39.RBK',
                components: [ {
                    type: 'uv-kodverk-value',
                    modelProp: 'prognos',
                    kvModelProps: ['prognos.typ', 'prognos.dagarTillArbete'],
                    kvLabelKeys: ['KV_FKMU_0006.{var}.RBK', 'KV_FKMU_0007.{var}.RBK']
                } ]
            }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_7.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_40.RBK',
                components: [ {
                    type: 'uv-list',
                    labelKey: 'KV_FKMU_0004.{var}.RBK',
                    listKey: 'typ',
                    modelProp: 'arbetslivsinriktadeAtgarder'
                } ]
            },
            {
                type: 'uv-fraga',
                labelKey: 'FRG_44.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'arbetslivsinriktadeAtgarderBeskrivning'
                } ]
            }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_8.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_25.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'ovrigt'
                } ]
            }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_9.RBK',
        components: [
            {
                type: 'uv-fraga',
                labelKey: 'FRG_26.RBK',
                components: [
                    {
                        type: 'uv-boolean-statement',
                        labelKey: 'DFR_26.1.RBK',
                        modelProp: 'kontaktMedFk'
                    },
                    {
                        type: 'uv-simple-value',
                        labelKey: 'DFR_26.2.RBK',
                        modelProp: 'anledningTillKontakt'
                    }
                ]
            }
        ]
    }, {
        type: 'uv-tillaggsfragor',
        labelKey: 'KAT_9999.RBK',
        modelProp: 'tillaggsfragor'
    }, {
        type: 'uv-skapad-av',
        modelProp: 'grundData.skapadAv'
    } ];

    return {
        getViewConfig: function(webcert) {
            var config = angular.copy(viewConfig);

            if (webcert) {
                config = uvUtil.convertToWebcert(config, true);
            }

            return config;
        }
    };
} ]);
