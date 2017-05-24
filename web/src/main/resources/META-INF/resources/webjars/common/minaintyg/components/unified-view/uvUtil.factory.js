angular.module('common').factory('uvUtil', [
    '$parse', '$log', 'common.dynamicLabelService', 'common.messageService',
    function($parse, $log, dynamicLabelService, messageService) {
    'use strict';

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
                    var staticLabel = messageService.getProperty(value);
                    return staticLabel;
                } else {
                    return value;
                }
            }
        },
        getValue: function(obj, pathExpression) {
            return $parse(pathExpression)(obj);
        },
        resolveValue: function(prop, modelRow, colProp, rowIndex, colIndex){
            var value = null;
            if(typeof prop === 'function'){
                // Resolve using function
                value = prop(modelRow, rowIndex, colIndex, colProp);
            } else if(prop.indexOf('.') !== -1) {
                // Resolve dot-path
                /*value = prop.split('.').reduce(function index(obj, value) {
                 return obj[value];
                 }, modelRow);*/
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

        }
    };
} ]);
