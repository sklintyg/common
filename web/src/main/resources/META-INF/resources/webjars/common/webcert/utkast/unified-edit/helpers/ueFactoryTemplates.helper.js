/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.ueFactoryTemplatesHelper', [ 'common.PrefilledUserDataService', 'common.UserModel',
    function(prefilledUserDataService, UserModel) {
        'use strict';

        var labelColSize = 3;

        function _kategori(id, labelKey, helpKey, options, components) {

            if(!options){
                options = {};
            }

            return {
                type: 'ue-kategori',
                categoryId: id,
                label: {
                    key: labelKey,
                    helpKey: helpKey,
                    required: options.required,
                    requiredProp: options.requiredProp
                },
                components: components,
                hideExpression: options.hideExpression,
                signingDoctor: options.signingDoctor
            };
        }

        function _fraga(id, labelKey, helpKey, options, components) {
            var fraga = {
                type: 'ue-fraga',
                frageId: id,
                components: components,
                hideExpression: options.hideExpression,
                validationContext: options.validationContext,
                disabledFunc: options.disabledFunc,
                cssClass: options.cssClass,
                viewState: options.viewState
            };
            if (labelKey) {
                fraga.label = {
                    key: labelKey,
                    helpKey: helpKey,
                    required: options.required,
                    requiredMode: options.requiredMode,
                    requiredProp: options.requiredProp,
                    labelType: 'h4',
                    hideHelpExpression: options.hideHelpExpression
                };
            }
            return fraga;
        }



        return {

            kategori: _kategori,

            fraga: _fraga,

            patient: function(shouldDisableAddressInputWhen, allowUpdaterButton, signingDoctor) {
                var patient = _kategori('patient', 'common.intyg.patientadress', '', {signingDoctor: signingDoctor}, [
                    _fraga(null, '', '', {}, [{
                        type: 'ue-address-text-area',
                        modelProp: 'grundData.patient.postadress',
                        label: {
                            key: 'common.postadress',
                            required: true,
                            requiredProp: 'grundData.patient.postadress'
                        },
                        htmlMaxlength: 209,
                        size: 'full',
                        rows: 1,
                        labelColSize: labelColSize,
                        formType: 'horizontal',
                        disabled: shouldDisableAddressInputWhen
                    }]),
                    _fraga(null, '', '', {}, [{
                        type: 'ue-textfield',
                        modelProp: 'grundData.patient.postnummer',
                        label: {
                            key: 'common.postnummer',
                            required: true,
                            requiredProp: 'grundData.patient.postnummer'
                        },
                        htmlMaxlength: 6,
                        numbersOnly: true,
                        size: '5',
                        labelColSize: labelColSize,
                        formType: 'horizontal',
                        disabled: shouldDisableAddressInputWhen
                    }]),
                    _fraga(null, '', '', {}, [{
                        type: 'ue-textfield',
                        modelProp: 'grundData.patient.postort',
                        label: {
                            key: 'common.postort',
                            required: true,
                            requiredProp: 'grundData.patient.postort'
                        },
                        htmlMaxlength: 30,
                        size: '20',
                        labelColSize: labelColSize,
                        formType: 'horizontal',
                        disabled: shouldDisableAddressInputWhen
                    }])
                ]);
                if (allowUpdaterButton === true ||
                    (angular.isFunction(allowUpdaterButton) && allowUpdaterButton() === true)) {
                    patient.components.push({
                        type: 'ue-patient-address-updater',
                        formType: 'horizontal',
                        labelColSize: labelColSize
                    });
                }
                return patient;
            },

            vardenhet: _kategori('vardenhet', 'common.label.vardenhet', 'common.help.vardenhet', {}, [ _fraga('', '', '', {}, [{
                type: 'ue-labelvardenhet'
            }, {
                type: 'ue-address-text-area',
                modelProp: 'grundData.skapadAv.vardenhet.postadress',
                label: {
                    key: 'common.postadress',
                    required: true,
                    requiredProp: 'grundData.skapadAv.vardenhet.postadress',
                    whitespaceBreak: false
                },
                size: 'full',
                rows: 1,
                labelColSize: labelColSize,
                formType: 'horizontal',
                htmlMaxlength: 209
            }]),
            _fraga(null, '', '', {}, [{
                type: 'ue-textfield',
                modelProp: 'grundData.skapadAv.vardenhet.postnummer',
                label: {
                    key: 'common.postnummer',
                    required: true,
                    requiredProp: 'grundData.skapadAv.vardenhet.postnummer',
                    whitespaceBreak: false
                },
                size: '5',
                labelColSize: labelColSize,
                formType: 'horizontal',
                htmlMaxlength: 6,
                numbersOnly: true
            }]),
            _fraga(null, '', '', {}, [{
                type: 'ue-textfield',
                modelProp: 'grundData.skapadAv.vardenhet.postort',
                label: {
                    key: 'common.postort',
                    required: true,
                    requiredProp: 'grundData.skapadAv.vardenhet.postort',
                    whitespaceBreak: false
                },
                size: '20',
                labelColSize: labelColSize,
                formType: 'horizontal',
                htmlMaxlength: 30
            }]),
            _fraga(null, '', '', {}, [{
                type: 'ue-textfield',
                modelProp: 'grundData.skapadAv.vardenhet.telefonnummer',
                label: {
                    key: 'common.telefonnummer',
                    required: true,
                    requiredProp: 'grundData.skapadAv.vardenhet.telefonnummer',
                    whitespaceBreak: false
                },
                size: '15',
                labelColSize: labelColSize,
                formType: 'horizontal',
                htmlMaxlength: 20,
                numbersOnly: true,
                placeholder: 'Telefon'
            }])])

        };
    }]);
