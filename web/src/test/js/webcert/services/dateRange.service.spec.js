describe('DateRangeService', function() {
    'use strict';

    var DateRangeService;

    beforeEach(angular.mock.module('common'), function($provide){

    });

    beforeEach(angular.mock.inject(['common.DateRangeService',
        function(_DateRangeService) {
            DateRangeService = _DateRangeService;
        }
    ]));

    //describe('#DateUnit', function(){
    //    it('can create a DateUnit from a YYYY-MM-DD string', function(){
    //        var du = DateRangeService.DateUnit.build('2015-01-01');
    //        expect(du.valid).toBeTruthy();
    //        expect(du.dateString).toBe('2015-01-01');
    //    });
    //    it('will create an invalid DateUnit', function(){
    //        var du = DateRangeService.DateUnit.build('asdfasdf');
    //        expect(du.valid).toBeFalsy();
    //        expect(du.dirty).toBeFalsy();
    //        expect(du.dateString).toBe('asdfasdf');
    //
    //        // update with new false value
    //        du.update('hehe');
    //        expect(du.valid).toBeFalsy();
    //        expect(du.dirty).toBeTruthy();
    //        expect(du.dateString).toBe('hehe');
    //
    //    });
    //
    //    it('will create a valid date on update of an invalid DateUnit', function(){
    //        var du = DateRangeService.DateUnit.build('asdfasdf');
    //        expect(du.valid).toBeFalsy();
    //        expect(du.dirty).toBeFalsy();
    //        expect(du.dateString).toBe('asdfasdf');
    //
    //        // update with only year, should give us first month and first day
    //        du.update('2015');
    //        expect(du.valid).toBeTruthy();
    //        expect(du.dirty).toBeTruthy();
    //        expect(du.dateString).toBe('2015');
    //        expect(du.momentString).toBe('2015-01-01');
    //
    //        // update with year, month, should give us first day
    //        du.update('2015-02');
    //        expect(du.valid).toBeTruthy();
    //        expect(du.dirty).toBeTruthy();
    //        expect(du.dateString).toBe('2015-02');
    //        expect(du.momentString).toBe('2015-02-01');
    //
    //        // update with invalid date
    //        du.update('2015-55');
    //        expect(du.valid).toBeFalsy();
    //        expect(du.dirty).toBeTruthy();
    //        expect(du.dateString).toBe('2015-55');
    //        expect(du.momentString).toBe('invalid');
    //
    //        // update with valid date
    //        du.update('2015-12');
    //        expect(du.valid).toBeTruthy();
    //        expect(du.dirty).toBeTruthy();
    //        expect(du.dateString).toBe('2015-12');
    //        expect(du.momentString).toBe('2015-12-01');
    //
    //    });
    //});
    //
    //describe('#validateDateRange', function() {
    //
    //    it ('can validate 1 dateRange', function () {
    //        var fromTo = DateRangeService.FromTo.build('range1');
    //        expect(fromTo.daysBetween).toBe(8);
    //
    //        fromTo.from.update('2015-01-01');
    //        fromTo.to.update('2015-01-01');
    //        expect(fromTo.daysBetween).toBe(1);
    //
    //        fromTo.to.update('2015-01-02');
    //        expect(fromTo.daysBetween).toBe(2);
    //
    //        // update with a bad date
    //        fromTo.to.update('bad date');
    //        expect( fromTo.daysBetween).toBe(0);
    //    });
    //
    //});

    describe('#create fromTos ( ranges )', function() {

        //it ('can create two fromTo in a fromTos range', function () {
        //    var fromTos = DateRangeService.FromTos.build(['range1', 'range2']);
        //    expect(fromTos.dateRanges.length).toBe(2);
        //    expect(fromTos.range1).not.toBeUndefined();
        //    expect(fromTos.range1.from.moment).toBeUndefined();
        //    expect(fromTos.range2.from.moment).toBeUndefined();
        //});
        //
        //it ('can check on and off a fromTo date range', function () {
        //    var fromTos = DateRangeService.FromTos.build(['range1', 'range2']);
        //    var now = moment();
        //
        //    fromTos.range1.check(true);
        //
        //    expect(fromTos.range1.from.dateString).not.toBeUndefined();
        //    expect(fromTos.range1.to.dateString).not.toBeUndefined();
        //
        //    var nowString = now.format('YYYY-MM-DD');
        //    var now7String = now.add(7, 'days').format('YYYY-MM-DD');
        //    expect(fromTos.range1.from.dateString).toBe(nowString);
        //    expect(fromTos.range1.to.dateString).toBe(now7String);
        //
        //    expect(fromTos.minMax.min.dateString).toBe(nowString);
        //    expect(fromTos.minMax.max.dateString).toBe(now7String);
        //
        //    // create a bad date ... this should invalidate min max
        //    fromTos.range1.from.update('bad date!');
        //    expect(fromTos.minMax.min).toBeUndefined();
        //    expect(fromTos.minMax.max).toBeUndefined();
        //    expect(fromTos.daysBetween).toBe(0);
        //
        //    // update with good value
        //    fromTos.range1.from.update(nowString);
        //
        //    // min max should be reset
        //    expect(fromTos.minMax.min.dateString).toBe(nowString);
        //    expect(fromTos.minMax.max.dateString).toBe(now7String);
        //
        //    // check days between
        //    expect(fromTos.daysBetween).toBe(8);
        //
        //    // should reset from to dates
        //    // remove them from min max
        //    // days between should be 0
        //    fromTos.range1.check(false);
        //    expect(fromTos.minMax.min).toBeUndefined();
        //    expect(fromTos.minMax.max).toBeUndefined();
        //    expect(fromTos.daysBetween).toBe(0);
        //    expect(fromTos.range1.from.dateString).toBeUndefined();
        //    expect(fromTos.range1.to.dateString).toBeUndefined();
        //
        //    // check it back to make sure, min, max, days between are set.
        //    fromTos.range1.check(true);
        //    expect(fromTos.range1.from.dateString).toBe(nowString);
        //    expect(fromTos.range1.to.dateString).toBe(now7String);
        //
        //    expect(fromTos.minMax.min.dateString).toBe(nowString);
        //    expect(fromTos.minMax.max.dateString).toBe(now7String);
        //
        //    expect(fromTos.daysBetween).toBe(8);
        //
        //});

        it ('can handle more than one date', function () {
            var fromTos = DateRangeService.FromTos.build(['range1', 'range2']);
            var now = moment(),
                now7 = moment(now).add(6, 'days'),
                now8 = moment(now7).add(1, 'days'),
                now15 = moment(now8).add(6, 'days'),
                now16 = moment(now15).add(6, 'days'),
                now33 = moment(now26).add(6, 'days'),

                nowString = now.format('YYYY-MM-DD'),
                now7String = now7.format('YYYY-MM-DD'),
                now8String = now8.format('YYYY-MM-DD'),
                now25String = now25.format('YYYY-MM-DD'),
                now26String = now26.format('YYYY-MM-DD'),
                now33String = now33.format('YYYY-MM-DD');

            fromTos.range1.check(true);
            expect(fromTos.daysBetween).toBe(7);

            //// check min max
            //expect(fromTos.minMax.min.dateString).toBe(nowString);
            //expect(fromTos.minMax.max.dateString).toBe(now7String);
            //expect(fromTos.daysBetween).toBe(8);
            //
            //fromTos.range2.check(true);
            //
            //expect(fromTos.range1.from.dateString).toBe(nowString);
            //expect(fromTos.range1.to.dateString).toBe(now7String);
            //
            //expect(fromTos.range2.from.dateString).toBe(now8String);
            //expect(fromTos.range2.to.dateString).toBe(now25String);
            //
            //// uncheck 1, min max should now be range 2's
            //fromTos.range1.check(false);
            //expect(fromTos.minMax.min.dateString).toBe(now8String);
            //expect(fromTos.minMax.max.dateString).toBe(now25String);
            //
            //// check 1 we should get a weeks interval over range 2
            //fromTos.range1.check(true);
            //expect(fromTos.range1.from.dateString).toBe(now26String);
            //expect(fromTos.range1.to.dateString).toBe(now33String);
            //
            //// check min max
            //expect(fromTos.minMax.min.dateString).toBe(now8String);
            //expect(fromTos.minMax.max.dateString).toBe(now33String);
            //
            //// uncheck both and start again
            //fromTos.range1.check(false);
            //fromTos.range2.check(false);
            //
            //fromTos.range2.check(true);
            //expect(fromTos.daysBetween).toBe(8);
            //fromTos.range1.check(true);
            //expect(fromTos.daysBetween).toBe(16);
            //
            //expect(fromTos.range2.from.dateString).toBe(nowString);
            //expect(fromTos.range2.to.dateString).toBe(now7String);
            //
            //expect(fromTos.range1.from.dateString).toBe(now8String);
            //expect(fromTos.range1.to.dateString).toBe(now25String);
            //
            //expect(fromTos.minMax.min.dateString).toBe(nowString);
            //expect(fromTos.minMax.max.dateString).toBe(now25String);

        });

        //it ('can 4 dates', function () {
        //    var fromTos = DateRangeService.FromTos.build(['range1', 'range2', 'range3', 'range4']);
        //    var now = moment(),
        //        now7 = moment(now).add(7, 'days'),
        //
        //        now8 = moment(now7).add(1, 'days'),
        //        now25 = moment(now8).add(7, 'days'),
        //
        //        now26 = moment(now25).add(1, 'days'),
        //        now33 = moment(now26).add(7, 'days'),
        //
        //        now34 = moment(now33).add(1, 'days'),
        //        now41 = moment(now34).add(7, 'days'),
        //
        //        nowString = now.format('YYYY-MM-DD'),
        //        now7String = now7.format('YYYY-MM-DD'),
        //
        //        now8String = now8.format('YYYY-MM-DD'),
        //        now25String = now25.format('YYYY-MM-DD'),
        //
        //        now26String = now26.format('YYYY-MM-DD'),
        //        now33String = now33.format('YYYY-MM-DD'),
        //
        //        now34String = now34.format('YYYY-MM-DD'),
        //        now41String = now41.format('YYYY-MM-DD');
        //
        //    console.log('nowString : ' + nowString);
        //    console.log('now7String : ' + now7String);
        //
        //    console.log('now8String : ' + now8String);
        //    console.log('now25String : ' + now25String);
        //
        //    console.log('now26String : ' + now26String);
        //    console.log('now33String : ' + now33String);
        //
        //    console.log('now34String : ' + now34String);
        //    console.log('now41String : ' + now41String);
        //
        //    fromTos.range1.check(true);
        //    fromTos.range2.check(true);
        //    fromTos.range3.check(true);
        //    fromTos.range4.check(true);
        //
        //    expect(fromTos.range1.from.dateString).toBe(nowString);
        //    expect(fromTos.range1.to.dateString).toBe(now7String);
        //
        //    expect(fromTos.range2.from.dateString).toBe(now8String);
        //    expect(fromTos.range2.to.dateString).toBe(now25String);
        //
        //    expect(fromTos.range3.from.dateString).toBe(now26String);
        //    expect(fromTos.range3.to.dateString).toBe(now33String);
        //
        //    expect(fromTos.range4.from.dateString).toBe(now34String);
        //    expect(fromTos.range4.to.dateString).toBe(now41String);
        //
        //    expect(fromTos.daysBetween).toBe(32);
        //
        //});
        //
        //it ('can 4 dates', function () {
        //    var fromTos = DateRangeService.FromTos.build(['range1', 'range2', 'range3', 'range4']);
        //    var now = moment(),
        //        now7 = moment(now).add(7, 'days'),
        //
        //        now8 = moment(now7).add(1, 'days'),
        //        now25 = moment(now8).add(7, 'days'),
        //
        //        now26 = moment(now25).add(1, 'days'),
        //        now33 = moment(now26).add(7, 'days'),
        //
        //        now34 = moment(now33).add(1, 'days'),
        //        now41 = moment(now34).add(7, 'days'),
        //
        //        nowString = now.format('YYYY-MM-DD'),
        //        now7String = now7.format('YYYY-MM-DD'),
        //
        //        now8String = now8.format('YYYY-MM-DD'),
        //        now25String = now25.format('YYYY-MM-DD'),
        //
        //        now26String = now26.format('YYYY-MM-DD'),
        //        now33String = now33.format('YYYY-MM-DD'),
        //
        //        now34String = now34.format('YYYY-MM-DD'),
        //        now41String = now41.format('YYYY-MM-DD');
        //
        //    console.log('nowString : ' + nowString);
        //    console.log('now7String : ' + now7String);
        //
        //    console.log('now8String : ' + now8String);
        //    console.log('now25String : ' + now25String);
        //
        //    console.log('now26String : ' + now26String);
        //    console.log('now33String : ' + now33String);
        //
        //    console.log('now34String : ' + now34String);
        //    console.log('now41String : ' + now41String);
        //
        //    fromTos.range1.check(true);
        //    fromTos.range2.check(true);
        //    fromTos.range3.check(true);
        //    fromTos.range4.check(true);
        //
        //    expect(fromTos.range1.from.dateString).toBe(nowString);
        //    expect(fromTos.range1.to.dateString).toBe(now7String);
        //
        //    expect(fromTos.range2.from.dateString).toBe(now8String);
        //    expect(fromTos.range2.to.dateString).toBe(now25String);
        //
        //    expect(fromTos.range3.from.dateString).toBe(now26String);
        //    expect(fromTos.range3.to.dateString).toBe(now33String);
        //
        //    expect(fromTos.range4.from.dateString).toBe(now34String);
        //    expect(fromTos.range4.to.dateString).toBe(now41String);
        //
        //    expect(fromTos.daysBetween).toBe(32);
        //
        //    // uncheck 2 ranges
        //    fromTos.range3.check(false);
        //    fromTos.range4.check(false);
        //
        //    expect(fromTos.daysBetween).toBe(16);
        //
        //    // add a date to range3
        //    var now36 = moment(now34).add(2, 'days');
        //    console.log('now36 all : ' + moment().format());
        //
        //    console.log('now36 00 : ' + moment(0,'HH').format());
        //
        //    var now36String = now36.format('YYYY-MM-DD');
        //    console.log('now36String : ' + now36String);
        //    fromTos.range3.from.update(now34String);
        //    fromTos.range3.to.update(now36String);
        //    console.log('min : ' + fromTos.minMax.min.dateString + ', max:' + fromTos.minMax.max.dateString);
        //    var daysBetween = now36.diff(now, 'days') + 1;
        //    expect(fromTos.daysBetween).toBe(daysBetween);
        //
        //});
        //
        //it ('can validate when a from date is after a to date', function () {
        //    var fromTos = DateRangeService.FromTos.build(['range1', 'range2', 'range3', 'range4']);
        //    fromTos.range1.from.update('2015-04-07');
        //    fromTos.range1.to.update('2015-04-05');
        //    expect(fromTos.daysBetween).toBe(0);
        //    expect(fromTos.range1.valid).toBeFalsy();
        //});
        //
        //it ('can validate when a from date is the same as a to date', function () {
        //    var fromTos = DateRangeService.FromTos.build(['range1', 'range2', 'range3', 'range4']);
        //    fromTos.range1.from.update('2015-04-07');
        //    fromTos.range1.to.update('2015-04-07');
        //    expect(fromTos.daysBetween).toBe(1);
        //    expect(fromTos.range1.valid).toBeTruthy();
        //});
        //
        //it ('can validate when two ranges overlap', function () {
        //    var fromTos = DateRangeService.FromTos.build(['range1', 'range2', 'range3', 'range4']);
        //    fromTos.range1.from.update('2015-04-07');
        //    fromTos.range1.to.update('2015-04-14');
        //
        //    // ------|    |------- original
        //    // ----|    |--------- 1
        //    // ----|         |---- 2
        //    // ---------|    |---- 3
        //    // 1
        //    fromTos.range2.from.update('2015-04-04');
        //    fromTos.range2.to.update('2015-04-10');
        //
        //    expect(fromTos.daysBetween).toBe(0);
        //    expect(fromTos.range1.overlap).toBeTruthy();
        //    expect(fromTos.range2.overlap).toBeTruthy();
        //
        //    //// 2
        //    fromTos.range2.from.update('2015-04-04');
        //    fromTos.range2.to.update('2015-04-16');
        //
        //    expect(fromTos.daysBetween).toBe(0);
        //    expect(fromTos.range1.overlap).toBeTruthy();
        //    expect(fromTos.range2.overlap).toBeTruthy();
        //
        //    // 3
        //    fromTos.range2.from.update('2015-04-10');
        //    fromTos.range2.to.update('2015-04-16');
        //
        //    expect(fromTos.daysBetween).toBe(0);
        //    expect(fromTos.range1.overlap).toBeTruthy();
        //    expect(fromTos.range2.overlap).toBeTruthy();
        //
        //
        //    // back to valid
        //    fromTos.range2.from.update('2015-04-15');
        //    fromTos.range2.to.update('2015-04-15');
        //
        //    expect(fromTos.daysBetween).toBe(9);
        //    expect(fromTos.range1.valid).toBeTruthy();
        //    expect(fromTos.range2.valid).toBeTruthy();
        //
        //});


    });

});
