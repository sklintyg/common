/**
 * Created by pebe on 2015-02-12.
 */
angular.module('common').factory('common.LocationUtilsService',function($window, $location) {
    'use strict';

    function _getDomainAndPath(str) {
        var pattern = /.*\/\/([^\/]+)([^#]*)#?(\/.*)/;
        pattern.lastindex = 0;
        var match = pattern.exec(str);
        return {
            domain:match[1],
            path:match[2],
            search:match[3]
        };
    }

    function _changeUrl(oldUrl, newUrl) {
        // Setting $window.location.href doesnt work in IE
        // To solve this problem
        // $window.location should be used for external URL, with a window.location.reload() after
        // $location.path should be used for internal URL

        var oldUrlParsed = _getDomainAndPath(oldUrl);
        var newUrlParsed = _getDomainAndPath(newUrl);
        // An internal url has the same domain and contains a #/
        if (oldUrlParsed.domain === newUrlParsed.domain && newUrlParsed.search) {
            $location.path(newUrlParsed.search);
        }
        else {
            $window.location.href = newUrl;
            $window.location.reload();
        }
    }

    return {
        changeUrl: _changeUrl
    };

});
