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
angular.module('common').factory('common.IntygHelper',
    ['$log', '$state', 'common.ObjectHelper',
      function($log, $state, ObjectHelper) {
        'use strict';

        function _goToDraft(type, intygTypeVersion, intygId, extraStateParams) {
          if (ObjectHelper.isEmpty(type) || ObjectHelper.isEmpty(intygTypeVersion) || ObjectHelper.isEmpty(intygId)) {
            $log.error('goToDraft: Mandatory parameter missing, got type=' + type + ', intygTypeVersion=' + intygTypeVersion +
                ' intygId=' + intygId);
            return;
          }

          var stateParams = {
            certificateId: intygId,
            intygTypeVersion: intygTypeVersion
          };

          if (angular.isDefined(extraStateParams)) {
            stateParams = angular.extend(stateParams, extraStateParams);
          }

          $state.go(type + '.utkast', stateParams);
        }

        function _goToIntyg(type, intygTypeVersion, intygId) {
          if (ObjectHelper.isEmpty(type) || ObjectHelper.isEmpty(intygTypeVersion) || ObjectHelper.isEmpty(intygId)) {
            $log.error('goToIntyg: Mandatory parameter missing, got type=' + type + ', intygTypeVersion=' + intygTypeVersion +
                ' intygId=' + intygId);
            return;
          }

          $state.go('webcert.intyg.' + type, {
            certificateId: intygId,
            intygTypeVersion: intygTypeVersion
          });
        }

        function _isSentToTarget(statusArr, target) {
          if (statusArr) {
            for (var i = 0; i < statusArr.length; i++) {
              if (statusArr[i].target === target && statusArr[i].type === 'SENT') {
                return true;
              }
            }
          }
          return false;
        }

        function _sentToTargetTimestamp(statusArr, target) {
          if (statusArr) {
            for (var i = 0; i < statusArr.length; i++) {
              if (statusArr[i].target === target && statusArr[i].type === 'SENT') {
                return statusArr[i].timestamp;
              }
            }
          }
        }

        function _isRevoked(statusArr) {
          if (statusArr) {
            for (var i = 0; i < statusArr.length; i++) {
              if (statusArr[i].type === 'CANCELLED') {
                return true;
              }
            }
          }
          return false;
        }

        function _revokedTimestamp(statusArr) {
          if (statusArr) {
            for (var i = 0; i < statusArr.length; i++) {
              if (statusArr[i].type === 'CANCELLED') {
                return statusArr[i].timestamp;
              }
            }
          }
        }

        // Return public API for the service
        return {
          goToDraft: _goToDraft,
          goToIntyg: _goToIntyg,
          isRevoked: _isRevoked,
          revokedTimestamp: _revokedTimestamp,
          isSentToTarget: _isSentToTarget,
          sentToTargetTimestamp: _sentToTargetTimestamp
        };
      }]);
