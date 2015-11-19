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
                controller: function($scope) {

                    if ($scope.fieldLabel === null) {
                        if ($scope.fieldLabel === null) {
                            $scope.fieldLabel = undefined;
                        }

                        if ($scope.filled === undefined) {
                            $scope.filled = true;
                        }

                        if ($scope.fieldNumber === null) {
                            $scope.fieldNumber = undefined;
                        }
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
