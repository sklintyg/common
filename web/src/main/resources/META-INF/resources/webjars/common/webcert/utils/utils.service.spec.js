describe('UtilsService', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var utilsService;


    beforeEach(angular.mock.inject(['common.UtilsService', function(_utilService_) {
        utilsService = _utilService_;
    }]));


    it('should replace swedish accented characters"', function() {
        expect(utilsService.replaceAccentedCharacters('Hey ÅåÄäÖö there')).toEqual('Hey AaAaOo there');
    });

    it('should replace consider empty string as false"', function() {
        expect(utilsService.isValidString('')).toBeFalsy();
    });

    it('should replace consider null string as false"', function() {
        expect(utilsService.isValidString(null)).toBeFalsy();
    });

    it('should replace consider undefined string as false"', function() {
        expect(utilsService.isValidString(undefined)).toBeFalsy();
    });

    it('should replace consider \'Hello Sunshine!\' string as false"', function() {
        expect(utilsService.isValidString('Hello Sunshine!')).toBeTruthy();
    });

});
