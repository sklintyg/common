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
