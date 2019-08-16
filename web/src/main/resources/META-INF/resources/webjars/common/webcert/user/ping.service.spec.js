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

describe('Service: PingService', function() {
  'use strict';
  var $httpBackend, $interval, pingService;

  // Load the module and mock away everything that is needed necessary.
  beforeEach(angular.mock.module('common', function(/*$provide*/) {

  }));

  // Initialize the controller and a mock scope

  beforeEach(angular.mock.inject([
    '$httpBackend', '$interval', 'common.pingService',
    function(_$httpBackend_, _$interval_, _pingService_) {
      $interval = _$interval_;
      $httpBackend = _$httpBackend_;
      pingService = _pingService_;
    }
  ]));

  describe('pingService', function() {
    it('should not ping backend until after threshold delay value', function() {

      pingService.registerUserAction('Test');
      $interval.flush(pingService._THROTTLE_VALUE_ - 10);

    });
  });

  describe('pingService', function() {
    it('should ping backend after threshold delay value', function() {

      $httpBackend.expectGET('/api/anvandare/ping').respond(200);

      pingService.registerUserAction('Test');

      $interval.flush(pingService._THROTTLE_VALUE_ + 10);

      expect($httpBackend.flush).not.toThrow();

    });
  });

  describe('pingService', function() {
    it('should throttle backend ping requests', function() {

      $httpBackend.expectGET('/api/anvandare/ping').respond(200);

      pingService.registerUserAction('Test1');
      pingService.registerUserAction('Test2');
      pingService.registerUserAction('Test3');
      pingService.registerUserAction('Test4');
      $interval.flush(pingService._THROTTLE_VALUE_ + 10);
      expect($httpBackend.flush).not.toThrow();

    });
  });

  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

});
