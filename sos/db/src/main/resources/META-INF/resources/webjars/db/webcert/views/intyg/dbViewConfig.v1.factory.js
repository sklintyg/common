/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
angular.module('db').factory('db.viewConfigFactory.v1', [ 'uvUtil', function(uvUtil) {
    'use strict';

    var viewConfig = [ {
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
    }, {
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
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_4.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'DFR_5.1.RBK',
            components: [ {
                type: 'uv-boolean-value',
                modelProp: 'explosivImplantat'
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_5.2.RBK',
                components: [ {
                    type: 'uv-boolean-value',
                    modelProp: 'explosivAvlagsnat'
                } ]
            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_5.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'DFR_6.1.RBK',
            components: [ {
                type: 'uv-kodverk-value',
                kvModelProps: ['undersokningYttre'],
                kvLabelKeys: ['{var}.RBK']
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_6.3.RBK',
                components: [ {
                    type: 'uv-simple-value',
                    modelProp: 'undersokningDatum'
                } ]
            } ]
        } ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_6.RBK',
        components: [ {
            type: 'uv-fraga',
            labelKey: 'DFR_7.1.RBK',
            components: [{
                type: 'uv-enum-value',
                values: {
                    'true'  : 'DFR_7.1.SVA_1.RBK',
                    'false' : 'DFR_7.1.SVAR_NEJ.RBK'
                },
                modelProp: 'polisanmalan'
            },{
                type: 'uv-alert-value',
                showExpression: function(model) {
                    return model.polisanmalan === true;
                },
                labelKey: 'DFR_7.1.OBS',
                alertLevel: 'warning'
            } ]
        } ]
    }, {
        type: 'uv-skapad-av',
        modelProp: 'grundData.skapadAv'
    } ];

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
