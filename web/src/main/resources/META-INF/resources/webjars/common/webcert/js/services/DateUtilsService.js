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

    /**
     * Convert Date object dates to ISO dates.
     * @param viewValue
     * @returns {*}
     */
    function _convertDateToISOString(viewValue, format) {
        if(format === undefined){
            format = 'YYYY-MM-DD';
        }
        if (viewValue instanceof Date &&
            moment(moment(viewValue).format(format), format, true).isValid()) {
            viewValue = moment(viewValue).format(format);
        }
        return viewValue;
    }

    return {
        isDate: _isDate,
        toMoment: _toMoment,
        convertDateToISOString : _convertDateToISOString
    };

});
