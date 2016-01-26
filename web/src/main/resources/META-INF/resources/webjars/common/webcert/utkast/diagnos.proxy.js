angular.module('common').factory('common.DiagnosProxy',
    function($http, $log) {
        'use strict';

        /*
         * get diagnosis by code
         */
        function _getByCode(codeSystem, code, onSuccess, onError) {
            $log.debug('_searchByCode: codeFragment:' + code);
            var restPath = '/moduleapi/diagnos/kod/' + codeSystem;
            $http.post(restPath, code.toUpperCase()).success(function(response) {
                if (response && response.resultat === 'OK') {
                    onSuccess(response);
                }
                else {
                    onError(response);
                }
            }).error(function(response, status) {
                $log.error('error ' + status);
                onError(response);
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
            //$log.debug('_searchByDescription: ' + searchString);
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

