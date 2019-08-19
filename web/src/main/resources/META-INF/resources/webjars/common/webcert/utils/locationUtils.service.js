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
 * Created by pebe on 2015-02-12.
 */
angular.module('common').factory('common.LocationUtilsService', function($window, $location) {
  'use strict';

  function _getDomainAndPath(str) {
    var pattern = /.*\/\/([^\/]+)([^#]*)#?(\/[^\?]*)\??(.*)/;
    pattern.lastindex = 0;
    var match = pattern.exec(str);
    return {
      domain: match[1],
      path: match[3],
      search: match[4]
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
    if (oldUrlParsed.domain === newUrlParsed.domain && newUrlParsed.path) {
      $location.path(newUrlParsed.path);
    } else {
      $window.location.href = newUrl;
      $window.location.reload();
    }
  }

  return {
    changeUrl: _changeUrl
  };

});
