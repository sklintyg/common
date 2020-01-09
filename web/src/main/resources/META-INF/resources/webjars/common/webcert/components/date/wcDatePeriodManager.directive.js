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
angular.module('common').directive('wcDatePeriodManager',
    ['common.DateUtilsService', 'common.wcDatePeriodFieldHelper', 'common.wcDatePeriodShorthandService', function(dateUtilsService,
        datePeriodFieldHelper, datePeriodShorthandService) {
        'use strict';

        var datePeriods = {};

        var updateMoments = function() {
            angular.forEach(datePeriods, function(datePeriod) {
                datePeriod.from.moment = dateUtilsService.convertDateStrict(datePeriod.from.ngModel.$modelValue);
                datePeriod.tom.moment = dateUtilsService.convertDateStrict(datePeriod.tom.ngModel.$modelValue);
            });
        };

        var clearOverlaps = function() {
            angular.forEach(datePeriods, function(datePeriod) {
                datePeriod.overlap = false;
            });
        };

        var updateOverlaps = function() {
            angular.forEach(datePeriods, function(datePeriod) {
                angular.forEach(datePeriods, function(datePeriod2) {
                    if (datePeriod !== datePeriod2) {
                        var hasOverlap = datePeriodFieldHelper.hasOverlap(datePeriod, datePeriod2);
                        if (hasOverlap) {
                            datePeriod.overlap = true;
                            datePeriod2.overlap = true;
                        }
                    }
                });
            });
        };

        var updateValidity = function() {
            angular.forEach(datePeriods, function(datePeriod) {
                if (datePeriod.overlap) {
                    datePeriod.from.ngModel.$setValidity('dateperiod', false);
                    datePeriod.tom.ngModel.$setValidity('dateperiod', false);
                } else {
                    datePeriod.from.ngModel.$setValidity('dateperiod', true);
                    datePeriod.tom.ngModel.$setValidity('dateperiod', true);
                }
            });
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
                model: '=',
                validateDatePeriods: '@'
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

                    if ($scope.validateDatePeriods) {
                        ngModel.$validators.datePeriod = datePeriodValidator;
                    }
                };

                /*
                 If user enters a valid "in the future" code such a "d40" into the tom-field, it's
                 date value should be set to from-date + 39 so that the total length is 40 days (requires a valid date in the from-field).
                 */
                this.applyToDateCodes = function(index) {
                    var fromField = datePeriods[index].from;
                    var tomField = datePeriods[index].tom;

                    var newTomValue = datePeriodShorthandService.applyToDateCodes(
                        fromField.ngModel.$viewValue, tomField.ngModel.$viewValue);
                    if (newTomValue) {
                        tomField.ngModel.$setViewValue(newTomValue);
                        tomField.ngModel.$setValidity('date', true);
                        tomField.ngModel.$render();
                    }
                };
            }
        };
    }]);
