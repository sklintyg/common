angular.module('common').directive('wcDisableKeyDown',
    function($rootScope, $timeout) {
        'use strict';
        var removeEnter = function(element, scope, attrs){
            element.bind('keydown', function(event) {
                var code = event.keyCode || event.which;
                if (code === 13 && !event.shiftKey) {
                    event.preventDefault();
                    scope.$apply(attrs.enterSubmit);
                }
            });
        };
        return {
            restrict: 'EA',
            scope: {
                elements: '@wcDisableKeyDownElements',
                doneLoading: '=wcDisableKeyDownDoneLoading'
            },
            link: function (scope, elem, attrs, ctrl, transclude) {
                if (!scope.elements) {
                    removeEnter(elem, scope, attrs);
                } else {
                    // postpone until the dom is rendered
                    scope.$watch('doneLoading', function(doneLoading) {
                        if (doneLoading) {
                            var elements = scope.elements.split(',');
                            angular.forEach(elements, function(value) {
                                removeEnter(elem.parents('form').find(value), scope, attrs);
                            });
                        }
                    });

                }

            }
        };
    });
