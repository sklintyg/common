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
 * Common utkast proxy. All utkast-related REST-functions goes here.
 */
angular.module('common').factory('common.UtkastProxy',
    function($http, $log, $window) {
      'use strict';

      var saveDraftInProgress = false;

      function _handleError(callback, error) {
        if (callback) {
          callback(error);
        } else {
          $log.error(error);
        }
      }

      /**
       * Get a utkast with the specified id from the server.
       */
      function _getUtkast(intygsId, intygsTyp, onSuccess, onError) {
        $log.debug('_getDraft intygsId: ' + intygsId + ' intygsTyp: ' + intygsTyp);
        var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + intygsId;
        $http.get(restPath).then(function(response) {
          $log.debug('_getDraft data: ' + response.data);
          onSuccess(response.data);
        }, function(response) {
          $log.error('error ' + response.status);
          onError(response.data);
        });
      }

      /**
       * Saves a utkast to the server.
       */
      function _saveUtkast(intygsId, intygsTyp, version, intyg, onSuccess, onError) {
        $log.debug('_saveDraft id: ' + intygsId + ' intygsTyp: ' + intygsTyp);
        var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + intygsId + '/' + version;

        $window.saving = true;
        saveDraftInProgress = true;
        $http.put(restPath, intyg).then(function(response) {
          $log.debug('_saveDraft data: ' + response.data);
          onSuccess(response.data);
          saveDraftInProgress = false;
        }, function(response) {
          $log.error('error ' + response.status);
          onError(response.data);
          saveDraftInProgress = false;
        }).finally(function() { // jshint ignore:line
          saveDraftInProgress = false;
        });
      }

      function _isSaveUtkastInProgress() {
        return saveDraftInProgress;
      }

      /**
       * Discards a utkast and removes it from the server.
       */
      function _discardUtkast(intygsId, intygsTyp, version, onSuccess, onError) {
        $log.debug('_discardDraft id: ' + intygsId + ' intygsTyp: ' + intygsTyp);
        var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + intygsId + '/' + version;
        $http['delete'](restPath).then(function(response) {
          $log.debug('_discardDraft data: ' + response.data);
          onSuccess(response.data);
        }, function(response) {
          $log.error('error ' + response.status);
          onError(response.data);
        });
      }

      /**
       * Revoke a utkast
       */
      function _makuleraUtkast(intygId, intygType, revokeMessage, onSuccess, onError) {
        $log.debug('_revokeLockedDraft: ' + intygId + ' intygsTyp: ' + intygType);
        var restPath = '/moduleapi/utkast/' + intygType + '/' + intygId + '/aterkalla';
        $http.post(restPath, revokeMessage).then(function() {
          onSuccess();
        }, function(response) {
          _handleError(onError, response.data);
        });
      }

      /**
       * Copy utkast
       */
      function _copyUtkast(intygsId, intygsTyp, onSuccess, onError) {
        $log.debug('_copyUtkast id: ' + intygsId + ' intygsTyp: ' + intygsTyp);
        var restPath = '/moduleapi/utkast/' + intygsTyp + '/' + intygsId + '/copy';

        $http.post(restPath).then(function(response) {
          onSuccess(response.data);
        }, function(response) {
          $log.error('error ' + response.status);
          onError(response.data);
        });
      }

      /**
       * Används "i bakgrunden" av frontend för att kontrollera signaturstatus för pågående signering.
       *
       * Används av BankID/Mobilt BankID, Netid Access samt fejksignering. NetiD-plugin behöver inte polla
       * backend men verkar göra det ändå...
       *
       * @param ticketId
       * @param intygsTyp
       * @param onSuccess
       * @param onError
       * @private
       */
      function _getSigneringsstatus(ticketId, intygsTyp, onSuccess, onError) {
        $log.debug('_getSigneringsstatus, ticketId: ' + ' intygsTyp: ' + intygsTyp);
        var restPath = '/api/signature/' + intygsTyp + '/' + ticketId + '/signeringsstatus';
        $http.get(restPath).then(function(response) {
          onSuccess(response.data);
        }, function(response) {
          _handleError(onError, response.data);
        });
      }

      /**
       * Används av SAMTLIGA signeringsmetoder för att skapa upp signaturhash och "SignaturBiljett"
       * på backend och i förekommande fall (BankID, Mobilt BankID och NetiD Access Server) kicka
       * igång sign/collect-jobben i backend.
       *
       * @param intygsId
       * @param intygsTyp
       * @param version
       * @param onSuccess
       * @param onError
       * @private
       */
      function _startSigningProcess(intygsId, intygsTyp, version, signMethod, onSuccess, onError) {
        $log.debug('_startSigningProcess. intygsId:' + intygsId + ' intygsTyp: ' + intygsTyp);
        var restPath = '/api/signature/' + intygsTyp + '/' + intygsId + '/' + version + '/signeringshash/' + signMethod;
        $http.post(restPath).then(function(response) {
          onSuccess(response.data);
        }, function(response) {
          _handleError(onError, response.data);
        });
      }

      /**
       * Används ENBART vid fejksignering. Själva signeringen genomförs med hitte-på privat nyckel i backend.
       *
       * Backend-komponenten är EJ aktiv med prod-profil.
       *
       * @param intygsTyp
       * @param intygsId
       * @param version
       * @param ticketId
       * @param onError
       * @private
       */
      function _fejkSignera(intygsTyp, intygsId, version, ticketId, onError) {
        $log.debug('_fejkSignera, intygsId:' + intygsId + ' intygsTyp: ' + intygsTyp);
        var restPath = '/api/fake/signature/' + intygsTyp + '/' + intygsId + '/' + version + '/fejksignera/' + ticketId;
        $http.post(restPath).then(function(response) {
          $log.debug('Fake sign OK');
        }, function(response) {
          _handleError(onError, response.data);
        });
      }

      /**
       * Körs av NetiD-plugin efter att signering genomförts i pluginen. POST:ar ner signatur (raw) och
       * x509 certifikat (aka publik nyckel) till backend.
       *
       * @param ticketId
       * @param intygsTyp
       * @param signatur
       *      RAW
       * @param certifikat
       *      X509, infogas sedan i XML digital signature för att signaturen skall gå att validera.
       * @param onSuccess
       * @param onError
       * @private
       */
      function _signeraUtkastWithSignatur(ticketId, intygsTyp, signatur, certifikat, onSuccess, onError) {
        $log.debug('_signeraUtkastWithSignatur, ticketId: ' + ticketId + ' intygsTyp: ' + intygsTyp + ' sign:' + signatur);
        var restPath = '/api/signature/' + intygsTyp + '/' + ticketId + '/signeranetidplugin';
        $http.post(restPath, {
          'signatur': signatur,
          'certifikat': certifikat
        }).then(function(response) {
          onSuccess(response.data); // ticket
        }, function(response) {
          _handleError(onError, response.data);
        });
      }

      /*
       * Load all intygstyper about which there should be a warning shown for preexisting intyg
       */
      function _getPrevious(patientId, onSuccess, onError) {
        $log.debug('_getPrevious');
        var restPath = '/api/utkast/previousIntyg/' + patientId;
        $http.get(restPath).then(function(response) {
          $log.debug('_getPrevious got data:' + response.data);
          onSuccess(response.data);
        }, function(response) {
          $log.debug('_getPrevious error :' + response.status);
          onError(response.data);
        });
      }

      // Return public API for the service
      return {
        getUtkast: _getUtkast,
        saveUtkast: _saveUtkast,
        isSaveUtkastInProgress: _isSaveUtkastInProgress,
        discardUtkast: _discardUtkast,
        makuleraUtkast: _makuleraUtkast,
        copyUtkast: _copyUtkast,
        startSigningProcess: _startSigningProcess,
        getSigneringsstatus: _getSigneringsstatus,
        fejkSignera: _fejkSignera,
        signeraUtkastWithSignatur: _signeraUtkastWithSignatur,
        getPrevious: _getPrevious
      };
    });
