angular.module('common').factory('uvUtil', [ '$parse', '$log', function($parse, $log) {
    'use strict';

    return {
        getValue: function(obj, pathExpression) {
            return $parse(pathExpression)(obj);
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
