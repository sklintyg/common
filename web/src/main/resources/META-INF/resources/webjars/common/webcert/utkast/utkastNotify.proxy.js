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
/**
 * Utkast Notify Proxy Module - REST-Functions related to
 * sending notifications of utkast to a doctor via mail.
 */
angular.module('common').factory('common.UtkastNotifyProxy',
    ['$http', '$log',
      function($http, $log) {
        'use strict';

        /*
         * Toggle Notify state of a fragasvar entity with given id
         */
        function _setNotifyState(intygId, intygType, intygVersion, isNotified, callback, errorCallback) {
          $log.debug('_setNotifyState');
          var restPath = '/api/intyg/' + intygType + '/' + intygId + '/' + intygVersion + '/vidarebefordra';
          $http.put(restPath, {'notified': isNotified}).then(function(response) {
            $log.debug('_setNotifyState data:' + response.data);
            callback(response.data);
          }, function(response) {
            $log.error('error ' + response.status);
            errorCallback(response.data);
          });
        }

        /**
         * Ask the backend to send a status update to the journalsystem that the draft is ready to be signed
         */
        function _sendNotificationStatusUpdate(intygId, intygType, utkast, callback, errorCallback) {
          var restPath = '/api/intyg/' + intygType + '/' + intygId + '/' + utkast.version + '/redoattsignera';
          $http.put(restPath, {}).then(function(response) {
            $log.debug('sendNotificationStatusUpdate data:' + response.data);
            utkast.version++;
            callback(response.data);
          }, function(response) {
            $log.error('error ' + response.status);
            errorCallback(response.data);
          });
        }

        // Return public API for the service
        return {
          setNotifyState: _setNotifyState,
          sendNotificationStatusUpdate: _sendNotificationStatusUpdate
        };
      }]);
