angular.module('common').factory('common.ObjectHelper',
    function() {
        'use strict';

        return {
            isDefined: function(value) {
                return value !== null && typeof value !== 'undefined';
            },
            isEmpty: function(value) {
                return value === null || typeof value === 'undefined' || value === '';
            },
            isModelValue: function(value) {
                if (angular.isUndefined(value)) {
                    return false;
                }

                if (angular.isString(value)) {
                    return value.length > 0;
                }

                if (angular.isArray(value)) {
                    return value.length > 0;
                }
                if (angular.isObject(value)) {
                    return true;
                }

                return !!value;
            }
        };
    }
);
