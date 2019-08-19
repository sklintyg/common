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

  it('should consider empty string as invalid"', function() {
    expect(utilsService.isValidString('')).toBeFalsy();
  });

  it('should consider null string as invalid"', function() {
    expect(utilsService.isValidString(null)).toBeFalsy();
  });

  it('should consider undefined string as invalid"', function() {
    expect(utilsService.isValidString(undefined)).toBeFalsy();
  });

  it('should consider \'Hello Sunshine!\' string as valid"', function() {
    expect(utilsService.isValidString('Hello Sunshine!')).toBeTruthy();
  });

  it('should extract number from numerical value', function() {
    expect(utilsService.extractNumericalFrageId(25)).toBe(25);
  });

  it('should extract number from all-number string', function() {
    expect(utilsService.extractNumericalFrageId('9001')).toBe('9001');
  });

  it('should extract number from "fragerubrik" string', function() {
    expect(utilsService.extractNumericalFrageId('FRG_23.RBK')).toBe('23');
  });

  it('should not be able to number from nonnumerical string', function() {
    expect(utilsService.extractNumericalFrageId('HelloWorld')).toBeUndefined();
  });

  describe('endsWith', function() {
    it('should return false for undefined, null and empty strings', function() {
      expect(utilsService.endsWith(undefined, 'test')).toBeFalsy();
      expect(utilsService.endsWith(null, 'test')).toBeFalsy();
      expect(utilsService.endsWith('', 'test')).toBeFalsy();
    });

    it('should return false for no matching string', function() {
      expect(utilsService.endsWith('apa', 'bepa')).toBeFalsy();
    });

    it('should return false for matching string not at end', function() {
      expect(utilsService.endsWith('bepa apa', 'bepa')).toBeFalsy();
    });

    it('should return true for matching string at end', function() {
      expect(utilsService.endsWith('apa bepa', 'bepa')).toBeTruthy();
    });

    it('should return true for exact matching string at end', function() {
      expect(utilsService.endsWith('bepa', 'bepa')).toBeTruthy();
    });

    it('should return false for undefined, null and empty matcher strings', function() {
      expect(utilsService.endsWith('test', undefined)).toBeFalsy();
      expect(utilsService.endsWith('test', null)).toBeFalsy();
      expect(utilsService.endsWith('test', '')).toBeFalsy();
    });

  });

});
