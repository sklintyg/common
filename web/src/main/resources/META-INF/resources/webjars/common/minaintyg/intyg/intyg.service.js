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
/**
 * Common certificate management methods between certificate modules
 */
angular.module('common').factory('common.IntygService',
    ['$http', '$log', 'common.dynamicLabelService', function($http, $log, dynamicLabelService) {
        'use strict';

        /*
         * Load certificate details from the server.
         */
        function _getCertificate(type, intygTypeVersion, id, onSuccess, onError) {
            $log.debug('_getCertificate id:' + id + 'of type:' + type + '(' + intygTypeVersion + ')');
            var restPath = '/moduleapi/certificate/' + type + '/' + intygTypeVersion + '/' + id;
            $http.get(restPath).then(function(response) {
                $log.debug('_getCertificate data:' + response.data);
                if (response.data.meta.archived) {
                    onError('error.certarchived');
                } else {
                    dynamicLabelService.updateDynamicLabels(type, response.data.utlatande);
                    onSuccess(response.data);
                }

            }, function(response) {
                $log.error('error ' + response.status);
                if (response.status === 410) {
                    onError('info.certrevoked');
                } else {
                    onError('error.certnotfound');
                }

            });
        }


        // Return public API for the service
        return {
            getCertificate: _getCertificate
        };
    }]);
