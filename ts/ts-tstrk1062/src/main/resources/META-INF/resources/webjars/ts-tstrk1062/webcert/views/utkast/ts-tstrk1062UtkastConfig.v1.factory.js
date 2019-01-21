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
                    2: 'idkontroll'
                };
            }

            function _getConfig(viewState) {
                var categoryIds = _getCategoryIds();

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var patient = ueTSFactoryTemplates.patient(viewState);

                function requiredKorkortProperties(field, extraproperty) {
                    var antalKorkort = 16;
                    var korkortsarray = [];
                    for (var i = 0; i < antalKorkort; i++) {
                        korkortsarray.push( field + '.korkortstyp[' + i + '].selected');
                    }
                    korkortsarray.push(extraproperty);
                    return korkortsarray;
                }

                var config = [

                    patient,

                    // Intyget avser
                    kategori(categoryIds[1], 'KAT_1.RBK', {}, {}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', {required: true, requiredProp: requiredKorkortProperties('intygAvser')}, [{
                            type: 'ue-checkgroup-ts',
                            modelProp: 'intygAvser.korkortstyp',
                            labelTemplate:'KV_INTYGET_AVSER.{0}.RBK',
                            label: {}
                        }])
                    ]),

                    // ID kontroll
                    kategori(categoryIds[1], 'KAT_2.RBK', {}, {}, [
                        fraga(1, 'FRG_2.RBK', 'FRG_2.HLP', {required: true, requiredProp: 'idKontroll.typ'}, [{
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
