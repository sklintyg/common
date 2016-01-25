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

/**
 * Common certificate management methods between certificate modules
 */
angular.module('common').factory('common.IntygService',
    function($http, $log) {
        'use strict';

        /*
         * Load certificate details from the server.
         */
        function _getCertificate(id, onSuccess, onError) {
            $log.debug('_getCertificate id:' + id);
            var restPath = '/moduleapi/certificate/' + id;
            $http.get(restPath).success(function(data) {
                $log.debug('_getCertificate data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                onError(data);
            });
        }


        // Return public API for the service
        return {
            getCertificate: _getCertificate
        };
    });
