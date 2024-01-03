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
angular.module('common').filter('PersonIdFormatter', ['common.PersonIdValidatorService',
    function(PersonIdValidatorService) {
        'use strict';

        /**
         * The PersonIdValidatorService returns a yyyyMMdd-NNNN pnr from the validate method if and only if
         * the supplied input is a valid personnummer or samordningsnummer.
         * Otherwise, null or undefined are returned. In those cases, this filter will just return the input unprocessed.
         */
        return function(input) {
            var validated = PersonIdValidatorService.validate(input);
            if (PersonIdValidatorService.validResult(validated)) {
                return validated;
            }
            return input;
        };
    }]);
