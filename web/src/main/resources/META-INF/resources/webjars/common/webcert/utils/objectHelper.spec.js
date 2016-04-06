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

    
});
