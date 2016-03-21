/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        return (typeof(data) !== 'undefined' && data !== null );
    }

    /**
     * Helper function used in an Angular watch callback to check if Anguar
     * has resetted the value.
     *
     * Angular doesn't like having $viewValue of a model as undefined.
     * It will set the $viewValue to the same value of the input node on
     * every "change" event. Since many of our models are initialized as
     * undefined this check is needed to not unnecessarily update our models.
     *
     * @param   {String} newVal New value from watch function
     * @param   {String} oldVal Old value from watch function
     * @returns {Boolean} Is reset by Angular change event
     */
    function _isResetByAngular (newVal, oldVal) {
        return (newVal === '' && oldVal === undefined);
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
        isDefined: _isDefined,
        isResetByAngular: _isResetByAngular,
        replaceAccentedCharacters: _replaceAccentedCharacters
    };

});
