angular.module('common').factory('uvUtil', [ '$parse', function($parse) {
    'use strict';

    return {
        getValue: function(obj, pathExpression) {
            return $parse(pathExpression)(obj);
        }
    };
} ]);
