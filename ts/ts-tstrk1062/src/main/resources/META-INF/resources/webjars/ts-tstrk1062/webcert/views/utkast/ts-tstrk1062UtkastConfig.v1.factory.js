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

angular.module('ts-tstrk1062').factory('ts-tstrk1062.UtkastConfigFactory.v1',
    ['$log', '$timeout', 'common.ueFactoryTemplatesHelper', 'common.ueTSFactoryTemplatesHelper',
        function($log, $timeout, ueFactoryTemplates, ueTSFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                return {
                    1: 'intygavser',
                    2: 'idkontroll',
                    3: 'diagnos',
                    4: 'lakemedelsbehandling',
                    5: 'symptom'
                };
            }

            function _getConfig(viewState) {
                var categoryIds = _getCategoryIds();

                var thisYear = moment().format('YYYY');
                var today = moment().format('YYYY-MM-DD');

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var patient = ueTSFactoryTemplates.patient(viewState);

                function requiredKorkortProperties(field, extraproperty) {
                    var antalKorkort = 16;
                    var korkortsarray = [];
                    for (var i = 0; i < antalKorkort; i++) {
                        korkortsarray.push(field + '.korkortstyp[' + i + '].selected');
                    }
                    korkortsarray.push(extraproperty);
                    return korkortsarray;
                }

                function diagnosRegistreringKodad(scope) {
                    if (!scope.model.diagnosRegistrering || !scope.model.diagnosRegistrering.typ) {
                        return true;
                    }
                    return !(scope.model.diagnosRegistrering.typ === 'DIAGNOS_KODAD');
                }

                function diagnosRegistreringFritext(scope) {
                    if (!scope.model.diagnosRegistrering || !scope.model.diagnosRegistrering.typ) {
                        return true;
                    }
                    return !(scope.model.diagnosRegistrering.typ === 'DIAGNOS_FRITEXT');
                }

                function lakemedelsbehandlingSaknas(scope) {
                    if (scope.model.lakemedelsbehandling === undefined ||
                        scope.model.lakemedelsbehandling.harHaft === undefined) {
                        return true;
                    }
                    return scope.model.lakemedelsbehandling.harHaft === false;
                }

                function lakemedelsbehandlingPagar(scope) {
                    if (scope.model.lakemedelsbehandling === undefined ||
                        scope.model.lakemedelsbehandling.harHaft === undefined ||
                        scope.model.lakemedelsbehandling.harHaft === false ||
                        scope.model.lakemedelsbehandling.pagar === undefined) {
                        return true;
                    }
                    return scope.model.lakemedelsbehandling.pagar === true;
                }

                function lakemedelsbehandlingAvslutad(scope) {
                    if (scope.model.lakemedelsbehandling === undefined ||
                        scope.model.lakemedelsbehandling.harHaft === undefined ||
                        scope.model.lakemedelsbehandling.harHaft === false ||
                        scope.model.lakemedelsbehandling.pagar === undefined) {
                        return true;
                    }
                    return scope.model.lakemedelsbehandling.pagar === false;
                }

                var config = [

                    patient,

                    // Intyget avser
                    kategori(categoryIds[1], 'KAT_1.RBK', {}, {}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP',
                            {required: true, requiredProp: requiredKorkortProperties('intygAvser')}, [{
                                type: 'ue-checkgroup-ts',
                                modelProp: 'intygAvser.korkortstyp',
                                labelTemplate: 'KV_INTYGET_AVSER.{0}.RBK',
                                label: {}
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
                        fraga(50, 'FRG_50.RBK', 'FRG_50.HLP', {required: true, requiredProp: 'diagnosRegistrering'},
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
                                required: true, hideExpression: diagnosRegistreringKodad,
                                requiredProp: 'diagnosKodad[0].diagnosKod'
                            },
                            [{
                                type: 'ue-diagnos',
                                modelProp: 'diagnosKodad',
                                diagnosKodLabel: 'DFR_51.1.RBK'
                            }]),
                        fraga(52, 'FRG_52.RBK', '', {
                            required: true, hideExpression: diagnosRegistreringFritext,
                            requiredProp: 'diagnosFritext.diagnosFritext'
                        }, [{
                            type: 'ue-textarea',
                            modelProp: 'diagnosFritext.diagnosFritext',
                        },
                            {
                                type: 'ue-year-picker',
                                modelProp: 'diagnosFritext.diagnosArtal',
                                maxYear: thisYear,
                                label: {
                                    key: 'DFR_52.2.RBK',
                                    required: true,
                                    requiredProp: 'diagnosFritext.diagnosArtal'
                                }
                            }
                        ]),
                    ]),

                    // Lakemedelsbehandling
                    kategori(categoryIds[4], 'KAT_4.RBK', {}, {}, [
                        fraga(53, 'FRG_53.RBK', '', {required: true, requiredProp: 'lakemedelsbehandling.harHaft'},
                            [{
                                type: 'ue-radio',
                                modelProp: 'lakemedelsbehandling.harHaft',
                                paddingBottom: true,
                            }]),
                        fraga(54, 'FRG_54.RBK', '', {required: true, hideExpression: lakemedelsbehandlingSaknas ,requiredProp: 'lakemedelsbehandling.pagar'},
                            [{
                                type: 'ue-radio',
                                modelProp: 'lakemedelsbehandling.pagar',
                                paddingBottom: true,
                            }]),
                        fraga(55, 'FRG_55.RBK', '', {required: true, hideExpression: lakemedelsbehandlingAvslutad ,requiredProp: 'lakemedelsbehandling.aktuell'},
                            [{
                                type: 'ue-textarea',
                                modelProp: 'lakemedelsbehandling.aktuell'
                            }]),
                        fraga(56, 'FRG_56.RBK', '', {required: true, hideExpression: lakemedelsbehandlingAvslutad ,requiredProp: 'lakemedelsbehandling.pagatt'},
                            [{
                                type: 'ue-radio',
                                modelProp: 'lakemedelsbehandling.pagatt',
                                paddingBottom: true,
                            }]),
                        fraga(57, 'FRG_57.RBK', '', {required: true, hideExpression: lakemedelsbehandlingAvslutad ,requiredProp: 'lakemedelsbehandling.effekt'},
                            [{
                                type: 'ue-radio',
                                modelProp: 'lakemedelsbehandling.effekt',
                                paddingBottom: true,
                            }]),
                        fraga(58, 'FRG_58.RBK', '',
                            {required: true, hideExpression: lakemedelsbehandlingAvslutad ,requiredProp: 'lakemedelsbehandling.foljsamhet'},
                            [{
                                type: 'ue-radio',
                                modelProp: 'lakemedelsbehandling.foljsamhet',
                                paddingBottom: true,
                            }]),
                        fraga(58, 'FRG_59.RBK', '',
                            {required: true, hideExpression: lakemedelsbehandlingPagar ,requiredProp: 'lakemedelsbehandling.avslutadTidpunkt'},
                            [{
                                type: 'ue-date',
                                modelProp: 'lakemedelsbehandling.avslutadTidpunkt',
                                maxDate: today
                            },
                                {
                                    type: 'ue-textarea',
                                    modelProp: 'lakemedelsbehandling.avslutadOrsak',
                                    label: {
                                        key: 'DFR_59.2.RBK',
                                        required: true,
                                        requiredProp: 'lakemedelsbehandling.avslutadOrsak'
                                    }
                                }
                            ]),
                    ]),
                    // Symptom (Symptom, funktionshinder och prognos)
                    kategori(categoryIds[5], 'KAT_5.RBK', {}, {}, [
                        fraga(60, 'FRG_60.RBK', 'FRG_60.HLP', {required: true, requiredProp: 'bedomningAvSymptom'},
                            [{
                                type: 'ue-textarea',
                                modelProp: 'bedomningAvSymptom',
                            }]),
                        fraga(61, 'FRG_61.RBK', '', {required: true, requiredProp: 'prognosTillstand.typ'},
                            [{
                                type: 'ue-radiogroup',
                                modelProp: 'prognosTillstand.typ',
                                htmlClass: 'col-md-6 no-padding',
                                paddingBottom: true,
                                choices: [
                                    {label: 'SVAR_JA.RBK', id: 'true'},
                                    {label: 'SVAR_NEJ.RBK', id: 'false'},
                                    {label: 'SVAR_KANEJBEDOMA.RBK', id: 'NI'}
                                ]
                            }]),
                    ]),

                    ueFactoryTemplates.vardenhet/*,

    Befattning and specialitet was present in code but not working in 5.4
                kategori(null, '', '', {}, [
                    fraga(null, '', '', {}, [{
                        type: 'ue-befattning-specialitet'
                    }])
                ])
*/
                ];
                return config;
            }


            return {
                getConfig: _getConfig,
                getCategoryIds: _getCategoryIds
            };
        }]);
