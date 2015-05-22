angular.module('common').factory('common.statService',
    ['$http', '$log', '$rootScope', '$timeout', 'common.User', function($http, $log, $rootScope, $timeout, User) {
        'use strict';

        var timeOutPromise;
        var msPollingInterval = 60 * 1000;
        var lastData = null;

        /*
         * stop regular polling of stats from server
         */
        function _stopPolling() {
            if (timeOutPromise) {
                $timeout.cancel(timeOutPromise);
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
                timeOutPromise = $timeout(_refreshStat, msPollingInterval);
            }).error(function(data, status) {
                $log.error('_getStat error ' + status);
                _stopPolling();
                timeOutPromise = $timeout(_refreshStat, msPollingInterval);
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
