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
angular.module('af00213').factory('af00213.viewConfigFactory.v1', ['uvUtil', function (uvUtil) {
    'use strict';

    var viewConfig = [{
        type: 'uv-kategori',
        labelKey: 'KAT_1.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_1.RBK',
            components: [{
                type: 'uv-boolean-value',
                modelProp: 'harFunktionsnedsattning'
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_1.2.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'funktionsnedsattning'
                }]
            }]
        }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_2.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_2.RBK',
            components: [{
                type: 'uv-boolean-value',
                modelProp: 'harAktivitetsbegransning'
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_2.2.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'aktivitetsbegransning'
                }]
            }]
        }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_3.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_3.RBK',
            components: [{
                type: 'uv-boolean-value',
                modelProp: 'harUtredningBehandling'
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_3.2.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'utredningBehandling'
                }]
            }]
        }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_4.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_4.RBK',
            components: [{
                type: 'uv-boolean-value',
                modelProp: 'harArbetetsPaverkan'
            }, {
                type: 'uv-del-fraga',
                labelKey: 'DFR_4.2.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'arbetetsPaverkan'
                }]
            }]
        }
        ]
    }, {
        type: 'uv-kategori',
        labelKey: 'KAT_5.RBK',
        components: [{
            type: 'uv-fraga',
            labelKey: 'FRG_5.RBK',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'ovrigt'
            }]
        }
        ]
    }, {
        type: 'uv-skapad-av',
        modelProp: 'grundData.skapadAv'
    }];

    return {
        getViewConfig: function (webcert) {
            var config = angular.copy(viewConfig);

            if (webcert) {
                config = uvUtil.convertToWebcert(config, true);
            }

            return config;
        }
    };
}]);
