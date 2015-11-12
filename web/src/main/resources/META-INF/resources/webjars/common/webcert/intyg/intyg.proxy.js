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

        function _copyIntyg(intygCopyRequest, onSuccess, onError) {
            $log.debug('_copyIntyg ' + intygCopyRequest.intygType + ', ' + intygCopyRequest.intygId);

            var payload = {};
            payload.patientPersonnummer = intygCopyRequest.patientPersonnummer;
            if (intygCopyRequest.nyttPatientPersonnummer) {
                payload.nyttPatientPersonnummer = intygCopyRequest.nyttPatientPersonnummer;
            }

            var restPath = '/api/intyg/' + intygCopyRequest.intygType + '/' + intygCopyRequest.intygId + '/kopiera/';
            $http.post(restPath, payload).success(function(data) {
                $log.debug('got callback data: ' + data);
                onSuccess(data);
                statService.refreshStat();

            }).error(function(data, status) {
                $log.error('error ' + status);
                onError(data);
            });
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
            copyIntyg: _copyIntyg,
            logPrint: _logPrint
        };
    }]);
