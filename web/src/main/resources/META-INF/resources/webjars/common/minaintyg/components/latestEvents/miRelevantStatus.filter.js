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

angular.module('common').filter('miRelevantStatusFilter', function() {
    'use strict';
    //For now only this status. If needed, _visibleStatuses could be made coonfigurable
    var _visibleStatuses = [ 'SENT' ];

    function _isRelevant(status) {
        var result = false;
        angular.forEach(_visibleStatuses, function(relevantStatus) {
            if (relevantStatus === status.type) {
                result = true;
            }
        });
        return result;
    }

    return function(statuses) {
        var result = [];

        angular.forEach(statuses, function(status) {
            if (_isRelevant(status)) {
                result.push(status);
            }
        });
        return result;
    };

});
