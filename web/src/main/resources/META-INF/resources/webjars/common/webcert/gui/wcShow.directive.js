angular.module('common').directive('wcShow',
    function($window, $animate, $log) {
        'use strict';

        return {
            restrict: 'A',
            multiElement: true,
            link: function(scope, element, attr) {
                scope.$watch(attr.wcShow, function ngShowWatchAction(value){

                    $animate[value ? 'removeClass' : 'addClass'](element, 'ng-hide');

                    element.on('$animate:before', function() {
                        $window.animations++;
                        $window.rendered = false;
                    });
                    element.on('$animate:close', function() {
                        $window.animations--;
                        if ($window.animations === 0) {
                            $window.rendered = true;
                        }
                    });
                });
            }
        };
    });
