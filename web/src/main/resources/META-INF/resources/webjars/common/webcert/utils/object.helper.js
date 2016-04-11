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
             * @param  {String} path Path to nested value. Eg: 'x.y.0.z'
             * @return {*} Value at object path
             */
            deepGet: function (obj, path) {
                if (typeof path !== 'string') { return obj; }
                return path.split('.').reduce(function (accum, cur) {
                    return accum[cur];
                }, obj);
            }
        };
    }
);
