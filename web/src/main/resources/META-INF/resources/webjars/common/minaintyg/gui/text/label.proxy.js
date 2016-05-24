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
            if (intygType === 'fk7263') {
                promise.resolve(null);
            } else {
                var restPath = '/api/certificates/questions/' + intygType + '/' + version;
                $http.get(restPath, {timeout: timeout}).success(function(data) {
                    $log.debug('registration - got data:');
                    $log.debug(data);
                    if (!ObjectHelper.isDefined(data)) {
                        promise.reject({ errorCode: data, message: 'invalid data'});
                    } else {
                        //var data = sjukersattningDynamicLabelsMock;
                        promise.resolve(data);
                    }
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    // Let calling code handle the error of no data response
                    if (data === null) {
                        promise.reject({errorCode: data, message: 'no response'});
                    } else {
                        promise.reject(data);
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
