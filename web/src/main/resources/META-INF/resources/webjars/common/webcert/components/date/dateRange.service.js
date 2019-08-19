/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
 * DateRangeService
 * Provides a date range service to help with validating date ranges.
 *
 * Created by stephenwhite on 25/01/15.
 */
angular.module('common').factory('common.DateRangeService', ['$log', 'common.DateUtilsService',  function($log, dateUtils) {
    'use strict';
    // private vars
    var format = 'YYYY-MM-DD';
    var formatWithoutDashes = 'YYYYMMDD';
    var interval = 1;
    var week = 6;
    var months = 6;

    var _updateDaysBetween = function(from, to){
        if(!from || !to){
            return;
        }
        // calculate days between
        if(to.valid && from.valid){
            return to.moment.diff(from.moment, 'days') + 1;
        } else {
            return 0;
        }
    };

    var _areDatesOutOfRange = function(min, now) {
        if(min === undefined){
            return false;
        }
        if(now === undefined){
            now = moment(0,'HH');
        } else {
            now = moment(now);
        }
        if(min.dateString !== undefined){
            min = min.moment;
        }
        return min.isBefore(now.subtract(7, 'days'));
    };

    var _areDatesPeriodTooLong = function(minMax) {
        if(!minMax || !minMax.min || !minMax.max){
            return false;
        }
        var min, max;
        if(minMax.max.dateString !== undefined){
            max = minMax.max.moment;
        } else {
            max = minMax.max;
        }
        if(minMax.min.dateString !== undefined){
            min = minMax.min.moment;
        } else {
            min = minMax.min;
        }
        var offset = Math.abs(max.diff(min, 'months'));
        var periodTooLong = (offset >= months);
        if(periodTooLong){
            minMax.max.viewValid = false;
            minMax.min.viewValid = false;
        }
        return periodTooLong;
    };

    var _now = function(){
        return moment(0, 'HH');
    };

    //-----------------------------------------------------------------------
    // --- single DateUnit
    var DateUnit = function(dateString, fromTo, name){
        if(fromTo){
            this.fromTo = fromTo;
        } else {
            this.fromTo = undefined;
        }
        this.dateString = undefined;
        this.dirty = false;
        this.moment = undefined;
        this.valid = false;
        this.viewValid = true;
        this.longTime = undefined;
        this.form = undefined;
        this.name = name;
        if(dateString){
            this.update(dateString);
            this.dirty = false;
        }
    };

    var createAndValidateMomentOf = function createAndValidateMomentOf( that, dateString ) {
        // first check if dateString is in fact ... a date or a moment
        if(dateString instanceof Date || dateString.format instanceof Function){
            that.moment = dateUtils.toMomentStrict(dateString);
            dateString = that.moment.format(format);
        }
        // before we even create the moment we must be sure that the date is in the correct format YYYY-MM-DD
        if(dateUtils.dateReg.test(dateString)){
            that.dateString = dateString;
            that.moment = moment(dateString, format, true);
            that.valid = that.moment.isValid();
        } else if(dateUtils.dateRegDashesOptional.test(dateString)){
            that.moment = moment(dateString.replace(/-/g, ''), formatWithoutDashes, true);
            that.dateString = that.moment.format(format);
            that.valid = that.moment.isValid();
        } else {
            that.valid = false;
            that.dateString = dateString;
        }

        if(that.valid){
            that.momentString = that.moment.format(format);
            that.longTime = that.moment.valueOf();
        } else {
            that.momentString = 'invalid';
            that.longTime = 0;
            that.viewValid = false;
            that.moment = null;
        }
    };

    DateUnit.prototype.update = function( dateString ){
        var oldDateString = this.dateString;
        this.outOfRange = false;
        if(dateString === this.dateString){
            this.dirty = false;
            return;
        }
        if(dateString === null || dateString === undefined || dateString.length === 0){
            this.dateString = undefined;
            this.moment = undefined;
            this.valid = false;
            this.empty = true;
        } else {
            this.empty = false;
            createAndValidateMomentOf( this, dateString );
        }

        this.dirty = (this.dateString !== oldDateString) ? true : false;

        if(this.valid){
            this.outOfRange = _areDatesOutOfRange(this.moment);
        }

        if(this.fromTo){
            this.fromTo.updateOutOfRange(this.outOfRange);
            this.fromTo.updateDaysBetween();
            this.fromTo.updateModel();
        }
    };

    DateUnit.prototype.setValidity = function(){
        if(!this.fromTo){
            this.viewValid = true;
            return;
        }
        if(this.fromTo.isEmpty() ){ // if dates are undefined then show no validation errors
            this.viewValid = true;
        } else {
            if (!this.fromTo.valid || !this.valid) {
                this.viewValid = false;
            } else {
                if (this.fromTo.overlap) {
                    this.viewValid = false;
                } else {
                    this.viewValid = true;
                }
            }
            if (this.fromTo.parent && this.datesPeriodTooLong) {
                this.fromTo.parent.minMax.min.viewValid = false;
                this.fromTo.parent.max.viewValid = false;
            }
        }
    };

    DateUnit.prototype.isEmpty = function(){
        return this.dateString === undefined;
    };

    DateUnit.build = function( dateString, fromTo, name ){
        return new DateUnit(dateString, fromTo, name);
    };

    //-----------------------------------------------------------------------
    // --- FromTo
    var FromTo = function(name, startDate){
        if(name === undefined){
            return;
        }
        this.name = name;
        this.fromTos = undefined;
        this.overlap = false;
        this.valid = false;
        this.workState = false;
        this.isValid = false;
        this.outOfRange = false;
        this.createToFrom(startDate);
    };

    /**
     * This will get called on user interaction and also on datepicker input.
     * The date picker will provide a utc formatted date so we can moment it and then get a formatted string, which
     * we can then pass into the dateunit.
     * @param formElement
     * @param dateUnit
     * @private
     */
    FromTo.prototype._addParser = function(formElement, dateUnit, name, scope){

        if(formElement && dateUnit){
            formElement.$parsers.unshift(function(modelValue){
                // here we should always go with the view value
                // the date directive will convert the model value into a date object
                // although we need to apply our own validation ...

                var formValue = formElement.$viewValue;
                // convert datepicker date

                // utc Thu Aug 13 2015 00:00:00 GMT+0200 (CEST)
                if(formValue instanceof Date){
                    // then the date is from the date picker
                    var utcm = moment(formValue);
                    // format the date to a YYYY-MM-DD
                    formValue = utcm.format(format);
                }
                dateUnit.update(formValue); // will trigger a fromTo.updateModel
                if(dateUnit.valid) {
                    formElement.$setValidity('date', true);
                } else {
                    if (!formValue) {
                        formElement.$setValidity('date', true);
                        return null;
                    }
                    else {
                        formElement.$setValidity('date', false);
                    }
                }
                return formValue;
            });

            formElement.$formatters = [];
            formElement.$formatters.push(function(value){
                return value;
            });

        } else {
            $log.info('formElement or dateUnit not available. cannot add parser or modelupdate.');
        }
    };

    FromTo.prototype.updateModel = function(){
        if(!this.parent || !this.parent.model){
            return;
        }

        if(this.from.dateString === undefined && this.to.dateString === undefined){
            delete this.parent.model[this.name];
            return;
        }
        if(!this.parent.model[this.name]) {
            this.parent.model[this.name] = {from:undefined, tom:undefined};
        }

        this.parent.model[this.name].from = this.from.dateString;
        this.parent.model[this.name].tom = this.to.dateString;
    };

    FromTo.prototype.createToFrom = function(startDate){
        if(!startDate){
            startDate = _now();
        }
        if(startDate === -1){

            this.from = DateUnit.build(undefined, this, this.name+'from');
            this.to = DateUnit.build(undefined, this, this.name+'tom');

        } else {

            this.from = DateUnit.build(undefined, this, this.name+'from');
            this.to = DateUnit.build(undefined, this, this.name+'tom');

            this.from.update(startDate);
            this.to.update(this.sevenDaysAhead(startDate));
            this.updateDaysBetween();

        }
    };

    FromTo.prototype.sevenDaysAhead = function( fromDate ){
        var sevenDaysAhead = moment(fromDate);
        sevenDaysAhead.add(week, 'days');
        return sevenDaysAhead;
    };

    FromTo.prototype.check = function( val ){

        if(!this.parent){
            return;
        }
        if(val !== undefined){
            this.workState = val;
        }
        if(this.workState) {
            var minMax = this.parent.minMax;
            if (!minMax || !minMax.min) {
                var now = _now();
                // INTYG-2992: Only allow updating datevalue if the field is empty
                this.update({
                    from: this.from.dateString ? this.from.dateString : now,
                    to:   this.to.dateString   ? this.to.dateString   : this.sevenDaysAhead(now)
                });
            } else {
                var nextStartDate = moment(minMax.max.moment).add(interval, 'days');
                this.update({
                    from: this.from.dateString ? this.from.dateString : nextStartDate,
                    to:   this.to.dateString   ? this.to.dateString   : this.sevenDaysAhead(nextStartDate)
                });
            }
        } else {
            this.update(undefined);
        }
    };

    FromTo.prototype.update = function( fromTo ){
        this.outOfRange = false;
        if(this.parent){
            this.parent.datesOutOfRange = false;
        }
        if(fromTo){
            this.to.update(fromTo.to);
            this.from.update(fromTo.from);
        } else {
            this.to.update(undefined);
            this.from.update(undefined);
        }
    };

    // this is where the cascading updates happen
    FromTo.prototype.updateDaysBetween = function( ){
        this.overlap = false;
        this.daysBetween = _updateDaysBetween(this.from, this.to);
        // set validity
        if(this.from && this.to){
            this.valid = this.from.valid && this.to.valid;
        }
        if (this.valid && this.from.moment.isAfter(this.to.moment)) {
            this.valid = false;
        }

        // Set to checked if any string is entered
        if (this.from.dateString && this.to.dateString) {
            this.workState = true;
        } else {
            this.workState = false;
        }

        if(this.parent) {
            this.parent.checkOverlaps();
            this.parent.rangeCheck();
            this.isValid = this.overlap;
            this.parent.setValidity();
        }
    };

    FromTo.prototype.updateOutOfRange = function(){
        if(this.to && this.from){
            this.outOfRange = this.to.outOfRange || this.from.outOfRange;
        } else if(this.to && !this.from){
            this.outOfRange = this.to.outOfRange;
        } else if(this.from && !this.to){
            this.outOfRange = this.from.outOfRange;
        }
    };

    FromTo.prototype.isEmpty = function(){
        return (!this.to && !this.from) || (this.to.isEmpty() && this.from.isEmpty());
    };

    FromTo.build = function(name, startDate){
        return new FromTo(name, startDate);
    };

    //-----------------------------------------------------------------------
    // --- FromTos
    var FromTos = function(names, startDate){
        this.minToMaxCount = 0;
        this.valid = false;
        this.minMax = {min:undefined, max:undefined};
        this.dateRanges = [];
        this.totalCertDays = 0;
        this.minDateOutOfRange = false;
        if(!startDate){
            startDate = -1;
        }
        // Check that the earliest startdate in arbetsförmåga is no more than a week before today and no more than 6 months in the future
        this.datesOutOfRange = false;

        // Check that the period between the earliest startdate and the latest end date is no more than 6 months in the future
        this.datesPeriodTooLong = false;

        this.names = names;

        if(names && names.length !== 0){
            for(var i = 0; i<names.length; i++){
                var name = names[i];
                var fromTo = FromTo.build(name, startDate);
                this.dateRanges.push(fromTo);
                fromTo.parent = this;
                this[name] = fromTo;
            }
        }
    };

    FromTos.prototype.linkFormAndModel = function(form, model, scope){

        if(!this.names){
            return;
        }
        this.model = model;
        for(var i=0; i<this.names.length;i++){
            var name = this.names[i];
            var fromTo = this[name];
            var formFrom = form[name+'from'];
            var formTo = form[name+'tom'];
            fromTo.from.form = formFrom;
            fromTo.to.form = formTo;
            fromTo._addParser(formFrom, fromTo.from, 'from', scope);
            fromTo._addParser(formTo, fromTo.to, 'tom', scope);

            // set the initial values
            if(model[name]){
                fromTo.update({from:model[name].from,to:model[name].tom});
            }
        }
    };

    FromTos.prototype.rangeCheck = function(){
        this.datesPeriodTooLong = false;
        this.updateMinMax();
        this.totalCertDays = 0;
        this.datesOutOfRange = false;
        for(var i=0; i<this.dateRanges.length; i++) {
            var dateRange = this.dateRanges[i];
            if(dateRange.daysBetween !== undefined && !dateRange.overlap) {
                this.totalCertDays += dateRange.daysBetween;
            }
            // work out if dates are out of range
            if(!this.datesOutOfRange) {
                this.datesOutOfRange = dateRange.outOfRange;
            }
        }
        if(this.totalCertDays < 0){
            this.totalCertDays = 0;
        }
        this.datesPeriodTooLong = _areDatesPeriodTooLong(this.minMax);
    };

    /**
     * -------|      |------
     * -------|      |------
     */
    function isToFromDatesSame(dateRange, dateRange2) {
        var sameTo = dateRange.to.moment.isSame(dateRange2.to.moment);
        var sameFrom = dateRange.from.moment.isSame(dateRange2.from.moment);
        var sameFromTo = dateRange.to.moment.isSame(dateRange2.from.moment);
        var sameToFrom = dateRange.from.moment.isSame(dateRange2.to.moment);
        return (sameTo || sameFrom || sameFromTo || sameToFrom);
    }

    /**
     * -------|      |------
     * ----|      |------
     */
    function isPeriodsOverlappingFrom(dateRange, dateRange2) {
        return dateRange2.from.moment.isBefore(dateRange.from.moment) &&
            (dateRange2.to.moment.isBefore(dateRange.to.moment) &&
                dateRange2.to.moment.isAfter(dateRange.from.moment));
    }

    /**
     * -------|      |------
     * ----------|      ...|------
     */
    function isPeriodFromWithin(dateRange, dateRange2) {
        return dateRange2.from.moment.isAfter(dateRange.from.moment) &&
            dateRange2.from.moment.isBefore(dateRange.to.moment);
    }

    /**
     * -------|      |------
     * ----|            |------
     */
    function isPeriodContaining(dateRange, dateRange2) {
        return dateRange2.from.moment.isBefore(dateRange.from.moment) &&
            dateRange2.to.moment.isAfter(dateRange.to.moment);
    }

    var hasOverlap = function(dateRange, dateRange2) {
        return isToFromDatesSame(dateRange, dateRange2) ||
            isPeriodsOverlappingFrom(dateRange, dateRange2) ||
            isPeriodFromWithin(dateRange, dateRange2) ||
            isPeriodContaining(dateRange, dateRange2);
    };

    function updateOverlaps(dateRange, dateRange2) {
        if(dateRange.name !== dateRange2.name && dateRange2.valid){
            var overlap = hasOverlap(dateRange, dateRange2);
            if(!dateRange.overlap){
                dateRange.overlap = overlap;
            }
            if(!dateRange2.overlap) {
                dateRange2.overlap = overlap;
            }
        }
    }

    // ------|    |------- start
    // ----|    |--------- 1
    // ---------|    |---- 2
    // ----|         |---- 3
    FromTos.prototype.checkOverlaps = function(){
        this.clearOverlaps();
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            if(dateRange.valid){
                for(var j = i + 1; j < this.dateRanges.length; j++){
                    var dateRange2 = this.dateRanges[j];
                    updateOverlaps(dateRange, dateRange2);
                }
            }
        }
    };

    FromTos.prototype.setValidity = function() {
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            // set validity
            dateRange.to.setValidity();
            dateRange.from.setValidity();
        }
    };

    FromTos.prototype.updateMinMax = function(newMinMax){
        var validDateUnits = [];

        if (newMinMax) {
            if (newMinMax.min) {
                this.minMax.min = new DateUnit(newMinMax.min, null, 'manualMinMaxMin');
            } else {
                throw new Error('DateRangeService#updateMinMax: expected "newMinMax" parameter to have "min" property but it was falsy.');
            }
            if (newMinMax.max) {
                this.minMax.max = new DateUnit(newMinMax.max, null, 'manualMinMaxMax');
            } else {
                this.minMax.max = new DateUnit(newMinMax.min, null, 'manualMinMaxMax');
            }
            return;
        }

        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            if(dateRange.from && dateRange.from.valid){
                validDateUnits.push(dateRange.from);
            }
            if(dateRange.to && dateRange.to.valid){
                validDateUnits.push(dateRange.to);
            }
        }

        if(validDateUnits !== undefined && validDateUnits.length > 0){
            validDateUnits = validDateUnits.sort(function(a,b){
                return a.longTime - b.longTime;
            });
            this.minMax.min = validDateUnits[0];
            this.minMax.max = validDateUnits[validDateUnits.length-1];
        } else {
            this.minMax.min = undefined;
            this.minMax.max = undefined;
        }
    };

    FromTos.prototype.clearOverlaps = function(){
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            dateRange.overlap = false;
        }
    };

    FromTos.build = function(names, startDate){
        return new FromTos(names, startDate);
    };

    // --- service methods
    return {
        DateUnit : DateUnit,
        FromTo : FromTo,
        FromTos : FromTos
    };

}]);
