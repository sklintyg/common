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
angular.module('common').factory('common.fmbService', [
  '$http', '$q', '$log', '$rootScope',
  'common.fmbViewState', 'common.fmbProxy', 'common.ObjectHelper',
  function($http, $q, $log, $rootScope, fmbViewState, fmbProxy, ObjectHelper) {
    'use strict';

    function _checkDiagnos(diagnos) {
      if (angular.isObject(diagnos) && !ObjectHelper.isEmpty(diagnos.diagnosKod) &&
          diagnos.hasInfo) {
        return true;
      }
      return false;
    }

    function _isAnyFMBDataAvailable(fmbStates) {
      if (angular.isObject(fmbStates) && angular.isObject(fmbStates.diagnoses)) {
        if (_checkDiagnos(fmbStates.diagnoses[0]) ||
            _checkDiagnos(fmbStates.diagnoses[1]) ||
            _checkDiagnos(fmbStates.diagnoses[2])) {
          return true;
        }
      }
      return false;
    }

    function _updateFmbTextsForAllDiagnoses(diagnoser) {
      fmbViewState.reset(0);
      fmbViewState.reset(1);
      fmbViewState.reset(2);

      if (!angular.isArray(diagnoser)) {
        $log.error('_updateFmbTextsForAllDiagnoses called with invalid parameter - array required');
        return false;
      }

      var diagnosTypes = [0, 1, 2];
      var fmbDiagnosRequest = [];
      var promises = [];

      // Request FMB texts for all entered diagnoses
      var i;
      for (i = 0; i < diagnoser.length; i++) {
        if (diagnoser[i].diagnosKod && diagnoser[i].diagnosKodSystem === 'ICD_10_SE') {
          fmbDiagnosRequest.push({
            type: diagnosTypes[i],
            code: diagnoser[i].diagnosKod
          });
          promises.push(fmbProxy.getFMBHelpTextsByCode(diagnoser[i].diagnosKod));
        }
      }

      // Resolve all server responses
      $q.all(promises).then(function(formDatas) {
        var j;
        for (j = 0; j < formDatas.length; j++) {
          fmbViewState.setState(fmbDiagnosRequest[j].type, formDatas[j], fmbDiagnosRequest[j].code, diagnoser[j].diagnosBeskrivning);
        }
      }, function(errors) {
        var j;
        for (j = 0; j < errors.length; j++) {
          $log.debug('Error searching fmb help text for diagnostype ' + fmbDiagnosRequest[j].type +
              ' with diagnoscode: ' + fmbDiagnosRequest[j].code);
          fmbViewState.reset(fmbDiagnosRequest[j].type);
        }
      });

      return true;
    }

    function _updateFmbText(diagnosType, originalDiagnosKod, kodSystem, originalDiagnosBeskrivning) {
      $rootScope.$broadcast('diagnos.changed');
      if (!ObjectHelper.isDefined(originalDiagnosKod) || originalDiagnosKod.length === 0) {
        fmbViewState.reset(diagnosType);
        return false;
      } else {
        if (!angular.isObject(fmbViewState.diagnoses[diagnosType]) ||
            fmbViewState.diagnoses[diagnosType].diagnosKod !== originalDiagnosKod) {

          if (kodSystem === 'ICD_10_SE') {
            var fmbSuccess = function fmbSuccess(formData) {
              fmbViewState.setState(diagnosType, formData, originalDiagnosKod, originalDiagnosBeskrivning);
            };
            var fmbReject = function fmbReject(data) {
              $log.debug('Error searching fmb help text for diagnostype ' + diagnosType);
              $log.debug(data);
            };
            fmbProxy.getFMBHelpTextsByCode(originalDiagnosKod).then(fmbSuccess, fmbReject);
          }
        }
      }
      return true;
    }

    function _updateKodverk(kodverk) {
      fmbViewState.isIcdKodVerk = kodverk === 'ICD_10_SE';
    }

    // Return public API for the service
    return {
      checkDiagnos: _checkDiagnos,
      isAnyFMBDataAvailable: _isAnyFMBDataAvailable,
      updateFmbTextsForAllDiagnoses: _updateFmbTextsForAllDiagnoses,
      updateFmbText: _updateFmbText,
      updateKodverk: _updateKodverk
    };
  }]);
