/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
                            labelKey: 'KV_KORKORTSBEHORIGHET.{var}.RBK',
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
                    labelKey: 'FRG_205.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [{
                            type: 'uv-kodverk-value',
                            kvModelProps: ['allmant.patientenFoljsAv'],
                            kvLabelKeys: ['KV_VARDNIVA_{var}.RBK']
                        }]
                    }]
                },
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
                    labelKey: 'FRG_209.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [
                            {
                                type: 'uv-list',
                                labelKey: 'DFR_209.{var}.RBK',
                                listKey: function(model, index) {
                                    switch (index) {
                                    case 0: //insulin
                                        index = 1; //DRF_209.1.RBK
                                        break;
                                    case 1: //tabletter
                                        index = 2; //DRF_209.2.RBK
                                        break;
                                    case 2: //annan
                                        index = 3; //DRF_209.3.RBK
                                        break;
                                    default:
                                        index = 0;
                                    }
                                    return model ? index : null; // return index for {var} if true, otherwise null -> list item will not be shown
                                },
                                separator: ', ',
                                modelProp: ['allmant.behandling.insulin', 'allmant.behandling.tabletter', 'allmant.behandling.annan']
                            },
                            {
                                type: 'uv-del-fraga',
                                labelKey: 'DFR_209.4.RBK',
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'allmant.behandling.annanAngeVilken'
                                }]
                            }
                        ]
                    }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_210.RBK',
                    components: [{
                        type: 'uv-del-fraga',
                        components: [{
                            type: 'uv-simple-value',
                            modelProp: 'allmant.medicineringMedforRiskForHypoglykemiTidpunkt'
                        }]
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
                    labelKey: 'FRG_200.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemi.kontrollSjukdomstillstand'
                            }]
                        },
                        {
                            type: 'uv-del-fraga',
                            labelKey: 'DFR_200.2.RBK',
                            components: [{
                                type: 'uv-simple-value',
                                modelProp: 'hypoglykemi.kontrollSjukdomstillstandVarfor'
                            }]
                        }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_201.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemi.forstarRiskerMedHypoglykemi'
                            }]
                        }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_110.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemi.formagaKannaVarningstecken'
                            }]
                        }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_202.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemi.vidtaAdekvataAtgarder'
                            }]
                        }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_106.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemi.aterkommandeSenasteAret'
                            }]
                        },
                        {
                            type: 'uv-del-fraga',
                            labelKey: 'DFR_106.2.RBK',
                            components: [{
                                type: 'uv-simple-value',
                                modelProp: 'hypoglykemi.aterkommandeSenasteAretTidpunkt'
                            }]
                        },
                        {
                            type: 'uv-del-fraga',
                            labelKey: 'DFR_106.3.RBK',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemi.aterkommandeSenasteAretKontrolleras'
                            }]
                        },
                        {
                            type: 'uv-del-fraga',
                            labelKey: 'DFR_106.5.RBK',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemi.aterkommandeSenasteAretTrafik'
                            }]
                        }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_107.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemi.aterkommandeVaketSenasteTolv'
                            }]
                        },
                        {
                            type: 'uv-del-fraga',
                            labelKey: 'DFR_107.3.RBK',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemi.aterkommandeVaketSenasteTre'
                            }]
                        },
                        {
                            type: 'uv-del-fraga',
                            labelKey: 'DFR_107.5.RBK',
                            components: [{
                                type: 'uv-simple-value',
                                modelProp: 'hypoglykemi.aterkommandeVaketSenasteTreTidpunkt'
                            }]
                        }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_203.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemi.allvarligSenasteTolvManaderna'
                            }]
                        },
                        {
                            type: 'uv-del-fraga',
                            labelKey: 'DFR_203.2.RBK',
                            components: [{
                                type: 'uv-simple-value',
                                modelProp: 'hypoglykemi.allvarligSenasteTolvManadernaTidpunkt'
                            }]
                        }]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_204.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'hypoglykemi.regelbundnaBlodsockerkontroller'
                            }]
                        }]
                }]
        },
        {
            type: 'uv-kategori',
            labelKey: 'KAT_6.RBK',
            components: [
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_206.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-boolean-value',
                                modelProp: 'ovrigt.komplikationerAvSjukdomen'
                            }]
                        },
                        {
                            type: 'uv-del-fraga',
                            labelKey: 'DFR_206.2.RBK',
                            components: [{
                                type: 'uv-simple-value',
                                modelProp: 'ovrigt.komplikationerAvSjukdomenAnges'
                            }]
                        }
                    ]
                },
                {
                    type: 'uv-fraga',
                    labelKey: 'FRG_34.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-simple-value',
                                modelProp: 'ovrigt.borUndersokasAvSpecialist'
                            }]
                        }]
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
                    labelKey: 'FRG_32.RBK',
                    components: [
                        {
                            type: 'uv-del-fraga',
                            components: [{
                                type: 'uv-simple-value',
                                modelProp: 'bedomning.ovrigaKommentarer'
                            }]
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
