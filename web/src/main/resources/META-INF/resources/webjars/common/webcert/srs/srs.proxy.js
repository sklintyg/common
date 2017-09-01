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

angular.module('common').factory('common.srsProxy', ['$http', '$q', '$log',
    function ($http, $q, $log) {
        'use strict';

        function _setConsent(consentGiven) {
            return "you have " + (!consentGiven ? "not " : "") + "accepeted";
        }

        function _getStatistik(){
            var opt = [{questionId: 1, answerId: 1}]
            return $http.post('/api/srs/12345/191212121212/J20/HELT_NEDSATT?prediktion=true&atgard=true&statistik=true', opt).then(function(response) {
                return response.data;
              });
        }

        function _setConsent(consentGiven){
            return $http.put('/api/srs/consent/191212121212/19101010-1010', consentGiven).then(function(response) {
                return response.data;
            });
        }

        function _getConsent(){
            return $http.get('/api/srs/consent/191212121212/19101010-1010').then(function(response) {
                return response.data;
            });
        }

        function _getQuestions(){
            return $http.get('/api/srs/questions/J20').then(function(response) {
                return response.data;
            });
        }

        // Return public API for the service
        return {
            getStatistik: _getStatistik,
            getConsent: _getConsent,
            getQuestions: _getQuestions,
            setConsent: _setConsent,
        };
    }]);

