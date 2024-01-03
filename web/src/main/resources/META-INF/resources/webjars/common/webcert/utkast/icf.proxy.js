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
angular.module('common').factory('common.IcfProxy',
    function($http, $log) {
        'use strict';

        function _getIcf(diagnoser, onSuccess, onError) {
            var restPath = '/api/icf?' + _generateQueryFromDiagnos(diagnoser);
            $http.get(restPath).then(function(response) {
                if (response && response.status === 200) {
                    onSuccess(response.data);
                } else {
                    onError(response);
                }
            }, function(response) {
                $log.error('IcfProxy.getIcf, Error: ' + response.status + ': ' + response.statusText);
                onError(response);
            });
        }

        function _generateQueryFromDiagnos(diagnoser) {
            var icdKodNr = 0;
            var diagnosPath = diagnoser.map(function(v){
                icdKodNr++;
                return 'icd10Kod' + icdKodNr + '=' + v.diagnosKod;
            }).join('&');
            return diagnosPath;
        }

        return {
            getIcf: _getIcf
        };
    });

