angular.module('common').directive('wcDetectAnimation',
    function($window) {
        'use strict';
        return {
            link: function(scope, element) {
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
            }
        };
    });
