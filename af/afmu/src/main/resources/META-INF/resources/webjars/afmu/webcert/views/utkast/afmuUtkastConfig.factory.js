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

angular.module('afmu').factory('afmu.UtkastConfigFactory',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ueFactoryTemplatesHelper',
        function($log, $timeout, DateUtils, ueFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                return {
                    1: 'funktionsnedsattning',
                    2: 'aktivitetsbegransning',
                    3: 'medicinskaBehandlingar',
                    5: 'ovrigt'
                };
            }

            function _getConfig(viewState) {
                var categoryIds = _getCategoryIds();

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;

                var config = [

                    kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', {}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', {}, [{
                            type: 'ue-textarea',
                            modelProp: 'funktionsnedsattning',
                            label: {
                                key: 'DFR_1.2.RBK',
                                helpKey: 'DFR_1.2.HLP',
                                required: true,
                                requiredProp: 'funktionsnedsattning'
                            }
                        }])
                    ]),

                    kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', {}, [
                        fraga(2, 'FRG_2.RBK', 'FRG_2.HLP', {}, [{
                            type: 'ue-textarea',
                            modelProp: 'aktivitetsbegransning',
                            label: {
                                key: 'DFR_2.2.RBK',
                                helpKey: 'DFR_2.2.HLP',
                                required: true,
                                requiredProp: 'aktivitetsbegransning'
                            }
                        }])]),

                    kategori(categoryIds[3], 'KAT_3.RBK', 'KAT_3.HLP', {}, [
                        fraga(3, 'FRG_3.RBK', 'FRG_3.HLP', {}, [{
                            type: 'ue-textarea',
                            modelProp: 'pagaendeBehandling',
                            label: {
                                key: 'DFR_3.2.RBK',
                                helpKey: 'DFR_3.2.HLP'
                            }
                        }]),
                        fraga(4, 'FRG_4.RBK', 'FRG_4.HLP', {}, [{
                            type: 'ue-textarea',
                            modelProp: 'planeradBehandling',
                            label: {
                                key: 'DFR_4.2.RBK',
                                helpKey: 'DFR_4.2.HLP'
                            }
                        }])
                    ]),

                    kategori(categoryIds[5], 'KAT_5.RBK', 'KAT_5.HLP', {}, [
                        fraga(5, 'FRG_5.RBK', 'FRG_5.HLP', { }, [{
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
        }]);
