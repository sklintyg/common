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

/**
 * Monitoring log in backend.
 */
angular.module('common').factory('common.MonitoringLogService',
    ['$http', function($http) {
        'use strict';

        function post(request) {
            $http.post('/api/jslog/monitoring', request);
        }

        function isDefined(input) {
            return input !== undefined && input !== '';
        }

        function _diagnoskodverkChanged(id, type) {
            if (isDefined(id) && isDefined(type)) {
                post({
                    'event': 'DIAGNOSKODVERK_CHANGED',
                    'info': {
                        'intygId': id,
                        'intygType': type
                    }
                });
            }
        }

        function _screenResolution(width, height) {
            if (isDefined(width) && isDefined(height)) {
                post({
                    'event': 'SCREEN_RESOLUTION',
                    'info': {
                        'width': width,
                        'height': height
                    }
                });
            }
        }

        return {
            diagnoskodverkChanged: _diagnoskodverkChanged,
            screenResolution: _screenResolution
        };

    }]);
