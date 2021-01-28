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
angular.module('tstrk1062').factory('tstrk1062.UtkastConfigFactory.v1',
    ['$log', '$timeout', 'common.ueFactoryTemplatesHelper', 'common.ueTSFactoryTemplatesHelper',
        function($log, $timeout, ueFactoryTemplates, ueTSFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                return {
                    1: 'intygavser',
                    2: 'idkontroll',
                    3: 'allmant',
                    4: 'lakemedelsbehandling',
                    5: 'symptom',
                    6: 'ovrigt',
                    7: 'bedomning'
                };
            }

            function _getConfig(viewState) {
                var categoryIds = _getCategoryIds();

                var thisYear = moment().format('YYYY');

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;

                function requiredKorkortProperties(field, antalKorkort, extraproperty) {
                    var korkortsarray = [];
                    for (var i = 0; i < antalKorkort; i++) {
                        korkortsarray.push(field + '[' + i + '].selected');
                    }
                    korkortsarray.push(extraproperty);
                    return korkortsarray;
                }

                function diagnosRegistreringKodad(scope) {
                    var isKodad = scope.model.diagnosRegistrering.typ === 'DIAGNOS_KODAD';
                    return !isKodad;
                }

                function diagnosRegistreringFritext(scope) {
                    var isFritext = scope.model.diagnosRegistrering.typ === 'DIAGNOS_FRITEXT';
                    return !isFritext;
                }

                function requiredDiagnosKodadValue(model) {
                    var hasValue = true;
                    if (model.diagnosKodad && model.diagnosKodad.length > 0) {
                        if (model.diagnosKodad[0].diagnosKod && hasArtal(model.diagnosKodad[0].diagnosArtal)) {
                            hasValue = false;
                        }
                    }
                    return hasValue;
                }

                function hasArtal(diagnosArtal) {
                    return diagnosArtal && diagnosArtal.length === 4;
                }

                var config = [

                    // Intyget avser
                    kategori(categoryIds[1], 'KAT_1.RBK', {}, {signingDoctor: true}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP',
                            {required: true, requiredProp: requiredKorkortProperties('intygAvser.behorigheter', 16)}, [{
                                type: 'ue-checkgroup-ts',
                                modelProp: 'intygAvser.behorigheter',
                                htmlClass: 'no-padding',
                                labelTemplate: 'KV_INTYGET_AVSER.{0}.RBK'
                            }])
                    ]),

                    // ID kontroll
                    kategori(categoryIds[2], 'KAT_2.RBK', {}, {}, [
                        fraga(2, 'FRG_2.RBK', 'FRG_2.HLP', {required: true, requiredProp: 'idKontroll.typ'}, [{
                            type: 'ue-radiogroup',
                            modelProp: 'idKontroll.typ',
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

                    // Diagnos
                    kategori(categoryIds[3], 'KAT_3.RBK', {}, {}, [
                        fraga(50, 'FRG_50.RBK', 'FRG_50.HLP', {required: true, requiredProp: 'diagnosRegistrering.typ'},
                            [{
                                type: 'ue-radiogroup',
                                modelProp: 'diagnosRegistrering.typ',
                                htmlClass: 'col-md-6 no-padding',
                                paddingBottom: true,
                                choices: [
                                    {label: 'SVAR_KODAD.RBK', id: 'DIAGNOS_KODAD'},
                                    {label: 'SVAR_FRITEXT.RBK', id: 'DIAGNOS_FRITEXT'}
                                ]
                            }]),
                        fraga(51, 'FRG_51.RBK', '', {
                                required: true,
                                hideExpression: diagnosRegistreringKodad,
                                requiredProp: requiredDiagnosKodadValue
                            },
                            [{
                                type: 'ue-diagnos-ar',
                                modelProp: 'diagnosKodad',
                                defaultKodSystem: 'ICD_10_SE',
                                diagnosKodLabel: 'DFR_51.1.RBK',
                                diagnosArtalLabel: 'DFR_51.3.RBK',
                                yearConfig: [
                                    {modelProp: 'diagnosKodad[0].diagnosArtal', maxYear: thisYear, skipAttic: true},
                                    {modelProp: 'diagnosKodad[1].diagnosArtal', maxYear: thisYear, skipAttic: true},
                                    {modelProp: 'diagnosKodad[2].diagnosArtal', maxYear: thisYear, skipAttic: true},
                                    {modelProp: 'diagnosKodad[3].diagnosArtal', maxYear: thisYear, skipAttic: true}
                                ]
                            }]),
                        fraga(52, 'FRG_52.RBK', '', {
                            required: true,
                            hideExpression: diagnosRegistreringFritext,
                            requiredProp: 'diagnosFritext.diagnosFritext'
                        }, [{
                            type: 'ue-textarea',
                            modelProp: 'diagnosFritext.diagnosFritext'
                        }]),
                        fraga(52, '', '',
                            {
                                hideExpression: diagnosRegistreringFritext
                            },
                            [{
                                type: 'ue-year-picker',
                                modelProp: 'diagnosFritext.diagnosArtal',
                                maxYear: thisYear,
                                label: {
                                    key: 'DFR_52.2.RBK',
                                    required: true,
                                    requiredProp: 'diagnosFritext.diagnosArtal'
                                }
                            }])
                    ]),

                    // Lakemedelsbehandling
                    kategori(categoryIds[4], 'KAT_4.RBK', {}, {}, [
                        fraga(53, 'FRG_53.RBK', '', {required: true, requiredProp: 'lakemedelsbehandling.harHaft'},
                            [{
                                type: 'ue-radio',
                                modelProp: 'lakemedelsbehandling.harHaft',
                                paddingBottom: true
                            }]),
                        fraga(54, 'FRG_54.RBK', '', {
                                required: true,
                                hideExpression: '!model.lakemedelsbehandling.harHaft',
                                requiredProp: 'lakemedelsbehandling.pagar'
                            },
                            [{
                                type: 'ue-radio',
                                modelProp: 'lakemedelsbehandling.pagar',
                                paddingBottom: true
                            }]),
                        fraga(55, 'FRG_55.RBK', '', {
                                required: true,
                                hideExpression: '!model.lakemedelsbehandling.pagar',
                                requiredProp: 'lakemedelsbehandling.aktuell'
                            },
                            [{
                                type: 'ue-textarea',
                                modelProp: 'lakemedelsbehandling.aktuell'
                            }]),
                        fraga(56, 'FRG_56.RBK', '', {
                                required: true,
                                hideExpression: '!model.lakemedelsbehandling.pagar',
                                requiredProp: 'lakemedelsbehandling.pagatt'
                            },
                            [{
                                type: 'ue-radio',
                                modelProp: 'lakemedelsbehandling.pagatt',
                                paddingBottom: true
                            }]),
                        fraga(57, 'FRG_57.RBK', '', {
                                required: true,
                                hideExpression: '!model.lakemedelsbehandling.pagar',
                                requiredProp: 'lakemedelsbehandling.effekt'
                            },
                            [{
                                type: 'ue-radio',
                                modelProp: 'lakemedelsbehandling.effekt',
                                paddingBottom: true
                            }]),
                        fraga(58, 'FRG_58.RBK', '',
                            {
                                required: true,
                                hideExpression: '!model.lakemedelsbehandling.pagar',
                                requiredProp: 'lakemedelsbehandling.foljsamhet'
                            },
                            [{
                                type: 'ue-radio',
                                modelProp: 'lakemedelsbehandling.foljsamhet',
                                paddingBottom: true
                            }]),
                        fraga(59, 'FRG_59.RBK', '',
                            {
                                required: true,
                                hideExpression: '!(model.lakemedelsbehandling.pagar == false)',
                                requiredProp: 'lakemedelsbehandling.avslutadTidpunkt'
                            },
                            [{
                                type: 'ue-textfield',
                                modelProp: 'lakemedelsbehandling.avslutadTidpunkt'
                            }]),
                        fraga(59, '', '',
                            {
                                hideExpression: '!(model.lakemedelsbehandling.pagar == false)'
                            },
                            [{
                                type: 'ue-textarea',
                                modelProp: 'lakemedelsbehandling.avslutadOrsak',
                                label: {
                                    key: 'DFR_59.2.RBK',
                                    required: true,
                                    requiredProp: 'lakemedelsbehandling.avslutadOrsak'
                                }
                            }])
                    ]),
                    // Symptom (Symptom, funktionshinder och prognos)
                    kategori(categoryIds[5], 'KAT_5.RBK', {}, {}, [
                        fraga(60, 'FRG_60.RBK', 'FRG_60.HLP', {required: true, requiredProp: 'bedomningAvSymptom'},
                            [{
                                type: 'ue-textarea',
                                modelProp: 'bedomningAvSymptom'
                            }]),
                        fraga(61, 'FRG_61.RBK', '', {required: true, requiredProp: 'prognosTillstand.typ'},
                            [{
                                type: 'ue-radiogroup',
                                modelProp: 'prognosTillstand.typ',
                                htmlClass: 'col-md-6 no-padding',
                                paddingBottom: true,
                                choices: [
                                    {label: 'SVAR_JA.RBK', id: 'JA'},
                                    {label: 'SVAR_NEJ.RBK', id: 'NEJ'},
                                    {label: 'SVAR_KANEJBEDOMA.RBK', id: 'KANEJBEDOMA'}
                                ]
                            }])
                    ]),
                    // Ovrigt
                    kategori(categoryIds[6], 'KAT_6.RBK', {}, {}, [
                        fraga(32, 'FRG_32.RBK', '', {},
                            [{
                                type: 'ue-textarea',
                                modelProp: 'ovrigaKommentarer'
                            }])
                    ]),
                    // Bedomning
                    kategori(categoryIds[7], 'KAT_7.RBK', '', {}, [
                        fraga(33, 'FRG_33.RBK', 'FRG_33.HLP', {
                                required: true,
                                requiredProp: requiredKorkortProperties('bedomning.uppfyllerBehorighetskrav', 17)
                            },
                            [{
                                type: 'ue-checkgroup-ts',
                                modelProp: 'bedomning.uppfyllerBehorighetskrav',
                                labelTemplate: 'KV_KORKORTSBEHORIGHET.{0}.RBK',
                                watcher: ueTSFactoryTemplates.getBedomningListenerConfig('uppfyllerBehorighetskrav',
                                    'VAR11')
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
        }]);
