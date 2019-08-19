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
angular.module('ts-diabetes').factory('ts-diabetes.viewConfigFactory.v2', [
  '$filter', 'uvUtil',
  function($filter, uvUtil) {
    'use strict';

    var viewConfig = [
      {
        type: 'uv-kategori',
        labelKey: 'KAT_99.RBK',
        components: [
          {
            type: 'uv-fraga',
            components: [{
              type: 'uv-del-fraga',
              components: [{
                type: 'uv-list',
                labelKey: 'KORKORT_{var}.RBK',
                listKey: function(model) {
                  return model.selected ? model.type : null;
                },
                separator: ', ',
                modelProp: 'intygAvser.korkortstyp'
              }]
            }]
          }
        ]
      },
      {
        type: 'uv-kategori',
        labelKey: 'KAT_100.RBK',
        components: [{
          type: 'uv-fraga',
          components: [{
            type: 'uv-kodverk-value',
            kvModelProps: ['vardkontakt.idkontroll'],
            kvLabelKeys: ['IDENTITET_{var}.RBK']
          }]
        }]
      },
      {
        type: 'uv-kategori',
        labelKey: 'KAT_1.RBK',
        components: [
          {
            type: 'uv-fraga',
            labelKey: 'FRG_35.RBK',
            components: [{
              type: 'uv-del-fraga',
              components: [{
                type: 'uv-simple-value',
                modelProp: 'diabetes.observationsperiod'
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
                kvModelProps: ['diabetes.diabetestyp'],
                kvLabelKeys: ['ts-diabetes.label.diabetes.diabetestyp.{var}']
              }]
            }]
          },
          {
            type: 'uv-fraga',
            labelKey: 'FRG_19.RBK',
            components: [{
              type: 'uv-del-fraga',
              components: [
                {
                  type: 'uv-list',
                  labelKey: 'DFR_19.{var}.RBK',
                  listKey: function(model, index, count) {
                    return model ? index + 1 : null; // return index for {var} if true, otherwise null -> list item will not be shown
                  },
                  separator: ', ',
                  modelProp: ['diabetes.endastKost', 'diabetes.tabletter',
                    'diabetes.insulin']
                },
                {
                  type: 'uv-del-fraga',
                  labelKey: 'DFR_19.4.RBK',
                  components: [{
                    type: 'uv-simple-value',
                    modelProp: 'diabetes.insulinBehandlingsperiod'
                  }]
                },
                {
                  type: 'uv-del-fraga',
                  labelKey: 'DFR_19.5.RBK',
                  components: [{
                    type: 'uv-simple-value',
                    modelProp: 'diabetes.annanBehandlingBeskrivning'
                  }]
                }
              ]
            }]
          }
        ]
      },
      {
        type: 'uv-kategori',
        labelKey: 'KAT_2.RBK',
        components: [
          {
            type: 'uv-fraga',
            labelKey: 'FRG_36.RBK',
            components: [{
              type: 'uv-del-fraga',
              components: [
                {
                  type: 'uv-boolean-value',
                  modelProp: 'hypoglykemier.kunskapOmAtgarder'
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
                  modelProp: 'hypoglykemier.teckenNedsattHjarnfunktion'
                }
              ]
            }]
          },
          {
            type: 'uv-fraga',
            labelKey: 'FRG_38.RBK',
            components: [{
              type: 'uv-del-fraga',
              components: [
                {
                  type: 'uv-boolean-value',
                  modelProp: 'hypoglykemier.saknarFormagaKannaVarningstecken'
                }
              ]
            }]
          },
          {
            type: 'uv-fraga',
            labelKey: 'FRG_39.RBK',
            components: [{
              type: 'uv-del-fraga',
              components: [
                {
                  type: 'uv-boolean-value',
                  modelProp: 'hypoglykemier.allvarligForekomst'
                },
                {
                  type: 'uv-del-fraga',
                  labelKey: 'DFR_39.2.RBK',
                  components: [
                    {
                      type: 'uv-simple-value',
                      modelProp: 'hypoglykemier.allvarligForekomstBeskrivning'
                    }
                  ]
                }
              ]
            }]
          },
          {
            type: 'uv-fraga',
            labelKey: 'FRG_40.RBK',
            components: [{
              type: 'uv-del-fraga',
              components: [
                {
                  type: 'uv-boolean-value',
                  modelProp: 'hypoglykemier.allvarligForekomstTrafiken'
                },
                {
                  type: 'uv-del-fraga',
                  labelKey: 'DFR_40.2.RBK',
                  components: [
                    {
                      type: 'uv-simple-value',
                      modelProp: 'hypoglykemier.allvarligForekomstTrafikBeskrivning'
                    }
                  ]
                }
              ]
            }]
          },
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
            labelKey: 'FRG_42.RBK',
            components: [{
              type: 'uv-del-fraga',
              components: [
                {
                  type: 'uv-boolean-value',
                  modelProp: 'hypoglykemier.allvarligForekomstVakenTid'
                },
                {
                  type: 'uv-del-fraga',
                  labelKey: 'DFR_42.2.RBK',
                  components: [
                    {
                      type: 'uv-simple-value',
                      modelProp: 'hypoglykemier.allvarligForekomstVakenTidObservationstid'
                    }
                  ]
                }
              ]
            }]
          }
        ]
      },
      {
        type: 'uv-kategori',
        labelKey: 'KAT_3.RBK',
        components: [
          {
            type: 'uv-fraga',
            labelKey: 'FRG_43.RBK',
            components: [{
              type: 'uv-del-fraga',
              components: [
                {
                  type: 'uv-boolean-value',
                  modelProp: 'syn.separatOgonlakarintyg'
                }
              ]
            }]
          },
          {
            type: 'uv-fraga',
            labelKey: 'FRG_44.RBK',
            components: [{
              type: 'uv-del-fraga',
              components: [
                {
                  type: 'uv-boolean-value',
                  modelProp: 'syn.synfaltsprovningUtanAnmarkning'
                }
              ]
            }]
          },
          {
            type: 'uv-fraga',
            labelKey: 'FRG_8.RBK',
            components: [
              {
                type: 'uv-del-fraga',
                components: [{
                  type: 'uv-table',
                  contentUrl: 'ts',
                  modelProp: 'syn',
                  headers: ['', 'ts-diabetes.label.syn.utankorrektion',
                    'ts-diabetes.label.syn.medkorrektion'],
                  colProps: ['hoger', 'vanster', 'binokulart'],
                  valueProps: [
                    function(model, rowIndex, colIndex, colProp) {

                      var message = 'ts-diabetes.label.syn.';
                      switch (rowIndex) {
                      case 0:
                        message += 'hogeroga';
                        break;
                      case 1:
                        message += 'vansteroga';
                        break;
                      case 2:
                        message += 'binokulart';
                        break;
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
          },
          {
            type: 'uv-fraga',
            labelKey: 'FRG_6.RBK',
            components: [{
              type: 'uv-del-fraga',
              components: [{
                type: 'uv-boolean-value',
                modelProp: 'syn.diplopi'
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
            labelKey: 'FRG_33.RBK',
            components: [
              {
                type: 'uv-del-fraga',
                labelKey: 'DFR_33.1.RBK',
                components: [{
                  type: 'uv-list',
                  labelKey: 'KORKORT_{var}.RBK',
                  listKey: function(model) {
                    return model.selected ? model.type : null;
                  },
                  separator: ', ',
                  modelProp: 'bedomning.korkortstyp',
                  noValue: 'DFR_33.2.RBK'
                }]
              }
            ]
          },
          {
            type: 'uv-fraga',
            labelKey: 'FRG_34.RBK',
            components: [
              {
                type: 'uv-simple-value',
                modelProp: 'bedomning.lakareSpecialKompetens'
              }
            ]
          },
          {
            type: 'uv-fraga',
            labelKey: 'FRG_45.RBK',
            components: [
              {
                type: 'uv-boolean-value',
                modelProp: 'bedomning.lamplighetInnehaBehorighet'
              }
            ]
          }
        ]
      },
      {
        type: 'uv-kategori',
        labelKey: 'FRG_32.RBK',
        components: [
          {
            type: 'uv-fraga',
            components: [
              {
                type: 'uv-simple-value',
                modelProp: 'kommentar'
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
          config = uvUtil.convertToWebcert(config);
        }

        return config;
      }
    };
  }]);
