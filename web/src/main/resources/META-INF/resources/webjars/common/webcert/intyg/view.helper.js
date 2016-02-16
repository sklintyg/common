angular.module('common').factory('common.ViewHelper',
    ['common.ObjectHelper', function(ObjectHelper) {
        'use strict';

        function recurseGetFieldValue(model, field) {

            if(!ObjectHelper.isDefined(field) || !ObjectHelper.isDefined(model[field[0]])) {
                return null;
            }

            var nextFieldName = field.shift();
            if(field.length == 0) {
                return model[nextFieldName];
            }

            return recurseGetFieldValue(model[nextFieldName], field);
        }

        return {
            getNestedModelValue: function(model, key) {
                var fieldPath = key.split('.');
                if(fieldPath.length == 1) {
                    return model[key];
                }
                else if(fieldPath.length > 1) {
                    return recurseGetFieldValue(model, fieldPath);
                }
                return null;
            }
        };
    }]
);
