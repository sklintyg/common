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
angular.module('common').factory('common.IntygListService', ['$rootScope', '$http', '$log',
  function($rootScope, $http, $log) {
    'use strict';

    // cached certificates response
    var cachedArchiveList = null;

    var _selectedCertificate = null;

    function _emptyCache() {
      $log.debug('Clearing archived cache');
      cachedArchiveList = null;
    }

    function _getCertificates(callback) {
      $http.get('/api/certificates').then(function(response) {
        callback(response.data);
      }, function(response) {
        $log.error('error ' + response.status);
        //give calling code a chance to handle error
        callback(null);
      });
    }

    function _getArchivedCertificates(callback) {
      if (cachedArchiveList !== null) {
        $log.debug('returning cached archive response');
        callback(cachedArchiveList);
        return;
      }
      $http.get('/api/certificates/archived').then(function(response) {
        $log.debug('populating archive cache');
        cachedArchiveList = response.data;
        callback(cachedArchiveList);
      }, function(response) {
        $log.error('error ' + response.status);
        //give calling code a chance to handle error
        callback(null);
      });
    }

    function _archiveCertificate(item, callback) {
      $log.debug('Archiving ' + item.id);

      $http.put('/api/certificates/' + item.id + '/archive').then(function(response) {
        _emptyCache();
        callback(response.data, item);
      }, function(response) {
        $log.error('error ' + response.status);
        //give calling code a chance to handle error
        callback(null);
      });
    }

    function _restoreCertificate(item, callback) {
      $log.debug('restoring ' + item.id);
      $http.put('/api/certificates/' + item.id + '/restore').then(function(response) {
        _emptyCache();
        callback(response.data, item);
      }, function(response) {
        $log.error('error ' + response.status);
        //give calling code a chance to handle error
        callback(null);
      });
    }

    function _getKnownRecipients(callback) {
      $log.debug('getting all available recipients');
      $http.get('/api/certificates/recipients/list').then(function(response) {
        $rootScope.$broadcast('recipients.updated');
        callback(response.data);
      }, function(response) {
        $log.error('error ' + response.status);
        callback(null);
      });
    }

    // Return public API for our service
    return {
      getCertificates: _getCertificates,
      getArchivedCertificates: _getArchivedCertificates,
      archiveCertificate: _archiveCertificate,
      restoreCertificate: _restoreCertificate,
      selectedCertificate: _selectedCertificate,
      emptyCache: _emptyCache,
      getKnownRecipients: _getKnownRecipients
    };
  }]);
