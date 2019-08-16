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

describe('commonIntygViewstateService', function() {
  'use strict';

  var CommonIntygViewState;

  beforeEach(angular.mock.inject(['common.IntygViewStateService',
    function(_CommonIntygViewState_) {
      CommonIntygViewState = _CommonIntygViewState_;
    }
  ]));

  describe('isRevoked', function() {
    it('should be true if intyg is not already makulerat or on queue to be makulerat', function() {
      CommonIntygViewState.isIntygOnRevokeQueue = true;
      CommonIntygViewState.intygProperties.isRevoked = false;
      expect(CommonIntygViewState.isRevoked()).toBeTruthy();

      CommonIntygViewState.isIntygOnRevokeQueue = false;
      CommonIntygViewState.intygProperties.isRevoked = true;
      expect(CommonIntygViewState.isRevoked()).toBeTruthy();

      CommonIntygViewState.isIntygOnRevokeQueue = true;
      CommonIntygViewState.intygProperties.isRevoked = true;
      expect(CommonIntygViewState.isRevoked()).toBeTruthy();
    });
    it('should be false if intyg is already makulerat or on queue to be makulerat', function() {
      CommonIntygViewState.isIntygOnRevokeQueue = false;
      CommonIntygViewState.intygProperties.isRevoked = false;
      expect(CommonIntygViewState.isRevoked()).toBeFalsy();
    });
  });
});
