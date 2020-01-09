/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

    describe('#DateUnit', function(){
        it('can create a DateUnit from a YYYY-MM-DD string', function(){
            var du = DateRangeService.DateUnit.build('2015-01-09');
            expect(du.valid).toBeTruthy();
            expect(du.dateString).toBe('2015-01-09');
        });

        it('can create a DateUnit from a YYYYMMDD string', function(){
            var du = DateRangeService.DateUnit.build('20150109');
            expect(du.valid).toBeTruthy();
            expect(du.dateString).toBe('2015-01-09');
        });

        it('can create a DateUnit from a YYYYMM-DD string', function(){
            var du = DateRangeService.DateUnit.build('201501-09');
            expect(du.valid).toBeTruthy();
            expect(du.dateString).toBe('2015-01-09');
        });

        it('can create a DateUnit from a YYYY-MMDD string', function(){
            var du = DateRangeService.DateUnit.build('2015-0109');
            expect(du.valid).toBeTruthy();
            expect(du.dateString).toBe('2015-01-09');
        });

        it('retains invalid date string with only dashes', function(){
            var du = DateRangeService.DateUnit.build('--');
            expect(du.valid).toBeFalsy();
            expect(du.dateString).toBe('--');

            du = DateRangeService.DateUnit.build('aa-');
            expect(du.valid).toBeFalsy();
            expect(du.dateString).toBe('aa-');

            du = DateRangeService.DateUnit.build('2016-01-');
            expect(du.valid).toBeFalsy();
            expect(du.dateString).toBe('2016-01-');
        });

        it('will create a non swedish DateUnit, not YYYY-MM-DD', function(){
            // english
            var du = DateRangeService.DateUnit.build('03-12-2015');
            expect(du.valid).toBeFalsy();
            expect(du.viewValid).toBeFalsy();
            expect(du.dirty).toBeFalsy();
            expect(du.dateString).toBe('03-12-2015');

            // update with new false value
            du.update('hehe');
            expect(du.valid).toBeFalsy();
            expect(du.dirty).toBeTruthy();
            expect(du.dateString).toBe('hehe');
        });

        it('can test if empty', function() {
            var du = DateRangeService.DateUnit.build('03-12-2015');
            expect(du.isEmpty()).toBeFalsy();

            du.update('');
            expect(du.isEmpty()).toBeTruthy();

            du.update(null);
            expect(du.isEmpty()).toBeTruthy();

            du.update(undefined);
            expect(du.isEmpty()).toBeTruthy();
        });

        it('will create an invalid DateUnit', function(){
            var du = DateRangeService.DateUnit.build('asdfasdf');
            expect(du.valid).toBeFalsy();
            expect(du.dirty).toBeFalsy();
            expect(du.dateString).toBe('asdfasdf');

            // update with new false value
            du.update('hehe');
            expect(du.valid).toBeFalsy();
            expect(du.dirty).toBeTruthy();
            expect(du.dateString).toBe('hehe');
        });

        it('will create a valid date on update of an invalid DateUnit', function(){
            var du = DateRangeService.DateUnit.build('asdfasdf');
            expect(du.valid).toBeFalsy();
            expect(du.dirty).toBeFalsy();
            expect(du.dateString).toBe('asdfasdf');

            // update with only year, should give us first month and first day
            du.update('2015');
            expect(du.valid).toBeFalsy();
            expect(du.dirty).toBeTruthy();
            expect(du.dateString).toBe('2015');
            expect(du.momentString).toBe('invalid');

            // update with year, month, should give us first day
            du.update('2015-02');
            expect(du.valid).toBeFalsy();
            expect(du.dirty).toBeTruthy();
            expect(du.dateString).toBe('2015-02');
            expect(du.momentString).toBe('invalid');

            // update with invalid date
            du.update('2015-55');
            expect(du.valid).toBeFalsy();
            expect(du.dirty).toBeTruthy();
            expect(du.dateString).toBe('2015-55');
            expect(du.momentString).toBe('invalid');

            // update with valid date
            du.update('2015-12');
            expect(du.valid).toBeFalsy();
            expect(du.dirty).toBeTruthy();
            expect(du.dateString).toBe('2015-12');
            expect(du.momentString).toBe('invalid');

        });
    });

    describe('#validateDateRange', function() {

        it ('can validate 1 dateRange', function () {
            var fromTo = DateRangeService.FromTo.build('range1');
            expect(fromTo.daysBetween).toBe(7);

            fromTo.from.update('2015-01-01');
            fromTo.to.update('2015-01-01');
            expect(fromTo.daysBetween).toBe(1);

            fromTo.to.update('2015-01-02');
            expect(fromTo.daysBetween).toBe(2);

            // update with a bad date
            fromTo.to.update('bad date');
            expect( fromTo.daysBetween).toBe(0);
        });

        it ('can check for emptiness', function () {
            var fromTo = DateRangeService.FromTo.build('range1');
            expect(fromTo.isEmpty()).toBeFalsy();

            fromTo.to.update('');
            expect(fromTo.isEmpty()).toBeFalsy();
            expect(fromTo.to.isEmpty()).toBeTruthy();
            expect(fromTo.from.isEmpty()).toBeFalsy();

            fromTo.from.update('');
            expect(fromTo.isEmpty()).toBeTruthy();
            expect(fromTo.from.isEmpty()).toBeTruthy();

        });

        it ('can check for view validity', function () {
            var fromTo = DateRangeService.FromTo.build('range1');
            expect(fromTo.isEmpty()).toBeFalsy();
            expect(fromTo.to.viewValid).toBeTruthy();
            expect(fromTo.from.viewValid).toBeTruthy();


            fromTo.to.update('23-255');
            expect(fromTo.isEmpty()).toBeFalsy();
            expect(fromTo.to.viewValid).toBeFalsy();
            expect(fromTo.from.viewValid).toBeTruthy();
            expect(fromTo.viewValid).toBeFalsy();


        });


        it ('make sure we only allow YYYY-MM-DD dates', function () {
            var fromTo = DateRangeService.FromTo.build('range1');
            expect(fromTo.isEmpty()).toBeFalsy();
            expect(fromTo.to.viewValid).toBeTruthy();
            expect(fromTo.from.viewValid).toBeTruthy();


            fromTo.to.update('23-255');
            expect(fromTo.isEmpty()).toBeFalsy();
            expect(fromTo.to.viewValid).toBeFalsy();
            expect(fromTo.from.viewValid).toBeTruthy();
            expect(fromTo.viewValid).toBeFalsy();
            expect(fromTo.to.moment).toBeNull();


            fromTo.to.update('2015');
            expect(fromTo.isEmpty()).toBeFalsy();
            expect(fromTo.to.viewValid).toBeFalsy();
            expect(fromTo.from.viewValid).toBeTruthy();
            expect(fromTo.viewValid).toBeFalsy();
            expect(fromTo.to.moment).toBeNull();

        });

    });

    describe('#date range checks', function() {

        it ('can validate min date out of range directly', function () {
            var result = false;
            var fromTos = DateRangeService.FromTos.build(['range1']);
            var now = moment();
            var date = moment(now).subtract(4, 'days');
            fromTos.range1.from.update(date);
            result = fromTos.datesOutOfRange;
            expect(result).toBeFalsy();

            date = moment(now).subtract(6, 'days');
            fromTos.range1.from.update(date);
            result = fromTos.datesOutOfRange;
            expect(result).toBeFalsy();

            date = moment(now).subtract(8, 'days');
            fromTos.range1.from.update(date);
            result = fromTos.datesOutOfRange;
            expect(result).toBeTruthy();
        });

        it ('can validate period ranges directly', function () {
            var result = false;
            var fromTos = DateRangeService.FromTos.build(['range1']);
            var min = moment('2014-01-01');
            var max = moment('2014-04-28');
            fromTos.range1.update({from:min, to:max});
            result = fromTos.datesPeriodTooLong;
            expect(result).toBeFalsy();

            max = moment('2014-07-01');
            fromTos.range1.update({from:min, to:max});
            result = fromTos.datesPeriodTooLong;
            expect(result).toBeTruthy();
        });

        it ('can validate min date out of range through update', function () {
            var result = false;
            var now = moment();

            var date = moment(now).subtract(4, 'days');

            var fromTos = DateRangeService.FromTos.build(['range1']);

            fromTos.range1.update({from:date, to:undefined});
            result = fromTos.datesOutOfRange;
            expect(result).toBeFalsy();

            date = moment(now).subtract(6,'days');
            fromTos.range1.update({from:date, to:undefined});
            result = fromTos.datesOutOfRange;
            expect(result).toBeFalsy();

            date = moment(now).subtract(10,'days');
            fromTos.range1.update({from:date, to:undefined});
            result = fromTos.datesOutOfRange;
            expect(result).toBeTruthy();
        });

        it ('can validate period ranges through update', function () {
            var result = false;
            var fromTos = DateRangeService.FromTos.build(['range1']);
            var min = moment('2014-01-01');
            var max = moment('2014-04-28');
            fromTos.range1.update({from:min, to:max});

            result = fromTos.datesPeriodTooLong;
            expect(result).toBeFalsy();

            max = moment('2014-07-01');
            fromTos.range1.update({from:min, to:max});
            result = fromTos.datesPeriodTooLong;
            expect(result).toBeTruthy();
        });


    });

    describe('#create fromTos ( ranges )', function() {

        var now,
            now7,

            now8,
            now15,

            now16,
            now23,

            now24,
            now31,

            nowString,
            now7String,

            now8String,
            now15String,

            now16String,
            now23String,

            now24String,
            now31String;

        beforeEach(function(){
            now = moment();
            now7 = moment(now).add(6, 'days');

            now8 = moment(now7).add(1, 'days');
            now15 = moment(now8).add(6, 'days');

            now16 = moment(now15).add(1, 'days');
            now23 = moment(now16).add(6, 'days');

            now24 = moment(now23).add(1, 'days');
            now31 = moment(now24).add(6, 'days');

            nowString = now.format('YYYY-MM-DD');
            now7String = now7.format('YYYY-MM-DD');

            now8String = now8.format('YYYY-MM-DD');
            now15String = now15.format('YYYY-MM-DD');

            now16String = now16.format('YYYY-MM-DD');
            now23String = now23.format('YYYY-MM-DD');

            now24String = now24.format('YYYY-MM-DD');
            now31String = now31.format('YYYY-MM-DD');

        });


        it ('can check on and off a fromTo date range', function () {
            var fromTos = DateRangeService.FromTos.build(['range1', 'range2']);
            var now = moment();

            fromTos.range1.check(true);

            expect(fromTos.range1.from.dateString).not.toBeUndefined();
            expect(fromTos.range1.to.dateString).not.toBeUndefined();

            var nowString = now.format('YYYY-MM-DD');
            var now7String = now.add(6, 'days').format('YYYY-MM-DD');
            expect(fromTos.range1.from.dateString).toBe(nowString);
            expect(fromTos.range1.to.dateString).toBe(now7String);

            expect(fromTos.minMax.min.dateString).toBe(nowString);
            expect(fromTos.minMax.max.dateString).toBe(now7String);

            // create a bad date ... if only one valid date then this is set on both min and max
            fromTos.range1.from.update('bad date!');
            expect(fromTos.minMax.min).toBe(fromTos.range1.to);
            expect(fromTos.minMax.max).toBe(fromTos.range1.to);
            expect(fromTos.totalCertDays).toBe(0);

            // update with good value
            fromTos.range1.from.update(nowString);

            // min max should be reset
            expect(fromTos.minMax.min.dateString).toBe(nowString);
            expect(fromTos.minMax.max.dateString).toBe(now7String);

            // check days between
            expect(fromTos.totalCertDays).toBe(7);

            // should reset from to dates
            // remove them from min max
            // days between should be 0

            fromTos.range1.check(false);
            expect(fromTos.minMax.min).toBeUndefined();
            expect(fromTos.minMax.max).toBeUndefined();
            expect(fromTos.totalCertDays).toBe(0);
            expect(fromTos.range1.from.dateString).toBeUndefined();
            expect(fromTos.range1.to.dateString).toBeUndefined();

            // check it back to make sure, min, max, days between are set.
            fromTos.range1.check(true);
            expect(fromTos.range1.from.dateString).toBe(nowString);
            expect(fromTos.range1.to.dateString).toBe(now7String);

            expect(fromTos.minMax.min.dateString).toBe(nowString);
            expect(fromTos.minMax.max.dateString).toBe(now7String);

            expect(fromTos.totalCertDays).toBe(7);

        });

        it ('can handle more than one date', function () {
            var fromTos = DateRangeService.FromTos.build(['range1', 'range2']);

            fromTos.range1.check(true);
            expect(fromTos.totalCertDays).toBe(7);

            // check min max
            expect(fromTos.minMax.min.dateString).toBe(nowString);
            expect(fromTos.minMax.max.dateString).toBe(now7String);
            expect(fromTos.totalCertDays).toBe(7);

            fromTos.range2.check(true);

            expect(fromTos.range1.from.dateString).toBe(nowString);
            expect(fromTos.range1.to.dateString).toBe(now7String);

            expect(fromTos.range2.from.dateString).toBe(now8String);
            expect(fromTos.range2.to.dateString).toBe(now15String);

            // uncheck 1, min max should now be range 2's
            fromTos.range1.check(false);
            expect(fromTos.minMax.min.dateString).toBe(now8String);
            expect(fromTos.minMax.max.dateString).toBe(now15String);

            // check 1 we should get a weeks interval over range 2
            fromTos.range1.check(true);
            expect(fromTos.range1.from.dateString).toBe(now16String);
            expect(fromTos.range1.to.dateString).toBe(now23String);

            // check min max
            expect(fromTos.minMax.min.dateString).toBe(now8String);
            expect(fromTos.minMax.max.dateString).toBe(now23String);

            // uncheck both and start again
            fromTos.range1.check(false);
            fromTos.range2.check(false);

            fromTos.range2.check(true);
            expect(fromTos.totalCertDays).toBe(7);
            fromTos.range1.check(true);
            expect(fromTos.totalCertDays).toBe(14);

            expect(fromTos.range2.from.dateString).toBe(nowString);
            expect(fromTos.range2.to.dateString).toBe(now7String);

            expect(fromTos.range1.from.dateString).toBe(now8String);
            expect(fromTos.range1.to.dateString).toBe(now15String);

            expect(fromTos.minMax.min.dateString).toBe(nowString);
            expect(fromTos.minMax.max.dateString).toBe(now15String);

        });

        it ('can have 4 dates', function () {
            var fromTos = DateRangeService.FromTos.build(['range1', 'range2', 'range3', 'range4']);

            fromTos.range1.check(true);
            fromTos.range2.check(true);
            fromTos.range3.check(true);
            fromTos.range4.check(true);

            expect(fromTos.range1.from.dateString).toBe(nowString);
            expect(fromTos.range1.to.dateString).toBe(now7String);

            expect(fromTos.range2.from.dateString).toBe(now8String);
            expect(fromTos.range2.to.dateString).toBe(now15String);

            expect(fromTos.range3.from.dateString).toBe(now16String);
            expect(fromTos.range3.to.dateString).toBe(now23String);

            expect(fromTos.range4.from.dateString).toBe(now24String);
            expect(fromTos.range4.to.dateString).toBe(now31String);

            expect(fromTos.totalCertDays).toBe(28);

        });

        it ('can 4 dates', function () {
            var fromTos = DateRangeService.FromTos.build(['range1', 'range2', 'range3', 'range4']);

            fromTos.range1.check(true);
            fromTos.range2.check(true);
            fromTos.range3.check(true);
            fromTos.range4.check(true);

            expect(fromTos.range1.from.dateString).toBe(nowString);
            expect(fromTos.range1.to.dateString).toBe(now7String);

            expect(fromTos.range2.from.dateString).toBe(now8String);
            expect(fromTos.range2.to.dateString).toBe(now15String);

            expect(fromTos.range3.from.dateString).toBe(now16String);
            expect(fromTos.range3.to.dateString).toBe(now23String);

            expect(fromTos.range4.from.dateString).toBe(now24String);
            expect(fromTos.range4.to.dateString).toBe(now31String);

            expect(fromTos.totalCertDays).toBe(28);

            // uncheck 2 ranges
            fromTos.range3.check(false);
            fromTos.range4.check(false);

            expect(fromTos.totalCertDays).toBe(14);

            // add a date to range3
            var now26 = moment(now24).add(2, 'days');

            var now26String = now26.format('YYYY-MM-DD');
            fromTos.range3.from.update(now24String);
            fromTos.range3.to.update(now26String);

            // result should be 7+7 + (24 to 26 = 3 days ) +3 = 17
            expect(fromTos.totalCertDays).toBe(17);

        });

        it ('can validate when a from date is after a to date', function () {
            var fromTos = DateRangeService.FromTos.build(['range1', 'range2', 'range3', 'range4']);
            fromTos.range1.from.update('2015-04-07');
            fromTos.range1.to.update('2015-04-05');
            expect(fromTos.totalCertDays).toBe(0);
            expect(fromTos.range1.valid).toBeFalsy();
        });

        it ('can validate when a from date is the same as a to date', function () {
            var fromTos = DateRangeService.FromTos.build(['range1', 'range2', 'range3', 'range4']);
            fromTos.range1.from.update('2015-04-07');
            fromTos.range1.to.update('2015-04-07');
            expect(fromTos.totalCertDays).toBe(1);
            expect(fromTos.range1.valid).toBeTruthy();
        });

        it ('can validate when two ranges overlap', function () {
            var fromTos = DateRangeService.FromTos.build(['range1', 'range2', 'range3', 'range4']);
            fromTos.range1.from.update('2015-04-07');
            fromTos.range1.to.update('2015-04-14');
            // should be 8 days

            // ------|    |------- original
            // ----|    |--------- 1
            // ----|         |---- 2
            // ---------|    |---- 3
            // 1
            fromTos.range2.from.update('2015-04-04');
            fromTos.range2.to.update('2015-04-10');
            // 7 days

            expect(fromTos.totalCertDays).toBe(0);
            expect(fromTos.range1.overlap).toBeTruthy();
            expect(fromTos.range2.overlap).toBeTruthy();

            //// 2
            fromTos.range2.from.update('2015-04-04');
            fromTos.range2.to.update('2015-04-16');

            expect(fromTos.totalCertDays).toBe(0);
            expect(fromTos.range1.overlap).toBeTruthy();
            expect(fromTos.range2.overlap).toBeTruthy();

            // 3
            fromTos.range2.from.update('2015-04-10');
            fromTos.range2.to.update('2015-04-16');

            expect(fromTos.totalCertDays).toBe(0);
            expect(fromTos.range1.overlap).toBeTruthy();
            expect(fromTos.range2.overlap).toBeTruthy();


            // back to valid
            fromTos.range2.from.update('2015-04-15');
            fromTos.range2.to.update('2015-04-15');

            expect(fromTos.totalCertDays).toBe(9);
            expect(fromTos.range1.valid).toBeTruthy();
            expect(fromTos.range2.valid).toBeTruthy();

        });

        it ('can vallidate that the min and max dates are within 6 months', function () {
            var fromTos = DateRangeService.FromTos.build(['range1', 'range2']);

            var now7MonthsAheadString = moment('2015-01-01').add(7, 'months').format('YYYY-MM-DD');

            // simulate check with fixed dates
            fromTos.range1.update({from:'2015-01-01', to:'2015-01-08'});
            fromTos.range2.update({from:'2015-01-09', to:'2015-01-16'});

            expect(fromTos.datesPeriodTooLong).toBeFalsy();

            fromTos.range2.to.update(now7MonthsAheadString);

            expect(fromTos.datesPeriodTooLong).toBeTruthy();

            // take the date below 6 months
            var now4MonthsAheadString = moment('2015-01-01').add(4, 'months').format('YYYY-MM-DD');

            fromTos.range2.to.update(now4MonthsAheadString);

            expect(fromTos.datesPeriodTooLong).toBeFalsy();

            // take the date exactly below 6 months
            var plus6minus1day = moment('2015-01-01').add(6, 'months');
            plus6minus1day.subtract(1, 'days');
            fromTos.range2.to.update(plus6minus1day);

            expect(fromTos.datesPeriodTooLong).toBeFalsy();

            plus6minus1day.add(1, 'days');
            fromTos.range2.to.update(plus6minus1day);

            expect(fromTos.datesPeriodTooLong).toBeTruthy();
        });

    });

});
