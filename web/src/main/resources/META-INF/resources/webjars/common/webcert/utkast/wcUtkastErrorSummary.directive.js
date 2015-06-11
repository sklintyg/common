/**
 * wcField directive. Used to abstract common layout for full-layout form fields in cert modules
 */
angular.module('common').directive('wcUtkastErrorSummary',
    [
        function() {
            'use strict';

            return {
                restrict: 'A',
                replace: true,
                templateUrl: '/web/webjars/common/webcert/utkast/wcUtkastErrorSummary.directive.html',
                scope: true,
                controller: function($scope) {

                }
            };
        }]);
