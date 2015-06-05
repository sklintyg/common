/**
 * Created by eriklupander on 15-04-29.
 */
angular.module('common').factory('common.anchorScrollService', ['$location', 'smoothScroll', function($location, smoothScrollService) {
    'use strict';

    /**
     *
     * @param anchorName ID on the anchor element.
     * @param offset If undefined, an offset based on the top menu height will be applied.
     * @private
     */
    function _scrollTo(anchorName, offset) {

        var old = $location.hash();
        $location.hash(anchorName);

        var elementToScrollTo = angular.element.find('#' + _escape(anchorName))[0];

        var options = {
            duration: 500,
            easing: 'easeInOutQuart',
            offset: _calculateOffset(offset)
        }

        smoothScrollService(elementToScrollTo, options);

        // reset to old to keep any additional routing logic from kicking in
        $location.hash(old);
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
        scrollToWithOffset : _scrollToWithOffset
    };

}]);