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
/**
 * Created by eriklupander on 15-04-29.
 */
angular.module('common').factory('common.anchorScrollService', ['$location', '$log', 'smoothScroll', function($location, $log, smoothScrollService) {
    'use strict';

    /**
     *
     * @param anchorName ID on the anchor element.
     * @param offset If undefined, an offset based on the top menu height will be applied.
     * @private
     */
    function _scrollTo(anchorName, offset) {

        var elementToScrollTo = angular.element.find('#' + _escape(anchorName))[0];

        if (!elementToScrollTo) {
            $log.warn('Unable to find scrollTo target "' + anchorName + '"');
            return;
        }

        var options = {
            duration: 500,
            easing: 'easeInOutQuart',
            offset: _calculateOffset(offset)
        };

        smoothScrollService(elementToScrollTo, options);

    }

    /**
     * Scrolls to an anchor tag within a div handling it's overflow.
     * @param anchorName
     * @param offset
     * @private
     */
    function _scrollIntygContainerTo(anchorName, offset) {
        var intygContainerId = 'certificate-content-container';

        var elementToScrollTo = angular.element.find('#' + _escape(anchorName))[0];

        if (!elementToScrollTo) {
            $log.warn('Unable to find scrollTo target "' + anchorName + '"');
            return;
        }

        var options = {
            containerId: intygContainerId,
            duration: 500,
            easing: 'easeInOutQuart',
            offset: offset || 0
        };

        smoothScrollService(elementToScrollTo, options);

    }


    function _scrollToWithOffset(anchorName, offset) {
        _scrollTo(anchorName, offset);
    }

    /**
     * Calculate offset based on top menu height.
     *
     * @param offset
     * @returns if offset was supplied, the unchanged value is returned. If an undefined value was supplied,
     *          the return value is calculated from the first element found in the DOM having the 'header-fix-top'
     *          class.
     * @private
     */
    function _calculateOffset(offset) {
        if (offset === null || typeof offset === 'undefined') {

            var topMenuElements = angular.element.find('.header-fix-top');
            if (topMenuElements.length > 0) {
                offset = angular.element(topMenuElements[0]).prop('offsetHeight');
            } else {
                offset = 0;
            }
        }
        return offset;
    }

    /**
     * Since the anchors uses dots in their id's, we need to esacpe them before passing them to the selector function.
     */
    function _escape(str) {
        return str.replace(/\./g, '\\.');
    }


    return {
        scrollTo : _scrollTo,
        scrollIntygContainerTo: _scrollIntygContainerTo,
        scrollToWithOffset : _scrollToWithOffset
    };

}]);
