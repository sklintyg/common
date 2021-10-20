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
angular.module('ts-diabetes').factory('ts-diabetes.viewConfigFactory.v4', [
    '$filter','uvUtil',
    function ($filter, uvUtil) {
    'use strict';

    var viewConfig = [
        {
            type: 'uv-kategori',
            labelKey: 'KAT_1.RBK',
            components: [
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_1.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [{
                            type: 'uv-list',
                            labelKey: 'KV_INTYGET_AVSER.{var}.RBK',
                            listKey: function(model) {
                                return model.selected ? model.type : null;
                            },
                            separator: ', ',
                            modelProp: 'intygAvser.kategorier'
                        }]
                    }]
                }
            ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_2.RBK',
            components: [{
                type: 'uv-fraga',
                labelKey: 'FRG_2.RBK',
                components: [{
                    type: 'uv-kodverk-value',
                    kvModelProps: ['identitetStyrktGenom.typ'],
                    kvLabelKeys: ['KV_ID_KONTROLL.{var}.RBK']
                }]
            }]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_3.RBK',
            components: [
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_35.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [{
                            type: 'uv-simple-value',
                            modelProp: 'allmant.diabetesDiagnosAr'
                        }]
                    }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_18.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [{
                            type: 'uv-kodverk-value',
                            kvModelProps: ['allmant.typAvDiabetes'],
                            kvLabelKeys: ['SVAR_{var}.RBK']
                        }]
                    },{
                        type: 'uv-del-fraga',
                        labelKey: 'DFR_18.2.RBK',
                        components: [
                        {
                            type: 'uv-simple-value',
                            modelProp: 'allmant.beskrivningAnnanTypAvDiabetes'
                        }
                    ]}]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_207.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [{
                            type: 'uv-boolean-value',
                            modelProp: 'allmant.medicineringForDiabetes'
                        }]
                    }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_208.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [{
                            type: 'uv-boolean-value',
                            modelProp: 'allmant.medicineringMedforRiskForHypoglykemi'
                        }]
                    }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_109.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [
                            {
                                type: 'uv-list',
                                labelKey: 'DFR_109.{var}.RBK',
                                listKey: function(model, index) {
                                    switch (index) {
                                    case 0: //endastKost
                                        index = 1; //DRF_109.1.RBK
                                        break;
                                    case 1: //tabletter
                                        index = 2; //DRF_109.2.RBK
                                        break;
                                    case 2: //insulin
                                        index = 3; //DRF_109.3.RBK
                                        break;
                                    case 3: //annanBehandling
                                        index = 5; //DRF_109.5.RBK
                                        break;
                                    default:
                                        index = 0;
                                    }
                                    return model ? index : null; // return index for {var} if true, otherwise null -> list item will not be shown
                                },
                                separator: ', ',
                                modelProp: ['allmant.behandling.endastKost', 'allmant.behandling.tabletter',
                                    'allmant.behandling.insulin','allmant.behandling.annanBehandling']
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_109.4.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'allmant.behandling.insulinSedanAr'
                                }]
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_109.6.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'allmant.behandling.annanBehandlingBeskrivning'
                                }]
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_109.7.RBK',
                                components: [{
                                    type: 'uv-boolean-value',
                                    modelProp: 'allmant.behandling.riskHypoglykemi'
                                }]
                            }
                        ]
                    }]
                }
            ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_4.RBK',
            components: [
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_41.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [
                            {
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemier.egenkontrollBlodsocker'
                            }
                        ]
                    }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_37.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [
                            {
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemier.nedsattHjarnfunktion'
                            }
                        ]
                    }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_100.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [
                            {
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemier.sjukdomenUnderKontroll'
                            }
                        ]
                    }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_110.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [
                            {
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemier.formagaVarningstecken'
                            }
                        ]
                    }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_106.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [
                            {
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemier.aterkommandeSenasteAret'
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_106.2.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'hypoglykemier.aterkommandeSenasteTidpunkt'
                                }]
                            }

                        ]
                    }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_107.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [
                            {
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemier.aterkommandeSenasteKvartalet'
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_107.2.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'hypoglykemier.senasteTidpunktVaken'
                                }]
                            }

                        ]
                    }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_108.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [
                            {
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemier.forekomstTrafik'
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_108.2.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'hypoglykemier.forekomstTrafikTidpunkt'
                                }]
                            }

                        ]
                    }]
                }
            ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_5.RBK',
            components: [
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_103.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [
                            {
                                type: 'uv-boolean-value',
                                modelProp: 'synfunktion.misstankeOgonsjukdom'
                            }
                        ]
                    },
                    {
                        type: 'uv-alert-value',
                        labelKey: 'TSDIA-001.ALERT',
                        alertLevel: 'warning',
                        showExpression: function(model) {
                            return model.synfunktion && model.synfunktion.misstankeOgonsjukdom === true;}
                    }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_8.RBK',
                    components: [
                        {
                            type: 'uv-alert-value',
                            labelKey: 'TSDIA-002.ALERT',
                            alertLevel: 'info'
                        },
                        {
                            type: 'uv-del-fraga',
                            labelKey: 'DFR_8.1.RBK',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'synfunktion.skickasSeparat'
                            }]
                        },
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-table',
                                contentUrl: 'ts',
                                modelProp: 'synfunktion',
                                headers: ['', 'ts-diabetes.label.syn.utankorrektion',
                                    'ts-diabetes.label.syn.medkorrektion'],
                                colProps: ['hoger', 'vanster', 'binokulart'],
                                valueProps: [
                                    function(model, rowIndex, colIndex, colProp) {

                                        var message = 'ts-diabetes.label.syn.';
                                        switch (rowIndex){
                                            case 0:
                                                message += 'hogeroga'; break;
                                            case 1:
                                                message += 'vansteroga'; break;
                                            case 2:
                                                message += 'binokulart'; break;
                                        }
                                        return message;
                                    },
                                    function(model) {
                                        return $filter('number')(model.utanKorrektion, 1);
                                    },
                                    function(model) {
                                        return $filter('number')(model.medKorrektion, 1);
                                    }
                                ]
                            }]
                        }
                    ]
                }
            ]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_6.RBK',
            components: [
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_32.RBK',
                    components: [
                        {
                            type: 'uv-simple-value',
                            modelProp: 'ovrigt'
                        }
                    ]
                }]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_7.RBK',
            components: [
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_33.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            labelKey: 'DFR_33.1.RBK',
                            components: [{
                                type: 'uv-list',
                                labelKey: 'KV_KORKORTSBEHORIGHET.{var}.RBK',
                                listKey: function(model) {
                                    return model.selected ? model.type : null;
                                },
                                separator: ', ',
                                modelProp: 'bedomning.uppfyllerBehorighetskrav'
                            }]
                        }
                    ]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_45.RBK',
                    components: [
                        {
                            type: 'uv-boolean-value',
                            modelProp: 'bedomning.lampligtInnehav'
                        }
                    ]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_34.RBK',
                    components: [
                        {
                            type: 'uv-simple-value',
                            modelProp: 'bedomning.borUndersokasBeskrivning'
                        }
                    ]
                }
            ]
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
