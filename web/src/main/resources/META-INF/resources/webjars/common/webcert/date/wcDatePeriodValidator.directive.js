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

angular.module('common').directive('wcDatePeriodValidator',
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
//                    console.log('registerDatePeriod', element, fieldOptions);
                    if (!datePeriods[fieldOptions.index]) {
                        datePeriods[fieldOptions.index] = {};
                    }
                    datePeriods[fieldOptions.index][fieldOptions.type] = {
                        ngModel: ngModel,
                        fieldOptions: fieldOptions
                    };

                    ngModel.$validators.datePeriod = datePeriodValidator;
                };
            }
        };
    }]);
