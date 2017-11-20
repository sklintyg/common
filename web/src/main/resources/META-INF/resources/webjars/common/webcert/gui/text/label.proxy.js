angular.module('common').factory('common.DynamicLabelProxy', [
    '$http', '$log', '$q', 'common.ObjectHelper', 'networkConfig',
    function($http, $log, $q, ObjectHelper, networkConfig) {
        'use strict';

        var timeout = networkConfig.defaultTimeout;

        /*
         * Get the dynamic label for this intyg type
         */
        function _getDynamicLabels(intygType, version) {

            var promise = $q.defer();

            // Don't even bother with old intyg types
            if (!version || intygType === 'fk7263') {
                promise.resolve(null);
            } else {
                var restPath = '/api/utkast/questions/' + intygType + '/' + version;
                $http.get(restPath, {timeout: timeout}).then(function(response) {
                    $log.debug('registration - got data:');
                    $log.debug(response.data);
                    if (!ObjectHelper.isDefined(response.data)) {
                        promise.reject({ errorCode: response.data, message: 'invalid data'});
                    } else {
                        promise.resolve(response.data);
                    }
                }, function(response) {
                    $log.error('error ' + response.status);
                    // Let calling code handle the error of no data response
                    if (response.data === null) {
                        promise.reject({errorCode: response.data, message: 'no response'});
                    } else {
                        promise.reject(response.data);
                    }
                });
            }

            return promise.promise;
        }

        // Return public API for the service
        return {
            getDynamicLabels: _getDynamicLabels
        };
    }]);
