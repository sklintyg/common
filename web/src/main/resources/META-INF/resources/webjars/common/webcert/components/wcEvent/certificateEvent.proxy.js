/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.certificateEventProxy', ['$http', '$log',
    function($http, $log) {
      'use strict';

      function _getCertificateEvents(certificateId, onSuccess, onError) {
        $log.debug('_getCertificateEvents id:' + certificateId );
        var restPath = '/api/intyg/' + certificateId + '/events';
        $http.get(restPath).then(function(response) {
          $log.debug('_getCertificateEvents data:' + response.data);
          onSuccess(response.data);
        },function(response) {
          $log.error('error ' + response.status);
          onError(response.data);
        });
      }

      // Return public API for the service
      return {
        getCertificateEvents: _getCertificateEvents
      };

    }]);



