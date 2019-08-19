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

describe('sjukskrivningar', function() {
    'use strict';

    var SjukskrivningarViewState;
    var model;

    beforeEach(angular.mock.module('common'));

    beforeEach(inject(['common.SjukskrivningarViewStateService', function(_SjukskrivningarViewState_) {
        SjukskrivningarViewState = _SjukskrivningarViewState_;
        SjukskrivningarViewState.reset();
        model = {
            HELT_NEDSATT: {
                period: {
                    from: '',
                    tom: ''
                }
            },
            TRE_FJARDEDEL: {
                period: {
                    from: '',
                    tom: ''
                }
            },
            HALFTEN: {
                period: {
                    from: '',
                    tom: ''
                }
            },
            EN_FJARDEDEL: {
                period: {
                    from: '',
                    tom: ''
                }
            }
        };
        SjukskrivningarViewState.setModel(model);
        SjukskrivningarViewState.updatePeriods();
    }]));

    it('Should be able to calculate workperiod', function() {
        model.TRE_FJARDEDEL.period.from = '2016-05-05';
        model.TRE_FJARDEDEL.period.tom = '2016-05-07';
        SjukskrivningarViewState.hoursPerWeek = 38;
        SjukskrivningarViewState.updatePeriods();

        expect(SjukskrivningarViewState.periods.TRE_FJARDEDEL.days).toBe(3);
        expect(SjukskrivningarViewState.periods.TRE_FJARDEDEL.hoursPerWeek).toBe(9.5);
        expect(SjukskrivningarViewState.periods.TRE_FJARDEDEL.valid).toBeTruthy();
        expect(SjukskrivningarViewState.totalDays).toBe(3);
    });

    it('Should be able to calculate total workperiod with several periods', function() {
        model.EN_FJARDEDEL.period.from = '2016-05-05';
        model.EN_FJARDEDEL.period.tom = '2016-05-07';
        model.TRE_FJARDEDEL.period.from = '2016-05-08';
        model.TRE_FJARDEDEL.period.tom = '2016-05-12';
        SjukskrivningarViewState.hoursPerWeek = 36;
        SjukskrivningarViewState.updatePeriods();

        expect(SjukskrivningarViewState.periods.EN_FJARDEDEL.days).toBe(3);
        expect(SjukskrivningarViewState.periods.EN_FJARDEDEL.hoursPerWeek).toBe(27);
        expect(SjukskrivningarViewState.periods.EN_FJARDEDEL.valid).toBeTruthy();
        expect(SjukskrivningarViewState.periods.TRE_FJARDEDEL.days).toBe(5);
        expect(SjukskrivningarViewState.periods.TRE_FJARDEDEL.hoursPerWeek).toBe(9);
        expect(SjukskrivningarViewState.periods.TRE_FJARDEDEL.valid).toBeTruthy();
        expect(SjukskrivningarViewState.totalDays).toBe(8);
    });

    it('Should not set valid workperiod if period is incomplete', function() {
        model.TRE_FJARDEDEL.period.tom = '2016-05-07';
        SjukskrivningarViewState.updatePeriods();

        expect(SjukskrivningarViewState.periods.TRE_FJARDEDEL.valid).toBeFalsy();
    });

    it('Should set today if no previous sjukskrivningsgrad', function() {
        // Simulate user enables checkbox 50% sjukskrivning
        SjukskrivningarViewState.periods.HALFTEN.checked = true;
        SjukskrivningarViewState.updateCheckBox('HALFTEN');

        var now = new Date();
        var month = now.getMonth() + 1;
        month = (month < 10) ? '0' + month : month;
        var day = now.getDate();
        day = (day < 10) ? '0' + day : day;

        var today = now.getFullYear() + '-' + month + '-' + day;
        expect(model.HALFTEN.period.from).toBe(today);
        expect(model.HALFTEN.period.tom).toBe(undefined);
    });

    it('Should be able to continue period from a higher sjukskrivningsgrad', function() {
        model.TRE_FJARDEDEL.period.from = '2016-05-05';
        model.TRE_FJARDEDEL.period.tom = '2016-05-07';
        SjukskrivningarViewState.updatePeriods();

        // Simulate user enables checkbox 50% sjukskrivning
        SjukskrivningarViewState.periods.HALFTEN.checked = true;
        SjukskrivningarViewState.updateCheckBox('HALFTEN');

        expect(model.HALFTEN.period.from).toBe('2016-05-08');
        expect(model.HALFTEN.period.tom).toBe(undefined);
    });

    it('Should be able to continue period from a lower sjukskrivningsgrad', function() {
        model.EN_FJARDEDEL.period.from = '2016-05-05';
        model.EN_FJARDEDEL.period.tom = '2016-05-07';
        SjukskrivningarViewState.updatePeriods();

        // Simulate user enables checkbox 50% sjukskrivning
        SjukskrivningarViewState.periods.HALFTEN.checked = true;
        SjukskrivningarViewState.updateCheckBox('HALFTEN');

        expect(model.HALFTEN.period.from).toBe('2016-05-08');
        expect(model.HALFTEN.period.tom).toBe(undefined);
    });

    it('Should be able to calculate total workhours from a decimal number', function() {
        var result = SjukskrivningarViewState.calculateWorkHours('40', 0);
        expect(result).toBe(0);
        result = SjukskrivningarViewState.calculateWorkHours('40', 0.25);
        expect(result).toBe(10);
        result = SjukskrivningarViewState.calculateWorkHours('40', 0.75);
        expect(result).toBe(30);
        result = SjukskrivningarViewState.calculateWorkHours('35', 0.75);
        expect(result).toBe(26.25);
        result = SjukskrivningarViewState.calculateWorkHours('35.5', 0.25);
        expect(result).toBe(8.88);
        result = SjukskrivningarViewState.calculateWorkHours('35,5', 0.75);
        expect(result).toBe(26.63);
        result = SjukskrivningarViewState.calculateWorkHours('35,5', 0.25);
        expect(result).toBe(8.88);
        result = SjukskrivningarViewState.calculateWorkHours(35.5, 0.25);
        expect(result).toBe(8.88);
        result = SjukskrivningarViewState.calculateWorkHours(35, 0.75);
        expect(result).toBe(26.25);
    });
});

