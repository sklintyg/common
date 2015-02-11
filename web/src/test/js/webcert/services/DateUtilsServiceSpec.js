describe('DateUtilsService', function() {
    'use strict';

    var DateUtilsService;

    beforeEach(angular.mock.module('common'), function($provide){

    });

    beforeEach(angular.mock.inject(['common.DateUtilsService',
        function(_DateUtilsService) {
            DateUtilsService = _DateUtilsService;
        }
    ]));

    describe('#isDate', function() {

        it ('check that isDate on 2015-01-05 is true', function () {
            var dateString = "2015-01-05";
            expect(DateUtilsService.isDate(dateString)).toBeTruthy();
        });

        it ('check that isDate on 2015-01-6666 is false', function () {
            var dateString = "2015-01-666";
            expect(DateUtilsService.isDate(dateString)).toBeFalsy();
        });

    });

    describe('#toMoment', function() {

        it ('check that a date can be converted to a moment date', function () {
            var date = new Date(2015, 1, 5, 11, 33, 30, 0);
            expect(DateUtilsService.toMoment(date)).not.toBe(null);
        });

        it ('check that we can use moment to formate a date', function () {
            var date = new Date(2015, 0, 5, 11, 33, 30, 0);
            var momentDate = DateUtilsService.toMoment(date);
            var expected = "2015 01 05";
            var result = momentDate.format("YYYY MM DD");
            expect(expected).toBe(result);
        });

    });

    describe('#convertDateToISOString', function() {

        it ('can convert a date to iso', function () {
            var date = new Date(2015, 0, 5, 11, 33, 30, 0);
            var result = DateUtilsService.convertDateToISOString(date);
            var expected = "2015-01-05";
            expect(expected).toBe(result);
        });

        it ('can convert a date with format YY M D', function () {
            var date = new Date(2015, 0, 5, 11, 33, 30, 0);
            var result = DateUtilsService.convertDateToISOString(date, "YY M D");
            var expected = "15 1 5";
            expect(expected).toBe(result);
        });

    });

    describe('#convertDateStrict', function() {

        it ('can convert a date strictly', function () {
            var date = new Date(2015, 0, 5, 11, 33, 30, 0);
            var result = DateUtilsService.convertDateStrict(date);
            // only yyyy, mm and dd, should be able to test that secs is 0
            var expected = "2015-01-05";
            expect(result).toBe(expected);
        });

    });

    describe('#pushValidDate', function() {

        it ('can filter out invalid dates', function () {
            var date = "222222222";
            var list = [];
            DateUtilsService.pushValidDate(list, date);
            // only yyyy, mm and dd, should be able to test that secs is 0

            expect(list.length).toBe(0);
        });

        it ('can push a valid dates', function () {
            var date = "2015-01-05";
            var list = [];
            DateUtilsService.pushValidDate(list, date);

            DateUtilsService.pushValidDate(list, date);
            // only yyyy, mm and dd, should be able to test that secs is 0

            expect(list.length).toBe(2);
        });
    });

    describe('#areDatesWithinMonthRange', function() {

        it ('can check that dates are within month range', function () {
            var startDate = new Date(2014, 11, 5, 11, 33, 30, 0);
            var startMoment = DateUtilsService.toMoment(startDate);

            var endDate = new Date(2015, 4, 4, 11, 33, 30, 0);
            var endMoment = DateUtilsService.toMoment(endDate);

            expect(DateUtilsService.areDatesWithinMonthRange(startMoment, endMoment)).toBeTruthy();
        });
        it ('can check that dates are within month range - edge case', function () {
            var startDate = new Date(2014, 10, 5, 11, 33, 30, 0);
            var startMoment = DateUtilsService.toMoment(startDate);

            var endDate = new Date(2015, 4, 4, 11, 33, 30, 0);
            var endMoment = DateUtilsService.toMoment(endDate);

            expect(DateUtilsService.areDatesWithinMonthRange(startMoment, endMoment)).toBeTruthy();
        });

        it ('can check that dates are not within month range', function () {
            var startDate = new Date(2015, 0, 5, 11, 33, 30, 0);
            var startMoment = DateUtilsService.toMoment(startDate);

            var endDate = new Date(2015, 8, 4, 11, 33, 30, 0);
            var endMoment = DateUtilsService.toMoment(endDate);

            expect(DateUtilsService.areDatesWithinMonthRange(startMoment, endMoment)).toBeFalsy();
        });
        it ('can check that dates are not within month range - edge case', function () {
            var startDate = new Date(2014, 10, 5, 11, 33, 30, 0);
            var startMoment = DateUtilsService.toMoment(startDate);

            var endDate = new Date(2015, 4, 5, 11, 33, 30, 0);
            var endMoment = DateUtilsService.toMoment(endDate);

            expect(DateUtilsService.areDatesWithinMonthRange(startMoment, endMoment)).toBeFalsy();
        });

    });

    describe('#isDateOutOfRange', function() {

        it ('can check that a date is out of range', function () {
            var startMoment = moment();
            // bump the date by 7 months
            startMoment.add(7, 'months');

            expect(DateUtilsService.isDateOutOfRange(startMoment)).toBeTruthy();
        });

        it ('can check that a date is within 6 months ', function () {
            var startMoment = moment();
            // bump the date by 7 months
            startMoment.add(5, 'months');

            expect(DateUtilsService.isDateOutOfRange(startMoment)).toBeFalsy();
        });

    });

    describe('#isDateOutOfRange', function() {

        it ('can check that a date is out of range', function () {
            var startMoment = moment();
            // bump the date by 7 months
            startMoment.add(7, 'months');

            expect(DateUtilsService.isDateOutOfRange(startMoment)).toBeTruthy();
        });

        it ('can check that a date is longer than 6 months and more than one week', function () {
            var startMoment = moment();
            // bump the date by 7 months
            startMoment.add(7, 'months');

            expect(DateUtilsService.olderThanAWeek(startMoment) || DateUtilsService.isDateOutOfRange(startMoment)).toBeTruthy();
        });


        it ('can check that a date is less than a week and less than 6 months from now', function () {
            var startMoment = moment();
            startMoment.add(3, 'days');

            expect(DateUtilsService.olderThanAWeek(startMoment) || DateUtilsService.isDateOutOfRange(startMoment)).toBeFalsy();
        });

    });

    describe('#daysBetween', function() {

        it ('can check that we can get days between two dates', function () {
            var startDate = new Date(2015, 0, 5);
            var startMoment = DateUtilsService.toMoment(startDate);

            var endDate = new Date(2015, 4, 4);
            var endMoment = DateUtilsService.toMoment(endDate);
            var days = DateUtilsService.daysBetween(startMoment, endMoment);
            expect(days).toBe(120);
        });

        it ('can check that we get false if we provide no dates', function () {
            var days = DateUtilsService.daysBetween(null, null);
            expect(days).toBeFalsy();
        });

    });

});
