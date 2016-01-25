/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

angular.module('common').directive( 'wcSizeTarget', ['common.UtkastViewStateService', function(CommonViewState) {
    'use strict';
    return {
        link: function($scope, element, attrs) {

            $scope.commonViewState = CommonViewState;

            $scope.$watch('commonViewState.headerSize', function(newVal, old) {
                element.attr( 'style', 'margin-top: ' + (newVal.height) + 'px');
            }, true);

        }
    };
}]);

angular.module('common').directive('wcSizeSource', ['$rootScope', '$timeout', function($root, $timeout) {
    'use strict';
    return {
        scope: {
            size: '=wcSizeSource'
        },
        link: function($scope, element, attrs) {

            $root.ngSizeDimensions  = (angular.isArray($root.ngSizeDimensions)) ? $root.ngSizeDimensions : [];
            $root.ngSizeWatch       = (angular.isArray($root.ngSizeWatch)) ? $root.ngSizeWatch : [];

            var handler = function() {

                angular.forEach($root.ngSizeWatch, function(el, i) {
                    // Dimensions Not Equal?
                    if ($root.ngSizeDimensions[i][0] !== el.offsetWidth || $root.ngSizeDimensions[i][1] !== el.offsetHeight) {
                        // Update Them
                        $root.ngSizeDimensions[i] = [el.offsetWidth, el.offsetHeight];
                        // Update Scope?
                        $root.$broadcast('size::changed', i);
                    }
                });
            };

            // Add Element to Chain?
            var exists = false;
            angular.forEach($root.ngSizeWatch, function(el, i) { if (el === element[0]) {
                    exists = i;
                }
            });

            // Ok.
            if (exists === false) {
                $root.ngSizeWatch.push(element[0]);
                $root.ngSizeDimensions.push([element[0].offsetWidth, element[0].offsetHeight]);
                exists = $root.ngSizeWatch.length-1;
            }

            // Update Scope?
            $scope.$on('size::changed', function(event, i) {
                // Relevant to the element attached to *this* directive
                if (i === exists) {
                    $timeout(function() {
                        $scope.size = {
                            width: $root.ngSizeDimensions[i][0],
                            height: $root.ngSizeDimensions[i][1]
                        };
                    });
                }
            });

            $scope.$on('wcAnimationStart', function() {
                stopSizeListener();
            });

            $scope.$on('wcAllAnimationsEnd', function() {
                startSizeListener();
            });

            function stopSizeListener() {
                clearInterval(window.ngSizeHandler);
                window.ngSizeHandler = null;
            }

            function startSizeListener() {
                // Refresh: 100ms
                if (!window.ngSizeHandler) {
                    window.ngSizeHandler = setInterval(handler, 100);
                }
            }

            startSizeListener();
        }
    };
}]);
