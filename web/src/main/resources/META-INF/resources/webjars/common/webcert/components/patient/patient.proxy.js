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
angular.module('common').factory('common.PatientProxy',
    [ '$http', '$stateParams', '$log', 'common.PatientModel',
        function($http, $stateParams, $log, PatientModel) {
            'use strict';

            /**
             * getNameAndAddress
             * @param personnummer
             * @param onSuccess
             * @param onNotFound
             * @param onError
             */
            function _getPatient(personnummer, onSuccess, onNotFound, onError) {
                $log.debug('_getPatient');

                var that = PatientModel;
                that.personnummer = personnummer;

                var restPath = '/api/person/' + personnummer;
                $http.get(restPath).then(function(response) {
                    $log.debug(response.data);

                    var data = response.data;
                    if (data.status === 'FOUND' && data.person) {
                        that.sekretessmarkering = data.person.sekretessmarkering;
                        that.personnummer = data.person.personnummer;
                        that.fornamn = data.person.fornamn;
                        that.mellannamn = data.person.mellannamn;
                        that.efternamn = data.person.efternamn;
                        that.postadress = data.person.postadress;
                        that.postnummer = data.person.postnummer;
                        that.postort = data.person.postort;
                        that.avliden = data.person.avliden;
                        that.testIndicator = data.person.testIndicator;
                        onSuccess(that);
                    } else if (data.status === 'ERROR') {
                        $log.warn('PU-tjänsten kunde inte kontaktas.');
                        onError(true);
                    } else {
                        $log.debug('Personen hittades inte i PU-tjänsten.');
                        onNotFound();
                    }

                }, function() {
                    $log.warn('Tekniskt fel vid begäran om slagning mot PU-tjänsten.');
                    onError(false);
                });
            }

             // Return public API for the service
            return {
                getPatient: _getPatient
            };
        }]);
