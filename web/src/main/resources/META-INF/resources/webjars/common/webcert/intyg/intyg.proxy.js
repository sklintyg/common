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

        function _sendIntyg(intygsId, intygsTyp, recipientId, onSuccess, onError) {
            $log.debug('_sendSigneratIntyg: ' + intygsId);
            var restPath = '/moduleapi/intyg/' + intygsTyp + '/' + intygsId + '/skicka';
            $http.post(restPath, {'recipient': recipientId}).
                success(function(data) {
                    onSuccess(data);
                }).
                error(function(error) {
                    _handleError(onError, error);
                });
        }

        function _makuleraIntyg(intygId, intygType, revokeMessage, onSuccess, onError) {
            $log.debug('_revokeSigneratIntyg: ' + intygId + ' intygsTyp: ' + intygType);
            var restPath = '/moduleapi/intyg/' + intygType + '/' + intygId + '/aterkalla';
            $http.post(restPath, revokeMessage).success(function(data) {
                if (data === 'OK') {
                    onSuccess();
                } else {
                    onError();
                }
            }).error(function(error) {
                _handleError(onError, error);
            });
        }

        function buildPayloadFromCopyIntygRequest(intygCopyRequest) {
            var payload = {};
            payload.patientPersonnummer = intygCopyRequest.patientPersonnummer;
            if (intygCopyRequest.nyttPatientPersonnummer) {
                payload.nyttPatientPersonnummer = intygCopyRequest.nyttPatientPersonnummer;
            }
            payload.fornamn = intygCopyRequest.fornamn;
            payload.efternamn = intygCopyRequest.efternamn;
            payload.mellannamn = intygCopyRequest.mellannamn;
            payload.postadress = intygCopyRequest.postadress;
            payload.postnummer = intygCopyRequest.postnummer;
            payload.postort = intygCopyRequest.postort;

            if (intygCopyRequest.coherentJournaling) {
                payload.coherentJournaling = intygCopyRequest.coherentJournaling;
            }
            return payload;
        }

        function _createIntygCopyActionType(action) {
            return function doIntygCopyAction(intygCopyRequest, onSuccess, onError) {
                $log.debug(action + ' intyg' + intygCopyRequest.intygType + ', ' + intygCopyRequest.intygId);

                var restPath = '/moduleapi/intyg/' +
                    intygCopyRequest.intygType +
                    '/' +
                    intygCopyRequest.intygId +
                    '/';

                switch (action) {
                case 'fornya':
                    restPath += 'fornya' + '/';
                    break;
                case 'ersatt':
                    restPath += 'ersatt' + '/';
                    break;
                case 'create':
                    restPath += intygCopyRequest.newIntygType + '/' + 'create' + '/';
                    break;
                default:
                    throw new Error('common.IntygProxy#_executeIntygCopyActionType: Unknown action parameter', action);
                }

                var payload = buildPayloadFromCopyIntygRequest(intygCopyRequest);

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

        /*
         * answer komplettering with a new intyg (basically do a copy with a 'komplettering' relation to this intyg)
         */
        function _answerWithIntyg(arende, intygsTyp, intygCopyRequest, onSuccess, onError) {
            $log.debug('_answerWithIntyg: arendeId:' + arende.fraga.internReferens + ' intygsTyp: ' + intygsTyp);

            var restPath = '/moduleapi/intyg/' + intygsTyp + '/' + intygCopyRequest.intygId + '/' +
                arende.fraga.internReferens + '/komplettera';
            var payload = buildPayloadFromCopyIntygRequest(intygCopyRequest);

            $http.post(restPath, payload).success(function(data) {
                $log.debug('got data:' + data.intygsUtkastId);
                onSuccess(data);
            }).error(function(data, status) {
                $log.error('error ' + status);
                // Let calling code handle the error of no data response
                onError(data);
            });
        }

        // Return public API for the service
        return {
            getIntyg: _getIntyg,
            makuleraIntyg: _makuleraIntyg,
            sendIntyg: _sendIntyg,
            copyIntyg: _createIntygCopyActionType('copy'),
            fornyaIntyg: _createIntygCopyActionType('fornya'),
            create: _createIntygCopyActionType('create'),
            ersattIntyg: _createIntygCopyActionType('ersatt'),
            answerWithIntyg: _answerWithIntyg
        };
    }]);
