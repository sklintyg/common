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
    var interval = 1;
    var week = 6;

    var _updateDaysBetween = function(from, to){
            // calculate days between
            if(to.valid && from.valid){
                //console.log('-- min : ' + from.dateString + ', max:' + to.dateString);
                var db = to.moment.diff(from.moment, 'days') + 1;
                //console.log('-- db ' + db);
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

    // ------|    |------- start
    // ----|    |--------- 1
    // ---------|    |---- 2
    // ----|         |---- 3
    FromTos.prototype.checkOverlaps = function(){
        this.clearOverlaps();
        console.log('----------- check overlaps');
        console.log('------|    |-------');
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            if(dateRange.valid && !dateRange.overlap){
                console.log('-- dr1 ' + dateRange.name + ', from:' + dateRange.from.dateString + ', to:' + dateRange.to.dateString );
                for(var j=0; j<this.dateRanges.length; j++){
                    var dateRange2 = this.dateRanges[j];
                    if(dateRange.name !== dateRange2.name && dateRange2.valid && !dateRange.overlap){
                        console.log('-- dr2 ' + dateRange2.name + ', from:' + dateRange2.from.dateString+ ', to:' + dateRange2.to.dateString);
                        var overlap = false;
                        var sameTo = dateRange.to.moment.isSame(dateRange2.to.moment);
                        var sameFrom = dateRange.from.moment.isSame(dateRange2.from.moment);
                          //console.log('------|    |-------');
                        if(sameTo || sameFrom){
                            overlap = true;
                        } else if(dateRange2.from.moment.isBefore(dateRange.from.moment) &&
                            (dateRange2.to.moment.isBefore(dateRange.to.moment) &&
                            dateRange2.to.moment.isAfter(dateRange.from.moment) )
                        ){
                            console.log('----|    |--------- 1');
                            overlap = true;
                        } else if(dateRange2.from.moment.isAfter(dateRange.from.moment) &&
                            dateRange2.from.moment.isBefore(dateRange.to.moment)){
                            console.log('--------|    |----- 2');
                            overlap = true;
                        } else if(dateRange2.from.moment.isBefore(dateRange.from.moment) &&
                            dateRange2.to.moment.isAfter(dateRange.to.moment) ){
                            console.log('----|            |- 3');
                            overlap = true;
                        }

                        dateRange.overlap = overlap;
                        dateRange2.overlap = overlap;

                    }
                }
            }
        }
        // clear overlaps
        //for(var i=0; i<this.dateRanges.length; i++) {
        //    var dateRange = this.dateRanges[i];
        //    dateRange.overlapChecked = false;
        //}

    };

    FromTos.prototype.updateMinMax = function(){
        //console.log('---------------- updateMinMax');
        var validDateUnits = [];
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            if(dateRange.valid && !dateRange.overlap){
                validDateUnits.push(dateRange.from);
                validDateUnits.push(dateRange.to);
            }
        }

        if(validDateUnits !== undefined && validDateUnits.length > 0){
            //console.log('-- before');
            for(i=0; i<validDateUnits.length; i++){
                var du = validDateUnits[i];
                //console.log('-- du : ' + du.dateString);
            }

            validDateUnits = validDateUnits.sort(function(a,b){
                //console.log('-- sort du a: ' + a.dateString);
                //console.log('-- sort du b: ' + b.dateString);
                return a.longTime - b.longTime;
            });

            //console.log('-- after');
            for(i=0; i<validDateUnits.length; i++){
                var du = validDateUnits[i];
                //console.log('-- du : ' + du.dateString);
            }

            this.minMax.min = validDateUnits[0];
            this.minMax.max = validDateUnits[validDateUnits.length-1];
        } else {
            this.minMax.min = undefined;
            this.minMax.max = undefined;
        }
    };

    FromTos.prototype.clearOverlaps = function(){
        console.log('----------- clear overlaps');
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            dateRange.overlap = false;
        }
    }

    FromTos.build = function(names){
        return new FromTos(names);
    };

    // --- FromTo
    var FromTo = function(name, startDate){
        if(name === undefined){
            return;
        }
        this.name = name;
        this.fromTos = undefined;
        this.overlap = false;
        this.valid = false;
        this.createToFrom(startDate);

    };

    FromTo.prototype.createToFrom = function(startDate){
        if(!startDate){
            startDate = moment(0, 'HH');
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
        sevenDaysAhead.add(week, 'days');
        return sevenDaysAhead;
    };

    FromTo.prototype.check = function( value ){

        if(!this.parent){
            return;
        }

        if(value) {
            var minMax = this.parent.minMax;
            if (!minMax || !minMax.min) {
                this.createToFrom();
            } else {
                var nextStartDate = moment(minMax.max.moment).add(interval, 'days');
                this.createToFrom(nextStartDate);
            }
        } else {
            this.update(undefined);
        }
    };

    FromTo.prototype.update = function( fromTo ){
        if(fromTo){
            this.to.update(fromTo.to);
            this.from.update(fromTo.from);
        } else {
            this.to.update(fromTo);
            this.from.update(fromTo);
        }
        this.updateDaysBetween();
    };

    // this is where the cascading updates happen
    FromTo.prototype.updateDaysBetween = function( ){
        this.overlap = false;
        this.daysBetween = _updateDaysBetween(this.from, this.to);
        // set validity
        this.valid = this.from.valid && this.to.valid;
        if(this.valid) {
            // check that to is after from
            if (this.from.moment.isAfter(this.to.moment)) {
                this.valid = false;
            }
        }
        if(this.parent) {
            //console.log('has parent');
            if(this.valid){
                this.parent.checkOverlaps();
            }
            this.parent.updateDaysBetween();
        }
    };

    FromTo.build = function(name, startDate){
        return new FromTo(name, startDate);
    }

    // --- single DateUnit
    var DateUnit = function(dateString){
        this.fromTo = undefined;
        this.dateString = undefined;
        this.dirty = false;
        this.moment = undefined;
        this.valid = false;
        this.longTime = undefined;
        if(dateString){
            this.update(dateString);
            this.dirty = false;
        }
    };

    DateUnit.prototype.update = function( dateString ){
        if(dateString === this.dateString){
            this.dirty = false;
            return;
        }

        if(dateString === undefined || dateString.length === 0){
            this.dateString = undefined;
            this.moment = undefined;
            this.dirty = true;
            this.valid = false;
        } else {
            if(dateString.format !== undefined){
                // must be a moment
                this.dateString = dateString.format(format);
                this.moment = dateString;
            } else {
                this.dateString = dateString;
                this.moment = moment(dateString, format);
            }

            this.dirty = true;
            this.valid = this.moment.isValid();
            if(this.valid){
                this.momentString = this.moment.format(format);
                this.longTime = this.moment.valueOf();
            } else {
                this.momentString = 'invalid';
            }
        }

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
