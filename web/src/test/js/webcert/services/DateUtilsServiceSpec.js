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

});
