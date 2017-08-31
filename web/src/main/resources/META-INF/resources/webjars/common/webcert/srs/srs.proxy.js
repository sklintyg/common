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

angular.module('common').factory('common.srsProxy', ['$http', '$q', '$log',
    function ($http, $q, $log) {
        'use strict';

        /*
         * get diagnosis by code
         */
        function _getSRSHelpTextsByCode(diagnosisCode) {

            var restPath = '/api/fmb/' + diagnosisCode.toUpperCase();
            return $http.get(restPath)
                .then(function successfullCallback(response) {
                    return response.data;
                },
                function errorCallback(response) {
                    return response.data;
                });

            var deferred = $q.defer(),
                restPath = '/api/fmb/' + diagnosisCode.toUpperCase();

            $http.get(restPath).success(function (response) {
                deferred.resolve(response);
            }).error(function (response, status) {
                $log.error('error ' + status);
                deferred.reject(status);
            });

            return deferred.promise;
        }

        function _setConsent(consentGiven) {
            return "you have " + (!consentGiven ? "not " : "") + "accepeted";
        }

        function _test(){
            return $http.get('/api/srs/191212121212/J20?isPrediktion=true&isAtgard=true&isStatistik=true').then(function(response) {
                return response.data;
              });
        }

        // Return public API for the service
        return {
            test: _test,
            getSRSHelpTextsByCode: _getSRSHelpTextsByCode,
            setConsent: _setConsent,
        };
    }]);

