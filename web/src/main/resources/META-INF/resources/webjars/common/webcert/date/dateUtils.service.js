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
 * DateUtilsService
 * Provides dateutils for common date formatting.
 *
 * Created by stephenwhite on 25/01/15.
 */
angular.module('common').factory('common.DateUtilsService', function() {
    'use strict';
    var _dateReg = /[1-2][0-9]{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])/;
    var _format = 'YYYY-MM-DD';
   /**
     * Does supplied date look like an iso date XXXX-XX-XX
     * @param date
     * @returns {*}
     */
    function _isDate(date) {
        if (date === undefined || date === null) {
            return false;
        }

        return moment(date, _format, true).isValid();
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

    function _toMomentStrict(date) {
        if (date) {
            return moment(date, _format, true);
        } else {
            return null;
        }
    }

    function _todayAsYYYYMMDD(){
        return moment(new Date()).format(_format);
    }

    /**
     * Convert Date object dates to ISO dates.
     * @param viewValue
     * @returns {*}
     */
    function _convertDateToISOString(viewValue, format) {
        if(format === undefined){
            format = _format;
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

        var momentDate = _toMoment(date);
        if (momentDate !== null) {
            // Format date strictly to _format.
            momentDate = moment(momentDate.format(_format), _format, true).format(_format);
            if (momentDate === 'invalid date') {
                // We don't want to handle invalid dates at all
                momentDate = null;
            } else {
                momentDate = _toMoment(momentDate);
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
            var momentDate = _toMoment(dateValue);
            if (momentDate !== null && momentDate.isValid()) {
                var formattedDate = moment(momentDate.format(_format), _format, true);
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
    }

    function _isAfterOrEqual(moment1, moment2) {
        return moment1.isAfter(moment2) || moment1.isSame(moment2, 'day');
    }

    function _isSame(moment1, moment2) {
        if(moment1 && moment2 ){
            return moment1.isSame(moment2, 'day');
        } else {
            return false;
        }

    }

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
        return endMoment.diff(startMoment, 'days');
    }

    function _addDateParserFormatter(formElement) {

        function parseDateLoose(viewValue) {
            viewValue = _convertDateToISOString(viewValue);
            if (_isDate(viewValue)) {
                formElement.$setValidity('date', true);
            }
            else if ((/[0-9]{8}/).test(viewValue)) {
                // Allow date input without dashes
                var checkDate = moment(viewValue, 'YYYYMMDD');
                if (checkDate.isValid()) {
                    viewValue = checkDate.format(_format);
                    formElement.$setValidity('date', true);
                    formElement.$setViewValue(viewValue);
                    formElement.$render();
                    return viewValue;
                }
            }
            else {
                formElement.$setValidity('date', false);
                return viewValue;
            }

            return viewValue;
        }

        // Replace parsers with only this one
        formElement.$parsers = [parseDateLoose];

        formElement.$formatters = [function (modelValue) {
            if (modelValue) {
                // convert date to iso
                modelValue = _convertDateToISOString(modelValue);
            }
            return modelValue;
        }];
    }

    function _addStrictDateParser(formElement) {

        function parseDateStrict(viewValue) {

            viewValue = _convertDateToISOString(viewValue);

            if (!viewValue) {
                formElement.$setValidity('date', true);
                return null;
            }
            else if (_isDate(viewValue)) {
                formElement.$setValidity('date', true);
                return viewValue;
            }
            else if ((/[0-9]{8}/).test(viewValue)) {
                // Allow date input without dashes
                var checkDate = moment(viewValue, 'YYYYMMDD');
                if (checkDate.isValid()) {
                    viewValue = checkDate.format(_format);
                    formElement.$setValidity('date', true);
                    formElement.$setViewValue(viewValue);
                    formElement.$render();
                    return viewValue;
                }
            }
            else {
                formElement.$setValidity('date', false);
                return undefined;
            }
        }

        // Replace parsers with only this one
        formElement.$parsers = [parseDateStrict];
    }

    return {
        isDate: _isDate,
        toMoment: _toMoment,
        toMomentStrict: _toMomentStrict,
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
        addDateParserFormatter : _addDateParserFormatter,
        addStrictDateParser : _addStrictDateParser,
        dateReg : _dateReg
    };

});
