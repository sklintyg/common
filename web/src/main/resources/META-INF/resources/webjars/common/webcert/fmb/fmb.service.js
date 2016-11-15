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

angular.module('common').factory('common.fmbService', [
    '$http' , '$q', '$log',
    'common.fmbViewState', 'common.fmbProxy',
    function($http, $q, $log, fmbViewState, fmbProxy) {
        'use strict';

        function _updateFmbTextsForAllDiagnoses(diagnoser) {
            if (!angular.isArray(diagnoser)) {
                $log.error('_updateFmbTextsForAllDiagnoses called with invalid parameter - array required');
                return;
            }

            if(diagnoser.length !== 3)
            {
                $log.error('_updateFmbTextsForAllDiagnoses - diagnose type missing from array. should be length 3');
                return;
            }

            var diagnosTypes = ['main', 'bi1', 'bi2'];
            var fmbDiagnosRequest = [];
            var promises = [];

            // Request FMB texts for all entered diagnoses
            var i;
            for (i = 0; i < diagnoser.length; i++){
                if (diagnoser[i].diagnosKod) {
                    fmbDiagnosRequest.push({
                        type: diagnosTypes[i],
                        code: diagnoser[i].diagnosKod
                    });
                    promises.push(fmbProxy.getFMBHelpTextsByCode(diagnoser[i].diagnosKod));
                }
            }

            // Resolve all server responses
            $q.all(promises).then(function(formDatas){
                var j;
                for(j = 0; j < formDatas.length; j++){
                    fmbViewState.setState(fmbDiagnosRequest[j].type, formDatas[j], fmbDiagnosRequest[j].code);
                }
            }, function(errors) {
                var j;
                for(j = 0; j < errors.length; j++){
                    $log.debug('Error searching fmb help text for diagnostype ' + fmbDiagnosRequest[j].type + ' with diagnoscode: ' + fmbDiagnosRequest[j].code);
                    fmbViewState.reset(fmbDiagnosRequest[j].type);
                }
            });
        }

        function _updateFmbText(diagnosType, originalDiagnosKod) {
            if (originalDiagnosKod === undefined || originalDiagnosKod.length === 0) {
                fmbViewState.reset(diagnosType);
            } else {
                if (!angular.isObject(fmbViewState.diagnoses[diagnosType]) ||
                    fmbViewState.diagnoses[diagnosType].diagnosKod !== originalDiagnosKod) {
                    var fmbSuccess = function fmbSuccess(formData) {
                        fmbViewState.setState(diagnosType, formData, originalDiagnosKod);
                    };
                    var fmbReject = function fmbReject(data) {
                        $log.debug('Error searching fmb help text for diagnostype ' + diagnosType);
                        $log.debug(data);
                    };
                    fmbProxy.getFMBHelpTextsByCode(originalDiagnosKod).then(fmbSuccess, fmbReject);
                }
            }
        }

        // Return public API for the service
        return {
            updateFmbTextsForAllDiagnoses: _updateFmbTextsForAllDiagnoses,
            updateFmbText: _updateFmbText
        };
    }]);

