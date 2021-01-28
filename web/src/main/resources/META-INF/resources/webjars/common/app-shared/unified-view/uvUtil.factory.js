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
angular.module('common').factory('uvUtil', [
    '$parse', '$log', 'common.dynamicLabelService', 'common.messageService', 'common.ObjectHelper',
    function($parse, $log, dynamicLabelService, messageService, ObjectHelper) {
    'use strict';

        function _isDynamicText(str) {
            return angular.isString(str) && str.search(/{(.*)}/) > -1;
        }

        function _replacer(propertyName, model) {

            function replacer(match, p1, offset, string) {

                if(!model[p1]){
                    throw 'Property ' + p1 + ' does not exist in model. This is probably OK.';
                }

                return model[p1];
            }

            try {
                return propertyName.replace(/{(.*)}/g, replacer);
            } catch (error){
                return null;
            }
        }


        function _getModelProps(viewConfig) {
            var props = [];

            if (angular.isDefined(viewConfig.modelProp)) {
                props.push(viewConfig.modelProp);
            }

            angular.forEach(viewConfig.components, function(component) {
                props = props.concat(_getModelProps(component));
            });

            return props;
        }

        function _replaceType(viewConfig, from, to) {
            var propertiesToAdd = {type: to};
            return _updateProperties(viewConfig, from, propertiesToAdd);
        }

        function _updateProperties(viewConfig, type, properties) {
            angular.forEach(viewConfig, function(component) {
                if (angular.isDefined(component.type) && component.type === type) {
                    angular.extend(component, properties);
                }

                if (angular.isDefined(component.components)) {
                    _updateProperties(component.components, type, properties);
                }
            });

            return viewConfig;
        }

        function _convertToWebcert(viewConfig, skipPatient, isDraft) {
            viewConfig.pop();

            if (!skipPatient) {
                viewConfig.unshift({
                    type: 'uv-kategori',
                    labelKey: 'common.intyg.patientadress',
                    components: [{
                        type: 'uv-patient-grund',
                        modelProp: 'grundData.patient'
                    }]
                });
            }
            // As of WC 6.0, we show also drafts using uv-framework, and since drafts cant possibly be signed,
            // skip this component in that case.
            if(!isDraft) {
                viewConfig.push({
                    type: 'uv-signed-by',
                    modelProp: 'grundData'
                });
            }
            viewConfig = _replaceType(viewConfig, 'uv-fraga', 'uv-wc-fraga');

            var propertiesToUpdate = {contentUrl: null};
            viewConfig = _updateProperties(viewConfig, 'uv-table', propertiesToUpdate);

            propertiesToUpdate = {
                questionType: 'uv-wc-fraga'
            };
            viewConfig = _updateProperties(viewConfig, 'uv-tillaggsfragor', propertiesToUpdate);

            return viewConfig;
        }

        return {

        getTextFromConfig: function(value){
            if(value === ''){
                return value;
            }
            // Generate value from dynamic label if it existed, fallback to messageservice,
            // otherwise assume supplied value is what we want already and let it fall through
            var dynamicLabel = dynamicLabelService.getProperty(value);
            if(angular.isDefined(dynamicLabel) && dynamicLabel !== ''){
                return dynamicLabel;
            } else {
                if(messageService.propertyExists(value)){
                    return messageService.getProperty(value);
                } else {
                    return value;
                }
            }
        },
        getValue: function(obj, pathExpression) {

            // Process each element of an array into a new array, otherwise just the expression
            if(angular.isArray(pathExpression)){
                var list = [];

                pathExpression.forEach(function(item){
                    list.push($parse(item)(obj));
                });

                return list;
            }

            return $parse(pathExpression)(obj);
        },
        resolveValue: function(prop, modelRow, colProp, rowIndex, colIndex){
            var value = null;
            if(angular.isFunction(prop)){
                // Resolve using function
                value = prop(modelRow, rowIndex, colIndex, colProp);
            } else if(_isDynamicText(prop)) {
                // This prop is a string with a dynamic text.
                // Property should be on format <prefix>.{property-name}.<suffix>
                // Example: KV_FKMU_0001.{typ}.RBK => KV_FKMU_0001.ANHORIG.RBK
                value = _replacer(prop, modelRow);
            } else if(prop.indexOf('.') !== -1) {
                value = this.getValue(modelRow, prop);
            } else if(modelRow.hasOwnProperty(prop)) {
                // Resolve using property name
                value = modelRow[prop];
            } else if(ObjectHelper.isDefined(modelRow) && typeof modelRow !== 'object') {
                value = modelRow; // uv-table needs this for single row objects. just use the value from the model
            }
            return value;
        },
        isValidValue: function(value) {

            if (angular.isNumber(value)) {
                return true;
            }

            if (angular.isString(value)) {
                return value.length > 0;
            }

            if (angular.isArray(value)) {
                return value.length > 0;
            }

            if (angular.isDefined(value) && angular.isObject(value)) {
                return true;
            }

            if (value === true || value === false) {
                return true;
            }

            return false;
        },
        getModelProps: _getModelProps,
        replaceType: _replaceType,
        convertToWebcert: _convertToWebcert
    };
} ]);
