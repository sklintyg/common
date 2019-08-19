/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
        $http.get(restPath).then(function(response) {
          $log.debug('_getCertificate data:' + response.data);
          onSuccess(response.data);
        }, function(response) {
          $log.error('error ' + response.status);
          onError(response.data);
        });
      }

      function _getIntygTypeInfo(intygsId, onSuccess, onError) {
        $log.debug('_getIntygTypeInfo id:' + intygsId);
        var restPath = '/api/intyg/intygTypeVersion/' + intygsId;
        $http.get(restPath).then(function(response) {
          $log.debug('_getIntygTypeInfo data:' + response.data);
          onSuccess(response.data);
        }, function(response) {
          $log.error('error ' + response.status);
          onError(response.data);
        });
      }

      function _sendIntyg(intygsId, intygsTyp, recipientId, onSuccess, onError) {
        $log.debug('_sendSigneratIntyg: ' + intygsId);
        var restPath = '/moduleapi/intyg/' + intygsTyp + '/' + intygsId + '/skicka';
        $http.post(restPath, {'recipient': recipientId}).then(function(response) {
          onSuccess(response.data);
        }, function(response) {
          _handleError(onError, response.data);
        });
      }

      function _makuleraIntyg(intygId, intygType, revokeMessage, onSuccess, onError) {
        $log.debug('_revokeSigneratIntyg: ' + intygId + ' intygsTyp: ' + intygType);
        var restPath = '/moduleapi/intyg/' + intygType + '/' + intygId + '/aterkalla';
        $http.post(restPath, revokeMessage).then(function(response) {
          if (response.data === 'OK') {
            onSuccess();
          } else {
            onError();
          }
        }, function(response) {
          _handleError(onError, response.data);
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
        if (intygCopyRequest.kommentar) {
          payload.kommentar = intygCopyRequest.kommentar;
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

          $http.post(restPath, payload).then(function(response) {
            $log.debug(restPath + ' response: ' + angular.toJson(response.data));
            onSuccess(response.data);
            statService.refreshStat();

          }, function(response) {
            $log.error('error ' + response.status);
            onError(response.data);
          });
        };
      }

      /*
       * answer komplettering with a new intyg (basically do a copy with a 'komplettering' relation to this intyg)
       */
      function _answerWithIntyg(intygsTyp, intygCopyRequest, onSuccess, onError) {
        $log.debug('_answerWithIntyg: intygsTyp: ' + intygsTyp);

        var restPath = '/moduleapi/intyg/' + intygsTyp + '/' + intygCopyRequest.intygId + '/komplettera';
        var payload = buildPayloadFromCopyIntygRequest(intygCopyRequest);

        $http.post(restPath, payload).then(function(response) {
          $log.debug(restPath + ' response: ' + response.data.intygsUtkastId);
          onSuccess(response.data);
        }, function(response) {
          $log.error('error ' + response.status);
          // Let calling code handle the error of no data response
          onError(response.data);
        });
      }

      // Return public API for the service
      return {
        getIntyg: _getIntyg,
        getIntygTypeInfo: _getIntygTypeInfo,
        makuleraIntyg: _makuleraIntyg,
        sendIntyg: _sendIntyg,
        copyIntyg: _createIntygCopyActionType('copy'),
        fornyaIntyg: _createIntygCopyActionType('fornya'),
        create: _createIntygCopyActionType('create'),
        ersattIntyg: _createIntygCopyActionType('ersatt'),
        answerWithIntyg: _answerWithIntyg
      };
    }]);
