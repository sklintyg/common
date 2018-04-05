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

/**
 * AutoExpand directive.
 *
 * Used to let text areas auto size it's height.
 */

angular.module('common').directive('autoExpand', ['$window', '$sniffer', function($window, $sniffer) {
    'use strict';

    return  {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs, ctrl) {

            // Default settings
            scope.attrs = {
                rows: 2
            };

            // Merge defaults with user preferences
            for(var i in scope.attrs){
                if(attrs[i]){
                    scope.attrs[i] = parseInt(attrs[i], 10);
                }
            }

            // Ensure the element is a textarea, browser is capable and autoexpand something else than false
            if (element[0].nodeName !== 'TEXTAREA' || !$window.getComputedStyle || attrs.autoExpand === 'false') {
                return;
            }

            var node = element[0];
            var rows = parseInt(scope.attrs.rows, 10);
            var lineHeight = _getLineHeight(node, rows);

            if (ctrl){
                // view value changed from ngModelController - textarea content changed via javascript
                scope.$watch(function(){
                    return ctrl.$viewValue;
                }, adjust);
            }

            // element became visible
            scope.$watch(function(){
                // element is visible if at least one of those values is not 0
                return node.offsetHeight || node.offsetWidth;
            }, function(newVal, oldVal){
                if(newVal && !oldVal) {
                    adjust();
                }
            });

            // browser resize occurrence
            angular.element($window).on('resize', adjust);

            // initial adjust
            adjust();

            function adjust() {
                if (isNaN(lineHeight)) {
                    lineHeight = _getLineHeight(node);
                }
                if (!(node.offsetHeight || node.offsetWidth)) {
                    return;
                }
                if (node.scrollHeight <= node.clientHeight) {
                    node.style.height = '0px';
                }
                var h = node.scrollHeight + // actual height defined by content
                    node.offsetHeight - // border size compensation
                    node.clientHeight; //       -- || --
                var isIE = $sniffer.msie || $sniffer.vendorPrefix && $sniffer.vendorPrefix.toLowerCase() === 'ms';
                node.style.height = Math.max(h, lineHeight) +
                    (isIE ? 1 : 0) + // ie quirk
                    'px';
            }

            function cleanUp() {
                angular.element($window).off('resize', adjust);
            }

            scope.$on('$destroy', cleanUp);
        }
    };

    function _px2val(str) {
        return +str.slice(0, -2);
    }

    function _getLineHeight(node, rows){
        var lineHeightThreshold = 1.6;

        var computedStyle = $window.getComputedStyle(node);
        var lineHeightStyle = computedStyle.lineHeight;
        var fontSizeStyle = computedStyle.fontSize;

        var minHeight = _px2val(fontSizeStyle) * rows * lineHeightThreshold;

        if (lineHeightStyle === 'normal') {
            return minHeight;
        } else {
            var height = _px2val(lineHeightStyle);
            return minHeight > height ? minHeight : height;
        }
    }

    angular.element(document.head).append(
        '<style>[autoheight]{overflow: hidden; resize: none; box-sizing: border-box;}</style>'
    );

}]);
