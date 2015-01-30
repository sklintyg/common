/**
 * DateUtilsService
 * Provides dateutils for common date formatting.
 *
 * Created by stephenwhite on 25/01/15.
 */
angular.module('common').factory('common.DateUtilsService', function() {
    'use strict';

   /**
     * Does supplied date look like an iso date XXXX-XX-XX
     * @param date
     * @returns {*}
     */
    function _isDate(date) {
        if (date === undefined || date === null) {
            return false;
        }

        return moment(date, 'YYYY-MM-DD', true).isValid();
    }

    /**
     * Convert date to a moment date
     * @param date
     * @returns {*}
     */
    function _toMoment(date) {
        if (date) {
            return moment(date);
        } else {
            return null;
        }
    }

    return {
        isDate: _isDate,
        toMoment: _toMoment
    };

});
