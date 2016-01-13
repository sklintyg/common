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
             * TODO One could make a controller for the <head> and <title>    section, in the case we could bind an
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
