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

    describe('getValue', function() {
        it('should handle getValue for undefined property', function() {
            expect(uvUtil.getValue(testObject, 'doesNotExist')).toEqual(undefined);
        });

        it('should handle getValue for simple property', function() {
            expect(uvUtil.getValue(testObject, 'simpleProp')).toEqual('simplePropValue');
        });

        it('should handle getValue for nested property', function() {
            expect(uvUtil.getValue(testObject, 'nestedProp.nestedProp1')).toEqual('nestedProp1Value');
        });

        it('should handle getValue for array property', function() {
            expect(uvUtil.getValue(testObject, 'arrayObjectProp[1].svar')).toEqual('svar2');
        });

        it('should handle getValue for primitive array property value', function() {
            expect(uvUtil.getValue(testObject, 'nestedProp.arrayObjectProp[1].svar[0]')).toEqual(
                'primitiveArrayValue');
        });
    });

    describe('isValidValue', function() {
        it('should return false for undefined and null', function() {
            expect(uvUtil.isValidValue()).toBeFalsy();
            expect(uvUtil.isValidValue(undefined)).toBeFalsy();
            expect(uvUtil.isValidValue(null)).toBeFalsy();
        });

        it('should return false for empty string', function() {
            expect(uvUtil.isValidValue('')).toBeFalsy();
        });

        it('should return false for empty array', function() {
            expect(uvUtil.isValidValue([])).toBeFalsy();
        });

        it('should return true for object', function() {
            expect(uvUtil.isValidValue({a:'b'})).toBeTruthy();
            expect(uvUtil.isValidValue(new Date())).toBeTruthy();
        });

        it('should return true for strings and numbers', function() {
            expect(uvUtil.isValidValue('a')).toBeTruthy();
            expect(uvUtil.isValidValue(1)).toBeTruthy();
        });

        it('should return true for booleans', function() {
            expect(uvUtil.isValidValue(true)).toBeTruthy();
            expect(uvUtil.isValidValue(false)).toBeTruthy();
        });
    });

    describe('getModelProps', function() {
        it('handle empty object', function() {
            var viewConfig = {};
            expect(uvUtil.getModelProps(viewConfig)).toEqual([]);
        });

        it('on level 2', function() {
            var viewConfig = {
                components: [{
                    modelProp: 'level2'
                }]
            };
            expect(uvUtil.getModelProps(viewConfig)).toEqual(['level2']);
        });

        it('two levels', function() {
            var viewConfig = {
                modelProp: 'level1',
                components: [{
                    modelProp: 'level2'
                }]
            };
            expect(uvUtil.getModelProps(viewConfig)).toEqual(['level1', 'level2']);
        });
    });

    describe('replaceType', function() {
        it('handle empty array', function() {
            var viewConfig = [];
            expect(uvUtil.replaceType(viewConfig)).toEqual([]);
        });

        it('one level', function() {
            var viewConfig = [{
                type: 'uv-kategory'
            }];

            var viewConfigNew = [{
                type: 'uv-kategory2'
            }];

            expect(uvUtil.replaceType(viewConfig, 'uv-kategory', 'uv-kategory2')).toEqual(viewConfigNew);
        });

        it('two levels', function() {
            var viewConfig = [{
                type: 'uv-kategory',
                components: [{
                    type: 'uv-field',
                    modelProp: 'level2'
                }]
            }];

            var viewConfigNew = [{
                type: 'uv-kategory',
                components: [{
                    type: 'uv-field2',
                    modelProp: 'level2'
                }]
            }];

            expect(uvUtil.replaceType(viewConfig, 'uv-field', 'uv-field2')).toEqual(viewConfigNew);
        });
    });
});
