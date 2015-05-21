/**
 * Utkast Notify Proxy Module - REST-Functions related to
 * sending notifications of utkast to a doctor via mail.
 */
angular.module('common').factory('common.utkastNotifyProxy',
    ['$http', '$log',
        function($http, $log) {
            'use strict';

            /*
             * Toggle Notify state of a fragasvar entity with given id
             */
            function _setNotifyState(intygId, intygType, intygVersion, isNotified, callback,  errorCallback) {
                $log.debug('_setNotifyState');
                var restPath = '/api/intyg/' + intygType + '/' + intygId + '/' + intygVersion + '/vidarebefordra';
                $http.put(restPath, isNotified.toString()).success(function(data) {
                    $log.debug('_setNotifyState data:' + data);
                    callback(data);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    errorCallback(data);
                });
            }

            // Return public API for the service
            return {
                setNotifyState: _setNotifyState
            };
        }]);