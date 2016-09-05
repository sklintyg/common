angular.module('common').factory('common.ErrorHelper',
    ['common.ObjectHelper', function(ObjectHelper) {
        'use strict';

        return {
            safeGetError: function(errorData) {
                if(ObjectHelper.isDefined(errorData)){
                    return errorData.errorCode;
                } else {
                    return'unknown';
                }
            }
        };
    }]
);
