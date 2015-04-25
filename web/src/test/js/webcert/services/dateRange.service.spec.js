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

    //xdescribe('#DateUnit', function(){
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

    describe('#validateDateRange', function() {

        it ('can validate 1 dateRange', function () {
            var fromTo = DateRangeService.FromTo.build('range1');
            expect(fromTo.daysBetween).toBe(8);

            fromTo.from.update('2015-01-01');
            fromTo.to.update('2015-01-01');
            expect(fromTo.daysBetween).toBe(1);

            fromTo.to.update('2015-01-02');
            expect(fromTo.daysBetween).toBe(2);

            // update with a bad date
            fromTo.to.update('bad date');
            expect( fromTo.daysBetween).toBe(0);
        });

    });

    describe('#create fromTos ( ranges )', function() {

        it ('can create two fromTo in a fromTos range', function () {
            var fromTos = DateRangeService.FromTos.build(['range1', 'range2']);
            expect(fromTos.dateRanges.length).toBe(2);
            expect(fromTos.range1).not.toBeUndefined();
            expect(fromTos.range1.from.moment).toBeUndefined();
            expect(fromTos.range2.from.moment).toBeUndefined();
        });

        it ('can check on and off a fromTo date range', function () {
            var fromTos = DateRangeService.FromTos.build(['range1', 'range2']);
            var now = moment();

            fromTos.range1.check(true);

            expect(fromTos.range1.from.dateString).not.toBeUndefined();
            expect(fromTos.range1.to.dateString).not.toBeUndefined();

            var nowString = now.format('YYYY-MM-DD');
            var now7String = now.add(7, 'days').format('YYYY-MM-DD');
            expect(fromTos.range1.from.dateString).toBe(nowString);
            expect(fromTos.range1.to.dateString).toBe(now7String);

            expect(fromTos.minMax.min.dateString).toBe(nowString);
            expect(fromTos.minMax.max.dateString).toBe(now7String);

            // create a bad date ... this should invalidate min max
            fromTos.range1.from.update('bad date!');
            expect(fromTos.minMax.min).toBeUndefined();
            expect(fromTos.minMax.max).toBeUndefined();
            expect(fromTos.daysBetween).toBe(0);

            // update with good value
            fromTos.range1.from.update(nowString);

            // min max should be reset
            expect(fromTos.minMax.min.dateString).toBe(nowString);
            expect(fromTos.minMax.max.dateString).toBe(now7String);

            // check days between
            expect(fromTos.daysBetween).toBe(8);
        });

    });

});
