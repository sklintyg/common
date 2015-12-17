/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

angular.module('common').factory('common.IntygSendService',
    function($http, $log) {
        'use strict';

        function _sendCertificate(certId, recipientId, callback) {
            $http.put('/moduleapi/certificate/' + certId + '/send/' + recipientId, {}).success(function(data) {
                $log.debug('Sending certificate:' + certId + ' to target: ' + recipientId);
                callback(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                //give calling code a chance to handle error
                callback(null);
            });
        }

        function _getRecipients(type, onSuccess, onError) {
            $log.debug('_getRecipients type: ' + type);
            var restPath = '/api/certificates/' + type + '/recipients';
            $http.get(restPath).
                success(function(data) {
                    onSuccess(data);
                }).
                error(function(data, status) {
                    $log.error('error ' + status);
                    onError(data);
                });
        }

        // Return public API for our service
        return {
            sendCertificate: _sendCertificate,
            getRecipients: _getRecipients
        };
    });
