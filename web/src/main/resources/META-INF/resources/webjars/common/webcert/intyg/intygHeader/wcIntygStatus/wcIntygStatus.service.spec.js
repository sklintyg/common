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

describe('wcIntygStatus', function() {
  'use strict';

  var IntygStatusService;

  beforeEach(angular.mock.module('htmlTemplates'));
  beforeEach(angular.mock.module('common'));
  beforeEach(inject(['common.IntygStatusService',
    function(_IntygStatusService_) {
      IntygStatusService = _IntygStatusService_;
    }]));

  describe('Intyg status ', function() {

    it('without code should always be last', function() {

      var statuses = [{
        code: 'is-001',
        timestamp: '2018-02-27T16:22:59'
      }, {
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-001',
        timestamp: '2018-02-27T16:22:59'
      }];

      IntygStatusService.sortByStatusAndTimestamp(statuses);
      expect(statuses[0].code).toBe('is-001');
      expect(statuses[1].code).toBe('is-001');
      expect(statuses[2].code).toBeUndefined();
    });

    it('is-001 should always be after is-008', function() {

      var statuses = [{
        code: 'is-001',
        timestamp: '2018-02-27T16:22:59'
      }, {
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-001',
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-008',
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-001',
        timestamp: '2018-02-27T16:22:59'
      }];

      IntygStatusService.sortByStatusAndTimestamp(statuses);
      expect(statuses[0].code).toBe('is-008');
      expect(statuses[1].code).toBe('is-001');
      expect(statuses[2].code).toBe('is-001');
      expect(statuses[3].code).toBe('is-001');
      expect(statuses[4].code).toBeUndefined();
    });

    it('is-008 should always be after is-002', function() {

      var statuses = [{
        code: 'is-001',
        timestamp: '2018-02-27T16:22:59'
      }, {
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-008',
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-002',
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-001',
        timestamp: '2018-02-27T16:22:59'
      }];

      IntygStatusService.sortByStatusAndTimestamp(statuses);
      expect(statuses[0].code).toBe('is-002');
      expect(statuses[1].code).toBe('is-008');
      expect(statuses[2].code).toBe('is-001');
      expect(statuses[3].code).toBe('is-001');
      expect(statuses[4].code).toBeUndefined();
    });

    it('with latest timestamp should be first', function() {

      var statuses = [{
        code: 'is-001',
        timestamp: '2018-02-27T16:22:59'
      }, {
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-008',
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-002',
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-001',
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-004',
        timestamp: '2018-02-27T16:22:59'
      }, {
        code: 'is-005',
        timestamp: '2018-02-27T18:22:59'
      }, {
        code: 'is-003',
        timestamp: '2018-02-27T17:22:59'
      }];

      IntygStatusService.sortByStatusAndTimestamp(statuses);
      expect(statuses[0].code).toBe('is-005');
      expect(statuses[1].code).toBe('is-003');
      expect(statuses[2].code).toBe('is-004');
      expect(statuses[3].code).toBe('is-002');
      expect(statuses[4].code).toBe('is-008');
      expect(statuses[5].code).toBe('is-001');
      expect(statuses[6].code).toBe('is-001');
      expect(statuses[7].code).toBeUndefined();
    });

  });
});