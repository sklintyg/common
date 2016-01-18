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

angular.module('common').factory('common.User',
    [ '$http', '$log', 'common.UserModel', '$q', function($http, $log, userModel) {
        'use strict';

        return {

            getUser: function() {
                return userModel.user;
            },

            /**
             * returns a list of selectable vardenheter and mottagningar from user context
             * @returns valdVardenhet
             */
            getVardenhetSelectionList: function() {

                var ucVardgivare = angular.copy(userModel.user.vardgivare);

                var vardgivareList = [];

                angular.forEach(ucVardgivare, function(vardgivare, key1) {

                    this.push({ 'id': vardgivare.id, 'namn': vardgivare.namn, 'vardenheter': [] });

                    angular.forEach(vardgivare.vardenheter, function(vardenhet) {

                        this.push({ 'id': vardenhet.id, 'namn': vardenhet.namn });

                        angular.forEach(vardenhet.mottagningar, function(mottagning) {

                            mottagning.namn = vardenhet.namn + ' - ' + mottagning.namn;
                            this.push(mottagning);

                        }, vardgivareList[key1].vardenheter);

                    }, vardgivareList[key1].vardenheter);

                }, vardgivareList);

                return vardgivareList;
            },

            /**
             * Returns a list of the selected vardenhet and all its mottagningar
             * @returns {*}
             */
            getVardenhetFilterList: function(vardenhet) {
                if (!vardenhet) {

                    if (userModel.user.valdVardenhet) {
                        $log.debug('getVardenhetFilterList: using valdVardenhet');
                        vardenhet = userModel.user.valdVardenhet;
                    } else {
                        $log.debug('getVardenhetFilterList: parameter vardenhet was omitted');
                        return [];
                    }
                }

                var vardenhetCopy = angular.copy(vardenhet); // Don't modify the original!
                var units = [];
                units.push(vardenhetCopy);

                angular.forEach(vardenhetCopy.mottagningar, function(mottagning) {
                    this.push(mottagning);
                }, units);

                return units;
            },

            /**
             * returns valdVardgivare from user context
             * @returns valdVardgivare
             */
            getValdVardgivare: function() {
                return userModel.user.valdVardgivare;
            },

            /**
             * returns valdVardenhet from user context
             * @returns valdVardenhet
             */
            getValdVardenhet: function() {
                return userModel.user.valdVardenhet;
            },

            /**
             * setValdVardenhet. Tell server which vardenhet is active in user context
             * @param vardenhet - complete vardenhet object to send
             * @param onSuccess - success callback on successful call
             * @param onError - error callback on connection failure
             */
            setValdVardenhet: function(vardenhet, onSuccess, onError) {
                $log.debug('setValdVardenhet' + vardenhet.namn);

                var payload = vardenhet;

                var restPath = '/api/anvandare/andraenhet';
                $http.post(restPath, payload).success(function(data) {
                    $log.debug('got callback data: ' + data);

                    // Update user context
                    userModel.setUser(data);

                    onSuccess(data);
                }).error(function(data, status) {
                    $log.error('error ' + status);
                    onError(data);
                });
            }

        };
    }]);
