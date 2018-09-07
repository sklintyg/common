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
    ['$log', '$timeout', 'common.ObjectHelper', 'common.DateUtilsService', 'common.ueFactoryTemplatesHelper',
        function ($log, $timeout, ObjectHelper, DateUtils, ueFactoryTemplates) {
            'use strict';

            function R6_OR_R17(model) {
                return ObjectHelper.deepGet(model, 'allmant.behandling.tablettRiskHypoglykemi') ||
                ObjectHelper.deepGet(model, 'allmant.behandling.insulin');
            }

            function _getCategoryIds() {
                return {
                    1: 'intygAvser',
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
                                }
                            },
                            {
                                type: 'ue-checkbox',
                                modelProp: 'allmant.behandling.tabletter',
                                label: {
                                    key: 'DFR_109.2.RBK'
                                }
                            },
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'allmant.behandling.tablettRiskHypoglykemi',
                                hideExpression: '!model.allmant.behandling.tabletter',
                                label: {
                                    key: 'DFR_109.3.RBK'
                                }
                            },
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'allmant.behandling.insulin',
                                label: {
                                    key: 'DFR_109.4.RBK'
                                }
                            },
                            {
                                type: 'ue-year',
                                modelProp: 'allmant.behandling.insulinSedanAr',
                                hideExpression: '!model.allmant.behandling.insulin',
                                label: {
                                    key: 'DFR_109.5.RBK',
                                    required: true,
                                    requiredProp: 'allmant'
                                }
                            },
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'allmant.behandling.annanBehandling',
                                label: {
                                    key: 'DFR_109.6.RBK'
                                },
                                paddingBottom: true
                            },
                            {
                                type: 'ue-textfield',
                                modelProp: 'allmant.behandling.annanBehandlingBeskrivning',
                                hideExpression: '!model.allmant.behandling.annanBehandling',
                                htmlMaxlength: '53',
                                label: {
                                    key: 'DFR_109.7.RBK',
                                    required: true,
                                    requiredProp: 'allmant.behandling.annanBehandling'
                                }
                            }])
                        ]
                    ),

                    kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', {
                        hideExpression: function (scope) { return !R6_OR_R17(scope.model);}}, [
                        fraga(100, 'FRG_100.RBK', 'FRG_100.HLP', {
                            required: true, requiredProp: 'hypoglykemier.sjukdomenUnderkontroll'
                        }, [
                             {
                                 type: 'ue-radio',
                                 yesLabel: 'SVAR_JA.RBK',
                                 noLabel: 'SVAR_NEJ.RBK',
                                 modelProp: 'hypoglykemier.sjukdomenUnderkontroll'
                            }
                        ]),
                        fraga(37, 'FRG_37.RBK', 'FRG_37.HLP', {
                            required: true, requiredProp: 'hypoglykemier.nedsattHjarnfunktion'
                        }, [
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemier.nedsattHjarnfunktion'
                            }
                        ]),
                        fraga(101, 'FRG_101.RBK', 'FRG_101.HLP', {
                            required: true, requiredProp: 'hypoglykemier.forstarRisker'
                        }, [
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemier.forstarRisker'
                            }
                        ]),
                        fraga(102, 'FRG_102.RBK', 'FRG_102.HLP', {
                            required: true, requiredProp: 'hypoglykemier.fortrogenMedSymptom'
                        }, [
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemier.fortrogenMedSymptom'
                            }
                        ]),
                        fraga(38, 'FRG_38.RBK', 'FRG_38.HLP', {
                            required: true, requiredProp: 'hypoglykemier.saknarFormagaVarningstecken'
                        }, [
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemier.saknarFormagaVarningstecken'
                            }
                        ]),
                        fraga(36, 'FRG_36.RBK', 'FRG_36.HLP', {
                            required: true, requiredProp: 'hypoglykemier.kunskapLampligaAtgarder'
                        }, [
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemier.kunskapLampligaAtgarder'
                            }
                        ]),
                        fraga(105, 'FRG_105.RBK', 'FRG_105.HLP', {
                            required: true, requiredProp: 'hypoglykemier.egenkontrollBlodsocker'
                        }, [
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemier.egenkontrollBlodsocker'
                            }
                        ]),
                        fraga(106, 'FRG_106.RBK', 'FRG_106.HLP', {
                            required: true, requiredProp: 'hypoglykemier.aterkommandeSenasteAret'
                        }, [
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemier.aterkommandeSenasteAret'
                            },
                            {
                                type: 'ue-date',
                                modelProp: 'hypoglykemier.aterkommandeSenasteTidpunkt',
                                hideExpression: '!model.hypoglykemier.aterkommandeSenasteAret',
                                label: {
                                    key: 'DFR_106.2.RBK',
                                    helpKey: 'DFR_106.2.HLP',
                                    required: true,
                                    requiredProp: 'hypoglykemier.aterkommandeSenasteTidpunkt'
                                }
                            }
                        ]),
                        fraga(107, 'FRG_107.RBK', 'FRG_107.HLP', {
                            required: true, requiredProp: 'hypoglykemier.aterkommandeSenasteKvartalet'
                        }, [
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemier.aterkommandeSenasteKvartalet'
                            },
                            {
                                type: 'ue-date',
                                modelProp: 'hypoglykemier.senasteTidpunktVaken',
                                hideExpression: '!model.hypoglykemier.aterkommandeSenasteKvartalet',
                                label: {
                                    key: 'DFR_107.2.RBK',
                                    helpKey: 'DFR_107.2.HLP',
                                    required: true,
                                    requiredProp: 'hypoglykemier.senasteTidpunktVaken'
                                }
                            }
                        ]),
                        fraga(108, 'FRG_108.RBK', 'FRG_108.HLP', {
                            required: true, requiredProp: 'hypoglykemier.forekomstTrafik'
                        }, [
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemier.forekomstTrafik'
                            },
                            {
                                type: 'ue-date',
                                modelProp: 'hypoglykemier.forekomstTrafikTidpunkt',
                                hideExpression: '!model.hypoglykemier.forekomstTrafik',
                                label: {
                                    key: 'DFR_108.2.RBK',
                                    helpKey: 'DFR_108.2.HLP',
                                    required: true,
                                    requiredProp: 'hypoglykemier.forekomstTrafikTidpunkt'
                                }
                            }
                        ])
                    ]),

                    kategori(categoryIds[5], 'KAT_5.RBK', 'KAT_5.HLP', {}, [
                        fraga(103, 'FRG_103.RBK', 'FRG_103.HLP', {
                            required: true, requiredProp: 'synfunktion.misstankeOgonsjukdom'
                        }, [
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'synfunktion.misstankeOgonsjukdom'
                            }
                        ]),
                        fraga(104, 'FRG_104.RBK', 'FRG_104.HLP', {
                            required: true, requiredProp: 'synfunktion.ogonbottenFotoSaknas'
                        }, [
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'synfunktion.ogonbottenFotoSaknas'
                            }
                        ]),
                        fraga(8, 'FRG_8.RBK', 'FRG_8.HLP', {hideExpression:'!model.ovrigt'}, [
                            {
                                type: 'ue-alert',
                                alertType: 'info',
                                key: 'TSDIA-002.ALERT'
                            },
                            {
                                type: 'ue-grid',
                                components: [
                                    // Row 1
                                    [{}, {
                                        type: 'ue-form-label',
                                        key: 'ts-diabetes.label.syn.utankorrektion',
                                        helpKey: 'ts-diabetes.helptext.synfunktioner.utan-korrektion',
                                        required: true,
                                        requiredMode: 'AND',
                                        requiredProp: ['synfunktion.hoger.utanKorrektion', 'synfunktion.vanster.utanKorrektion',
                                            'synfunktion.binokulart.utanKorrektion']
                                    }, {
                                        type: 'ue-form-label',
                                        key: 'ts-diabetes.label.syn.medkorrektion',
                                        helpKey: 'ts-diabetes.helptext.synfunktioner.med-korrektion'
                                    }],
                                    // Row 2
                                    [{
                                        type: 'ue-text',
                                        label: {
                                            key: 'ts-diabetes.label.syn.hogeroga'
                                        }
                                    }, {
                                        type: 'ue-synskarpa',
                                        modelProp: 'synfunktion.hoger.utanKorrektion'
                                    }, {
                                        type: 'ue-synskarpa',
                                        modelProp: 'synfunktion.hoger.medKorrektion'
                                    }],
                                    // Row 3
                                    [{
                                        type: 'ue-text',
                                        label: {
                                            key: 'ts-diabetes.label.syn.vansteroga'
                                        }
                                    }, {
                                        type: 'ue-synskarpa',
                                        modelProp: 'synfunktion.vanster.utanKorrektion'
                                    }, {
                                        type: 'ue-synskarpa',
                                        modelProp: 'synfunktion.vanster.medKorrektion'
                                    }],
                                    // Row 4
                                    [{
                                        type: 'ue-text',
                                        label: {
                                            key: 'ts-diabetes.label.syn.binokulart'
                                        }
                                    }, {
                                        type: 'ue-synskarpa',
                                        modelProp: 'synfunktion.binokulart.utanKorrektion'
                                    }, {
                                        type: 'ue-synskarpa',
                                        modelProp: 'synfunktion.binokulart.medKorrektion'
                                    }]
                                ]

                            }])
                    ]),

                    kategori(categoryIds[6], 'KAT_6.RBK', 'KAT_6.HLP', {}, [
                        fraga(32, 'FRG_32.RBK', 'FRG_32.HLP', {}, [{
                            modelProp: 'ovrigt',
                            type: 'ue-textarea'
                        }])
                    ]),


                    kategori(categoryIds[7], 'KAT_7.RBK', 'KAT_7.HLP', {}, [
                        fraga(33, 'FRG_33.RBK', 'FRG_33.HLP', {
                            required: true,
                            requiredProp: ''
                        }, [{
                            type: 'ue-checkgroup-ts',
                            modelProp: 'bedomning.uppfyllerBehorighetskrav',
                            labelTemplate: 'KV_KORKORTSBEHORIGHET_{0}.RBK'
                        }]),
                        fraga(45, 'FRG_45.RBK', 'FRG_45.HLP', {
                            required: true,
                            hideExpression: '',//korkortHogreBehorighet,
                            requiredProp: 'bedomning.lampligtInnehav'
                        }, [{
                            type: 'ue-radio',
                            yesLabel: 'SVAR_JA.RBK',
                            noLabel: 'SVAR_NEJ.RBK',
                            modelProp: 'bedomning.lampligtInnehav'
                        }]),
                        fraga(34, 'FRG_34.RBK', 'FRG_34.HLP', {
                            required: true,
                            requiredProp: 'bedomning.borUndersokasBeskrivning'
                        }, [{
                            type: 'ue-textarea',
                            modelProp: 'bedomning.borUndersokasBeskrivning'
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
