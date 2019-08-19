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

angular.module('tstrk1009').factory('tstrk1009.UtkastConfigFactory.v1',
    ['$log', '$timeout', 'common.ObjectHelper', 'common.DateUtilsService', 'common.ueFactoryTemplatesHelper',
        'common.ueTSFactoryTemplatesHelper',
        function($log, $timeout, ObjectHelper, DateUtils, ueFactoryTemplates, ueTSFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                return {
                    1: 'identitet',
                    2: 'anmalan',
                    3: 'medicinskaforhallanden',
                    4: 'bedomning',
                    5: 'informationombeslut'
                };
            }

            function _getConfig() {
                var categoryIds = _getCategoryIds();

                var today = moment().format('YYYY-MM-DD');

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var config = [

                    kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', {signingDoctor: true}, [
                        fraga(2, 'FRG_2.RBK', 'FRG_2.HLP', {required: true, requiredProp: 'identitetStyrktGenom.typ'}, [
                            {
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
                            }
                        ])
                    ]),

                    kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', {}, [
                        fraga(46, 'FRG_46.RBK', 'FRG_46.HLP', {required: true, requiredProp: 'anmalanAvser.typ'}, [{
                            type: 'ue-radiogroup',
                            modelProp: 'anmalanAvser.typ',
                            htmlClass: 'col-md-6 no-padding',
                            paddingBottom: true,
                            choices: [
                                {label: 'KV_SVAR_OLAMPLIGHET.OLAMPLIGHET.RBK', id: 'OLAMPLIGHET'},
                                {label: 'KV_SVAR_OLAMPLIGHET.SANNOLIK_OLAMPLIGHET.RBK', id: 'SANNOLIK_OLAMPLIGHET'}
                            ]
                        }])
                    ]),

                    kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', {}, [
                        fraga(47, 'FRG_47.RBK', 'FRG_47.HLP', {required: true, requiredProp: 'medicinskaForhallanden'},
                            [{
                                type: 'ue-textarea',
                                modelProp: 'medicinskaForhallanden'
                            }]),
                        fraga(48, 'FRG_48.RBK', 'FRG_48.HLP',
                            {required: true, requiredProp: 'senasteUndersokningsdatum'}, [{
                                type: 'ue-date',
                                maxDate: today,
                                modelProp: 'senasteUndersokningsdatum'
                            }])
                    ]),

                    kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', {required: true, requiredProp: 
                            ['intygetAvserBehorigheter.typer[0].selected', 'intygetAvserBehorigheter.typer[1].selected',
                            'intygetAvserBehorigheter.typer[2].selected', 'intygetAvserBehorigheter.typer[3].selected',
                            'intygetAvserBehorigheter.typer[4].selected', 'intygetAvserBehorigheter.typer[5].selected']}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', {}, [{
                            type: 'ue-checkgroup-ts',
                            modelProp: 'intygetAvserBehorigheter.typer',
                            htmlClass: 'no-padding',
                            labelTemplate: 'SVAR_{0}.RBK',
                            colSize: {
                                md: 12,
                                sm: 12
                            },
                            watcher: ueTSFactoryTemplates.getBedomningGroupListenerConfig('typer')
                        }])
                    ]),

                    kategori(categoryIds[5], 'KAT_5.RBK', 'KAT_5.HLP', {}, [
                        fraga(null, '', '', {}, [{
                            type: 'ue-checkbox',
                            modelProp: 'informationOmTsBeslutOnskas',
                            label: {
                                key: 'FRG_49.RBK'
                            },
                            paddingBottom: true
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
