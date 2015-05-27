describe('wcPersonNumber', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var personIdValidatorService;

    // Create a form to test the validation directive on.
    beforeEach(angular.mock.inject(['common.PersonIdValidatorService', function(_personIdValidatorService_) {
        personIdValidatorService = _personIdValidatorService_;
    }]));

    var isCheckDigitValid = function(value) {

        // Remove separator.
        var cleanValue = value.replace(/[-+]/, '');

        // Multiply each of the digits with 2,1,2,1,...
        var digits = cleanValue.substring(0, cleanValue.length - 1).split('');
        var multipliers = [2, 1, 2, 1, 2, 1, 2, 1, 2];
        var digitsMultiplied = '';
        for (var i = 0; i < digits.length; i++) {
            digitsMultiplied += parseInt(digits[i], 10) * multipliers[i];
        }

        // Calculate the sum of all of the digits.
        digits = digitsMultiplied.split('');
        var sum = 0;
        for (i = 0; i < digits.length; i++) {
            sum += parseInt(digits[i], 10);
        }
        sum = sum % 10;

        // Get the specified check digit.
        var checkDigit = cleanValue.substring(cleanValue.length - 1);

        if (sum === 0 && checkDigit === '0') {
            return true;
        } else {
            return (10 - sum) === parseInt(checkDigit, 10);
        }
    };

    var createValidPersonnummer = function(dateStr) {
        // dateStr must be on the form yyyyMMdd

        var PERSONNUMMER_REGEXP = /^(\d{2})?(\d{2})(\d{2})([0-3]\d)$/;
        var SUFFIX = '121';

        var parts = PERSONNUMMER_REGEXP.exec(dateStr);

        if (parts) {
            for (var i = 0; i < 10; i++) {
                var pnr = parts[2] + parts[3] + parts[4] + '-' + SUFFIX + i;
                if (isCheckDigitValid(pnr)) {
                    return parts[1] + pnr;
                }
            }
        }

        return undefined;
    };

    // Pass

    it('should pass with a valid "personnummer" with format "yyyyMMdd-nnnn"', function() {
        var result = personIdValidatorService.validate('19121212-1212');
        expect(result).toEqual('19121212-1212');
    });

    it('should pass with a valid "personnummer" with format "yyyyMMddnnnn"', function() {
        var result = personIdValidatorService.validate('191212121212');
        expect(result).toEqual('19121212-1212');
    });

    it('should pass with a valid "personnummer" with format yyMMdd-nnnn', function() {
        var result = personIdValidatorService.validate('121212-1212');
        expect(result).toEqual('20121212-1212');
    });

    it('should pass with a valid "personnummer" with format yyMMdd+nnnn', function() {
        var result = personIdValidatorService.validate('121212+1212');
        expect(result).toEqual('19121212-1212');
    });

    it('should pass with a valid "personnummer" with format yyMMddnnnn', function() {
        var result = personIdValidatorService.validate('1212121212');
        expect(result).toEqual('20121212-1212');
    });

    it('should pass with a "personnummer" with an age of 125 years or less', function() {
        var pnr = createValidPersonnummer(moment().subtract('years', 125).format('YYYYMMDD'));

        var result = personIdValidatorService.validate(pnr);
        expect(result).not.toBeNull();
        expect(result).not.toBeUndefined();

        pnr = createValidPersonnummer(moment().subtract('years', 124).format('YYYYMMDD'));
        result = personIdValidatorService.validate(pnr);
        expect(result).not.toBeNull();
        expect(result).not.toBeUndefined();
    });

    it('should pass with a valid "samordningsnummer" with format "yyyyMMnn-nnnn"', function() {
        var result = personIdValidatorService.validate('19121272-1219');
        expect(result).toEqual('19121272-1219');
    });

    it('should pass with a valid "samordningsnummer" with format "yyyyMMnnnnnn"', function() {
        var result = personIdValidatorService.validate('191212721219');
        expect(result).toEqual('19121272-1219');
    });

    it('should pass with a valid "samordningsnummer" with format "yyMMnn-nnnn"', function() {
        var result = personIdValidatorService.validate('121272-1219');
        expect(result).toEqual('20121272-1219');
    });

    it('should pass with a valid "samordningsnummer" with format "yyMMnnnnnn"', function() {
        var result = personIdValidatorService.validate('1212721219');
        expect(result).toEqual('20121272-1219');
    });

    // Fail

    it('should fail if "personnummer" has invalid check digit', function() {
        var result = personIdValidatorService.validate('121212-1213');
        expect(result).toBeUndefined();
    });

    it('should fail if "personnummer" has invalid date', function() {
        var result = personIdValidatorService.validate('121232-1213');
        expect(result).toBeUndefined();
    });

    it('should fail if "personnummer" with an age greater than 125 years', function() {
        var pnr = createValidPersonnummer(moment().subtract('years', 126).format('YYYYMMDD'));
        var result = personIdValidatorService.validate(pnr);
        expect(result).toBeUndefined();
    });

    it('should fail with if "personnummer" has invalid characters', function() {
        var result = personIdValidatorService.validate('121212.1213');
        expect(result).toBeUndefined();
    });

    it('should fail if "samordningsnummer" has invalid check digit', function() {
        var result = personIdValidatorService.validate('121272-1213');
        expect(result).toBeUndefined();
    });

    it('should fail with if "samordningsnummer" has invalid date', function() {
        var result = personIdValidatorService.validate('121292-1215');
        expect(result).toBeUndefined();
    });

    it('should fail with if "samordningsnummer" has invalid characters', function() {
        var result = personIdValidatorService.validate('121272.1219');
        expect(result).toBeUndefined();
    });
});
