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
 * Common intyg proxy functions. All intyg-related REST-functions goes here.
 */
angular.module('common').factory('common.IntygProxy',
    ['$http', '$log', 'common.statService', function($http, $log, statService) {
        'use strict';

        function _handleError(callback, error) {
            if (callback) {
                callback(error);
            } else {
                $log.error(error);
            }
        }

        /*
         * Load certificate details from the server.
         */
        function _getIntyg(intygsId, intygsTyp, onSuccess, onError) {
            $log.debug('_getCertificate id:' + intygsId + ' intygsTyp: ' + intygsTyp);
            var restPath = '/moduleapi/intyg/' + intygsTyp + '/' + intygsId;
            $http.get(restPath).success(function(data) {
                $log.debug('_getCertificate data:' + data);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                onError(data);
            });
        }

        function _sendIntyg(intygsId, intygsTyp, recipientId, patientConsent, onSuccess, onError) {
            $log.debug('_sendSigneratIntyg: ' + intygsId);
            var restPath = '/moduleapi/intyg/' + intygsTyp + '/' + intygsId + '/skicka';
            $http.post(restPath, {'recipient': recipientId, 'patientConsent': patientConsent}).
                success(function(data) {
                    onSuccess(data);
                }).
                error(function(error) {
                    _handleError(onError, error);
                });
        }

        function _makuleraIntyg(intygsId, intygsTyp, onSuccess, onError) {
            $log.debug('_revokeSigneratIntyg: ' + intygsId + ' intygsTyp: ' + intygsTyp);
            var restPath = '/moduleapi/intyg/' + intygsTyp + '/' + intygsId + '/aterkalla';
            $http.post(restPath, {}).
                success(function(data) {
                    if (data === 'OK') {
                        onSuccess();
                    } else {
                        onError();
                    }
                }).
                error(function(error) {
                    _handleError(onError, error);
                });
        }

        function _fornyaOrCopyIntyg (action) {
            var restEndpoint;

            switch (action) {
                case 'copy':
                    restEndpoint = 'kopiera';
                    break;
                case 'fornya':
                    restEndpoint = 'fornya';
                    break;
                default:
                    throw new Error('common.IntygProxy#_fornyaOrCopyIntyg: Unknown action parameter', action);
            }

            return function doFornyaOrCopyIntyg (intygCopyRequest, onSuccess, onError) {
                $log.debug(action + ' intyg' + intygCopyRequest.intygType + ', ' + intygCopyRequest.intygId);

                var payload = {};
                payload.patientPersonnummer = intygCopyRequest.patientPersonnummer;
                if (intygCopyRequest.nyttPatientPersonnummer) {
                    payload.nyttPatientPersonnummer = intygCopyRequest.nyttPatientPersonnummer;
                }

                var restPath = '/api/intyg/' +
                    intygCopyRequest.intygType +
                    '/' +
                    intygCopyRequest.intygId +
                    '/' +
                    restEndpoint +
                    '/';
                $http.post(restPath, payload).success(function(data) {
                    $log.debug('got callback data: ' + data);
                    onSuccess(data);
                    statService.refreshStat();

                }).error(function(data, status) {
                    $log.error('error ' + status);
                    onError(data);
                });
            };
        }

        function _logPrint(intygsId, intygsTyp, onSuccess, onError) {
            $log.debug('_logPrint, intygsId: ' + intygsId + ' intygsTyp: ' + intygsTyp);
            var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + intygsId + '/loggautskrift';
            $http.post(restPath, intygsId).
                success(function(data) {
                    onSuccess(data);
                }).
                error(function(error) {
                    _handleError(onError, error);
                });
        }

        // Return public API for the service
        return {
            getIntyg: _getIntyg,
            makuleraIntyg: _makuleraIntyg,
            sendIntyg: _sendIntyg,
            copyIntyg: _fornyaOrCopyIntyg('copy'),
            fornyaIntyg: _fornyaOrCopyIntyg('fornya'),
            logPrint: _logPrint
        };
    }]);
