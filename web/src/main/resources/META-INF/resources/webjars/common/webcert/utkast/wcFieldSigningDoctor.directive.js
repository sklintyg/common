/**
 * Show signing doctor name if it exists in the stateParams.
 * Broadcast a intyg.loaded event on rootscope when the intyg is loaded to update the message.
 */
angular.module('common').directive('wcFieldSigningDoctor',
    [ '$stateParams',
        function($stateParams) {
            'use strict';

            return {
                restrict: 'A',
                controller: function($scope) {
                    $scope.show = $stateParams.hospName !== undefined && $stateParams.hospName !== '';
                    $scope.hospName = $stateParams.hospName;
                },
                templateUrl: '/web/webjars/common/webcert/utkast/wcFieldSigningDoctor.directive.html'
            };
        }]);
