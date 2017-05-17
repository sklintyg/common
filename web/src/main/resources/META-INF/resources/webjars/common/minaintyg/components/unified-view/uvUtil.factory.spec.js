/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

describe('UvUtil', function() {
    'use strict';

    beforeEach(angular.mock.module('common'));

    var uvUtil;
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
    };

    beforeEach(angular.mock.inject(['uvUtil', function(_uvUtil_) {
        uvUtil = _uvUtil_;
    }]));


    it('should should handle getValue for undefined property', function() {
        expect(uvUtil.getValue(testObject, 'doesNotExist')).toEqual(undefined);
    });

    it('should should handle getValue for simple property', function() {
        expect(uvUtil.getValue(testObject, 'simpleProp')).toEqual('simplePropValue');
    });

    it('should should handle getValue for nested property', function() {
        expect(uvUtil.getValue(testObject, 'nestedProp.nestedProp1')).toEqual('nestedProp1Value');
    });

    it('should should handle getValue for array property', function() {
        expect(uvUtil.getValue(testObject, 'arrayObjectProp[1].svar')).toEqual('svar2');
    });

    it('should should handle getValue for primitive array property value', function() {
        expect(uvUtil.getValue(testObject, 'nestedProp.arrayObjectProp[1].svar[0]')).toEqual(
            'primitiveArrayValue');
    });

});
