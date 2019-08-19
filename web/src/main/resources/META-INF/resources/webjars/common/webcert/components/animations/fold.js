/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
                from: { height:'0', padding:'0',  overflow: 'hidden' },
                to: { height: height + 'px', overflow: 'hidden' },
                duration: 0.3,
                cleanupStyles: true
            });

            return animator;
        },
        leave: function(element, doneFn) {
            var height = element[0].offsetHeight;
            return $animateCss(element, {
                addClass: 'fold-slide-fade-animation-hidden',
                easing: 'ease-out',
                from: { height: height + 'px', overflow: 'hidden' },
                to: { height:'0', overflow: 'hidden' },
                duration: 0.3
            });
        }
    };
}]);


angular.module('common').animation('.slide-animation', ['$animateCss', '$log', function($animateCss, $log) {
    'use strict';
    return {
        enter: function(element, doneFn) {

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
                addClass: 'slide-fade-animation',
                easing: 'ease-out',
                duration: 0.3,
                cleanupStyles: true
            });

            return animator;
        },
        leave: function(element, doneFn) {
            return $animateCss(element, {
                addClass: 'slide-fade-animation-hidden',
                easing: 'ease-out',
                duration: 0.3
            });
        }
    };
}]);
