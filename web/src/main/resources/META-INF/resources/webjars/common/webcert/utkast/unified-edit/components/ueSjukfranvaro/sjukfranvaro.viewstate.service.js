/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
angular.module('common').service('common.SjukfranvaroViewStateService',
    ['common.DateUtilsService',
        function(DateUtilsService) {
            'use strict';

            this.reset = function() {
                this.model = undefined;
                this.totalDays = undefined;
                this.maxRows = undefined;

                return this;
            };

            this.setup = function(model, maxRows, dirtyCallback) {
                this.model = model;
                this.maxRows = maxRows;
                this.dirtyCallback = dirtyCallback;
            };

            this.updateCheckBox = function(index) {

                if (this.model[index].checked) {

                    // Klickar jag i checkboxen för en sjukskrivningsgrad ska 'från och med' datum sättas till dagens datum,
                    // tabben ska hamna i fältet för 'till och med' datum. 'till och med' rutan ska vara tom.

                    // Väljer jag en andra/tredje/fjärde sjukskrivningsperiod ska 'från och med' datum sättas till
                    // dagen efter 'till och med' datumet för den föregående perioden.

                    var maxDate;
                    angular.forEach(this.model, function(row) {
                        var checkDate = DateUtilsService.convertDateStrict(row.period.tom);
                        if (checkDate && (!maxDate || checkDate > maxDate)) {
                            maxDate = checkDate;
                        }
                    });

                    if (maxDate) {
                        this.model[index].period.from = maxDate.add(1, 'days').format('YYYY-MM-DD');
                    }
                    else {
                        this.model[index].period.from = moment().format('YYYY-MM-DD');
                    }
                    this.model[index].period.tom = undefined;
                    var toEl = $('#sjukfranvaro-' + index + '-tom');
                    if (toEl) {
                        toEl.focus();
                    }
                }
                else {
                    this.model[index].period.from = undefined;
                    this.model[index].period.tom = undefined;
                    if (this.model[index].niva !== 100) {
                        this.model[index].niva = undefined;
                    }
                }
            };

            this.updatePeriods = function() {

                var calculateTotalDays = 0;
                var periods = [];

                angular.forEach(this.model, function(value) {

                    // Om det står något i något av fälten kryssa i checkboxen
                    if (value.period.from || value.period.tom || value.niva) {
                        value.checked = true;
                    }

                    var fromMoment = DateUtilsService.convertDateStrict(value.period.from);
                    var toMoment = DateUtilsService.convertDateStrict(value.period.tom);

                    // Checkboxen för den valda sjukskrivningsgraden ska fortfarande vara ifylld och endast försvinna
                    // om man klickar ur den, eller tömmer både 'från och med' och 'till och med' datumen.
                    if (!value.period.from && !value.period.tom) {
                        value.checked = false;
                    }

                    if (fromMoment && toMoment) {
                        periods.push({from: fromMoment, to: toMoment});
                        calculateTotalDays += toMoment.diff(fromMoment, 'days') + 1;
                    }
                }, this);

                this.totalDays = undefined;
                if (calculateTotalDays && !DateUtilsService.hasOverlap(periods)) {
                    this.totalDays = calculateTotalDays;
                }
            };

            this.addRow = function () {

                if (this.model.length >= this.maxRows) {
                    return;
                }

                this.model.push({
                    checked: false,
                    niva: '',
                    period: {
                        from: '',
                        tom: ''
                    }});
                this.dirtyCallback();
            };

            this.deleteRow = function (index) {

                if (index > 1) {
                    this.model.splice(index, 1);
                }

                this.dirtyCallback();
            };
        }
    ]);
