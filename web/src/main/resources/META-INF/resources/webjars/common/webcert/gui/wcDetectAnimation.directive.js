angular.module('common').directive('wcDetectAnimation',
    function($window, $rootScope) {
        'use strict';
        return {
            link: function(scope, element) {
                element.on('$animate:before', function() {
                    $window.animations++;
                    $window.rendered = false;
                    $rootScope.$broadcast('wcAnimationStart');
                });
                element.on('$animate:close', function() {
                    $window.animations--;
                    if ($window.animations === 0) {
                        $window.rendered = true;
                        $rootScope.$broadcast('wcAllAnimationsEnd');
                    }
                });
            }
        };
    });
