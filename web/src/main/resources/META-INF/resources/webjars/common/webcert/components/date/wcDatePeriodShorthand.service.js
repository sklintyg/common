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

angular.module('common').factory('common.wcDatePeriodShorthandService',
    ['$log', 'common.DateUtilsService', function($log, dateUtilsService) {
      'use strict';

      /*
       If user enters a valid "in the future" code such a "d40" into the tom-field, it's
       date value should be set to from-date + 39 so that the total length is 40 days (requires a valid date in the from-field).
       */
      function _applyToDateCodes(fromValue, tomValue) {

        //1. fromField must have a valid date for his to work
        if (!fromValue || !dateUtilsService.dateReg.test(fromValue)) {
          return;
        }

        //2. The entered code must be a parsable expression
        var days = dateUtilsService.parseDayCodes(tomValue);
        if (days !== null) {
          //Take away 1 day, because the dayCode defines the total length of the interval we should get.
          return moment(fromValue).add(days - 1, 'days').format('YYYY-MM-DD');
        }

        return;
      }

      return {
        applyToDateCodes: _applyToDateCodes
      };

    }]);