/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('common').filter('miRelevantEventFilter', function() {
    'use strict';
    //For now only these events. If needed, _visibleEvents could be made coonfigurable
    var _visibleEvents = [ 'SENT', 'ERSATT', 'ERSATTER', 'KOMPLETTERAT', 'KOMPLETTERAR'];

    function _isRelevant(event) {
        var result = false;
        angular.forEach(_visibleEvents, function(relevantEvent) {
            if (relevantEvent === event.type) {
                result = true;
            }
        });
        return result;
    }

    return function(events) {
        var result = [];

        angular.forEach(events, function(event) {
            if (_isRelevant(event)) {
                result.push(event);
            }
        });
        return result;
    };

});
