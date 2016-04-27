angular.module('common').factory('common.ObjectHelper',
    ['$parse', function($parse) {
        'use strict';


        return {
            isDefined: function(value) {
                return value !== null && typeof value !== 'undefined';
            },
            isEmpty: function(value) {
                return value === null || typeof value === 'undefined' || value === '';
            },
            isFalsy: function(value) {
                return value === null || typeof value === 'undefined' || value === '' || value === 'false' || value === false;
            },
            returnJoinedArrayOrNull: function(value) {
                return value !== null && value !== undefined ? value.join(', ') : null;
            },
            valueOrNull: function(value) {
                return value !== null && value !== undefined ? value : null;
            },
            /**
             * Deep get property from an object
             * @param  {Object} obj  Nested object to get values from
             * @param  {String} path Path to nested value. Eg: 'x.y.[0].z'
             * @return {*} Value at object path
             */
            deepGet: function (obj, path) {
                //Create a getter function for the property path, using angular built-in $parse service to evaluate a nested property
                var getNestedProperty = angular.isString(path) ? $parse(path) : function(item) {
                    return item;
                };
                return getNestedProperty(obj);
            }


        };
    }]
);
