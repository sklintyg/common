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

angular.module('ts-diabetes').factory('ts-diabetes.UtkastConfigFactory.v3',
    ['$log', '$timeout', 'common.ObjectHelper', 'common.DateUtilsService', 'common.ueFactoryTemplatesHelper', 'common.ueTSFactoryTemplatesHelper',
        function ($log, $timeout, ObjectHelper, DateUtils, ueFactoryTemplates, ueTSFactoryTemplates) {
            'use strict';

            var today = moment().format('YYYY-MM-DD');
            var twelveMonthsBack = moment()
                .subtract(12, 'months')
                .format('YYYY-MM-DD');
            var threeMonthsBack = moment()
                .subtract(3, 'months')
                .format('YYYY-MM-DD');

            function _hasAnyOfIntygAvserBehorighet(model, targetKategorier) {
                var valueArray = model.intygAvser.kategorier || [];

                for (var i = 0; i < valueArray.length; i++) {
                    for (var j = 0; j < targetKategorier.length; j++) {
                        if (valueArray[i].type === targetKategorier[j] && valueArray[i].selected) {
                            return true;
                        }
                    }
                }
                //None of targetKategorier selected in intygAvser
                return false;

            }

            /**
             * @return {boolean}
             */
            function R1(scope) {
                return _hasAnyOfIntygAvserBehorighet(scope.model, ['C1', 'C1E', 'C', 'CE', 'D1', 'D1E', 'D', 'DE', 'TAXI']);
            }

            /**
             * @return {boolean}
             */
            function R11(scope) {
                return ObjectHelper.deepGet(scope, 'model.synfunktion.misstankeOgonsjukdom') === true;
            }

            /**
             * @return {boolean}
             */
            function R12(model) {
                //Om nej på båda och någon saknas i utan korrektion
                return (ObjectHelper.deepGet(model, 'synfunktion.ogonbottenFotoSaknas') === false &&
                    ObjectHelper.deepGet(model, 'synfunktion.misstankeOgonsjukdom') === false) &&
                    ((_synvarde(model, 'synfunktion.hoger.utanKorrektion', -1) === -1) ||
                    (_synvarde(model, 'synfunktion.vanster.utanKorrektion', -1) === -1) ||
                    (_synvarde(model, 'synfunktion.binokulart.utanKorrektion', -1) === -1));
            }

            function _synvarde(model, synProperty, elseValue) {
                return ObjectHelper.getFloatOr(ObjectHelper.deepGet(model, synProperty), elseValue);
            }

            /**
             * @return {boolean}
             */
            function R13(model) {
                var binokulartUtanKorr = _synvarde(model, 'synfunktion.binokulart.utanKorrektion', 99);
                return (binokulartUtanKorr < 0.5) && _hasAnyOfIntygAvserBehorighet(model, ['AM', 'A1', 'A2', 'A', 'B', 'BE', 'TRAKTOR']);
            }

            /**
             * @return {boolean}
             */
            function R14(model) {
                var hogerUtanKorr = _synvarde(model, 'synfunktion.hoger.utanKorrektion', 99);
                var vansterUtanKorr =_synvarde(model, 'synfunktion.vanster.utanKorrektion', 99);
                return (hogerUtanKorr < 0.8 && vansterUtanKorr < 0.8) && _hasAnyOfIntygAvserBehorighet(model, ['C1', 'C1E', 'C', 'CE', 'D1', 'D1E', 'D', 'DE', 'TAXI']);
            }

            /**
             * @return {boolean}
             */
            function R15(model) {
                var hogerUtanKorr = _synvarde(model, 'synfunktion.hoger.utanKorrektion', 99);
                var vansterUtanKorr = _synvarde(model, 'synfunktion.vanster.utanKorrektion', 99);
                return (hogerUtanKorr < 0.1 || vansterUtanKorr < 0.1) && _hasAnyOfIntygAvserBehorighet(model, ['C1', 'C1E', 'C', 'CE', 'D1', 'D1E', 'D', 'DE', 'TAXI']);
            }


            function medKorrigeringRequired(model) {
                var answeredAll = (_synvarde(model, 'synfunktion.hoger.medKorrektion', -1) > -1) &&
                                    (_synvarde(model, 'synfunktion.vanster.medKorrektion', -1) > -1) &&
                                    (_synvarde(model, 'synfunktion.binokulart.medKorrektion', -1) > -1);
                return !answeredAll && (R13(model) || R14(model) || R15(model));
            }

            function R6_OR_R17(model) {
                return ObjectHelper.deepGet(model, 'allmant.behandling.tablettRiskHypoglykemi') ||
                ObjectHelper.deepGet(model, 'allmant.behandling.insulin');
            }

            function requiredKorkortProperties(field, antalKorkort, extraproperty) {
                var korkortsarray = [];
                for (var i = 0; i < antalKorkort; i++) {
                    korkortsarray.push(field + '[' + i + '].selected');
                }
                korkortsarray.push(extraproperty);
                return korkortsarray;
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
                var thisYear = moment().format('YYYY');

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var config = [

                    kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', {signingDoctor: true}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', {required: true, requiredProp: requiredKorkortProperties('intygAvser.kategorier', 16)}, [{
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
                            type: 'ue-year-picker',
                            modelProp: 'allmant.diabetesDiagnosAr',
                            maxYear: thisYear
                        }]),
                        fraga(18, 'FRG_18.RBK', '', {required: true, requiredProp: 'allmant.typAvDiabetes'}, [{
                            type: 'ue-radiogroup',
                            modelProp: 'allmant.typAvDiabetes',
                            choices: [
                                {label: 'SVAR_TYP1.RBK', id: 'TYP1'},
                                {label: 'SVAR_TYP2.RBK', id: 'TYP2'},
                                {label: 'SVAR_ANNAN.RBK', id: 'ANNAN'}
                            ]
                        },
                            {
                                type: 'ue-textfield',
                                modelProp: 'allmant.beskrivningAnnanTypAvDiabetes',
                                hideExpression: function(scope) {return (scope.model.allmant.typAvDiabetes !== 'ANNAN');},
                                htmlMaxlength: '160',
                                label: {
                                    key: 'DFR_18.2.RBK',
                                    required: true,
                                    requiredProp: 'allmant.beskrivningAnnanTypAvDiabetes'
                                }
                            }
                        ]),
                        fraga(109, 'FRG_109.RBK', 'FRG_109.HLP', {
                            validationContext: {key: 'allmant.behandling', type: 'ue-checkgroup'},
                            required: true,
                            requiredProp: [
                                'allmant.behandling.endastKost',
                                'allmant.behandling.tabletter',
                                'allmant.behandling.insulin',
                                'allmant.behandling.annanBehandling']},
                            [{
                                type: 'ue-grid',
                                independentRowValidation: true,
                                components: [[
                                    {
                                        type: 'ue-checkbox',
                                        modelProp: 'allmant.behandling.endastKost',
                                        label: {
                                            key: 'DFR_109.1.RBK'
                                        }
                                    }], [{
                                    type: 'ue-checkbox',
                                    modelProp: 'allmant.behandling.tabletter',
                                    label: {
                                        key: 'DFR_109.2.RBK'
                                    }
                                }], [{
                                    type: 'ue-radio',
                                    yesLabel: 'SVAR_JA.RBK',
                                    noLabel: 'SVAR_NEJ.RBK',
                                    modelProp: 'allmant.behandling.tablettRiskHypoglykemi',
                                    hideExpression: '!model.allmant.behandling.tabletter',
                                    label: {
                                        key: 'DFR_109.3.RBK',
                                        required: true,
                                        requiredProp: 'allmant.behandling.tablettRiskHypoglykemi'
                                    }
                                }], [{
                                    type: 'ue-checkbox',
                                    modelProp: 'allmant.behandling.insulin',
                                    label: {
                                        key: 'DFR_109.4.RBK'
                                    }
                                }], [{
                                    type: 'ue-year-picker',
                                    modelProp: 'allmant.behandling.insulinSedanAr',
                                    maxYear: thisYear,
                                    hideExpression: '!model.allmant.behandling.insulin',
                                    label: {
                                        key: 'DFR_109.5.RBK',
                                        required: true,
                                        requiredProp: 'allmant.behandling.insulinSedanAr'
                                    }
                                }], [{
                                    type: 'ue-checkbox',
                                    modelProp: 'allmant.behandling.annanBehandling',
                                    label: {
                                        key: 'DFR_109.6.RBK'
                                    },
                                    paddingBottom: true
                                }], [{
                                    type: 'ue-textfield',
                                    modelProp: 'allmant.behandling.annanBehandlingBeskrivning',
                                    hideExpression: '!model.allmant.behandling.annanBehandling',
                                    htmlMaxlength: '53',
                                    label: {
                                        key: 'DFR_109.7.RBK',
                                        required: true,
                                        requiredProp: 'allmant.behandling.annanBehandlingBeskrivning'
                                    }
                                }]]
                            }])
                    ]),

                    kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', {
                        hideExpression: function (scope) { return !R6_OR_R17(scope.model);}}, [
                        fraga(100, 'FRG_100.RBK', 'FRG_100.HLP', {
                            required: true, requiredProp: 'hypoglykemier.sjukdomenUnderKontroll'
                        }, [
                             {
                                 type: 'ue-radio',
                                 yesLabel: 'SVAR_JA.RBK',
                                 noLabel: 'SVAR_NEJ.RBK',
                                 modelProp: 'hypoglykemier.sjukdomenUnderKontroll'
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
                                minDate: twelveMonthsBack,
                                maxDate: today,
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
                                minDate: threeMonthsBack,
                                maxDate: today,
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
                                minDate: twelveMonthsBack,
                                maxDate: today,
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
                        fraga(8, 'FRG_8.RBK', 'FRG_8.HLP', {
                                required: true,
                                requiredProp: function(model) {
                                    return R12(model);
                                }
                        }, [
                            {
                                type: 'ue-alert',
                                alertType: 'warning',
                                key: 'TSDIA-001.ALERT',
                                hideExpression: function(scope) {return !R11(scope);}
                            },
                            {
                                type: 'ue-grid',
                                independentRowValidation: true,
                                components: [
                                    // Row 1
                                    [{}, {
                                        type: 'ue-form-label',
                                        key: 'ts-diabetes.label.syn.utankorrektion',
                                        helpKey: 'ts-diabetes.helptext.synfunktioner.utan-korrektion',
                                        required: true,
                                        requiredProp: function(model) {
                                            return R12(model);
                                        }
                                    }, {
                                        type: 'ue-form-label',
                                        key: 'ts-diabetes.label.syn.medkorrektion',
                                        helpKey: 'ts-diabetes.helptext.synfunktioner.med-korrektion',
                                        required: true,
                                        requiredProp: function(model) {
                                            return medKorrigeringRequired(model);
                                        }
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

                            },
                            {
                                type: 'ue-alert',
                                alertType: 'info',
                                key: 'TSDIA-002.ALERT'
                            }
                        ])
                    ]),

                    kategori(categoryIds[6], 'KAT_6.RBK', 'KAT_6.HLP', {}, [
                        fraga(32, 'FRG_32.RBK', 'FRG_32.HLP', {}, [{
                            modelProp: 'ovrigt',
                            type: 'ue-textarea',
                            htmlMaxlength: 189,
                            rows: 3
                        }])
                    ]),


                    kategori(categoryIds[7], 'KAT_7.RBK', 'KAT_7.HLP', {}, [
                        fraga(33, 'FRG_33.RBK', 'FRG_33.HLP', {
                                required: true,
                                requiredProp: requiredKorkortProperties('bedomning.uppfyllerBehorighetskrav', 17)
                            },
                            [{
                                type: 'ue-checkgroup-ts',
                                modelProp: 'bedomning.uppfyllerBehorighetskrav',
                                labelTemplate: 'KV_KORKORTSBEHORIGHET_{0}.RBK',
                                watcher: ueTSFactoryTemplates.getBedomningListenerConfig('uppfyllerBehorighetskrav', 'KANINTETASTALLNING')
                            }]
                        ),
                        fraga(45, 'FRG_45.RBK', 'FRG_45.HLP', {
                            required: true,
                            hideExpression: function(scope) { return !R1(scope);},
                            requiredProp: 'bedomning.lampligtInnehav'
                        }, [{
                            type: 'ue-radio',
                            yesLabel: 'SVAR_JA.RBK',
                            noLabel: 'SVAR_NEJ.RBK',
                            modelProp: 'bedomning.lampligtInnehav'
                        }]),
                        fraga(34, 'FRG_34.RBK', 'FRG_34.HLP', {}, [{
                            type: 'ue-textfield',
                            htmlMaxlength: '71',
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
