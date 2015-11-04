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
            //$log.info('-- min : ' + from.dateString + ', max:' + to.dateString);
            //$log.info('-- db ' + db);
            return to.moment.diff(from.moment, 'days') + 1;
        } else {
            return 0;
        }
    };

    var _isMinDateOutOfRange = function(minMax, now) { // jshint ignore:line
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
        //$log.info('**** min : ' + min.format('YYYY-MM-DD') + ', now : ' + now.format('YYYY-MM-DD'));
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
        //$log.info('**** min : ' + min.format('YYYY-MM-DD') + ', now : ' + now.format('YYYY-MM-DD'));
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
        //$log.info('**** offset : ' + offset);
        //$log.info('**** min : ' + min.format('YYYY-MM-DD') + ', max : ' + max.format('YYYY-MM-DD'));
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

        //$log.info('--- linkFormAndModel. --- form:');
        //$log.info(form);
        //$log.info('model:');
        //$log.info(model);
        if(!this.names){
            //$log.info('this.names is not valid:');
            //$log.info(this.names);
            return;
        }
        this.model = model;
        for(var i=0; i<this.names.length;i++){
            var name = this.names[i];
            var fromTo = this[name];

            //$log.info(name+'from');
            //$log.info(name+'tom');

            var formFrom = form[name+'from'];
            //$log.info('formFrom:');
            //$log.info(formFrom);
            var formTo = form[name+'tom'];
            //$log.info('formTo:');
            //$log.info(formTo);
            fromTo.from.form = formFrom;
            fromTo.to.form = formTo;
            //$log.info('--- addparsers. ---');
            fromTo._addParser(formFrom, fromTo.from, 'from', scope);
            fromTo._addParser(formTo, fromTo.to, 'tom', scope);

            // set the initial values
            if(model[name]){
                fromTo.update({from:model[name].from,to:model[name].tom});
            }
        }
    };

    FromTos.prototype.rangeCheck = function(){
        //$log.info('---------- FromTos.rangeCheck');
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

        // reason this is commented out?
        // this.minDateOutOfRange = _isMinDateOutOfRange(this.minMax);
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
        //$log.info('----------- check overlaps');
        //$log.info('------|    |-------');
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            //dateRange.overlap = false;
            //$log.info('++++++ dr valid:' + dateRange.valid + ',' + dateRange.name + ', from:' + dateRange.from.dateString + ', to:' + dateRange.to.dateString );

            if(dateRange.valid){
                for(var j=i+1; j<this.dateRanges.length; j++){
                    var dateRange2 = this.dateRanges[j];
                    if(dateRange.name !== dateRange2.name && dateRange2.valid){
                        //$log.info('-- dr1 ' + dateRange.name + ', from:' + dateRange.from.dateString + ', to:' + dateRange.to.dateString );
                        //$log.info('-- dr2 ' + dateRange2.name + ', from:' + dateRange2.from.dateString+ ', to:' + dateRange2.to.dateString);
                        var overlap = hasOverlap(dateRange, dateRange2);
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

	var hasOverlap = function(dateRange, dateRange2) {
		var overlap = false;
		var sameTo = dateRange.to.moment.isSame(dateRange2.to.moment);
		var sameFrom = dateRange.from.moment.isSame(dateRange2.from.moment);
		var sameFromTo = dateRange.to.moment.isSame(dateRange2.from.moment);
		var sameToFrom = dateRange.from.moment.isSame(dateRange2.to.moment);
		//$log.info('------|    |-------');
		if(sameTo || sameFrom || sameFromTo || sameToFrom){
			overlap = true;
			//$log.info('------||    ||-------');
		} else if(dateRange2.from.moment.isBefore(dateRange.from.moment) &&
			(dateRange2.to.moment.isBefore(dateRange.to.moment) &&
				dateRange2.to.moment.isAfter(dateRange.from.moment) )
		){
			//$log.info('----|    |--------- 1');
			overlap = true;
		} else if(dateRange2.from.moment.isAfter(dateRange.from.moment) &&
			dateRange2.from.moment.isBefore(dateRange.to.moment)){
			//$log.info('--------|    |----- 2');
			overlap = true;
		} else if(dateRange2.from.moment.isBefore(dateRange.from.moment) &&
			dateRange2.to.moment.isAfter(dateRange.to.moment) ){
			//$log.info('----|            |- 3');
			overlap = true;
		}

		return overlap;
	};

    FromTos.prototype.setValidity = function() {
        //$log.info('--------------- ');
        //$log.info('--- periodTooLong:'+this.datesPeriodTooLong+', datesOutOfRange:' + this.datesOutOfRange + ', totalCertDays:' + this.totalCertDays);
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            // set validity
            dateRange.to.setValidity();
            dateRange.from.setValidity();
            //$log.info('--- to<from:' + dateRange.valid);
            //$log.info('--- val from ' + dateRange.name + ', viewValid ' + dateRange.from.viewValid + ', outOfRange:' + dateRange.from.outOfRange + ', overlap:' + dateRange.overlap );
            //$log.info('--- val to ' + dateRange.name + ', viewValid ' + dateRange.to.viewValid + ', outOfRange:' + dateRange.to.outOfRange + ', overlap:' + dateRange.overlap);
        }
    };

    FromTos.prototype.updateMinMax = function(){
        //$log.info('---------------- updateMinMax');
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

        //$log.info('---------------- validDateUnits length : ' + validDateUnits.length);

        if(validDateUnits !== undefined && validDateUnits.length > 0){
            //$log.info('-- before');
            var du;
            for(i=0; i<validDateUnits.length; i++){
                du = validDateUnits[i];
                //$log.info('-- du : ' + du.dateString);
            }

            validDateUnits = validDateUnits.sort(function(a,b){
                //$log.info('-- sort du a: ' + a.dateString);
                //$log.info('-- sort du b: ' + b.dateString);
                return a.longTime - b.longTime;
            });

            //$log.info('-- after');
            for(i=0; i<validDateUnits.length; i++){
                du = validDateUnits[i];
                //$log.info('-- du : ' + du.dateString);
            }

            this.minMax.min = validDateUnits[0];
            this.minMax.max = validDateUnits[validDateUnits.length-1];
        } else {
            this.minMax.min = undefined;
            this.minMax.max = undefined;
        }
    };

    FromTos.prototype.clearOverlaps = function(){
        //$log.info('----------- clear overlaps');
        for(var i=0; i<this.dateRanges.length; i++){
            var dateRange = this.dateRanges[i];
            dateRange.overlap = false;
        }
    };

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

    /**
     * This will get called on user interaction and also on datepicker input.
     * The date picker will provide a utc formatted date so we can moment it and then get a formatted string, which
     * we can then pass into the dateunit.
     * @param formElement
     * @param dateUnit
     * @private
     */
    FromTo.prototype._addParser = function(formElement, dateUnit, name, scope){
        //$log.info('_addParser formElement');
        //$log.info(formElement);
        //$log.info('_addParser dateUnit');
        //$log.info(dateUnit);

        if(formElement && dateUnit){
            //formElement.$parsers = [];
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
                    formElement.$setValidity('date', false);
                    return formValue;
                } else {
                    formElement.$setValidity('date', false);
                    return undefined;
                }
                //return formValue;
            });

            formElement.$formatters.push(function(value){
               //$log.info('----- dp formatter : ' + value);
                return value;
            });

        } else {
            $log.info('formElement or dateUnit not available. cannot add parser or modelupdate.');
        }
    };

    FromTo.prototype.updateModel = function(){
        //$log.info('update model ++++++++++++++++++++');
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

        //$log.info('update model -------------------');
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
            //$log.info('1');
            this.update(undefined);
        }
    };

    FromTo.prototype.update = function( fromTo ){
        //$log.info('------------------- FromTo.update');
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
        //$log.info('---------- FromTo.updateDaysBetween');
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
            //$log.info('has parent');
            //if(this.valid){
            //$log.info('+++++++ checkOverlaps : ' + this.name);
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
        return (!this.to && !this.from) || (this.to.isEmpty() && this.from.isEmpty());
    };

    FromTo.build = function(name, startDate){
        return new FromTo(name, startDate);
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

    DateUnit.prototype.update = function( dateString ){
        //$log.info('3 update +++++++++++++++++++++++');
        //$log.info('dateString : ' + dateString );
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

        //$log.info('3.1' + this.name + ', dateString ' + this.dateString);
        if(this.fromTo){
            //$log.info('4');
            this.fromTo.updateOutOfRange(this.outOfRange);
            this.fromTo.updateDaysBetween();
            this.fromTo.updateModel();
        }

        //$log.info('2 dateString : ' + dateString + ', ' + format + ', valid:' + this.valid + ', viewValid:' + this.viewValid);

        //$log.info('3 update ------------------------');
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

    DateUnit.prototype.setValidity = function(){
        if(!this.fromTo){
            this.viewValid = true;
            return;
        }
        if(this.fromTo.isEmpty() ){ // if dates are undefined then show no validation errors
            this.viewValid = true;
        } else {
            //$log.info('viewValid : ' + this.viewValid + ', outOfRange : ' + this.outOfRange + ', overlap : ' + this.overlap);
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
    };

    DateUnit.prototype.isEmpty = function(){
        return this.dateString === undefined;
    };

    DateUnit.build = function( dateString, fromTo, name ){
        return new DateUnit(dateString, fromTo, name);
    };

    // --- service methods


    return {
        DateUnit : DateUnit,
        FromTo : FromTo,
        FromTos : FromTos
    };

}]);
