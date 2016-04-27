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

describe('ObjectHelper', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var objectHelper;
    var testObject = {
        simpleProp: 'simplePropValue',
        nestedProp: {
            nestedProp1: 'nestedProp1Value',
            arrayObjectProp: [
                {svar: 'svarA1'},
                {svar: ['primitiveArrayValue']}
            ]
        },
        arrayObjectProp: [
            {svar: 'svar1'},
            {svar: 'svar2'}
        ]
    }

    beforeEach(angular.mock.inject(['common.ObjectHelper', function(_objectHelper_) {
        objectHelper = _objectHelper_;
    }]));

    it('should return true if undefined is passed to isFalsy', function() {
        var result = objectHelper.isFalsy(undefined);
        expect(result).toEqual(true);
    });

    it('should return true if "false" is passed to isFalsy', function() {
        var result = objectHelper.isFalsy('false');
        expect(result).toEqual(true);
    });

    it('should return true if false is passed to isFalsy', function() {
        var result = objectHelper.isFalsy(false);
        expect(result).toEqual(true);
    });

    it('should return true if null is passed to isFalsy', function() {
        var result = objectHelper.isFalsy(null);
        expect(result).toEqual(true);
    });

    it('should return true if "" (empty string) is passed to isFalsy', function() {
        var result = objectHelper.isFalsy("");
        expect(result).toEqual(true);
    });

    it('should return true if "" (empty string) is passed to isFalsy', function() {
        var result = objectHelper.isFalsy("TEXT");
        expect(result).toEqual(false);
    });


    it('should return false for undefined', function() {
        expect(objectHelper.isDefined(undefined)).toEqual(false);
    });

    it('should return false for null', function() {
        expect(objectHelper.isDefined(null)).toEqual(false);
    });

    it('should return true for empty string', function() {
        expect(objectHelper.isDefined('')).toEqual(true);
    });


    it('should return true for null string', function() {
        expect(objectHelper.isEmpty(null)).toEqual(true);
        expect(objectHelper.isEmpty(undefined)).toEqual(true);
    });
    it('should return true for empty string', function() {
        expect(objectHelper.isEmpty('')).toEqual(true);
    });
    it('should return true for empty string', function() {
        expect(objectHelper.isEmpty('hello')).toEqual(false);
    });


    // deepGet tests
    it('should should handle deepGet for undefined property', function() {
        expect(objectHelper.deepGet(testObject, 'doesNotExist')).toEqual(undefined);
    });

    it('should should handle deepGet for simple property', function() {
        expect(objectHelper.deepGet(testObject, 'simpleProp')).toEqual('simplePropValue');
    });

    it('should should handle deepGet for nested property', function() {
        expect(objectHelper.deepGet(testObject, 'nestedProp.nestedProp1')).toEqual('nestedProp1Value');
    });

    it('should should handle deepGet for array property', function() {
        expect(objectHelper.deepGet(testObject, 'arrayObjectProp[1].svar')).toEqual('svar2');
    });

    it('should should handle deepGet for primitive array property value', function() {
        expect(objectHelper.deepGet(testObject, 'nestedProp.arrayObjectProp[1].svar[0]')).toEqual(
            'primitiveArrayValue');
    });
});
