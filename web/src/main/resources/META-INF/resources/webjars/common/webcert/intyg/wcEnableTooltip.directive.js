/**
 * Enable tooltips for other components than wcFields
 */
angular.module('common').directive('wcEnableTooltip',
    [ 'common.messageService',
        function(messageService) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                scope: {
                    fieldHelpText: '@'
                },
                controller: function($scope) {
                    $scope.getMessage = function(key) {
                        return messageService.getProperty(key);
                    };
                },
                templateUrl: '/web/webjars/common/webcert/intyg/wcEnableTooltip.directive.html'
            };
        }]);
