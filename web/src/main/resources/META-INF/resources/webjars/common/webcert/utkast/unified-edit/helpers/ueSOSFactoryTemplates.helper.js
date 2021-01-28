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
angular.module('common').factory('common.ueSOSFactoryTemplatesHelper', [
    '$log', 'common.ObjectHelper', 'common.UserModel', 'common.DateUtilsService', 'common.PersonIdValidatorService',
    'common.ueFactoryTemplatesHelper',
    function($log, ObjectHelper, UserModel, dateUtils, PersonIdValidator, ueFactoryTemplates) {
        'use strict';

        var kategori = ueFactoryTemplates.kategori;
        var fraga = ueFactoryTemplates.fraga;
        var today = moment().format('YYYY-MM-DD');

        var beginningOfLastYear = moment()
            .subtract(1, 'year')
            .dayOfYear(1)
            .format('YYYY-MM-DD');

        function _patient(viewState) {
            function _shouldDisableAddressInputWhen(model) {
                return viewState.common.validPatientAddressAquiredFromPU;
            }
            return ueFactoryTemplates.patient(_shouldDisableAddressInputWhen, false, true);
        }

        return {

            patient: _patient,

            identitet: function(categoryName, useFraga14){

                var fragor = [
                    fraga(1, '', '', {}, [
                        {
                            modelProp: 'identitetStyrkt',
                            type: 'ue-textfield',
                            htmlMaxlength: 27,
                            size: '25',
                            label: {
                                key: 'DFR_1.1.RBK',
                                helpKey: 'DFR_1.1.HLP',
                                required: true,
                                requiredProp: 'identitetStyrkt'
                            }
                        }
                    ])
                ];

                if(useFraga14){
                    fragor.push(fraga(14, '', '', {}, [
                        {
                            modelProp: 'land',
                            type: 'ue-textfield',
                            htmlMaxlength: 24,
                            size: '25',
                            label: {
                                key: 'DFR_14.1.RBK',
                                helpKey: 'DFR_14.1.HLP',
                                required: false
                            }
                        }
                    ]));
                }

                return kategori(categoryName, 'KAT_1.RBK', 'KAT_1.HLP', {}, fragor);
            },
            dodsDatum: function(categoryName){
                return kategori(categoryName, 'KAT_2.RBK', 'KAT_2.HLP', {}, [
                    fraga(2, 'FRG_2.RBK', 'FRG_2.HLP', { required: true, requiredProp: 'dodsdatumSakert' }, [
                        {
                            modelProp: 'dodsdatumSakert',
                            type: 'ue-radio',
                            yesLabel: 'SVAR_SAKERT.RBK',
                            noLabel: 'SVAR_EJ_SAKERT.RBK'
                        }
                        ]),
                    fraga(2, '', '', { required: true }, [
                        {
                            modelProp: 'dodsdatum',
                            type: 'ue-date',
                            minDate: beginningOfLastYear,
                            maxDate: today,
                            hideExpression: 'model.dodsdatumSakert !== true',
                            label: {key: 'DFR_2.2.RBK', helpKey: 'DFR_2.2.HLP', required: true, requiredProp: 'dodsdatum'}
                        },
                        {
                            modelProp: 'dodsdatum',
                            type: 'ue-vague-date',
                            hideExpression: 'model.dodsdatumSakert !== false',
                            label: {key: 'DFR_2.2.RBK', helpKey: 'DFR_2.2.HLP', required: true, requiredProp: 'dodsdatum'}
                        }
                    ]),
                    fraga(2, '', '', { required: true }, [
                        {
                            modelProp: 'antraffatDodDatum',
                            type: 'ue-date',
                            minDate: beginningOfLastYear,
                            maxDate: today,
                            hideExpression: 'model.dodsdatumSakert !== false',
                            label: {key: 'DFR_2.3.RBK', helpKey: 'DFR_2.2.HLP', required: true, requiredProp: 'antraffatDodDatum'}
                        }
                        ]),
                    fraga(3, 'FRG_3.RBK', 'FRG_3.HLP', {}, [
                        {
                            modelProp: 'dodsplatsKommun',
                            type: 'ue-typeahead',
                            htmlMaxlength: 28,
                            size: '25',
                            valuesUrl: '/api/config/kommuner',
                            orderByBeginning: true,
                            label: {
                                key: 'DFR_3.1.RBK',
                                helpKey: 'DFR_3.1.HLP',
                                required: true,
                                requiredProp: 'dodsplatsKommun'
                            }
                        }
                    ]),
                    fraga(3, '', 'FRG_3.HLP', {}, [
                        {
                            modelProp: 'dodsplatsBoende',
                            type: 'ue-radiogroup',
                            code: 'DODSPLATS_BOENDE',
                            choices: [
                                {label:'DODSPLATS_BOENDE.SJUKHUS.RBK', id:'SJUKHUS'},
                                {label:'DODSPLATS_BOENDE.ORDINART_BOENDE.RBK', id:'ORDINART_BOENDE'},
                                {label:'DODSPLATS_BOENDE.SARSKILT_BOENDE.RBK', id:'SARSKILT_BOENDE'},
                                {label:'DODSPLATS_BOENDE.ANNAN.RBK', id:'ANNAN'}
                            ],
                            label: {
                                key: 'DFR_3.2.RBK',
                                helpKey: 'DFR_3.2.HLP',
                                required: true,
                                requiredProp: 'dodsplatsBoende'
                            }
                        }
                    ])
                ]);
            },
            barn: function(categoryName){
                return kategori(categoryName, 'KAT_3.RBK', 'KAT_3.HLP', {}, [
                    fraga(4, '', '', {}, [
                        {
                            modelProp: 'barn',
                            type: 'ue-radio',
                            label: {key: 'DFR_4.1.RBK', helpKey: 'DFR_4.1.HLP', required: true, requiredProp: 'barn'},
                            yesLabel: 'SVAR_JA.RBK',
                            noLabel: 'SVAR_NEJ.RBK',
                            disabledExpression: 'form.formState.barnForced',
                            watcher: {
                                expression: 'model.dodsdatumSakert ? model.dodsdatum : null',
                                listener: function _barnDodsDatumListener(newValue, oldValue, scope) {
                                    var birthDate = dateUtils.toMomentStrict(PersonIdValidator.getBirthDate(scope.model.grundData.patient.personId));
                                    if (!birthDate) {
                                        $log.error('Invalid personnummer in _barnDodsDatumListener');
                                    }
                                    else {
                                        var barn28DagarDate = birthDate.add(28, 'days');
                                        var dodsDatum = dateUtils.toMomentStrict(newValue);
                                        if (dodsDatum && dodsDatum.isValid() &&
                                            (dodsDatum.isBefore(barn28DagarDate) ||
                                                dodsDatum.isSame(barn28DagarDate))) {
                                            scope.model.barn = true;
                                            scope.form.formState.barnForced = true;
                                        } else if (dodsDatum && dodsDatum.isValid() &&
                                            dodsDatum.isAfter(barn28DagarDate)) {
                                            scope.model.barn = false;
                                            scope.form.formState.barnForced = true;
                                        } else {
                                            scope.form.formState.barnForced = false;
                                        }
                                    }
                                }
                            }
                        },{
                            type: 'ue-alert',
                            alertType: 'info',
                            hideExpression: '!form.formState.barnForced || !model.barn',
                            key: 'DFR_4.1_INOM28.INFO'
                        },{
                            type: 'ue-alert',
                            alertType: 'info',
                            hideExpression: '!form.formState.barnForced || model.barn',
                            key: 'DFR_4.1_EJ_INOM28.INFO'
                        }
                    ])
                ]);
            }
        };
    }]);
