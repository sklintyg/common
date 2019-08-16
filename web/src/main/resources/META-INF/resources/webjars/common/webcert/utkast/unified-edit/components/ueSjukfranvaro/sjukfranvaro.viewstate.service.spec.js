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

describe('sjukfranvaro', function() {
  'use strict';

  var SjukfranvaroViewState;
  var model;

  var dirtyCallback = function() {

  };

  beforeEach(angular.mock.module('common'));

  beforeEach(inject(['common.SjukfranvaroViewStateService', function(_SjukfranvaroViewState_) {
    SjukfranvaroViewState = _SjukfranvaroViewState_;
    SjukfranvaroViewState.reset();
    model = [
      {
        checked: false,
        niva: '100',
        period: {
          from: '',
          tom: ''
        }
      },
      {
        checked: false,
        niva: '50',
        period: {
          from: '',
          tom: ''
        }
      }
    ];
    SjukfranvaroViewState.setup(model, 4, dirtyCallback);
    SjukfranvaroViewState.updatePeriods();
  }]));

  it('Should be able to calculate workperiod', function() {

    model[0].period.from = '2018-12-03';
    model[0].period.tom = '2018-12-06';
    model[0].checked = true;

    SjukfranvaroViewState.updatePeriods();

    expect(SjukfranvaroViewState.totalDays).toBe(4);
  });

  it('Should be able to calculate total workperiod with several periods', function() {

    model[0].period.from = '2018-12-03';
    model[0].period.tom = '2018-12-06';
    model[0].checked = true;

    model[1].period.from = '2018-12-07';
    model[1].period.tom = '2018-12-12';
    model[1].checked = true;

    SjukfranvaroViewState.updatePeriods();

    expect(SjukfranvaroViewState.totalDays).toBe(10);
  });

  it('Should set today if no previous sjukskrivningsgrad', function() {
    // Simulate user enables checkbox 50% sjukskrivning
    model[0].checked = true;
    SjukfranvaroViewState.updateCheckBox(0);

    var now = new Date();
    var month = now.getMonth() + 1;
    month = (month < 10) ? '0' + month : month;
    var day = now.getDate();
    day = (day < 10) ? '0' + day : day;

    var today = now.getFullYear() + '-' + month + '-' + day;
    expect(model[0].period.from).toBe(today);
    expect(model[0].period.tom).toBe(undefined);
  });

  it('Should be able to continue period from a higher sjukskrivningsgrad', function() {

    model[0].period.from = '2018-12-03';
    model[0].period.tom = '2018-12-06';
    model[0].checked = true;

    SjukfranvaroViewState.updatePeriods();

    // Simulate user enables next row checkbox
    model[1].checked = true;
    SjukfranvaroViewState.updateCheckBox(1);

    expect(model[1].period.from).toBe('2018-12-07');
    expect(model[1].period.tom).toBe(undefined);
  });

});

