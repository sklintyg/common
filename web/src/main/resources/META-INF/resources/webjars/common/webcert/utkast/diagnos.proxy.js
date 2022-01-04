/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.DiagnosProxy',
    function($http, $log) {
        'use strict';

        /*
         * get diagnosis by code
         */
        function _getByCode(codeSystem, code, onSuccess, onError) {
            $log.debug('_searchByCode: codeFragment:' + code);
            var restPath = '/moduleapi/diagnos/kod';
            $http.post(restPath, {
                codeSystem: codeSystem,
                codeFragment: code.toUpperCase()
            }).then(function(response) {
                if (response && response.data.resultat === 'OK') {
                    onSuccess(response.data);
                }
                else {
                    onError(response.data);
                }
            }, function(response) {
                $log.error('error ' + response.status);
                onError(response.data);
            });
        }

        /*
         * Search diagnosis by code
         */
        function _searchByCode(codeSystem, codeFragment) {
            $log.debug('_searchByCode: codeFragment:' + codeFragment);
            var restPath = '/moduleapi/diagnos/kod/sok';
            var data = {
                codeSystem: codeSystem,
                codeFragment: codeFragment.toUpperCase(),
                nbrOfResults: 18
            };
            return $http.post(restPath, data);
        }

        /*
         * Search diagnosis by description
         */
        function _searchByDescription(codeSystem, searchString) {
            $log.debug('_searchByDescription: ' + searchString);
            var restPath = '/moduleapi/diagnos/beskrivning/sok';
            var data = {
                codeSystem: codeSystem,
                descriptionSearchString: searchString,
                nbrOfResults: 18
            };
            return $http.post(restPath, data);
        }

        // Return public API for the service
        return {
            getByCode: _getByCode,
            searchByCode: _searchByCode,
            searchByDescription: _searchByDescription
        };
    });

