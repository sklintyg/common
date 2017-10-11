angular.module('db').factory('db.FormFactory',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ObjectHelper', 'common.UserModel',
        'common.FactoryTemplatesHelper', 'common.PersonIdValidatorService',
        function($log, $timeout,
            DateUtils, ObjectHelper, UserModel, FactoryTemplates, PersonIdValidator) {
            'use strict';

            var categoryNames = {
                1: 'personuppgifter',
                2: 'dodsdatumochdodsplats',
                3: 'barnsomavlidit',
                4: 'explosivimplantat',
                5: 'yttreundersokning',
                6: 'polisanmalan'
            };

            var kategori = FactoryTemplates.kategori;
            var fraga = FactoryTemplates.fraga;

            var formFields = [
                FactoryTemplates.adress,
                kategori(1, categoryNames[1], [
                    fraga(1, [
                        {
                            key: 'identitetStyrkt',
                            type: 'single-text-vertical',
                            templateOptions: {label: 'DFR_1.1', required: true, maxlength: 100}
                        }
                    ])
                ]),
                kategori(2, categoryNames[2], [
                    fraga(2, [
                        {
                            key: 'dodsdatumSakert',
                            type: 'boolean',
                            templateOptions: {
                                label: 'FRG_2',
                                yesLabel: 'DFR_2.1.SVA_1',
                                noLabel: 'DFR_2.1.SVA_2',
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
                                code: 'KV_DODSPLATS_BOENDE',
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
                ]),
                kategori(3, categoryNames[3], [
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
                                        var birthDate = DateUtils.toMomentStrict(PersonIdValidator.getBirthDate(scope.model.grundData.patient.personId));
                                        if (!birthDate) {
                                            $log.error('Invalid personnummer in _barnDodsDatumListener');
                                        }
                                        else {
                                            var barn28DagarDate = birthDate.add('days', 28);
                                            var dodsDatum = DateUtils.toMomentStrict(newValue);
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
                ]),
                kategori(4, categoryNames[4], [
                    fraga(5, [
                        {
                            key: 'explosivImplantat',
                            type: 'boolean',
                            templateOptions: {label: 'DFR_5.1', required: true}
                        },
                        {
                            key: 'explosivAvlagsnat',
                            type: 'boolean',
                            hideExpression: '!model.explosivImplantat',
                            templateOptions: {label: 'DFR_5.2', required: true}
                        }
                    ])
                ]),
                kategori(5, categoryNames[5], [
                    fraga(6, [
                        {
                            key: 'undersokningYttre',
                            type: 'radio-group',
                            templateOptions: {
                                label: 'DFR_6.1',
                                code: 'KV_DETALJER_UNDERSOKNING',
                                choices: ['JA',
                                    'UNDERSOKNING_SKA_GORAS',
                                    'UNDERSOKNING_GJORT_KORT_FORE_DODEN'
                                ],
                                required: true
                            }
                        },
                        {
                            key: 'undersokningDatum',
                            type: 'singleDate',
                            hideExpression: 'model.undersokningYttre != "UNDERSOKNING_GJORT_KORT_FORE_DODEN"',
                            templateOptions: {label: 'DFR_6.3', required: true}
                        }
                    ])
                ]),
                kategori(6, categoryNames[6], [
                    fraga(7, [
                        {
                            key: 'polisanmalan',
                            type: 'boolean',
                            templateOptions: {label: 'DFR_7.1', required: true},
                            expressionProperties: {
                                'templateOptions.disabled': 'model.undersokningYttre === "UNDERSOKNING_SKA_GORAS"'
                            },
                            watcher: {
                                expression: 'model.undersokningYttre',
                                listener: function _undersokningYttreListener(field, newValue, oldValue, scope, stopWatching) {
                                    if (newValue === 'UNDERSOKNING_SKA_GORAS') {
                                        scope.model.polisanmalan = true;
                                    } else if (oldValue === 'UNDERSOKNING_SKA_GORAS') {
                                        scope.model.polisanmalan = undefined;
                                    }
                                }
                            }
                        }, {
                            type: 'info',
                            hideExpression: 'model.polisanmalan !== true',
                            templateOptions: {label: 'db.info.polisanmalan'}
                        }
                    ])
                ]),
                FactoryTemplates.vardenhet
            ];

            return {
                getFormFields: function() {
                    return angular.copy(formFields);
                },
                getCategoryNames: function() {
                    return angular.copy(categoryNames);
                }
            };
        }]);
