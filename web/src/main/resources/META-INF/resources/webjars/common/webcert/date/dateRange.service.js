/**
 * DateRangeService
 * Provides a date range service to help with validating date ranges.
 *
 * Created by stephenwhite on 25/01/15.
 */
angular.module('common').factory('common.DateRangeService', function($log) {
    'use strict';
    // private vars
    var format = 'YYYY-MM-DD';
    var interval = 1;
    var week = 6;
    var months = 6;
    var minDaysRange = 6;

    var _updateDaysBetween = function(from, to){
        if(!from || !to){
            return;
        }
        // calculate days between
        if(to.valid && from.valid){
            //console.log('-- min : ' + from.dateString + ', max:' + to.dateString);
            //console.log('-- db ' + db);
            return to.moment.diff(from.moment, 'days') + 1;
        } else {
            return 0;
        }
    };

    var _isMinDateOutOfRange = function(minMax, now) {
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
        if(now === undefined){
            now = moment(0,'HH');
        } else {
            now = moment(now);
        }
        //console.log('**** min : ' + min.format('YYYY-MM-DD') + ', now : ' + now.format('YYYY-MM-DD'));
        return min.isAfter(now.add('days', minDaysRange));
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
        //console.log('**** min : ' + min.format('YYYY-MM-DD') + ', now : ' + now.format('YYYY-MM-DD'));
        return min.isBefore(now.subtract('days', 7));
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
        //console.log('**** offset : ' + offset);
        //console.log('**** min : ' + min.format('YYYY-MM-DD') + ', max : ' + max.format('YYYY-MM-DD'));
        var periodTooLong = (offset >= months);
        if(periodTooLong){
            minMax.max.viewValid = false;
            minMax.min.viewValid = false;
        }
        return periodTooLong;
    };

    var _now = function(){
        return moment(0, 'HH');
    }

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

    FromTos.prototype.linkFormAndModel = function(form, model){

        $log.debug('--- linkFormAndModel. --- form:');
        $log.debug(form);
        $log.debug('model:');
        $log.debug(model);
        if(!this.names){
            $log.debug('this.names is not valid:');
            $log.debug(this.names);
            return;
        }
        this.model = model;
        for(var i=0; i<this.names.length;i++){
            var name = this.names[i];
            var fromTo = this[name];

            $log.debug(name+'from');
            $log.debug(name+'tom');

            var formFrom = form[name+'from'];
            $log.debug('formFrom:');
            $log.debug(formFrom);
            var formTo = form[name+'tom'];
            $log.debug('formTo:');
            $log.debug(formTo);
            fromTo.from.form = formFrom;
            fromTo.to.form = formTo;

            $log.debug('--- addparsers. ---');

            fromTo._addParser(formFrom, fromTo.from, 'from');
            fromTo._addParser(formTo, fromTo.to, 'tom');

            // set the initial values
            if(model[name]){
                fromTo.update({from:model[name].from,to:model[name].tom});
            }
        }
    };

    FromTos.prototype.rangeCheck = function(){
        //console.log('---------- FromTos.rangeCheck');
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
        //this.minDateOutOfRange = _isMinDateOutOfRange(this.minMax);
        //if(this.minDateOutOfRange){
        //    this.datesOutOfRange = this.minDateOutOfRange;
        //}
    };

    // ------|    |------- start
    // ----|    |--------- 1
    // ---------|    |---- 2
    // ----|         |---- 3
    FromTos.prototype.checkOverlaps = function(){
        this.clearOverlaps();
        //console.log('----------- check overlaps');
        //console.log('------|    |-------');
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            //dateRange.overlap = false;
            //console.log('++++++ dr valid:' + dateRange.valid + ',' + dateRange.name + ', from:' + dateRange.from.dateString + ', to:' + dateRange.to.dateString );

            if(dateRange.valid){
                for(var j=i+1; j<this.dateRanges.length; j++){
                    var dateRange2 = this.dateRanges[j];
                    if(dateRange.name !== dateRange2.name && dateRange2.valid){
                        //console.log('-- dr1 ' + dateRange.name + ', from:' + dateRange.from.dateString + ', to:' + dateRange.to.dateString );
                        //console.log('-- dr2 ' + dateRange2.name + ', from:' + dateRange2.from.dateString+ ', to:' + dateRange2.to.dateString);
                        var overlap = false;
                        var sameTo = dateRange.to.moment.isSame(dateRange2.to.moment);
                        var sameFrom = dateRange.from.moment.isSame(dateRange2.from.moment);
                        var sameFromTo = dateRange.to.moment.isSame(dateRange2.from.moment);
                        var sameToFrom = dateRange.from.moment.isSame(dateRange2.to.moment);
                        //console.log('------|    |-------');
                        if(sameTo || sameFrom || sameFromTo || sameToFrom){
                            overlap = true;
                            //console.log('------||    ||-------');
                        } else if(dateRange2.from.moment.isBefore(dateRange.from.moment) &&
                            (dateRange2.to.moment.isBefore(dateRange.to.moment) &&
                            dateRange2.to.moment.isAfter(dateRange.from.moment) )
                        ){
                            //console.log('----|    |--------- 1');
                            overlap = true;
                        } else if(dateRange2.from.moment.isAfter(dateRange.from.moment) &&
                            dateRange2.from.moment.isBefore(dateRange.to.moment)){
                            //console.log('--------|    |----- 2');
                            overlap = true;
                        } else if(dateRange2.from.moment.isBefore(dateRange.from.moment) &&
                            dateRange2.to.moment.isAfter(dateRange.to.moment) ){
                            //console.log('----|            |- 3');
                            overlap = true;
                        }
                        if(!dateRange.overlap){
                            dateRange.overlap = overlap;
                        }
                        if(!dateRange2.overlap) {
                            dateRange2.overlap = overlap;
                        }
                    }
                }
            }
        }
    };

    FromTos.prototype.setValidity = function() {
        //console.log('--------------- ');
        //console.log('--- periodTooLong:'+this.datesPeriodTooLong+', datesOutOfRange:' + this.datesOutOfRange + ', totalCertDays:' + this.totalCertDays);
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            // set validity
            dateRange.to.setValidity();
            dateRange.from.setValidity();
            //console.log('--- to<from:' + dateRange.valid);
            //console.log('--- val from ' + dateRange.name + ', viewValid ' + dateRange.from.viewValid + ', outOfRange:' + dateRange.from.outOfRange + ', overlap:' + dateRange.overlap );
            //console.log('--- val to ' + dateRange.name + ', viewValid ' + dateRange.to.viewValid + ', outOfRange:' + dateRange.to.outOfRange + ', overlap:' + dateRange.overlap);
        }
    };

    FromTos.prototype.updateMinMax = function(){
        //console.log('---------------- updateMinMax');
        var validDateUnits = [];
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            //if(dateRange.valid){ //&& !dateRange.overlap){
            if(dateRange.from && dateRange.from.valid){
                validDateUnits.push(dateRange.from);
            }
            if(dateRange.to && dateRange.to.valid){
                validDateUnits.push(dateRange.to);
            }
            //}
        }

        //console.log('---------------- validDateUnits length : ' + validDateUnits.length);

        if(validDateUnits !== undefined && validDateUnits.length > 0){
            //console.log('-- before');
            var du;
            for(i=0; i<validDateUnits.length; i++){
                du = validDateUnits[i];
                //console.log('-- du : ' + du.dateString);
            }

            validDateUnits = validDateUnits.sort(function(a,b){
                //console.log('-- sort du a: ' + a.dateString);
                //console.log('-- sort du b: ' + b.dateString);
                return a.longTime - b.longTime;
            });

            //console.log('-- after');
            for(i=0; i<validDateUnits.length; i++){
                du = validDateUnits[i];
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
        //console.log('----------- clear overlaps');
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            dateRange.overlap = false;
        }
    }

    FromTos.build = function(names, startDate){
        return new FromTos(names, startDate);
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

    FromTo.prototype._addParser = function(formElement, dateUnit){
        $log.debug('_addParser formElement');
        $log.debug(formElement);
        $log.debug('_addParser dateUnit');
        $log.debug(dateUnit);

        if(formElement && dateUnit){
            $log.debug('--- end - pushing parser---');
            formElement.$parsers.push(function(modelValue){

                $log.debug('--- Parser called --- modelvalue:');
                $log.debug(modelValue);
                $log.debug('$viewvalue:');
                $log.debug(formElement.$viewValue);

                if(!modelValue) {
                    modelValue = formElement.$viewValue;
                }

                // convert to moment
                var mdate = moment(modelValue);
                var sdate;
                if(mdate && mdate.isValid()){
                    sdate = mdate.format(format);
                } else {
                    sdate = modelValue;
                }

                $log.debug('sdate:');
                $log.debug(sdate);
                dateUnit.update(sdate); // will trigger a fromTo.updateModel
                if(!mdate.isValid()){
                    //return undefined;
                } else {
                    return sdate;
                }
            });

        } else {
            $log.debug('formElement or dateUnit not available. cannot add parser or modelupdate.');
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
            this.from.update(startDate);
            this.to = DateUnit.build(undefined, this, this.name+'tom');

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
                this.update({from:now, to:this.sevenDaysAhead(now)});
            } else {
                var nextStartDate = moment(minMax.max.moment).add(interval, 'days');
                this.update({from:nextStartDate, to:this.sevenDaysAhead(nextStartDate)});
            }
        } else {
            //console.log('1');
            this.update(undefined);
        }
    };

    FromTo.prototype.update = function( fromTo ){
        //console.log('------------------- FromTo.update');
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
        //console.log('---------- FromTo.updateDaysBetween');
        this.overlap = false;
        this.daysBetween = _updateDaysBetween(this.from, this.to);
        // set validity
        if(this.from && this.to){
            this.valid = this.from.valid && this.to.valid;
        }
        if(this.valid) {
            // check that to is after from
            if (this.from.moment.isAfter(this.to.moment)) {
                this.valid = false;
            }
        }

        // finally if this is valid set the workState
        if(this.valid){
            // this is valid!!
            // set the workState
            this.workState = true;
        } else {
            this.workState = false;
        }

        if(this.parent) {
            //console.log('has parent');
            //if(this.valid){
            //console.log('+++++++ checkOverlaps : ' + this.name);
            this.parent.checkOverlaps();
            //}
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
        return !this.to || ! this.from || this.to.isEmpty() || this.from.isEmpty();
    }

    FromTo.build = function(name, startDate){
        return new FromTo(name, startDate);
    }

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

    DateUnit.prototype.update = function( dateString ){
        //console.log('3');
        this.outOfRange = false;
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
                this.longTime = 0;
            }
        }

        if(this.valid){
            this.outOfRange = _areDatesOutOfRange(this.moment);
        }

        //console.log('3.1' + this.name + ', dateString ' + this.dateString);
        if(this.fromTo){
            //console.log('4');
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
        if(this.fromTo.isEmpty()){ // if dates are undefined then show no validation errors
            this.viewValid = true;
        } else {
            if (!this.fromTo.valid || !this.valid || this.outOfRange) {
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
    }

    DateUnit.prototype.isEmpty = function(){
        return this.dateString === undefined;
    }

    DateUnit.build = function( dateString, fromTo, name ){
        return new DateUnit(dateString, fromTo, name);
    }

    // --- service methods


    return {
        DateUnit : DateUnit,
        FromTo : FromTo,
        FromTos : FromTos
    };

});
