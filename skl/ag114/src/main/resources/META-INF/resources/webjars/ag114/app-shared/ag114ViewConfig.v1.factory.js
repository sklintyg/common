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
angular.module('ag114').factory('ag114.viewConfigFactory.v1', ['uvUtil', function (uvUtil) {
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
                        labelKey: '',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'sysselsattning'
                        } ]
                    }, {
                        type: 'uv-del-fraga',
                        labelKey: 'FRG_2.RBK',
                        components: [ {
                            type: 'uv-simple-value',
                            modelProp: 'nuvarandeArbete'
                        } ]
                    }]
                }
            ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_5.RBK',
            components: [{
                type: 'uv-fraga',
                labelKey: 'FRG_8.RBK',
                components: [{
                    type: 'uv-simple-value',
                    modelProp: 'ovrigaUpplysningar'
                }]
            }]
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
