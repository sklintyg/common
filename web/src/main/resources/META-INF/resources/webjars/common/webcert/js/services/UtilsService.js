/**
 * UtilsService
 * Provides basic utility methods for strings.
 *
 * Created by stephenwhite on 25/01/15.
 */
angular.module('common').factory('common.UtilsService',function() {
    'use strict';

    /**
     * Check if variable is undefined, null, NaN or an empty string = invalid
     * @param data
     * @returns {boolean}
     */
    function _isValidString(data) {
        // data !== data from underscore.js: 'NaN' is the only value for which '===' is not reflexive.
        return (data !== undefined && data !== null && data === data && data !== '');
    }

    return {
        isValidString: _isValidString
    };

});
