/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

angular.module('db').factory('db.UtkastConfigFactory.v1',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ueFactoryTemplatesHelper', 'common.ueSOSFactoryTemplatesHelper',
        function($log, $timeout, DateUtils, ueFactoryTemplates, ueSOSFactoryTemplates) {
            'use strict';

            function _getCategoryIds() {
                // Validation category names matched with backend message strings from InternalDraftValidator
                return {
                    1: 'personuppgifter',
                    2: 'dodsdatumochdodsplats',
                    3: 'barnsomavlidit',
                    4: 'explosivimplantat',
                    5: 'yttreundersokning',
                    6: 'polisanmalan'
                };
            }

            function _getConfig(viewState) {
                var categoryIds = _getCategoryIds();

                var kategori = ueFactoryTemplates.kategori;
                var fraga = ueFactoryTemplates.fraga;
                var patient = ueSOSFactoryTemplates.patient(viewState);
                var beginningOfLastYear = moment()
                    .subtract(1, 'year')
                    .dayOfYear(1)
                    .format('YYYY-MM-DD');
                var today = moment().format('YYYY-MM-DD');

                var config = [

                    patient,

                    ueSOSFactoryTemplates.identitet(categoryIds[1], false),
                    ueSOSFactoryTemplates.dodsDatum(categoryIds[2]),
                    ueSOSFactoryTemplates.barn(categoryIds[3]),

                    kategori(categoryIds[4], 'KAT_4.RBK', 'KAT_4.HLP', {}, [
                        fraga(5, '', '', {}, [{
                            type: 'ue-radio',
                            modelProp: 'explosivImplantat',
                            label: {
                                key: 'DFR_5.1.RBK',
                                helpKey: 'DFR_5.1.HLP',
                                required: true,
                                requiredProp: 'explosivImplantat'
                            }
                        },{
                            type: 'ue-radio',
                            modelProp: 'explosivAvlagsnat',
                            hideExpression: '!model.explosivImplantat',
                            label: {
                                key: 'DFR_5.2.RBK',
                                helpKey: 'DFR_5.2.HLP',
                                required: true,
                                requiredProp: 'explosivAvlagsnat'
                            }
                        }])
                    ]),

                    kategori(categoryIds[5], 'KAT_5.RBK', 'KAT_5.HLP', {}, [
                        fraga(6, '', '', {}, [{
                            type: 'ue-radiogroup',
                            modelProp: 'undersokningYttre',
                            choices: [
                                {label:'SVAR_JA.RBK', id:'JA'},
                                {label:'DETALJER_UNDERSOKNING.UNDERSOKNING_SKA_GORAS.RBK', id:'UNDERSOKNING_SKA_GORAS'},
                                {label:'DETALJER_UNDERSOKNING.UNDERSOKNING_GJORT_KORT_FORE_DODEN.RBK', id:'UNDERSOKNING_GJORT_KORT_FORE_DODEN'}
                            ],
                            label: {
                                key: 'DFR_6.1.RBK',
                                helpKey: 'DFR_6.1.HLP',
                                required: true,
                                requiredProp: 'undersokningYttre'
                            }
                        }, {
                            type: 'ue-date',
                            minDate: beginningOfLastYear,
                            maxDate: today,
                            modelProp: 'undersokningDatum',
                            hideExpression: 'model.undersokningYttre != "UNDERSOKNING_GJORT_KORT_FORE_DODEN"',
                            label: {
                                key: 'DFR_6.3.RBK',
                                helpKey: 'DFR_6.3.HLP',
                                required: true,
                                requiredProp: 'undersokningDatum'
                            },
                            watcher: {
                                expression: ['model.antraffatDodDatum', 'model.dodsdatum', 'model.dodsdatumSakert'],
                                listener: function _maxDateUndersokningListener(newValue, oldValue, scope) {
                                    if (scope.model.dodsdatumSakert) {
                                        scope.config.maxDate = scope.model.dodsdatum || today;
                                        var minDate = (scope.model.dodsdatum ? moment(scope.model.dodsdatum)
                                            .subtract(4, 'week').format('YYYY-MM-DD') : undefined);
                                        scope.config.minDate = minDate;
                                    } else {
                                        scope.config.maxDate = scope.model.antraffatDodDatum || today;
                                        scope.config.minDate = undefined;
                                    }
                                }
                            }
                        }])
                    ]),

                    kategori(categoryIds[6], 'KAT_6.RBK', 'KAT_6.HLP', {}, [
                        fraga(7, '', '', {}, [{
                            type: 'ue-radio',
                            modelProp: 'polisanmalan',
                            yesLabel: 'DFR_7.1.SVA_1.RBK',
                            noLabel: 'DFR_7.1.SVA_2.RBK',
                            label: {
                                key: 'DFR_7.1.RBK',
                                helpKey: 'DFR_7.1.HLP',
                                required: true,
                                requiredProp: 'polisanmalan'
                            },
                            disabledExpression: 'model.undersokningYttre === "UNDERSOKNING_SKA_GORAS"',
                            watcher: {
                                expression: 'model.undersokningYttre',
                                listener: function _undersokningYttreListener(newValue, oldValue, scope) {
                                    if (newValue === 'UNDERSOKNING_SKA_GORAS') {
                                        scope.model.polisanmalan = true;
                                    } else if (oldValue === 'UNDERSOKNING_SKA_GORAS') {
                                        scope.model.polisanmalan = undefined;
                                    }
                                }
                            }
                        },{
                            type: 'ue-alert',
                            alertType: 'info',
                            hideExpression: 'model.undersokningYttre !== "UNDERSOKNING_SKA_GORAS"',
                            key: 'DFR_7.1_UNDERSOKNINGS_SKA_GORAS.INFO'
                        },{
                            type: 'ue-alert',
                            alertType: 'warning',
                            hideExpression: 'model.polisanmalan !== true',
                            key: 'DFR_7.1.OBS'
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
