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
angular.module('common').factory('common.fmbProxy', ['$http', '$q', '$log',
  function($http, $q, $log) {
    'use strict';

    /*
     * get diagnosis by code
     */
    function _getFMBHelpTextsByCode(diagnosisCode) {
      var deferred = $q.defer(),
          restPath = '/api/fmb/' + diagnosisCode.toUpperCase();

      $http.get(restPath).then(function(response) {
        deferred.resolve(response.data);
      }, function(response) {
        $log.error('error ' + response.status);
        deferred.reject(response.status);
      });

      return deferred.promise;
    }

    /*
     * validate sjukskrivningstid
     * sjukskrivningsTid {
     *  icd10kod1,
     *  icd10kod2,
     *  icd10kod3,
     *  foreslagenSjukskrivningstid,
     *  personnummer
     * }
     */
    function _getValidateSjukskrivningstid(sjukskrivningsTid) {
      var deferred = $q.defer(),
          restPath = '/api/fmb/valideraSjukskrivningstid';

      $http.get(restPath, {params: sjukskrivningsTid}).then(function(response) {
        deferred.resolve(response.data);
      }, function(response) {
        $log.error('error ' + response.status);
        deferred.reject(response.status);
      });

      return deferred.promise;
    }

    // Return public API for the service
    return {
      getFMBHelpTextsByCode: _getFMBHelpTextsByCode,
      getValidateSjukskrivningstid: _getValidateSjukskrivningstid
    };
  }]);

