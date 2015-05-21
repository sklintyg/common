/**
 * Common certificate management methods between certificate modules
 */
angular.module('common').factory('common.PrintService',
    ['$log', '$window', 'common.IntygProxy',
        function($log, $window, IntygProxy) {
            'use strict';

            function _printWebPage(intygsId, intygsTyp) {
                $window.print();
                IntygProxy.logPrint(intygsId, intygsTyp, function(data) {
                    $log.debug('_logPrint, success: ' + data);
                });
            }

            // Return public API for the service
            return {
                printWebPage: _printWebPage
            };
        }]);
