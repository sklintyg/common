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

  // isDefined
  it('should return false for undefined', function() {
    expect(objectHelper.isDefined(undefined)).toEqual(false);
  });

  it('should return false for null', function() {
    expect(objectHelper.isDefined(null)).toEqual(false);
  });

  it('should return true for empty string', function() {
    expect(objectHelper.isDefined('')).toEqual(true);
  });

  // isEmpty
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

  // isModelValue
  it('should return true for none-empty string', function() {
    expect(objectHelper.isModelValue('hello')).toEqual(true);
  });

  it('should return false for empty string', function() {
    expect(objectHelper.isModelValue('')).toEqual(false);
  });

  it('should return false for undefined', function() {
    expect(objectHelper.isModelValue()).toEqual(false);
  });

  it('should return false for null', function() {
    expect(objectHelper.isModelValue(null)).toEqual(false);
  });

  it('should return false for empty array', function() {
    expect(objectHelper.isModelValue([])).toEqual(false);
  });

  it('should return true for none-empty array', function() {
    expect(objectHelper.isModelValue(['something'])).toEqual(true);
  });

  it('should return true for object', function() {
    expect(objectHelper.isModelValue({})).toEqual(true);
  });

  it('should return false for false', function() {
    expect(objectHelper.isModelValue(false)).toEqual(false);
  });

  it('should return true for true', function() {
    expect(objectHelper.isModelValue(false)).toEqual(false);
  });

});
