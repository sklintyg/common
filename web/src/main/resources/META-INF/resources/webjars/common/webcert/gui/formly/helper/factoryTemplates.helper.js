/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

angular.module('common').factory('common.FactoryTemplatesHelper', [
    'common.ObjectHelper', 'common.UserModel',
    function(ObjectHelper, UserModel) {
        'use strict';

        return {
            adress: {
                wrapper: 'wc-field-static',
                templateOptions: {staticLabel: 'common.intyg.patientadress', categoryName: 'patient'},
                fieldGroup: [
                    {
                        key: 'grundData.patient.postadress',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postadress',
                            disabled: UserModel.isDjupintegration(),
                            size: 'full',
                            labelColSize: 3,
                            formType: 'horizontal',
                            maxlength: 500,
                            required: true
                        }
                    },
                    {
                        key: 'grundData.patient.postnummer',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postnummer',
                            disabled: UserModel.isDjupintegration(),
                            size: '5',
                            labelColSize: 3,
                            formType: 'horizontal',
                            maxlength: 6,
                            required: true
                        }
                    },
                    {
                        key: 'grundData.patient.postort',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postort',
                            disabled: UserModel.isDjupintegration(),
                            labelColSize: 3,
                            formType: 'horizontal',
                            maxlength: 100,
                            required: true
                        }
                    },
                    {
                        type: 'patient-address-updater',
                        hideExpression: function() {
                            return UserModel.isDjupintegration();
                        },
                        templateOptions: {formType: 'horizontal', labelColSize: 3, hideFromSigned: true}
                    }
                ]
            },
            vardenhet: {
                wrapper: 'wc-field-static',
                templateOptions: {staticLabel: 'common.label.vardenhet', categoryName: 'vardenhet'},
                fieldGroup: [
                    {
                        type: 'label-vardenhet'
                    },
                    {
                        key: 'grundData.skapadAv.vardenhet.postadress',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postadress',
                            size: 'full',
                            labelColSize: 3,
                            formType: 'horizontal',
                            maxlength: 500,
                            required: true
                        }
                    },
                    {
                        key: 'grundData.skapadAv.vardenhet.postnummer',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postnummer',
                            size: '5',
                            labelColSize: 3,
                            formType: 'horizontal',
                            maxlength: 6,
                            required: true
                        }
                    },
                    {
                        key: 'grundData.skapadAv.vardenhet.postort',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Postort',
                            labelColSize: 3,
                            formType: 'horizontal',
                            required: true
                        }
                    },
                    {
                        key: 'grundData.skapadAv.vardenhet.telefonnummer',
                        type: 'single-text',
                        templateOptions: {
                            staticLabel: 'Telefonnummer',
                            labelColSize: 3,
                            formType: 'horizontal',
                            maxlength: 100,
                            required: true
                        }
                    }
                ]
            },
            annatGrundForMUBeskrivning: {
                key: 'annatGrundForMUBeskrivning',
                type: 'single-text-vertical',
                hideExpression: '!model.annatGrundForMU',
                templateOptions: {
                    label: 'DFR_1.3',
                    help: 'DFR_1.3',
                    required: true,
                    size: 'full',
                    hideWhenEmpty: true,
                    forceLine: true,
                    kompletteringKey: 'annatGrundForMU'
                }
            }
        };
    }]);
