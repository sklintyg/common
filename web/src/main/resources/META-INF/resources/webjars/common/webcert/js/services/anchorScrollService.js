/**
 * Created by eriklupander on 15-04-29.
 */
angular.module('common').factory('common.anchorScrollService', function($window, $location, $anchorScroll) {
    'use strict';

    function _scrollTo(anchorName) {
        var old = $location.hash();
        $location.hash(anchorName);
        $anchorScroll();
        // TODO add soft scrolling.
        // reset to old to keep any additional routing logic from kicking in
        $location.hash(old);
    }

    function _scrollToWithOffset(anchorName, offset) {
        _scrollTo(anchorName);
        // TODO Apply offset

    }


    return {
        scrollTo : _scrollTo,
        scrollToWithOffset : _scrollToWithOffset
    };

});