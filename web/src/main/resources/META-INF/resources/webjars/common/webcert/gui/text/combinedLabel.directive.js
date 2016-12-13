angular.module('common').directive('combinedLabel',
    [ '$log', '$rootScope', 'common.dynamicLabelService', 'common.messageService',
        function($log, $rootScope, dynamicLabelService, staticMessageService) {
            'use strict';

            return {
                restrict: 'EA',
                scope: {
                    'dynKey': '@',
                    'statKey': '@'
                },
                replace: true,
                template: '<span ng-bind-html="resultValue"></span>',
                link: function(scope, element, attr) {
                    var result;

                    function updateText(interpolatedKey) {
                        result = dynamicLabelService.getProperty(interpolatedKey);
                        var statLabel = staticMessageService.getProperty(attr.statKey);
                        var dynLabel = statLabel.replace('{0}', result);
                        scope.resultValue = dynLabel;
                    }

                    scope.$on('dynamicLabels.updated', function() {
                        updateText(attr.dynKey);
                    });

                    // observe changes to interpolated attribute
                    attr.$observe('dynKey', updateText);
                }
            };
        }]);
