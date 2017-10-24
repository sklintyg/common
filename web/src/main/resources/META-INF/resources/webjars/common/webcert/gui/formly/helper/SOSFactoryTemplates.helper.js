/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

angular.module('common').factory('common.SOSFactoryTemplatesHelper', [
    'common.ObjectHelper', 'common.UserModel', 'common.DateUtilsService', 'common.PersonIdValidatorService', 'common.FactoryTemplatesHelper',
    function($log, ObjectHelper, UserModel, dateUtils, PersonIdValidator, FactoryTemplates) {
        'use strict';

        var kategori = FactoryTemplates.kategori;
        var fraga = FactoryTemplates.fraga;

        return {
            identitet: function(categoryName, useFraga14){

                var fragor = [
                    fraga(1, [
                        {
                            key: 'identitetStyrkt',
                            type: 'single-text-vertical',
                            templateOptions: {label: 'DFR_1.1', required: true, maxlength: 100}
                        }
                    ])
                ];

                if(useFraga14){
                    fragor.push(fraga(14, [
                        {
                            key: 'land',
                            type: 'single-text-vertical',
                            templateOptions: {label: 'DFR_14.1', required: false, maxlength: 100}
                        }
                    ]));
                }

                return kategori(1, categoryName, fragor);
            },
            dodsDatum: function(categoryName){
                return kategori(2, categoryName, [
                    fraga(2, [
                        {
                            key: 'dodsdatumSakert',
                            type: 'boolean',
                            templateOptions: {
                                label: 'FRG_2',
                                yesLabel: 'SVAR_SAKERT.RBK',
                                noLabel: 'SVAR_EJ_SAKERT.RBK',
                                required: true
                            }
                        },
                        {
                            key: 'dodsdatum',
                            type: 'singleDate',
                            hideExpression: 'model.dodsdatumSakert !== true',
                            templateOptions: {label: 'DFR_2.2', required: true}
                        },
                        {
                            key: 'dodsdatum',
                            type: 'vagueDate',
                            hideExpression: 'model.dodsdatumSakert !== false',
                            templateOptions: {label: 'DFR_2.2', required: true}
                        },
                        {
                            key: 'antraffatDodDatum',
                            type: 'singleDate',
                            hideExpression: 'model.dodsdatumSakert !== false',
                            templateOptions: {label: 'DFR_2.3', required: true}
                        }
                    ]),
                    fraga(3, [
                        {
                            key: 'dodsplatsKommun',
                            type: 'typeahead',
                            templateOptions: {
                                label: 'DFR_3.1',
                                required: true,
                                maxlength: 100,
                                valuesUrl: '/api/config/kommuner'
                            }
                        },
                        {
                            key: 'dodsplatsBoende',
                            type: 'radio-group',
                            templateOptions: {
                                label: 'DFR_3.2',
                                code: 'DODSPLATS_BOENDE',
                                choices: [
                                    'SJUKHUS',
                                    'ORDINART_BOENDE',
                                    'SARSKILT_BOENDE',
                                    'ANNAN'
                                ],
                                required: true
                            }
                        }
                    ])
                ]);
            },
            barn: function(categoryName){
                return kategori(3, categoryName, [
                    fraga(4, [
                        {
                            key: 'barn',
                            type: 'boolean',
                            templateOptions: {label: 'DFR_4.1', required: true},
                            expressionProperties: {
                                'templateOptions.disabled': 'formState.barnForced'
                            },
                            watcher: {
                                expression: 'model.dodsdatumSakert ? model.dodsdatum : null',
                                listener: function _barnDodsDatumListener(field, newValue, oldValue, scope) {
                                    if (newValue !== oldValue) {
                                        var birthDate = dateUtils.toMomentStrict(PersonIdValidator.getBirthDate(scope.model.grundData.patient.personId));
                                        if (!birthDate) {
                                            $log.error('Invalid personnummer in _barnDodsDatumListener');
                                        }
                                        else {
                                            var barn28DagarDate = birthDate.add('days', 28);
                                            var dodsDatum = dateUtils.toMomentStrict(newValue);
                                            if (dodsDatum && dodsDatum.isValid() &&
                                                (dodsDatum.isBefore(barn28DagarDate) ||
                                                    dodsDatum.isSame(barn28DagarDate))) {
                                                scope.model.barn = true;
                                                scope.options.formState.barnForced = true;
                                            } else if (dodsDatum && dodsDatum.isValid() &&
                                                dodsDatum.isAfter(barn28DagarDate)) {
                                                scope.model.barn = false;
                                                scope.options.formState.barnForced = true;
                                            } else {
                                                scope.model.barn = undefined;
                                                scope.options.formState.barnForced = false;
                                            }
                                        }
                                    }
                                }
                            }
                        },{
                            type: 'info',
                            hideExpression: '!formState.barnForced || !model.barn',
                            templateOptions: {label: 'db.info.barn.forced.true'}
                        },{
                            type: 'info',
                            hideExpression: '!formState.barnForced || model.barn',
                            templateOptions: {label: 'db.info.barn.forced.false'}
                        }
                    ])
                ]);
            }
        };
    }]);
