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

angular.module('common').factory('common.statService',
    ['$http', '$log', '$rootScope', '$interval', 'common.User', function($http, $log, $rootScope, $interval, User) {
        'use strict';

        var timeOutPromise;
        var msPollingInterval = 60 * 1000;
        var lastData = null;

        $rootScope.$on('$stateChangeSuccess',function() { _refreshStat(); });

        /*
         * stop regular polling of stats from server
         */
        function _stopPolling() {
            if (timeOutPromise) {
                $interval.cancel(timeOutPromise);
                $log.debug('statService -> Stop polling');
            }
        }

        /*
         * get stats from server
         */
        function _refreshStat() {
            $log.debug('_getStat');
            $http.get('/moduleapi/stat/').success(function(data) {
                $log.debug('_getStat success - data:' + data);
                lastData = data;
                $rootScope.$broadcast('wc-stat-update', data);
                _stopPolling();
                timeOutPromise = $interval(_refreshStat, msPollingInterval);
            }).error(function(data, status) {
                $log.error('_getStat error ' + status);
                _stopPolling();
                timeOutPromise = $interval(_refreshStat, msPollingInterval);
            });
        }

        function _getLatestData() {
            return lastData;
        }

        /*
         * start regular polling of stats from server
         */
        function _startPolling() {
            _refreshStat();
            $log.debug('statService -> Start polling');
        }

        // Return public API for the service
        return {
            startPolling: _startPolling,
            stopPolling: _stopPolling,
            refreshStat: _refreshStat,
            getLatestData: _getLatestData
        };
    }]);
