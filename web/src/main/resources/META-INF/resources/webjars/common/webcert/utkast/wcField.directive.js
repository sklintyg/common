/**
 * wcField directive. Used to abstract common layout for full-layout form fields in cert modules
 */
angular.module('common').directive('wcField',
    [ 'common.messageService',
        function(messageService) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                templateUrl: '/web/webjars/common/webcert/utkast/wcField.directive.html',
                scope: {
                    fieldLabel: '@?',
                    fieldNumber: '@?',
                    fieldHelpText: '@',
                    fieldHasErrors: '=',
                    fieldTooltipPlacement: '@',
                    filled: '@?'
                },
                compile: function(element, attrs){
                    if (!attrs.filled) { attrs.filled = true; }
                },
                controller: function($scope) {

                    if ($scope.fieldLabel === null) {
                        $scope.fieldLabel = undefined;
                    }

                    if ($scope.fieldNumber === null) {
                        $scope.fieldNumber = undefined;
                    }

                    if ($scope.fieldTooltipPlacement === undefined) {
                        $scope.placement = 'right';
                    } else {
                        $scope.placement = $scope.fieldTooltipPlacement;
                    }

                    $scope.getMessage = function(key) {
                        return messageService.getProperty(key);
                    };
                }
            };
        }]);
