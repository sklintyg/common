angular.module('luae_fs').factory('luae_fs.FormFactory',
    ['luae_fs.FormFactoryHelper', 'common.UserModel', 'common.FactoryTemplatesHelper', 'common.DateUtilsService',
        function(FactoryHelper, UserModel, FactoryTemplates, dateUtils) {
            'use strict';


            var categoryNames = {
                1: 'grundformu',
                2: 'underlag',
                3: 'diagnos',
                4: 'funktionsnedsattning',
                5: 'ovrigt',
                6: 'kontakt'
            };

            var formFields = [
                FactoryTemplates.adress,
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 1, categoryName: categoryNames[1]},
                    fieldGroup: [
                        //Fråga 1 -----
                        {type: 'headline', templateOptions: {id: 'FRG_1', label: 'FRG_1', level: 4, noH5After: true, required: true}},
                        FactoryTemplates.grundForMU,
                        FactoryTemplates.annatGrundForMUBeskrivning,
                        {
                            key: 'motiveringTillInteBaseratPaUndersokning',
                            type: 'multi-text',
                            className: 'fold-animation',
                            hideExpression: 'model.undersokningAvPatienten || !(model.anhorigsBeskrivningAvPatienten || model.journaluppgifter || model.annatGrundForMU)',
                            templateOptions: {
                                bold: 'bold',
                                forceHeadingTypeLabel: true,
                                staticLabelId: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning',
                                subTextId: 'smi.label.grund-for-mu.motivering_utlatande_baseras_inte_pa_undersokning.info',
                                subTextDynId: 'FRG_25',
                                required: true
                            }
                        },

                        //Fråga 2 -----
                        {key: 'kannedomOmPatient', type: 'singleDate', templateOptions: {label: 'FRG_2', required: true}},

                        // Underlag
                        {
                            key: 'underlagFinns', type: 'boolean', templateOptions: {label: 'FRG_3', required: true}
                        },
                        {
                            key: 'underlag',
                            type: 'underlag',
                            hideExpression: '!model.underlagFinns',
                            templateOptions: {
                                underlagsTyper: [
                                    'NEUROPSYKIATRISKT',
                                    'HABILITERING',
                                    'ARBETSTERAPEUT',
                                    'FYSIOTERAPEUT',
                                    'LOGOPED',
                                    'PSYKOLOG',
                                    'SKOLHALSOVARD',
                                    'SPECIALISTKLINIK',
                                    'VARD_UTOMLANDS',
                                    'OVRIGT_UTLATANDE'
                                ], //KV_FKMU_005
                                typLabel: 'FRG_4',
                                datumLabel: 'DFR_4.2',
                                hamtasFranLabel: 'DFR_4.3'
                            },
                            watcher: {
                                expression: 'model.underlagFinns',
                                listener: FactoryHelper.underlagListener
                            }
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 3, categoryName: categoryNames[3]},
                    fieldGroup: [
                        {type: 'headline', templateOptions: {label: 'FRG_6', level: 4, noH5: false, required: true}},
                        {
                            key: 'diagnoser',
                            type: 'diagnos',
                            templateOptions: {diagnosBeskrivningLabel: 'DFR_6.1', diagnosKodLabel: 'DFR_6.2'}
                        }
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 4, categoryName: categoryNames[4]},
                    fieldGroup: [
                        {
                            key: 'funktionsnedsattningDebut',
                            type: 'multi-text',
                            templateOptions: {label: 'DFR_15.1', required: true }
                        },
                        {
                            key: 'funktionsnedsattningPaverkan',
                            type: 'multi-text',
                            templateOptions: {label: 'FRG_16', required: true }
                        }

                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 5, categoryName: categoryNames[5]},
                    fieldGroup: [
                        {key: 'ovrigt', type: 'multi-text', templateOptions: {label: 'DFR_25.1'}}
                    ]
                },
                {
                    wrapper: 'wc-field',
                    templateOptions: {category: 6, categoryName: categoryNames[6]},
                    fieldGroup: [
                        {
                            key: 'kontaktMedFk',
                            type: 'checkbox-inline',
                            templateOptions: {label: 'DFR_26.1'}
                        },
                        {
                            key: 'anledningTillKontakt',
                            type: 'multi-text',
                            hideExpression: '!model.kontaktMedFk',
                            templateOptions: {label: 'DFR_26.2'}
                        }
                    ]
                },
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
