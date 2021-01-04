/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.wcDatePeriodFieldHelper', function() {
    'use strict';

    /**
     * -------|      |------
     * ----|...   |------
     */
    function isTomWithinPeriod(datePeriod, datePeriod2) {
        return datePeriod.from.moment &&
            datePeriod.tom.moment &&
            datePeriod2.tom.moment &&
            (datePeriod2.tom.moment.isSame(datePeriod.from.moment) || datePeriod2.tom.moment.isAfter(datePeriod.from.moment)) &&
            (datePeriod2.tom.moment.isSame(datePeriod.tom.moment) || datePeriod2.tom.moment.isBefore(datePeriod.tom.moment));
    }

    /**
     * -------|      |------
     * ----------|   ...|------
     */
    function isFromWithinPeriod(datePeriod, datePeriod2) {
        return datePeriod.from.moment &&
            datePeriod.tom.moment &&
            datePeriod2.from.moment &&
            (datePeriod2.from.moment.isSame(datePeriod.from.moment) || datePeriod2.from.moment.isAfter(datePeriod.from.moment)) &&
            (datePeriod2.from.moment.isSame(datePeriod.tom.moment) || datePeriod2.from.moment.isBefore(datePeriod.tom.moment));
    }

    var hasOverlap = function(datePeriod, datePeriod2) {
        return isFromWithinPeriod(datePeriod, datePeriod2) ||
            isTomWithinPeriod(datePeriod, datePeriod2) ||
            isFromWithinPeriod(datePeriod2, datePeriod) ||
            isTomWithinPeriod(datePeriod2, datePeriod);
    };

    return {
        hasOverlap: hasOverlap
    };
});