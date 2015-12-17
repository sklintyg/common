/**
 * UtilsService
 * Provides basic utility methods for strings.
 *
 * Created by stephenwhite on 25/01/15.
 */
angular.module('common').factory('common.UtilsService', function() {
    'use strict';

    /**
     * Check if variable is undefined, null, NaN or an empty string = invalid
     * @param data
     * @returns {boolean}
     */
    function _isValidString(data) {
        // from underscore.js: 'NaN' is the only value for which '===' is not reflexive. (referring to data !== data)
        return (data !== undefined && data !== null && data === data && data !== '');
    }

    function _isDefined(data) {
        return (typeof(data) !== 'undefined' && data !== null )
    }

    /**
     * Replaces accented character in swedish to the (closest?) matching ascii variant.
     *
     * @param str
     * @returns {String}
     * @private
     */
    function _replaceAccentedCharacters(str) {
        if (_isValidString(str)) {
            str = str.replace(/å/g, 'a');
            str = str.replace(/Å/g, 'A');
            str = str.replace(/ä/g, 'a');
            str = str.replace(/Ä/g, 'A');
            str = str.replace(/ö/g, 'o');
            str = str.replace(/Ö/g, 'O');
        }
        return str;
    }

    return {
        isValidString: _isValidString,
        isDefined: _isDefined
        replaceAccentedCharacters: _replaceAccentedCharacters
    };

});
