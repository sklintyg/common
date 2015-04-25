/**
 * DateRangeService
 * Provides a date range service to help with validating date ranges.
 *
 * Created by stephenwhite on 25/01/15.
 */
angular.module('common').factory('common.DateRangeService', function() {
    'use strict';
    // private vars
    var format = 'YYYY-MM-DD';

    var _updateDaysBetween = function(from, to){
            // calculate days between
            if(to.valid && from.valid){
                return to.moment.diff(from.moment, 'days') + 1;
            } else {
                return 0;
            }
    };

    // --- FromTos
    var FromTos = function(names){
        this.minToMaxCount = 0;
        this.valid = false;
        this.minMax = {min:undefined, max:undefined};
        this.dateRanges = [];
        if(names && names.length !== 0){
            for(var i = 0; i<names.length; i++){
                var name = names[i];
                var fromTo = FromTo.build(name, -1);
                this.dateRanges.push(fromTo);
                fromTo.parent = this;
                this[name] = fromTo;
            }
        }
    };

    FromTos.prototype.updateDaysBetween = function(){
        this.updateMinMax();
        if(this.minMax.min !== undefined){
            this.daysBetween = _updateDaysBetween(this.minMax.min, this.minMax.max);
        } else {
            this.daysBetween = 0;
        }

    };

    FromTos.prototype.updateMinMax = function(){

        var validDateUnits = [];
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            if(dateRange.valid){
                validDateUnits.push(dateRange.from);
                validDateUnits.push(dateRange.to);
            }
        }

        if(validDateUnits !== undefined && validDateUnits.length > 0){
            validDateUnits.sort(function(a,b){
                return a.moment.isBefore(b.moment);
            });

            this.minMax.min = validDateUnits[0];
            this.minMax.max = validDateUnits[1];
        } else {
            this.minMax.min = undefined;
            this.minMax.max = undefined;
        }
    };

    FromTos.build = function(names){
        return new FromTos(names);
    };

    // --- FromTo
    var FromTo = function(name, startDate){
        console.log('startdate : ' + startDate);
        if(name === undefined){
            return;
        }
        this.name = name;
        this.fromTos = undefined;

        this.createToFrom(startDate);

    };

    FromTo.prototype.createToFrom = function(startDate){
        if(!startDate){
            startDate = moment();
        }
        if(startDate === -1){
            this.from = DateUnit.build();
            this.from.fromTo = this;
            this.to = DateUnit.build();
            this.to.fromTo = this;
        } else {
            this.from = DateUnit.build(startDate);
            this.from.fromTo = this;
            this.to = DateUnit.build(this.createTo(startDate));
            this.to.fromTo = this;
            this.updateDaysBetween();
        }
    };

    FromTo.prototype.createTo = function( fromDate ){
        var sevenDaysAhead = moment(fromDate);
        sevenDaysAhead.add(7, 'days');
        return sevenDaysAhead;
    };

    FromTo.prototype.check = function( value ){
        if(!this.parent){
            return;
        }
        // get the latest date
        var minMax = this.parent.minMax;
        // add one day
        if(!minMax || !minMax.min){
            this.createToFrom();
        }
        // create a to date one week in the future
    };

    FromTo.prototype.update = function( fromTo ){
        this.to.update(fromTo.to);
        this.from.update(fromTo.from);
        this.updateDaysBetween();
    };

    // this is where the cascading updates happen
    FromTo.prototype.updateDaysBetween = function( ){
        this.daysBetween = _updateDaysBetween(this.from, this.to);
        // set validity
        this.valid = this.from.valid && this.to.valid;
        if(this.parent) {
            this.parent.updateDaysBetween();
        }
    };

    FromTo.build = function(name, startDate){
        console.log('------- building fromto')
        return new FromTo(name, startDate);
    }

    // --- single DateUnit
    var DateUnit = function(dateString){
        this.fromTo = undefined;
        this.dateString = undefined;
        this.overlap = false;
        this.dirty = false;
        this.moment = undefined;
        this.valid = false;
        if(dateString){
            this.update(dateString);
            this.dirty = false;
        }
    };

    DateUnit.prototype.update = function( dateString ){
        if(dateString === undefined || dateString.length === 0 || dateString === this.dateString){
            return;
        }

        if(dateString.format !== undefined){
            // must be a moment
            this.dateString = dateString.format(format);
            this.moment = dateString;
        } else {
            this.dateString = dateString;
            this.moment = moment(dateString, format);
        }

        this.overlap = false;
        this.dirty = true;
        this.valid = this.moment.isValid();
        if(this.valid){
            this.momentString = this.moment.format(format);
        } else {
            this.momentString = 'invalid';
        }
        console.log('---- update date unit : ' + this.dateString);
        console.log('-- valid ' + this.valid);
        if(this.fromTo){
            this.fromTo.updateDaysBetween();
        }
    };

    DateUnit.build = function( dateString ){
        return new DateUnit(dateString);
    }

    // --- service methods
    function _validateDateRanges(dates) {
        var dateRange = new Range(dates);
        return dateRange;
    }

    return {
        validateDateRanges: _validateDateRanges,
        DateUnit : DateUnit,
        FromTo : FromTo,
        FromTos : FromTos
    };

});
