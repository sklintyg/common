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
