/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
angular.module('doi').factory('doi.viewConfigFactory.v1', [ 'uvUtil', function(uvUtil) {
    'use strict';

    var viewConfig = [
        {
            type: 'uv-kategori',
            labelKey: 'KAT_1.RBK',
            components: [ {
                type: 'uv-fraga',
                labelKey: 'DFR_1.1.RBK',
                components: [ {
                    type: 'uv-del-fraga',
                    components: [ {
                        type: 'uv-simple-value',
                        modelProp: 'identitetStyrkt'
                    } ]
                } ]
            }, {
                type: 'uv-fraga',
                labelKey: 'DFR_14.1.RBK',
                components: [ {
                    type: 'uv-del-fraga',
                    components: [ {
                        type: 'uv-simple-value',
                        modelProp: 'land'
                    } ]
                } ]
            } ]
        }, {
            type: 'uv-kategori',
            labelKey: 'KAT_2.RBK',
            components: [{
                type: 'uv-fraga',
                labelKey: 'FRG_2.RBK',
                components: [{
                    type: 'uv-enum-value',
                    values: {
                        'true'  : 'SVAR_SAKERT.RBK',
                        'false' : 'SVAR_EJ_SAKERT.RBK'
                    },
                    modelProp: 'dodsdatumSakert'
                }, {
                    type: 'uv-del-fraga',
                    labelKey: 'DFR_2.2.RBK',
                    components: [{
                        type: 'uv-simple-value',
                        modelProp: 'dodsdatum'
                    }]
                }, {
                    type: 'uv-del-fraga',
                    labelKey: 'DFR_2.3.RBK',
                    components: [{
                        type: 'uv-simple-value',
                        modelProp: 'antraffatDodDatum'
                    }]
                }]
            }, {
                type: 'uv-fraga',
                labelKey: 'FRG_3.RBK',
                components: [ {
                    type: 'uv-del-fraga',
                    labelKey: 'DFR_3.1.RBK',
                    components: [ {
                        type: 'uv-simple-value',
                        modelProp: 'dodsplatsKommun'
                    } ]

                }, {
                    type: 'uv-del-fraga',
                    labelKey: 'DFR_3.2.RBK',
                    components: [ {
                        type: 'uv-kodverk-value',
                        kvModelProps: ['dodsplatsBoende'],
                        kvLabelKeys: ['DODSPLATS_BOENDE.{var}.RBK']
                    } ]

                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_3.RBK',
            components: [ {
                type: 'uv-fraga',
                labelKey: 'DFR_4.1.RBK',
                components: [ {
                    type: 'uv-boolean-value',
                    modelProp: 'barn'
                } ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_7.RBK',
            components: [
                {
                    type: 'uv-fraga',
                    labelKey: 'KAT_7.1.RBK',
                    components: [
                    ]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_8.RBK',
                    components: [
                        {
                            type: 'uv-table',
                            headers: ['DELAT_TEXT.BESKRIVNING.RBK', 'DELAT_TEXT.DEBUT.RBK', 'DELAT_TEXT.SPECIFIKATION.RBK'],
                            valueProps: ['beskrivning', 'datum', 'DELAD_SVAR.{specifikation}.RBK'],
                            modelProp: 'terminalDodsorsak'
                        }
                    ]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_9.RBK',
                    components: [
                        {
                            type: 'uv-table',
                            headers: ['DELAT_TEXT.BESKRIVNING.RBK', 'DELAT_TEXT.DEBUT.RBK', 'DELAT_TEXT.SPECIFIKATION.RBK'],
                            valueProps: ['beskrivning', 'datum', 'DELAD_SVAR.{specifikation}.RBK'],
                            modelProp: 'foljd'
                        }
                    ]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'KAT_7.2.RBK',
                    components: [
                        {
                            type: 'uv-table',
                            headers: ['DELAT_TEXT.BESKRIVNING.RBK', 'DELAT_TEXT.DEBUT.RBK', 'DELAT_TEXT.SPECIFIKATION.RBK'],
                            valueProps: ['beskrivning', 'datum', 'DELAD_SVAR.{specifikation}.RBK'],
                            modelProp: 'bidragandeSjukdomar'
                        }
                    ]
                }
            ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_8.RBK',
            components: [ {
                type: 'uv-fraga',
                labelKey: 'FRG_12.RBK',
                components: [
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_11.1.RBK',
                        components: [ {
                            type: 'uv-kodverk-value',
                            kvModelProps: ['operation'],
                            kvLabelKeys: ['DFR_11.1.{var}.RBK']
                        }  ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_11.2.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'operationDatum'
                        } ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_11.3.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'operationAnledning'
                        } ]
                    }
                ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_9.RBK',
            components: [ {
                type: 'uv-fraga',
                labelKey: 'FRG_12.RBK',
                components: [
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_12.1.RBK',
                        components: [ {
                            type: 'uv-boolean-value',
                            modelProp: 'forgiftning'
                        } ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_12.2.RBK',
                        components: [ {
                            type: 'uv-kodverk-value',
                            kvModelProps: ['forgiftningOrsak'],
                            kvLabelKeys: ['ORSAK.{var}.RBK']
                        }  ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_12.3.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'forgiftningDatum'
                        } ]
                    },
                    {
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_12.4.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'forgiftningUppkommelse'
                        } ]
                    }
                ]
            } ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_10.RBK',
            components: [ {
                type: 'uv-fraga',
                labelKey: 'DFR_13.1.RBK',
                components: [ {
                    type: 'uv-list',
                    labelKey: 'DODSORSAKSUPPGIFTER.{var}.RBK', // {var} is a placeholder for sysselsattning.typ values
                    modelProp: 'grunder'
                } ]
            } ]
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
                config = uvUtil.convertToWebcert(config);
            }

            return config;
        }
    };
} ]);
