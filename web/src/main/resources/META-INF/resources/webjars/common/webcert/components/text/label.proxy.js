/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
