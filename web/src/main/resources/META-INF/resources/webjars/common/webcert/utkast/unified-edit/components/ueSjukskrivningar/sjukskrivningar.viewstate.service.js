/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
angular.module('common').service('common.SjukskrivningarViewStateService',
    ['common.DateUtilsService',
        function(DateUtilsService) {
            'use strict';

            this.reset = function() {
                this.model = undefined;
                this.periods = {
                    'EN_FJARDEDEL': {},
                    'HALFTEN': {},
                    'TRE_FJARDEDEL': {},
                    'HELT_NEDSATT': {}
                };
                this.hoursPerWeek = undefined;
                this.totalDays = undefined;

                return this;
            };

            this.setModel = function(model) {
                this.model = model;
            };

            this.updateWorkPeriod = function(period) {
                var fromMoment = DateUtilsService.convertDateStrict(this.model[period].period.from);
                var toMoment = DateUtilsService.convertDateStrict(this.model[period].period.tom);

                this.periods[period].valid = false;
                if (fromMoment && toMoment && this.hoursPerWeek) {
                    this.periods[period].valid = true;
                    this.periods[period].days = toMoment.diff(fromMoment, 'days') + 1;

                    var workFactor = 0;
                    if (period === 'EN_FJARDEDEL') {
                        workFactor = 0.75;
                    }
                    else if (period === 'HALFTEN') {
                        workFactor = 0.5;
                    }
                    else if (period === 'TRE_FJARDEDEL') {
                        workFactor = 0.25;
                    }

                    this.periods[period].hoursPerWeek = this.calculateWorkHours(this.hoursPerWeek, workFactor);
                }
            };

            this.calculateWorkHours = function(hoursPerWeek, workFactor) {
                var parsedHours;
                if(typeof hoursPerWeek === 'string'){
                    parsedHours = parseFloat(hoursPerWeek.replace(',', '.'));
                } else if(typeof hoursPerWeek === 'number'){
                    parsedHours = hoursPerWeek;
                }
                var workHours = parsedHours * workFactor;
                workHours = Math.round((workHours + 0.00001) * 100) / 100;
                return workHours;
            };

            this.updateCheckBox = function(period) {

                if (this.periods[period].checked) {

                    // Klickar jag i checkboxen för en sjukskrivningsgrad ska 'från och med' datum sättas till dagens datum,
                    // tabben ska hamna i fältet för 'till och med' datum. 'till och med' rutan ska vara tom.

                    // Väljer jag en andra/tredje/fjärde sjukskrivningsperiod ska 'från och med' datum sättas till
                    // dagen efter 'till och med' datumet för den föregående perioden.

                    var maxDate;
                    angular.forEach(this.model, function(period) {
                        var checkDate = DateUtilsService.convertDateStrict(period.period.tom);
                        if (checkDate && (!maxDate || checkDate > maxDate)) {
                            maxDate = checkDate;
                        }
                    });

                    if (maxDate) {
                        this.model[period].period.from = maxDate.add(1, 'days').format('YYYY-MM-DD');
                    }
                    else {
                        this.model[period].period.from = moment().format('YYYY-MM-DD');
                    }
                    this.model[period].period.tom = undefined;
                    var toEl = $('#sjukskrivningar-' + period + '-tom');
                    if (toEl) {
                        toEl.focus();
                    }
                }
                else {
                    this.model[period].period.from = undefined;
                    this.model[period].period.tom = undefined;
                }

            };

            this.updateHoursPerWeek = function() {
                this.updateWorkPeriod('EN_FJARDEDEL');
                this.updateWorkPeriod('HALFTEN');
                this.updateWorkPeriod('TRE_FJARDEDEL');
                this.updateWorkPeriod('HELT_NEDSATT');
            };

            this.updatePeriods = function() {

                var minDate, maxDate;

                angular.forEach(this.model, function(value, key) {

                    // Om det står något i nåt av fälten kryssa i checkboxen
                    if (value.period.from || value.period.tom) {
                        this.periods[key].checked = true;
                    }

                    var fromMoment = DateUtilsService.convertDateStrict(value.period.from);
                    var toMoment = DateUtilsService.convertDateStrict(value.period.tom);

                    // Get min and max dates
                    if (fromMoment && (!minDate || fromMoment.isBefore(minDate))) {
                        minDate = fromMoment;
                    }
                    if (toMoment && (!maxDate || toMoment.isAfter(maxDate))) {
                        maxDate = toMoment;
                    }

                    // Checkboxen för den valda sjukskrivningsgraden ska fortfarande vara ifylld och endast försvinna
                    // om man klickar ur den, eller tömmer både 'från och med' och 'till och med' datumen.
                    if (!value.period.from && !value.period.tom) {
                        this.periods[key].checked = false;
                    }

                    // Uppdatera värden för arbetstid och period
                    this.updateWorkPeriod(key);
                }, this);

                this.totalDays = undefined;
                if (minDate && maxDate) {
                    this.totalDays = maxDate.diff(minDate, 'days') + 1;
                }

            };
        }
    ]);
