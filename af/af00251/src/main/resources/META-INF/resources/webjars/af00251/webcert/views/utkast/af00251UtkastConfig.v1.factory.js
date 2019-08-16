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

angular.module('af00251').factory('af00251.UtkastConfigFactory.v1',
    ['$log', '$timeout', 'common.DateUtilsService', 'common.ueFactoryTemplatesHelper',
      function($log, $timeout, DateUtils, ueFactoryTemplates) {
        'use strict';

        function _getCategoryIds() {
          return {
            1: 'medicinsktUnderlag',
            2: 'arbetsmarknadspolitisktProgram',
            3: 'konsekvenser',
            4: 'bedomning'
          };
        }

        function isSjukfranvaroRequired(perioder) {
          var result = true;

          angular.forEach(perioder, function(row) {
            if (row.checked) {
              if (row.period.from && row.period.tom && row.niva) {
                result = false;
              }
            }
          });

          return result;
        }

        function _getConfig() {
          var categoryIds = _getCategoryIds();

          var kategori = ueFactoryTemplates.kategori;
          var fraga = ueFactoryTemplates.fraga;
          var today = moment().format('YYYY-MM-DD');

          var config = [

            kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', {signingDoctor: true}, [
              fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', {
                    validationContext: {
                      key: 'undersokning',
                      type: 'ue-checkgroup'
                    },
                    required: true,
                    requiredProp: ['undersokningsDatum', 'annatDatum', 'annatBeskrivning']
                  },
                  [{
                    type: 'ue-grid',
                    independentRowValidation: true,
                    components: [
                      // Row 1
                      [{
                        label: {
                          key: 'KV_FKMU_0001.UNDERSOKNING.RBK',
                          helpKey: 'KV_FKMU_0001.UNDERSOKNING.HLP'
                        },
                        type: 'ue-checkbox-date',
                        modelProp: 'undersokningsDatum',
                        maxDate: today,
                        paddingBottom: true
                      }],
                      // Row 2
                      [{
                        label: {
                          key: 'KV_FKMU_0001.ANNAT.RBK',
                          helpKey: 'KV_FKMU_0001.ANNAT.HLP'
                        },
                        type: 'ue-checkbox-date',
                        modelProp: 'annatDatum',
                        maxDate: today,
                        paddingBottom: true
                      }],
                      // Row 3
                      [{
                        type: 'ue-textarea',
                        modelProp: 'annatBeskrivning',
                        hideExpression: '!model.annatDatum',
                        label: {
                          key: 'DFR_1.3.RBK',
                          helpKey: 'DFR_1.3.HLP',
                          required: true,
                          requiredProp: 'annatBeskrivning'
                        }
                      }]
                    ]
                  }]
              )
            ]),
            kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', {}, [
              fraga(2, 'FRG_2.RBK', 'FRG_2.HLP', {
                    required: true,
                    requiredProp: 'arbetsmarknadspolitisktProgram.medicinskBedomning'
                  },
                  [{
                    type: 'ue-grid',
                    independentRowValidation: true,
                    components: [
                      // Row 1
                      [{
                        type: 'ue-textarea',
                        modelProp: 'arbetsmarknadspolitisktProgram.medicinskBedomning',
                        paddingBottom: true
                      }],
                      // Row 2
                      [{
                        type: 'ue-radiogroup',
                        modelProp: 'arbetsmarknadspolitisktProgram.omfattning',
                        label: {
                          key: 'DFR_2.2.RBK',
                          required: true,
                          requiredProp: 'arbetsmarknadspolitisktProgram.omfattning',
                          helpKey: 'DFR_2.2.HLP'
                        },
                        choices: [
                          {
                            id: 'HELTID',
                            label: 'OMFATTNING.PROGRAM_HELTID.RBK'
                          },
                          {
                            id: 'DELTID',
                            label: 'OMFATTNING.PROGRAM_DELTID.RBK'
                          },
                          {
                            id: 'OKAND',
                            label: 'OMFATTNING.PROGRAM_OKAND.RBK'
                          }
                        ]
                      }],
                      // Row 3
                      [{
                        type: 'ue-textfield',
                        modelProp: 'arbetsmarknadspolitisktProgram.omfattningDeltid',
                        hideExpression: 'model.arbetsmarknadspolitisktProgram.omfattning !== \'DELTID\'',
                        numbersOnly: true,
                        size: 3,
                        htmlMaxlength: 2,
                        label: {
                          key: 'DFR_2.3.RBK',
                          required: true,
                          requiredProp: 'arbetsmarknadspolitisktProgram.omfattningDeltid'
                        }
                      }]
                    ]
                  }]
              )]
            ),

            kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', {}, [
              fraga(3, 'FRG_3.RBK', 'FRG_3.HLP', {required: true, requiredProp: 'funktionsnedsattning'},
                  [{
                    type: 'ue-textarea',
                    modelProp: 'funktionsnedsattning'
                  }]),
              fraga(4, 'FRG_4.RBK', 'FRG_4.HLP', {required: true, requiredProp: 'aktivitetsbegransning'},
                  [{
                    type: 'ue-textarea',
                    modelProp: 'aktivitetsbegransning'
                  }])
            ]),

            kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', {}, [
              fraga(5, 'FRG_5.RBK', 'FRG_5.HLP',
                  {required: true, requiredProp: 'harForhinder'},
                  [{
                    type: 'ue-radio',
                    modelProp: 'harForhinder',
                    paddingBottom: true
                  }]),
              fraga(6, 'FRG_6.RBK', 'FRG_6.HLP',
                  {
                    required: true,
                    requiredProp: function(model) {
                      return isSjukfranvaroRequired(model.sjukfranvaro);
                    },
                    hideExpression: '!model.harForhinder'
                  },
                  [{
                    type: 'ue-sjukfranvaro',
                    modelProp: 'sjukfranvaro',
                    maxRows: 4
                  },
                    {
                      type: 'ue-alert',
                      alertType: 'warning',
                      key: 'AF-001.ALERT',
                      hideExpression: function(scope) {
                        var sjukskrivningar = scope.model.sjukfranvaro;

                        var foundEarlyDate = false;
                        angular.forEach(sjukskrivningar, function(item, key) {
                          if (item.period &&
                              DateUtils.isDate(item.period.from) &&
                              DateUtils.isDate(item.period.tom) &&
                              DateUtils.olderThanAWeek(DateUtils.toMoment(item.period.from))) {
                            foundEarlyDate = true;
                          }
                        });

                        return !foundEarlyDate;
                      }
                    }
                  ]
              ),
              fraga(7, 'FRG_7.RBK', 'FRG_7.HLP', {
                    required: true,
                    requiredProp: 'begransningSjukfranvaro.kanBegransas'
                  },
                  [{
                    type: 'ue-radio',
                    modelProp: 'begransningSjukfranvaro.kanBegransas'
                  }, {
                    type: 'ue-textarea',
                    modelProp: 'begransningSjukfranvaro.beskrivning',
                    hideExpression: '!model.begransningSjukfranvaro.kanBegransas',
                    label: {
                      key: 'DFR_7.2.RBK',
                      helpKey: 'DFR_7.2.HLP',
                      required: true,
                      requiredProp: 'begransningSjukfranvaro.beskrivning'
                    }
                  }
                  ]),
              fraga(8, 'FRG_8.RBK', 'FRG_8.HLP', {
                    required: true,
                    requiredProp: ['prognosAtergang.prognos']
                  },
                  [
                    {
                      type: 'ue-radiogroup',
                      modelProp: 'prognosAtergang.prognos',
                      choices: [
                        {
                          id: 'ATERGA_UTAN_ANPASSNING',
                          label: 'PROGNOS_ATERGANG.ATERGA_UTAN_ANPASSNING.RBK'
                        },
                        {
                          id: 'ATERGA_MED_ANPASSNING',
                          label: 'PROGNOS_ATERGANG.ATERGA_MED_ANPASSNING.RBK'
                        },
                        {
                          id: 'KAN_EJ_ATERGA',
                          label: 'PROGNOS_ATERGANG.KAN_EJ_ATERGA.RBK'
                        },
                        {
                          id: 'EJ_MOJLIGT_AVGORA',
                          label: 'PROGNOS_ATERGANG.EJ_MOJLIGT_AVGORA.RBK'
                        }
                      ]
                    },
                    {
                      type: 'ue-textarea',
                      hideExpression: 'model.prognosAtergang.prognos !== \'ATERGA_MED_ANPASSNING\'',
                      modelProp: 'prognosAtergang.anpassningar',
                      label: {
                        key: 'DFR_8.2.RBK',
                        helpKey: 'DFR_8.2.HLP',
                        required: true,
                        requiredProp: 'prognosAtergang.anpassningar'
                      }
                    }
                  ])
            ]),

            ueFactoryTemplates.vardenhet
          ];

          return config;
        }

        return {
          getConfig: _getConfig,
          getCategoryIds: _getCategoryIds
        };
      }
    ]);
