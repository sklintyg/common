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
