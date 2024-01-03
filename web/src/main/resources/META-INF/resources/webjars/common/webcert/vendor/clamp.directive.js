/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
(function() {
    'use strict';
    angular
        .module('common')
        .directive('clamp', clampDirective);

    function clampDirective($timeout, $rootScope) {
        var directive = {
            restrict: 'A',
            scope: {
                clampExpandable: '@',
                disabledForSeo: '@',
                clampAutoresize: '@'
            },
            link: linkDirective
        };

        return directive;

        function linkDirective(scope, element, attrs) {
            var originalHtml = '';

            var reset = function reset() {
                $timeout(function() {
                    element.css('display', 'block');

                    originalHtml = element.html();

                    doClamp(scope, element, attrs, originalHtml);
                });
            };

            // ==========

            if (scope.clampAutoresize === 'true') {
                $rootScope.$on('window_resized', function() {
                    doClamp(scope, element, attrs, originalHtml);
                });
            }

            // hide the element for a bit to prevent flicker
            element.css('display', 'none');

            reset();

            // a way to refresh all clampings without data binding
            scope.$on('clampReset', reset);
        }
    }

    return;

    function doClamp(scope, element, attrs, originalHtml) {
        var lineCount = 1, lineMax = isNaN(attrs.clamp) ? 3 : +attrs.clamp;
        var lineStart = 0, lineEnd = 0;

        var expandable = scope.clampExpandable === 'true';
        var shouldExpand = false;

        // reset
        element.html(originalHtml);

        // 2 things:
        // - we need only the text for clamping & remove excessive spaces (>1 to 1)
        // - append a whitespace, so that the exact last word (rare case) will be calculated
        var text = element.text().replace(/\s+/g, ' ') + ' ';
        var maxWidth = element[0].offsetWidth;
        var estimateTag = createElement();

        element.empty();
        element.append(estimateTag);

        text.replace(/ /g, function(m, pos) {
            if (lineCount <= lineMax) {
                estimateTag.html(text.slice(lineStart, pos));

                var innerHTML = estimateTag[0].innerHTML;
                var hasBr = innerHTML.indexOf("<br>") > -1;
                if (estimateTag[0].offsetWidth > maxWidth || hasBr) {
                    lineCount++;

                    if (lineCount > lineMax) {
                        shouldExpand = true;
                        return;
                    }

                    estimateTag.html(text.slice(lineStart, lineEnd));
                    resetElement(estimateTag);
                    lineStart = hasBr ? pos : lineEnd + 1;
                    estimateTag = createElement();
                    element.append(estimateTag);
                }

                lineEnd = pos;
            }
        });
        estimateTag.html(text.slice(lineStart));
        resetElement(estimateTag, true);

        if (expandable && shouldExpand) {
            element.css('cursor', 'pointer');

            // clean up
            element.off('click');

            element.one('click', function() {
                element.html(originalHtml);
                element.css('cursor', 'default');
            });

            var showMore = angular.element('<a style="display: block; text-align: center">Read more</a>');

            element.append(showMore);
        }
    }

    function createElement() {
        var tagDiv = document.createElement('div');
        (function(s) {
            s.position = 'absolute';
            s.whiteSpace = 'pre';
            s.visibility = 'hidden';
            s.display = 'inline-block';
        })(tagDiv.style);

        return angular.element(tagDiv);
    }

    function resetElement(element, type) {
        if(type && element[0].innerHTML){
            var innerHTML = element[0].innerHTML.replace(/<br>/g, "");
            element[0].innerHTML = innerHTML;
        }
        element.css({
            position: 'inherit',
            overflow: 'hidden',
            display: 'block',
            textOverflow: (type ? 'ellipsis' : 'clip'),
            visibility: 'inherit',
            whiteSpace: 'nowrap',
            width: '100%'
        });
    }
})();