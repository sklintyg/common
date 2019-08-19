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

describe('commonUtkastViewstateService', function() {
  'use strict';

  var CommonUtkastViewState;

  beforeEach(angular.mock.inject(['common.UtkastViewStateService', 'common.User',
    function(_CommonUtkastViewState_, CommonUser) {
      CommonUtkastViewState = _CommonUtkastViewState_;
      CommonUser.getUser = function() {
        return {
          valdVardenhet: {
            id: 'enhetId'
          }
        };
      };
    }
  ]));

  describe('isCopied', function() {
    it('should be false by default', function() {
      expect(CommonUtkastViewState.isCopied()).toBeFalsy();
    });

    it('should be true when utkastCopy exists', function() {
      var draftModel = {
        update: function() {
        },
        isLocked: function() {
        },
        isSigned: function() {
        },
        isRevoked: function() {
        },
        isDraftComplete: function() {
        }
      };

      var data = {
        relations: {
          latestChildRelations: {
            utkastCopy: {}
          }
        },
        content: {
          grundData: {
            skapadAv: {
              vardenhet: {
                enhetsid: '123'
              }
            }
          }
        }
      };

      CommonUtkastViewState.update(draftModel, data);

      expect(CommonUtkastViewState.isCopied()).toBeTruthy();
    });
  });

  describe('getCopyUtkastId', function() {
    it('should be null by default', function() {
      expect(CommonUtkastViewState.getCopyUtkastId()).toBeNull();
    });

    it('should be true when utkastCopy exists', function() {
      var draftModel = {
        update: function() {
        },
        isLocked: function() {
        },
        isSigned: function() {
        },
        isRevoked: function() {
        },
        isDraftComplete: function() {
        }
      };

      var data = {
        relations: {
          latestChildRelations: {
            utkastCopy: {
              intygsId: 'copy-id'
            }
          }
        },
        content: {
          grundData: {
            skapadAv: {
              vardenhet: {
                enhetsid: '123'
              }
            }
          }
        }
      };

      CommonUtkastViewState.update(draftModel, data);

      expect(CommonUtkastViewState.getCopyUtkastId()).toEqual('copy-id');
    });
  });
});
