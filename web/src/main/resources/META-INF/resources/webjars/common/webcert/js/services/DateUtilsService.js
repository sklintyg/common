/**
 * DateUtilsService
 * Provides dateutils for common date formatting.
 *
 * Created by stephenwhite on 25/01/15.
 */
angular.module('common').factory('common.DateUtilsService', function($filter) {
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

    function _todayAsYYYYMMDD(){
        return moment(new Date()).format('YYYY-MM-DD');
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

    /**
     * Convert date from string to a strict moment date
     * @param date
     * @returns {*}
     */
    function _convertDateStrict(date) {

        if (typeof date === 'string' && date.length < 10) {
            return null;
        }

        var momentDate = this.toMoment(date);
        if (momentDate !== null) {
            // Format date strictly to 'YYYY-MM-DD'.
            momentDate = moment(momentDate.format('YYYY-MM-DD'), 'YYYY-MM-DD', true).format('YYYY-MM-DD');
            if (momentDate === 'invalid date') {
                // We don't want to handle invalid dates at all
                momentDate = null;
            } else {
                momentDate = this.toMoment(momentDate);
            }
        }

        return momentDate;
    }

    /**
     * adds a valid iso formatted date to the provided list
     * @param list
     * @param dateValue
     * @private
     */
    function _pushValidDate(list, dateValue) {
        if ((typeof dateValue === 'string' && dateValue.length === 10) || dateValue instanceof Date) {
            var momentDate = this.toMoment(dateValue);
            if (momentDate !== null && momentDate.isValid()) {
                var formattedDate = moment(momentDate.format('YYYY-MM-DD'), 'YYYY-MM-DD', true);
                if (formattedDate.isValid()) {
                    list.push(formattedDate);
                }
            }
        }
    }

    /**
     * Checks to see if the date is older than a week.
     * @param startMoment
     * @returns {*}
     * @private
     */
    function _olderThanAWeek( startMoment ){
        if( startMoment === null ){
            return;
        }
        var now = moment();
        return startMoment.isBefore(now.subtract('days', 7));
    }

    /**
     * Checks to see if the startDate is out of range.
     * If months is not provided it defaults to 6.
     * @param startMoment
     * @param months
     * @returns {*}
     * @private
     */
    function _isDateOutOfRange(startMoment, months){
        if (startMoment === null) {
            return;
        }
        if(months === undefined){
            months = 6;
        }

        var now = moment();
        return startMoment.isAfter(now.add('months', months));
    }

    function _isBeforeOrEqual(moment1, moment2) {
        return moment1.isBefore(moment2) || moment1.isSame(moment2, 'day');
    };

    function _isAfterOrEqual(moment1, moment2) {
        return moment1.isAfter(moment2) || moment1.isSame(moment2, 'day');
    };

    function _isSame(moment1, moment2) {
        if(moment1 && moment2 ){
            return moment1.isSame(moment2, 'day');
        } else {
            return false;
        }

    };

    /**
     * Checks whether the two dates are within the specific month range.
     * If no month range is provided this will defaul to 6 months.
     * @param startMoment
     * @param endMoment
     * @param months
     * @private
     */
    function _areDatesWithinMonthRange(startMoment, endMoment, months){
        if (startMoment === null || endMoment === null) {
            // Feels weird with true here but the truth is we need to consider this case "within range" since errors will
            // popup in implementations using this saying months aren't in range otherwise.
            return true;
        }
        if(months === undefined){
            months = 6;
        }

        return (Math.abs(startMoment.diff(endMoment, 'months')) < months);
    }

    function _daysBetween(startMoment, endMoment){
        if(!startMoment || !endMoment){
            return false;
        }
        return endMoment.diff(startMoment, 'days') + 1;
    }

    function _addDateParserFormatter(formElement) {
        if (formElement.$parsers.length > 1) {
            formElement.$parsers.shift();
        }

        formElement.$parsers.unshift(function (viewValue) {

            viewValue = _convertDateToISOString(viewValue);

            var transformedInput = viewValue;
            if(_isDate(viewValue)){
                transformedInput = $filter('date')(viewValue, 'yyyy-MM-dd', 'GMT+0100');
            }

            if (transformedInput !== viewValue) {
                formElement.$setViewValue(transformedInput);
                formElement.$render();
            }

            return transformedInput;
        });

        if (formElement.$formatters.length > 0) {
            formElement.$formatters.shift();
        }

        formElement.$formatters.unshift(function (modelValue) {
            if (modelValue) {
                // convert date to iso
                modelValue = _convertDateToISOString(modelValue);
            }
            return modelValue;
        });
    }

    return {
        isDate: _isDate,
        toMoment: _toMoment,
        convertDateToISOString : _convertDateToISOString,
        convertDateStrict : _convertDateStrict,
        pushValidDate : _pushValidDate,
        areDatesWithinMonthRange : _areDatesWithinMonthRange,
        olderThanAWeek :_olderThanAWeek,
        isDateOutOfRange : _isDateOutOfRange,
        daysBetween : _daysBetween,
        isAfterOrEqual : _isAfterOrEqual,
        isBeforeOrEqual : _isBeforeOrEqual,
        isSame : _isSame,
        todayAsYYYYMMDD :_todayAsYYYYMMDD,
        addDateParserFormatter : _addDateParserFormatter
    };

});
