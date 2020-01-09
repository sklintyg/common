/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.pingService',
    ['$http', '$log', '$interval', function($http, $log, $interval) {
        'use strict';

        var pingSessionPromise;

        //(Max) how often should session ping requestsf be sent
        var msMinPingSessionRequestInterval = 30 * 1000;

        /*
         * Extending session by making a request to server
         */
        function _executePingSessionRequest() {
            $log.debug('_executePingSessionRequest =>');
            $http.get('/api/anvandare/ping').then(function() {
                $log.debug('<= _executePingSessionRequest success');
            }, function(response) {
                $log.error('<= _executePingSessionRequest failed: ' + response.status);
            }).finally(function() { // jshint ignore:line
                //clear interval promise no matter the outcome of the request
                if (pingSessionPromise) {
                    $interval.cancel(pingSessionPromise);
                    pingSessionPromise = undefined;
                }
            });
        }


        function _registerUserAction(action) {
            if (!pingSessionPromise) {
                pingSessionPromise = $interval(_executePingSessionRequest, msMinPingSessionRequestInterval);
                $log.debug('_executePingSessionRequest for action ' + action + ' scheduled..');
            } else {
                $log.debug('_executePingSessionRequest already scheduled (ignoring)');
            }
        }


        // Return public API for the service
        return {
            registerUserAction: _registerUserAction,
            _THROTTLE_VALUE_: msMinPingSessionRequestInterval //used by test only
        };
    }]);
