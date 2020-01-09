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
/**
 * Interceptor that decorates all GET requests made by the $http service. Can be
 * used by the modules as a common component. To hook up the interceptor, simply
 * config the http provider for the app like this (in 1.1.5):
 *
 * app.config(function ($httpProvider) {
     *   $httpProvider.interceptors.push('httpRequestInterceptorCacheBuster');
     * })
 */
angular.module('common').factory('common.httpRequestInterceptorCacheBuster',
    function($q) {
        'use strict';

        return {
            request: function(config) {
                // Don't mess with view loading, ok if cached..
                if (config.url.indexOf('.html') === -1) {
                    var sep = config.url.indexOf('?') === -1 ? '?' : '&';
                    config.url = config.url + sep + 'cacheSlayer=' + new Date().getTime();
                }
                return config || $q.when(config);
            }
        };
    });
