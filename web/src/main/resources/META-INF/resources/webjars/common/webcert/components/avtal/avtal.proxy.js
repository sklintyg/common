/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.AvtalProxy',
    [ '$http', '$log', 'common.AvtalModel',
        function($http, $log, AvtalModel) {
            'use strict';

            /**
             * getLatestAvtal
             */
            function _getLatestAvtal(onSuccess, onError) {
                var restPath = '/api/anvandare/latestavtal';
                $http.get(restPath).then(function(response) {
                    $log.debug(response.data);
                    onSuccess(AvtalModel.build(response.data));
                }, function() {
                    $log.warn('Avtal-tjänsten kunde inte kontaktas.');
                    onError();
                });
            }

            function _approve(onSuccess, onError){
                var restPath = '/api/anvandare/godkannavtal';
                $http.put(restPath).then(function() {
                    onSuccess();
                }, function() {
                    $log.warn('Avtal-tjänsten kunde inte kontaktas.');
                    onError();
                });
            }

             // Return public API for the service
            return {
                getLatestAvtal: _getLatestAvtal,
                approve: _approve
            };
        }]);
