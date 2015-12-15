angular.module('common').directive('dynamicLabel',
    [ '$rootScope', 'common.dynamicLabelService',
        function($rootScope, dynamicLabelService) {
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
                    // observe changes to interpolated attribute
                    attr.$observe('key', function(interpolatedKey) {
                        //var normalizedKey = angular.lowercase(interpolatedKey);
                        var rootElement = 'texter';
                        /*
                            RootElement = Used for dev test json structure, might not be relevant later _> see label-data-mock.js for example data
                            Fallback
                         */
                        result = dynamicLabelService.getProperty(interpolatedKey, rootElement);

                        // now get the value to display..
                        scope.resultValue = result;
                    });
                }
            };
        }]);