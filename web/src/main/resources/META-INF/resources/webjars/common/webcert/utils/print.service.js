/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.PrintService',
    ['$log', '$window', 'common.IntygProxy',
        function($log, $window, IntygProxy) {
            'use strict';

            function _printWebPage(intygsId, intygsTyp) {
                $window.print();
                _logPrint(intygsId, intygsTyp);
            }

            /**
             * Lets the caller specify a custom "header" that's _usually_ printed somewhere in the header (or possibly footer,
             * it depends on how the web browser performs its printing) of the printed document.
             * E.g. default is 'Webcert'. This method changes the <title> of the <head> element
             * momentarily while printing the page.
             *
             * Works OK with IE9, Chrome, Firefox and Safari.
             *
             * One could make a controller for the <head> and <title>    section, in the case we could bind an
             * expression to <title>{{applicationTitle}}</title> and then just use $rootScope.applicationTitle
             */
            function _printWebPageWithCustomTitle(intygsId, intygsTyp, customHeader) {
                $('title').html(customHeader);  // Change <title> so printed page gets the specified text in its header.
                $window.print();
                $('title').html('Webcert');  // Change back to 'webcert'
                _logPrint(intygsId, intygsTyp);
            }

            function _logPrint(intygsId, intygsTyp) {
                IntygProxy.logPrint(intygsId, intygsTyp, function(data) {
                    $log.debug('_logPrint, success: ' + data);
                });
            }

            // Return public API for the service
            return {
                printWebPage: _printWebPage,
                printWebPageWithCustomTitle: _printWebPageWithCustomTitle
            };
        }]);
