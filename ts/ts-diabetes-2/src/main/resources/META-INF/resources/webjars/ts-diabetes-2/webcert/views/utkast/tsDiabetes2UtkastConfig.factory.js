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

angular.module('ts-diabetes-2').factory('ts-diabetes-2.UtkastConfigFactory',
    ['$log', '$timeout', 'common.DateUtilsService', 'common.ueFactoryTemplatesHelper',
        function ($log, $timeout, DateUtils, ueFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                return {
                    1: 'intygetAvserBehorighet',
                    2: 'identitetStyrktGenom',
                    3: 'allmant',
                    4: 'hypoglykemier',
                    5: 'synfunktion',
                    6: 'ovrigt',
                    7: 'bedomning'
                };
            }

            function _getConfig() {
                var categoryIds = _getCategoryIds();

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                //TODO: identitetStyrktGenom och allmant.typAvDiabetes labels borde hämtas från textfil, och kanske sammakonstruktion som för körkortstyper, men radios?
                var config = [

                    kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', {}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', {required: true, requiredProp: 'intygAvser.kategorier'}, [{
                            type: 'ue-checkgroup-ts',
                            modelProp: 'intygAvser.kategorier',
                            htmlClass: 'no-padding',
                            labelTemplate: 'KORKORT_{0}.RBK'
                        }])
                    ]),
                    kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', {}, [
                        fraga(1, 'FRG_2.RBK', 'FRG_2.HLP', {required: true, requiredProp: 'identitetStyrktGenom.typ'}, [{
                            type: 'ue-radiogroup',
                            modelProp: 'identitetStyrktGenom.typ',
                            htmlClass: 'col-md-6 no-padding',
                            paddingBottom: true,
                            choices: [
                                {label: 'IDENTITET_ID_KORT.RBK', id: 'ID_KORT'},
                                {label: 'IDENTITET_FORETAG_ELLER_TJANSTEKORT.RBK', id: 'FORETAG_ELLER_TJANSTEKORT'},
                                {label: 'IDENTITET_KORKORT.RBK', id: 'KORKORT'},
                                {label: 'IDENTITET_PERS_KANNEDOM.RBK', id: 'PERS_KANNEDOM'},
                                {label: 'IDENTITET_FORSAKRAN_KAP18.RBK', id: 'FORSAKRAN_KAP18'},
                                {label: 'IDENTITET_PASS.RBK', id: 'PASS'}
                            ]
                        }])
                    ]),

                    kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', {}, [
                        fraga(35, 'FRG_35.RBK', 'FRG_35.HLP', {required: true, requiredProp: 'allmant.diabetesDiagnosAr'}, [{
                            type: 'ue-year',
                            modelProp: 'allmant.diabetesDiagnosAr'
                        }]),
                        fraga(18, 'FRG_18.RBK', '', {required: true}, [{
                            type: 'ue-radiogroup',
                            modelProp: 'allmant.typAvDiabetes',
                            choices: [
                                {label: 'SVAR_TYP1.RBK', id: 'DIABETES_TYP_1'},
                                {label: 'SVAR_TYP2.RBK', id: 'DIABETES_TYP_2'},
                                {label: 'SVAR_ANNAN.RBK', id: 'DIABETES_TYP_ANNAN'}
                            ]
                        },
                            {
                                type: 'ue-textfield',
                                modelProp: 'allmant.beskrivningAnnanTypAvDiabetes',
                                hideExpression: function(scope) {return scope.model.allmant.typAvDiabetes !== 'DIABETES_TYP_ANNAN';},
                                htmlMaxlength: '160',
                                label: {
                                    key: 'DFR_18.2.RBK',
                                    required: true,
                                    requiredProp: 'allmant.beskrivningAnnanTypAvDiabetes'
                                }
                            }
                        ]),
                        fraga(109, 'FRG_109.RBK', 'FRG_109.HLP', {required: true}, [
                            {
                                type: 'ue-checkbox',
                                modelProp: 'allmant.behandling.endastKost',
                                label: {
                                    key: 'DFR_109.1.RBK'
                                },
                                paddingBottom: true
                            },
                            {
                                type: 'ue-checkbox',
                                modelProp: 'allmant.behandling.tabletter',
                                label: {
                                    key: 'DFR_109.2.RBK'
                                },
                                paddingBottom: true
                            },
                            {
                                type: 'ue-radio',
                                modelProp: 'allmant.behandling.tablettRiskHypoglykemi',
                                hideExpression: '!model.allmant.behandling.tabletter',
                                label: {
                                    key: 'DFR_109.3.RBK'
                                },
                                paddingBottom: true
                            }])
                        ]
                    ),

                    kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', {}, [
                        fraga(3, 'FRG_3.RBK', 'FRG_3.HLP', {required: true, requiredProp: 'harUtredningBehandling'}, [{
                            type: 'ue-radio',
                            modelProp: 'harUtredningBehandling'
                        }, {
                            type: 'ue-textarea',
                            modelProp: 'utredningBehandling',
                            hideExpression: '!model.harUtredningBehandling',
                            label: {
                                key: 'DFR_3.2.RBK',
                                helpKey: 'DFR_3.2.HLP',
                                required: true,
                                requiredProp: 'utredningBehandling'
                            }
                        }])
                    ]),

                    kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', {}, [
                        fraga(4, 'FRG_4.RBK', 'FRG_4.HLP', {required: true, requiredProp: 'harArbetetsPaverkan'}, [{
                            type: 'ue-radio',
                            modelProp: 'harArbetetsPaverkan'
                        }, {
                            type: 'ue-textarea',
                            modelProp: 'arbetetsPaverkan',
                            hideExpression: '!model.harArbetetsPaverkan',
                            label: {
                                key: 'DFR_4.2.RBK',
                                helpKey: 'DFR_4.2.HLP',
                                required: true,
                                requiredProp: 'arbetetsPaverkan'
                            }
                        }])
                    ]),

                    kategori(categoryIds[5], 'KAT_5.RBK', 'KAT_5.HLP', {}, [
                        fraga(5, 'FRG_5.RBK', 'FRG_5.HLP', {}, [{
                            modelProp: 'ovrigt',
                            type: 'ue-textarea'
                        }])
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
