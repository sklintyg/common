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
angular.module('luae_fs').factory('luae_fs.viewConfigFactory.v1', ['uvUtil', function (uvUtil) {
    'use strict';

    var viewConfig = [
        {
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
                        labelKey: 'KV_FKMU_0001.JOURNALUPPGIFTER.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'journaluppgifter'
                        } ]
                    }, {
                        type: 'uv-del-fraga',
                        labelKey: 'KV_FKMU_0001.ANHORIG.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'anhorigsBeskrivningAvPatienten'
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
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_2.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'kannedomOmPatient'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_3.RBK',
                    components: [ {
                        type: 'uv-del-fraga',
                        components: [ {
                            type: 'uv-boolean-value',
                            modelProp: 'underlagFinns'
                        } ]
                    } ]
                }, {
                    type: 'uv-fraga',
                    labelKey: 'FRG_4.RBK',
                    components: [ {
                        type: 'uv-table',
                        contentUrl: 'utlatande',
                        headers: ['FRG_4.RBK', 'Datum', 'DFR_4.3.RBK'], // labels for th cells
                        valueProps: ['KV_FKMU_0005.{typ}.RBK', 'datum', 'hamtasFran'], // {typ} refers to underlag.typ values
                        modelProp: 'underlag'
                    } ]
                } ]
            },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_3.RBK',
            components: [{
                type: 'uv-fraga',
                labelKey: 'FRG_6.RBK',
                components: [{
                    type: 'uv-table',
                    headers: ['DFR_6.2.RBK', ''], // labels for th cells
                    valueProps: ['diagnosKod', 'diagnosBeskrivning'], // properties on diagnoser entries to use in each rows cells
                    modelProp: 'diagnoser'
                }]
            }]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_4.RBK',
            components: [ {
                type: 'uv-fraga',
                labelKey: 'FRG_15.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'funktionsnedsattningDebut'
                } ]
            }, {
                type: 'uv-fraga',
                labelKey: 'FRG_16.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'funktionsnedsattningPaverkan'
                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_5.RBK',
            components: [{
                type: 'uv-fraga',
                labelKey: 'FRG_25.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'ovrigt'
                }]
            }]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_6.RBK',
            components: [ {
                type: 'uv-fraga',
                labelKey: 'FRG_26.RBK',
                components: [ {
                    type: 'uv-del-fraga',
                    labelKey: 'DFR_26.1.RBK',
                    components: [ {
                        type: 'uv-boolean-statement',
                        modelProp: 'kontaktMedFk'
                    } ]
                }, {
                    type: 'uv-del-fraga',
                    labelKey: 'DFR_26.2.RBK',
                    components: [ {
                        type: 'uv-simple-value',
                        modelProp: 'anledningTillKontakt'
                    } ]
                } ]
            } ]
        }, {
            type: 'uv-tillaggsfragor',
            labelKey: 'KAT_9999.RBK',
            modelProp: 'tillaggsfragor'
        },
        {
            type: 'uv-skapad-av',
            modelProp: 'grundData.skapadAv'
        }
    ];

    return {
        getViewConfig: function(webcert) {

            var config = angular.copy(viewConfig);

            if (webcert) {
                config = uvUtil.convertToWebcert(config, true);
            }

            return config;
        }
    };
}]);
