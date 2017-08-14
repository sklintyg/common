angular.module('common').factory('uvUtil', [
    '$parse', '$log', 'common.dynamicLabelService', 'common.messageService',
    function($parse, $log, dynamicLabelService, messageService) {
    'use strict';

        function _isDynamicText(str) {
            return angular.isString(str) && str.search(/{(.*)}/) > -1;
        }

        function _replacer(propertyName, model) {
            function replacer(match, p1, offset, string) {
                return model[p1];
            }
            return propertyName.replace(/{(.*)}/g, replacer);
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

        function _convertToWebcert(viewConfig, skipPatient) {
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

            viewConfig.push({
                type: 'uv-signed-by',
                modelProp: 'grundData'
            });

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
