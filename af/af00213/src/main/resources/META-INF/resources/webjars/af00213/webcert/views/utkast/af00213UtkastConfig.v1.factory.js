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

angular.module('af00213').factory('af00213.UtkastConfigFactory.v1',
    ['$log', '$timeout', 'common.DateUtilsService', 'common.ueFactoryTemplatesHelper',
        function ($log, $timeout, DateUtils, ueFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                return {
                    1: 'funktionsnedsattning',
                    2: 'aktivitetsbegransning',
                    3: 'utredningBehandling',
                    4: 'arbetetsPaverkan',
                    5: 'ovrigt'
                };
            }

            function _getConfig() {
                var categoryIds = _getCategoryIds();

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;

                var config = [

                    kategori(categoryIds[1], 'KAT_1.RBK', 'KAT_1.HLP', {signingDoctor: true}, [
                        fraga(1, 'FRG_1.RBK', 'FRG_1.HLP', {required: true, requiredProp: 'harFunktionsnedsattning'}, [{
                            type: 'ue-radio',
                            modelProp: 'harFunktionsnedsattning'
                        }, {
                            type: 'ue-textarea',
                            modelProp: 'funktionsnedsattning',
                            hideExpression: '!model.harFunktionsnedsattning',
                            label: {
                                key: 'DFR_1.2.RBK',
                                helpKey: 'DFR_1.2.HLP',
                                required: true,
                                requiredProp: 'funktionsnedsattning'
                            }
                        }])
                    ]),

                    kategori(categoryIds[2], 'KAT_2.RBK', 'KAT_2.HLP', { hideExpression: '!model.harFunktionsnedsattning' }, [
                        fraga(2, 'FRG_2.RBK', 'FRG_2.HLP', {required: true, requiredProp: 'harAktivitetsbegransning'}, [{
                            type: 'ue-radio',
                            modelProp: 'harAktivitetsbegransning'
                        }, {
                            type: 'ue-textarea',
                            modelProp: 'aktivitetsbegransning',
                            hideExpression: '!model.harAktivitetsbegransning',
                            label: {
                                key: 'DFR_2.2.RBK',
                                helpKey: 'DFR_2.2.HLP',
                                required: true,
                                requiredProp: 'aktivitetsbegransning'
                            }
                        }])]
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
