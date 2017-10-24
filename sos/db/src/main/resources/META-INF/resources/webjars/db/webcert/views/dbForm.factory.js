angular.module('db').factory('db.FormFactory',
    ['$log', '$timeout',
        'common.DateUtilsService', 'common.ObjectHelper', 'common.UserModel',
        'common.FactoryTemplatesHelper', 'common.PersonIdValidatorService', 'common.SOSFactoryTemplates',
        function($log, $timeout,
            DateUtils, ObjectHelper, UserModel, FactoryTemplates, PersonIdValidator, SOSFactoryTemplates) {
            'use strict';

            // Validation category names matched with backend message strings from InternalDraftValidator
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
                SOSFactoryTemplates.identitet(categoryNames[1], false),
                SOSFactoryTemplates.dodsDatum(categoryNames[2]),
                SOSFactoryTemplates.barn(categoryNames[3]),
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
                                code: 'DETALJER_UNDERSOKNING',
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
