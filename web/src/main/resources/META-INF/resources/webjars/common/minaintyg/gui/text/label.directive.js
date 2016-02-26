angular.module('common').directive('dynamicLabel',
    [ '$log', '$rootScope', 'common.dynamicLabelService',
        function($log, $rootScope, dynamicLabelService) {
            'use strict';

            return {
                restrict: 'EA',
                scope: {
                    'key': '@'
                },
                replace: true,
                template: '<span ng-bind-html="resultValue"></span>',
                link: function(scope, element, attr) {
                    var result;

                    function updateText(interpolatedKey) {
                        result = dynamicLabelService.getProperty(interpolatedKey);
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