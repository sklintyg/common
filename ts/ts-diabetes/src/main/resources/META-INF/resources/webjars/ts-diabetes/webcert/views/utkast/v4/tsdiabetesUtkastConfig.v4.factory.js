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
angular.module('ts-diabetes').factory('ts-diabetes.UtkastConfigFactory.v4',
    ['$log', '$timeout', 'common.ObjectHelper', 'common.DateUtilsService', 'common.PersonIdValidatorService',
        'common.ueFactoryTemplatesHelper', 'common.ueTSFactoryTemplatesHelper',
        function ($log, $timeout, ObjectHelper, DateUtils, PersonIdValidator, ueFactoryTemplates, ueTSFactoryTemplates) {
            'use strict';

            var today = moment().format('YYYY-MM-DD');

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
            function R1(model) {
                return _hasAnyOfIntygAvserBehorighet(model, ['IAV1', 'IAV2', 'IAV3', 'IAV4', 'IAV5', 'IAV6', 'IAV7', 'IAV8', 'IAV9']);
            }

            function R8(model) {
                return ObjectHelper.deepGet(model, 'hypoglykemi.aterkommandeSenasteAret');
            }

            function R9(model) {
                return ObjectHelper.deepGet(model, 'hypoglykemi.aterkommandeVaketSenasteTolv');
            }

            function R27(model) {
                return !ObjectHelper.deepGet(model, 'hypoglykemi.kontrollSjukdomstillstand');
            }

            // TODO REPLACE IAVs with VARs WHEN QUESTION HAS BEEN UPDATED
            function R28(model) {
                return _hasAnyOfIntygAvserBehorighet(model, ['IAV1', 'IAV2', 'IAV3', 'IAV4', 'IAV5', 'IAV6', 'IAV7', 'IAV8', 'IAV9']);
                //return _hasAnyOfIntygAvserBehorighet(model, ['VAR1', 'VAR2', 'VAR3', 'VAR4', 'VAR5', 'VAR6', 'VAR7', 'VAR8', 'VAR9']);
            }

            function R30(model) {
                return ObjectHelper.deepGet(model, 'allmant.medicineringMedforRiskForHypoglykemi');
            }

            function R32(model) {
                return ObjectHelper.deepGet(model, 'allmant.medicineringForDiabetes');
            }

            function R33(model) {
                return ObjectHelper.deepGet(model, 'hypoglykemi.aterkommandeVaketSenasteTre');
            }

            function R34(model) {
                return ObjectHelper.deepGet(model, 'hypoglykemi.allvarligSenasteTolvManaderna');
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
                    4: 'hypoglykemi',
                    6: 'ovrigt',
                    7: 'bedomning'
                };
            }

            function _getConfig(viewState) {
                var categoryIds = _getCategoryIds();

                var thisYear = moment().format('YYYY');
                var patientBirthDate = DateUtils.toMomentStrict(PersonIdValidator.getBirthDate(viewState.intygModel.grundData.patient.personId));
                var patientBirthDateValue = patientBirthDate.format('YYYY-MM-DD');
                var patientBirthYearValue = patientBirthDate.format('YYYY');

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var config = [

                    // Intyget avser
                    kategori(categoryIds[1], 'KAT_1.RBK', {}, {signingDoctor: true}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP',
                            {required: true, requiredProp: requiredKorkortProperties('intygAvser.kategorier', 16)}, [{
                                type: 'ue-checkgroup-ts',
                                modelProp: 'intygAvser.kategorier',
                                htmlClass: 'no-padding',
                                labelTemplate: 'KV_INTYGET_AVSER.{0}.RBK'
                            }])
                    ]),

                    kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', {}, [
                        fraga(1, 'FRG_2.RBK', 'FRG_2.HLP', {required: true, requiredProp: 'identitetStyrktGenom.typ'}, [{
                            type: 'ue-radiogroup',
                            modelProp: 'identitetStyrktGenom.typ',
                            htmlClass: 'col-md-6 no-padding',
                            paddingBottom: true,
                            choices: [
                                {label: 'KV_ID_KONTROLL.IDK1.RBK', id: 'IDK1'},
                                {label: 'KV_ID_KONTROLL.IDK2.RBK', id: 'IDK2'},
                                {label: 'KV_ID_KONTROLL.IDK3.RBK', id: 'IDK3'},
                                {label: 'KV_ID_KONTROLL.IDK4.RBK', id: 'IDK4'},
                                {label: 'KV_ID_KONTROLL.IDK5.RBK', id: 'IDK5'},
                                {label: 'KV_ID_KONTROLL.IDK6.RBK', id: 'IDK6'}
                            ]
                        }])
                    ]),

                    kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', {}, [
                        fraga(35, 'FRG_35.RBK', 'FRG_35.HLP', {required: true, requiredProp: 'allmant.diabetesDiagnosAr'}, [{
                            type: 'ue-year-picker',
                            modelProp: 'allmant.diabetesDiagnosAr',
                            minYear: patientBirthYearValue,
                            maxYear: thisYear
                        }]),
                        fraga(18, 'FRG_18.RBK', '', {required: true, requiredProp: 'allmant.typAvDiabetes'}, [{
                            type: 'ue-radiogroup',
                            modelProp: 'allmant.typAvDiabetes',
                            choices: [
                                {label: 'SVAR_TYP1.RBK', id: 'TYP1'},
                                {label: 'SVAR_TYP2.RBK', id: 'TYP2'},
                                {label: 'SVAR_LADA.RBK', id: 'LADA'},
                                {label: 'SVAR_ANNAN.RBK', id: 'ANNAN'}
                            ]
                        },
                            {
                                type: 'ue-textfield',
                                modelProp: 'allmant.beskrivningAnnanTypAvDiabetes',
                                hideExpression: function(scope) {return (scope.model.allmant.typAvDiabetes !== 'ANNAN');},
                                size: '53-width',
                                htmlMaxlength: '53',
                                label: {
                                    key: 'DFR_18.2.RBK',
                                    required: true,
                                    requiredProp: 'allmant.beskrivningAnnanTypAvDiabetes'
                                }
                            }
                        ]),
                        fraga(207, 'FRG_207.RBK', 'FRG_207.HLP', {required: true, requiredProp: 'allmant.medicineringForDiabetes'}, [{
                            type: 'ue-radio',
                            yesLabel: 'SVAR_JA.RBK',
                            noLabel: 'SVAR_NEJ.RBK',
                            modelProp: 'allmant.medicineringForDiabetes'
                        }]),
                        fraga(208, 'FRG_208.RBK', 'FRG_208.HLP', {
                            required: true,
                            requiredProp: function (model) {
                                return R32(model) && model.allmant.medicineringMedforRiskForHypoglykemi === undefined;
                            },
                            hideExpression: '!model.allmant.medicineringForDiabetes'
                        }, [{
                            type: 'ue-radio',
                            yesLabel: 'SVAR_JA.RBK',
                            noLabel: 'SVAR_NEJ.RBK',
                            modelProp: 'allmant.medicineringMedforRiskForHypoglykemi'
                        }]),
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
                                    type: 'ue-checkbox',
                                    modelProp: 'allmant.behandling.insulin',
                                    label: {
                                        key: 'DFR_109.3.RBK'
                                    }
                                }], [{
                                    type: 'ue-year-picker',
                                    modelProp: 'allmant.behandling.insulinSedanAr',
                                    minYear: patientBirthYearValue,
                                    maxYear: thisYear,
                                    hideExpression: '!model.allmant.behandling.insulin',
                                    label: {
                                        key: 'DFR_109.4.RBK',
                                        required: true,
                                        requiredProp: 'allmant.behandling.insulinSedanAr'
                                    }
                                }], [{
                                    type: 'ue-checkbox',
                                    modelProp: 'allmant.behandling.annanBehandling',
                                    label: {
                                        key: 'DFR_109.5.RBK'
                                    },
                                    paddingBottom: true
                                }], [{
                                    type: 'ue-textfield',
                                    modelProp: 'allmant.behandling.annanBehandlingBeskrivning',
                                    hideExpression: '!model.allmant.behandling.annanBehandling',
                                    size: '53-width',
                                    htmlMaxlength: '53',
                                    label: {
                                        key: 'DFR_109.6.RBK',
                                        required: true,
                                        requiredProp: 'allmant.behandling.annanBehandlingBeskrivning'
                                    }
                                }], [{
                                    type: 'ue-radio',
                                    yesLabel: 'SVAR_JA.RBK',
                                    noLabel: 'SVAR_NEJ.RBK',
                                    modelProp: 'allmant.behandling.riskHypoglykemi',
                                    hideExpression: '!model.allmant.behandling.tabletter && !model.allmant.behandling.annanBehandling',
                                    label: {
                                        key: 'DFR_109.7.RBK',
                                        required: true,
                                        requiredProp: 'allmant.behandling.riskHypoglykemi'
                                    }
                                }]]
                            }])
                    ]),

                    kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', {
                        hideExpression: function(scope) {return !R28(scope.model) && !R30(scope.model);}
                    }, [
                        fraga(200, 'FRG_200.RBK', 'FRG_200.HLP', {
                            hideExpression: function(scope) { return !R30(scope.model); },
                            required: true,
                            requiredProp: function (model) {
                                return R30(model) && model.hypoglykemi.kontrollSjukdomstillstand === undefined;
                            }
                            }, [
                                {
                                    type: 'ue-radio',
                                    yesLabel: 'SVAR_JA.RBK',
                                    noLabel: 'SVAR_NEJ.RBK',
                                    modelProp: 'hypoglykemi.kontrollSjukdomstillstand'
                                },
                            {
                                type: 'ue-textfield',
                                modelProp: 'hypoglykemi.kontrollSjukdomstillstandVarfor',
                                hideExpression: function(scope) { return !R27(scope.model) || scope.model.hypoglykemi.kontrollSjukdomstillstand === undefined; },
                                htmlMaxlength: 53,
                                size: '53-width',
                                label: {
                                    key: 'DFR_200.2.RBK',
                                    required: true,
                                    requiredProp: 'hypoglykemi.kontrollSjukdomstillstandVarfor'
                                }
                            }]
                        ),
                        fraga(201, 'FRG_201.RBK', 'FRG_201.HLP', {
                            hideExpression: function(scope) { return !R30(scope.model); },
                            required: true,
                            requiredProp: function (model) {
                                return R30(model) && model.hypoglykemi.forstarRiskerMedHypoglykemi === undefined;
                            }
                            }, [
                                {
                                    type: 'ue-radio',
                                    yesLabel: 'SVAR_JA.RBK',
                                    noLabel: 'SVAR_NEJ.RBK',
                                    modelProp: 'hypoglykemi.forstarRiskerMedHypoglykemi'
                                }]
                        ),
                        fraga(110, 'FRG_110.RBK', 'FRG_110.HLP', {
                            hideExpression: function(scope) { return !R30(scope.model); },
                            required: true,
                            requiredProp: function (model) {
                                return R30(model) && model.hypoglykemi.formagaKannaVarningstecken === undefined;
                            }
                            }, [
                                {
                                    type: 'ue-radio',
                                    yesLabel: 'SVAR_JA.RBK',
                                    noLabel: 'SVAR_NEJ.RBK',
                                    modelProp: 'hypoglykemi.formagaKannaVarningstecken'
                                }]
                        ),
                        fraga(202, 'FRG_202.RBK', 'FRG_202.HLP', {
                            hideExpression: function(scope) { return !R30(scope.model); },
                            required: true,
                            requiredProp: function (model) {
                                return R30(model) && model.hypoglykemi.vidtaAdekvataAtgarder === undefined;
                            }
                            }, [
                                {
                                    type: 'ue-radio',
                                    yesLabel: 'SVAR_JA.RBK',
                                    noLabel: 'SVAR_NEJ.RBK',
                                    modelProp: 'hypoglykemi.vidtaAdekvataAtgarder'
                                }]
                        ),
                        fraga(106, 'FRG_106.RBK', 'FRG_106.HLP', {
                            hideExpression: function(scope) { return !R30(scope.model); },
                            required: true,
                            requiredProp: function (model) {
                                return R30(model) && model.hypoglykemi.aterkommandeSenasteAret === undefined;
                            }
                            }, [
                                {
                                    type: 'ue-radio',
                                    yesLabel: 'SVAR_JA.RBK',
                                    noLabel: 'SVAR_NEJ.RBK',
                                    modelProp: 'hypoglykemi.aterkommandeSenasteAret'
                                },
                            {
                                type: 'ue-date',
                                minDate: patientBirthDateValue,
                                maxDate: today,
                                modelProp: 'hypoglykemi.aterkommandeSenasteAretTidpunkt',
                                hideExpression: function(scope) { return !R8(scope.model); },
                                label: {
                                    key: 'DFR_106.2.RBK',
                                    required: true,
                                    requiredProp: 'hypoglykemi.aterkommandeSenasteAretTidpunkt'
                                }
                            },
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemi.aterkommandeSenasteAretKontrolleras',
                                hideExpression: function(scope) { return !R8(scope.model); },
                                label: {
                                    key: 'DFR_106.3.RBK',
                                    required: true,
                                    requiredProp: 'hypoglykemi.aterkommandeSenasteAretKontrolleras'
                                }
                            },
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemi.aterkommandeSenasteAretTrafik',
                                hideExpression: function(scope) { return !R8(scope.model); },
                                label: {
                                    key: 'DFR_106.5.RBK',
                                    required: true,
                                    requiredProp: 'hypoglykemi.aterkommandeSenasteAretTrafik'
                                }
                            }]
                        ),
                        fraga(107, 'FRG_107.RBK', 'FRG_107.HLP', {
                            hideExpression: function(scope) { return !R30(scope.model); },
                            required: true,
                            requiredProp: function (model) {
                                return R30(model) && model.hypoglykemi.aterkommandeSenasteAret === undefined;
                            }
                            }, [
                                {
                                    type: 'ue-radio',
                                    yesLabel: 'SVAR_JA.RBK',
                                    noLabel: 'SVAR_NEJ.RBK',
                                    modelProp: 'hypoglykemi.aterkommandeVaketSenasteTolv'
                                },
                            {
                                type: 'ue-radio',
                                yesLabel: 'SVAR_JA.RBK',
                                noLabel: 'SVAR_NEJ.RBK',
                                modelProp: 'hypoglykemi.aterkommandeVaketSenasteTre',
                                hideExpression: function(scope) { return !R9(scope.model); },
                                label: {
                                    key: 'DFR_107.3.RBK',
                                    required: true,
                                    requiredProp: 'hypoglykemi.aterkommandeVaketSenasteTre'
                                }
                            },
                            {
                                type: 'ue-date',
                                minDate: patientBirthDateValue,
                                maxDate: today,
                                modelProp: 'hypoglykemi.aterkommandeVaketSenasteTreTidpunkt',
                                hideExpression: function(scope) { return !R33(scope.model); },
                                label: {
                                    key: 'DFR_107.5.RBK',
                                    required: true,
                                    requiredProp: 'hypoglykemi.aterkommandeVaketSenasteTreTidpunkt'
                                }
                            }]
                        ),
                        fraga(203, 'FRG_203.RBK', 'FRG_203.HLP', {
                            hideExpression: function(scope) { return !R28(scope.model); },
                            required: true,
                            requiredProp: function (model) {
                                return R28(model) && model.hypoglykemi.allvarligSenasteTolvManaderna === undefined;
                            }
                            }, [
                                {
                                    type: 'ue-radio',
                                    yesLabel: 'SVAR_JA.RBK',
                                    noLabel: 'SVAR_NEJ.RBK',
                                    modelProp: 'hypoglykemi.allvarligSenasteTolvManaderna'
                                },
                            {
                                type: 'ue-date',
                                minDate: patientBirthDateValue,
                                maxDate: today,
                                modelProp: 'hypoglykemi.allvarligSenasteTolvManadernaTidpunkt',
                                hideExpression: function(scope) { return !R34(scope.model); },
                                label: {
                                    key: 'DFR_203.2.RBK',
                                    required: true,
                                    requiredProp: 'hypoglykemi.allvarligSenasteTolvManadernaTidpunkt'
                                }
                            }]
                        ),
                        fraga(204, 'FRG_204.RBK', 'FRG_204.HLP', {
                            hideExpression: function(scope) { return !R28(scope.model); },
                            required: true,
                            requiredProp: function (model) {
                                return R28(model) && model.hypoglykemi.regelbundnaBlodsockerkontroller === undefined;
                            }
                            }, [
                                {
                                    type: 'ue-radio',
                                    yesLabel: 'SVAR_JA.RBK',
                                    noLabel: 'SVAR_NEJ.RBK',
                                    modelProp: 'hypoglykemi.regelbundnaBlodsockerkontroller'
                                }]
                        )
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
                                labelTemplate: 'KV_KORKORTSBEHORIGHET.{0}.RBK',
                                watcher: ueTSFactoryTemplates.getBedomningListenerConfig('uppfyllerBehorighetskrav', 'VAR11')
                            }]
                        ),
                        fraga(45, 'FRG_45.RBK', 'FRG_45.HLP', {
                            required: true,
                            requiredProp: function(model) {return R1(model) && model.bedomning.lampligtInnehav === undefined;}
                        }, [{
                            type: 'ue-radio',
                            yesLabel: 'SVAR_JA.RBK',
                            noLabel: 'SVAR_NEJ.RBK',
                            modelProp: 'bedomning.lampligtInnehav'
                        }]),
                        fraga(34, 'FRG_34.RBK', 'FRG_34.HLP', { }, [{
                            type: 'ue-textarea',
                            modelProp: 'bedomning.borUndersokasBeskrivning',
                            htmlMaxlength: 71,
                            rows: 3
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
