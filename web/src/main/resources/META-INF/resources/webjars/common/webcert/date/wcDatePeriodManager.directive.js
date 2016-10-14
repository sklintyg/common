/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

angular.module('common').directive('wcDatePeriodManager',
    ['common.DateUtilsService', 'common.wcDatePeriodFieldHelper', function(dateUtilsService, datePeriodFieldHelper) {
        'use strict';

        var datePeriods = [];

        var updateMoments = function() {
            for(var i=1; i<datePeriods.length; i++) {
                var datePeriod = datePeriods[i];
                datePeriod.from.moment = dateUtilsService.toMomentStrict(datePeriod.from.ngModel.$modelValue);
                datePeriod.tom.moment = dateUtilsService.toMomentStrict(datePeriod.tom.ngModel.$modelValue);
            }
        };

        var clearOverlaps = function() {
            for(var i=1; i<datePeriods.length; i++) {
                var datePeriod = datePeriods[i];
                datePeriod.overlap = false;
            }
        };

        var updateOverlaps = function() {
            for(var i=1; i<datePeriods.length; i++){
                var datePeriod = datePeriods[i];
                for(var j = i + 1; j < datePeriods.length; j++) {
                    var datePeriod2 = datePeriods[j];
                    var hasOverlap = datePeriodFieldHelper.hasOverlap(datePeriod, datePeriod2);
                    if (hasOverlap) {
                        datePeriod.overlap = true;
                        datePeriod2.overlap = true;
                    }
                }
            }
        };

        var updateValidity = function() {
            for(var i=1; i<datePeriods.length; i++) {
                var datePeriod = datePeriods[i];
                if (datePeriod.overlap) {
                    datePeriod.from.ngModel.$setValidity('dateperiod', false);
                    datePeriod.tom.ngModel.$setValidity('dateperiod', false);
                } else {
                    datePeriod.from.ngModel.$setValidity('dateperiod', true);
                    datePeriod.tom.ngModel.$setValidity('dateperiod', true);
                }
            }
        };

        var datePeriodValidator = function() {
            updateMoments();
            clearOverlaps();
            updateOverlaps();
            updateValidity();
        };

        return {
            restrict: 'A',
            scope: {
                model: '='
            },
            controller: function($scope) {

                this.registerDatePeriod = function(ngModel, fieldOptions) {
                    if (!datePeriods[fieldOptions.index]) {
                        datePeriods[fieldOptions.index] = {};
                    }
                    datePeriods[fieldOptions.index][fieldOptions.type] = {
                        ngModel: ngModel,
                        fieldOptions: fieldOptions
                    };

                    ngModel.$validators.datePeriod = datePeriodValidator;
                };

                /*
                 If user enters a valid "in the future" code such a "d40" into the tom-field, it's
                 date value should be set to from-date + 39 so that the total length is 40 days (requires a valid date in the from-field).
                 */
                    this.applyToDateCodes = function(index) {
                        var fromField = datePeriods[index].from;
                        var tomField = datePeriods[index].tom;

                        //1. fromField must have a valid date for his to work
                        if (!fromField.moment || !fromField.moment.isValid()) {
                            return;
                        }

                        //2. The entered code must be a parsable daysInFuture expression
                        var days = dateUtilsService.parseDayCodes(tomField.ngModel.$viewValue);
                        if (days !== null) {
                            //Take away 1 day, because the dayCode defines the total length of the interval we should get.
                            var newTomMoment = moment(fromField.moment).add(days - 1, 'days');

                            tomField.ngModel.$setViewValue(newTomMoment.format('YYYY-MM-DD'));
                            tomField.ngModel.$setValidity('date', true);
                            tomField.ngModel.$render();
                            return;
                        }

                    };
            }
        };
    }]);
