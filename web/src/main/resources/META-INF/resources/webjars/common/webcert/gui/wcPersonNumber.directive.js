/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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
 * Directive to check if a value is a valid personnummer or samordningsnummer. The validation follows the specification
 * in SKV 704 and SKV 707. The model holds the number in the format ååååMMdd-nnnn (or ååååMMnn-nnnn in the case of
 * samordningsnummer) but it allows the user to input the number in any of the valid formats.
 */
angular.module('common').directive('wcPersonNumber', ['common.PersonIdValidatorService',
    function(personIdValidator) {
        'use strict';

        return {

            restrict: 'A',
            require: 'ngModel',

            link: function(scope, element, attrs, ctrl) {

                ctrl.$parsers.unshift(function(viewValue) {
                    var number = personIdValidator.validate(viewValue);
                    ctrl.$setValidity('personNumberValidate', number !== undefined);
                    return number;
                });
            }
        };
    }]);
