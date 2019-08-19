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

describe('DatePeriodFieldHelper', function() {
  'use strict';

  var DatePeriodFieldHelper;

  beforeEach(angular.mock.module('common'), function($provide) {
  });

  beforeEach(angular.mock.inject(['common.wcDatePeriodFieldHelper',
    function(_DatePeriodFieldHelper_) {
      DatePeriodFieldHelper = _DatePeriodFieldHelper_;
    }
  ]));

  function buildPeriod(from, tom) {
    return {
      from: {moment: moment(from, 'YYYYMMDD')},
      tom: {moment: moment(tom, 'YYYYMMDD')}
    };
  }

  it('can detect nonoverlap', function() {
    var period1 = buildPeriod('20150410', '20150417');
    var period2 = buildPeriod('20150401', '20150403');

    var overlap = DatePeriodFieldHelper.hasOverlap(period1, period2);
    expect(overlap).toBeFalsy();
  });

  it('can detect overlap (2nd starts inside 1st)', function() {
    var period1 = buildPeriod('20150410', '20150417');
    var period2 = buildPeriod('20150415', '20150420');

    var overlap = DatePeriodFieldHelper.hasOverlap(period1, period2);
    expect(overlap).toBeTruthy();
  });

  it('can detect overlap (2nd ends inside 1st)', function() {
    var period1 = buildPeriod('20150415', '20150420');
    var period2 = buildPeriod('20150410', '20150417');

    var overlap = DatePeriodFieldHelper.hasOverlap(period1, period2);
    expect(overlap).toBeTruthy();
  });

  it('can detect overlap (1st is inside 2nd)', function() {
    var period1 = buildPeriod('20150410', '20150417');
    var period2 = buildPeriod('20150408', '20150420');

    var overlap = DatePeriodFieldHelper.hasOverlap(period1, period2);
    expect(overlap).toBeTruthy();
  });

  it('can detect overlap (2nd is inside 1st)', function() {
    var period1 = buildPeriod('20150408', '20150420');
    var period2 = buildPeriod('20150410', '20150417');

    var overlap = DatePeriodFieldHelper.hasOverlap(period1, period2);
    expect(overlap).toBeTruthy();
  });

  it('can detect overlap with same date', function() {
    var period1 = buildPeriod('20150410', '20150417');
    var period2 = buildPeriod('20150417', '20150420');

    var overlap = DatePeriodFieldHelper.hasOverlap(period1, period2);
    expect(overlap).toBeTruthy();
  });

  it('can detect overlap with exactly same dates', function() {
    var period1 = buildPeriod('20150410', '20150417');
    var period2 = buildPeriod('20150410', '20150417');

    var overlap = DatePeriodFieldHelper.hasOverlap(period1, period2);
    expect(overlap).toBeTruthy();
  });

  it('can detect overlap with same end date', function() {
    var period1 = buildPeriod('20150410', '20150417');
    var period2 = buildPeriod('20150415', '20150417');

    var overlap = DatePeriodFieldHelper.hasOverlap(period1, period2);
    expect(overlap).toBeTruthy();
  });

});