angular.module('common').factory('common.ResourceLinkService', function() {
    'use strict';

    /**
     * Check if a specific ActionLinkType is available among the links.
     *
     * @param links
     *              links to check
     * @param linkType
     *              linktype to look for
     * @returns {boolean}
     *              true if available
     */
    function _isLinkTypeExists(links, linkType) {
        if (links === undefined) {
            return false;
        }

        for (var i = 0; i < links.length; i++) {
            if (links[i].type === linkType) {
                return true;
            }
        }
        return false;
    }


    return {
        isLinkTypeExists: _isLinkTypeExists
    };
});
