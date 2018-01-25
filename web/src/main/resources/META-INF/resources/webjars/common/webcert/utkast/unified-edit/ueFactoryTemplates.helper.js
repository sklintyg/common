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

angular.module('common').factory('common.ueFactoryTemplatesHelper', [
    function() {
        'use strict';

        var singleTextLabelColSize = 3;

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
                    required: options.required
                },
                components: components,
                hideExpression: options.hideExpression
            };
        }

        function _fraga(id, labelKey, helpKey, options, components) {
            var fraga = {
                type: 'ue-fraga',
                frageId: id,
                components: components,
                hideExpression: options.hideExpression
            };
            if (labelKey) {
                fraga.label = {
                    key: labelKey,
                    helpKey: helpKey,
                    required: options.required,
                    type: 'h4'
                };
            }
            return fraga;
        }

        return {

            kategori: _kategori,

            fraga: _fraga,

            vardenhet: _kategori('vardenhet', 'common.label.vardenhet', 'common.help.vardenhet', {}, [ _fraga('', '', '', {}, [{
                type: 'ue-labelvardenhet'
            }, {
                type: 'ue-textfield',
                modelProp: 'grundData.skapadAv.vardenhet.postadress',
                label: {
                    key: 'common.postadress',
                    required: true,
                    whitespaceBreak: false
                },
                size: 'full',
                labelColSize: singleTextLabelColSize,
                formType: 'horizontal',
                htmlMaxlength: 265
            }, {
                type: 'ue-textfield',
                modelProp: 'grundData.skapadAv.vardenhet.postnummer',
                label: {
                    key: 'common.postnummer',
                    required: true,
                    whitespaceBreak: false
                },
                size: '5',
                labelColSize: singleTextLabelColSize,
                formType: 'horizontal',
                htmlMaxlength: 6,
                numbersOnly: true
            }, {
                type: 'ue-textfield',
                modelProp: 'grundData.skapadAv.vardenhet.postort',
                label: {
                    key: 'common.postort',
                    required: true,
                    whitespaceBreak: false
                },
                size: 'full',
                labelColSize: singleTextLabelColSize,
                formType: 'horizontal',
                htmlMaxlength: 265
            }, {
                type: 'ue-textfield',
                modelProp: 'grundData.skapadAv.vardenhet.telefonnummer',
                label: {
                    key: 'common.telefonnummer',
                    required: true,
                    whitespaceBreak: false
                },
                size: 'full',
                labelColSize: singleTextLabelColSize,
                formType: 'horizontal',
                htmlMaxlength: 265
            }])])

        };
    }]);
