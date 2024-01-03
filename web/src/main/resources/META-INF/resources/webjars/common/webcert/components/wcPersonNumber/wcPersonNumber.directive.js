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
/**
 * Directive to check if a value is a valid personnummer or samordningsnummer. The validation follows the specification
 * in SKV 704 and SKV 707. The model holds the number in the format ååååMMdd-nnnn (or ååååMMnn-nnnn in the case of
 * samordningsnummer) but it allows the user to input the number in any of the valid formats.
 */
angular.module('common').directive('wcPersonNumber', ['common.PersonIdValidatorService', 'common.UtilsService',
    function(personIdValidator, utils) {
        'use strict';

        return {
            restrict: 'A',
            require: 'ngModel',
            link: function(scope, element, attrs, ngModel) {

                ngModel.$validators.validPnr = function(modelValue, viewValue) {

                    if(viewValue === ''){
                        return true;
                    }

                    var number = personIdValidator.validate(viewValue);

                    if (ngModel.$viewValue === '') {
                        ngModel.$setUntouched();
                    }
                    return number !== undefined;
                };

                function handleViewValueUpdate(newValue, oldValue) {

                    if(newValue === ''){
                        ngModel.$setUntouched();
                        ngModel.$setValidity('validPnr', true);
                    }

                    if(!newValue) {
                        return;
                    }

                    function preventUnwantedCharacters(newValue, oldValue) {

                        function updateViewValue(value) {
                            ngModel.$setViewValue(value);
                            ngModel.$render();
                        }

                        var lookingLikePnr = /^[0-9]*-?[0-9]*$/i;

                        // if new value is longer than older we care, otherwise something that we already approved was removed
                        if (!oldValue || (newValue.length > oldValue.length)) {
                            if (!newValue.match(lookingLikePnr) ||
                                (newValue.length !== 9 && newValue[newValue.length - 1] === '-')) {
                                // remove last addition if it doesn't match the pnr pattern or if dash was added prematurely/late
                                newValue = oldValue;
                                updateViewValue(newValue);
                            } else if (newValue.length === 8 || newValue.length === 9) {
                                // add dash if 8 chars were typed
                                newValue = utils.insertAt(newValue, '-', 8);
                                updateViewValue(newValue);
                            }
                            if (newValue && newValue.length === 13) {
                                ngModel.$setTouched();
                            }
                        } else if ((!oldValue || (newValue.length <= oldValue.length)) &&
                            !newValue.match(lookingLikePnr)) {

                            // remove last addition if it doesn't match the pnr pattern or if dash was added prematurely/late
                            newValue = oldValue;
                            updateViewValue(newValue);
                        }
                    }

                    preventUnwantedCharacters(newValue, oldValue);
                }

                scope.$watch(function() {
                    return ngModel.$viewValue;
                }, handleViewValueUpdate);

            }
        };
    }]);
