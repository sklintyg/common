angular.module('common').directive('dynamicLabel',
    [ '$log', '$rootScope', 'common.dynamicLabelService', 'common.messageService',
        function($log, $rootScope, dynamicLabelService, messageService) {
            'use strict';

            return {
                restrict: 'EA',
                scope: {
                    'key': '@',
                    'fallbackValue': '@'
                },
                replace: true,
                template: '<span ng-bind-html="resultValue"></span>',
                link: function(scope, element, attr) {
                    var result;

                    function updateText(interpolatedKey) {
                        // Try to find the key in the messageService first
                        result = messageService.propertyExists(interpolatedKey);

                        if (!result) {
                            result = dynamicLabelService.getProperty(interpolatedKey);
                        }

                        if (!result && scope.fallbackValue) {
                            result = scope.fallbackValue;
                        }

                        scope.resultValue = result;
                    }

                    scope.$on('dynamicLabels.updated', function() {
                        updateText(attr.key);
                    });

                    // observe changes to interpolated attribute
                    attr.$observe('key', updateText);
                }
            };
        }]);