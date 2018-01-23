angular.module('common').directive('dynamicLabel',
    [ '$compile', '$log', '$rootScope', 'common.dynamicLabelService', 'common.messageService',
        function($compile, $log, $rootScope, dynamicLabelService, messageService) {
            'use strict';

            return {
                restrict: 'EA',
                scope: {
                    'key': '@',
                    'fallbackValue': '@'
                },
                replace: true,
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

                        element.empty();
                        element.append(result);
                    }

                    scope.$on('dynamicLabels.updated', function() {
                        updateText(attr.key);
                    });

                    // observe changes to interpolated attribute
                    attr.$observe('key', updateText);
                }
            };
        }]);