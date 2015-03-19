/**
 * Show signing doctor name if it exists in the routeParams.
 * Broadcast a intyg.loaded event on rootscope when the intyg is loaded to update the message.
 */
angular.module('common').directive('wcFieldSigningDoctor',
    [ '$routeParams',
        function($routeParams) {
            'use strict';

            return {
                restrict: 'A',
                controller: function($scope) {
                    $scope.show = $routeParams.hospName !== undefined && $routeParams.hospName !== '';
                    $scope.hospName = $routeParams.hospName;
                },
                templateUrl: '/web/webjars/common/webcert/js/directives/wcFieldSigningDoctor.html'
            };
        }]);
