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
angular.module('common').factory('common.ReceiversProxy',
    ['$http', '$log', function($http, $log) {
        'use strict';

        function _getPossibleReceivers(intygtyp, onSuccess, onError) {
            $log.debug('_getPossibleReceivers intygtyp:' + intygtyp);
            var restPath = '/api/receiver/possiblereceivers/' + intygtyp;
            $http.get(restPath).then(function(response) {
                $log.debug('_getPossibleReceivers data:' + response.data);
                onSuccess(response.data);
            },function(response) {
                $log.error('error ' + response.status);
                onError(response.data);
            });
        }

        function _getApprovedReceivers(intygtyp, intygid) {
            $log.debug('_getApprovedReceivers intygtyp:' + intygtyp + ', intygid:' + intygid);
            var restPath = '/api/receiver/approvedreceivers/' + intygtyp + '/' + intygid;
            return $http.get(restPath);
        }

        function _setApprovedReceivers(intygtyp, intygid, approvedlist) {
            $log.debug('_setApprovedReceivers intygtyp:' + intygtyp + ', intygid:' + intygid);
            var restPath = '/api/receiver/registerapproved/' + intygtyp + '/' + intygid;
            return $http.post(restPath, approvedlist);
        }


        // Return public API for the service
        return {
            getPossibleReceivers: _getPossibleReceivers,
            getApprovedReceivers: _getApprovedReceivers,
            setApprovedReceivers: _setApprovedReceivers
        };
    }]);
