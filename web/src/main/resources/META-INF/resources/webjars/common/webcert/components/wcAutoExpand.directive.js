/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
/**
 * AutoExpand directive.
 *
 * Used to let text areas auto size it's height.
 */

angular.module('common').directive('autoExpand', ['$window', '$interval', function($window, $interval) {
    'use strict';

    return  {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs, ctrl) {
            // Ensure the element is a textarea, browser is capable and autoexpand something else than false
            if (element[0].nodeName !== 'TEXTAREA' || attrs.autoExpand === 'false') {
                return;
            }

            var windowNode = angular.element($window);
            var node = element[0];
            var defaultHeight = 0;
            var changedHeight = false;

            function resizeTextarea() {
                node.style.height = 'auto';
                var height = node.scrollHeight;

                if (height > defaultHeight) {
                    if (!changedHeight) {
                        defaultHeight = node.clientHeight;
                        changedHeight = true;
                    }
                    node.style.height = height + 'px';
                }
            }

            function addWatches() {
                if (ctrl) {
                    scope.$watch(function() {
                        return ctrl.$viewValue;
                    }, function(newValue, oldValue) {
                        if (newValue !== oldValue) {
                            resizeTextarea();
                        }
                    });
                }
                // browser resize occurrence
                windowNode.on('resize', resizeTextarea);
            }

            // Run until the textarea is visible to be able to calculate the size of the textarea
            var stop = $interval(function() {
                if (node.offsetHeight || node.offsetWidth) {
                    resizeTextarea();

                    $interval.cancel(stop);

                    // add watches
                    addWatches();
                }
            }, 500);

            function cleanUp() {
                windowNode.off('resize', resizeTextarea);

                if (stop) {
                    $interval.cancel(stop);
                }
            }

            scope.$on('$destroy', cleanUp);
        }
    };
}]);
