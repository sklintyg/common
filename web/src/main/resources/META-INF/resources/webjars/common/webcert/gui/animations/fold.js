angular.module('common').animation('.fold-animation', ['$animateCss', '$log', function($animateCss, $log) {
    'use strict';
    return {
        enter: function(element, doneFn) {
            var height = element[0].offsetHeight;

            var current = element[0];
            var hidden = false;
            while (current.parentNode) {
                var display = angular.element(current).css('display');
                if (display === 'none') {
                    hidden = true;
                    break;
                }
                current = current.parentNode;
            }

            if (hidden) {
                return $animateCss(element, {
                    addClass: ''
                });
            }

            var animator = $animateCss(element, {
                addClass: 'fold-slide-fade-animation',
                easing: 'ease-out',
                from: { height:'0px' },
                to: { height: height + 'px' },
                duration: 0.3
            });

            return animator;
        },
        leave: function(element, doneFn) {
            var height = element[0].offsetHeight;
            return $animateCss(element, {
                addClass: 'fold-slide-fade-animation-hidden',
                easing: 'ease-out',
                from: { height: height + 'px' },
                to: { height:'0px' },
                duration: 0.3
            });
        }
    };
}]);