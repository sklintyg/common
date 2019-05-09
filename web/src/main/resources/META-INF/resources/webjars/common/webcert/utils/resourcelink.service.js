angular.module('common').factory('common.ResourceLinkService', function() {
    'use strict';

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
