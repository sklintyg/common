/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

describe('common.domain.ModelTransformService', function() {
    'use strict';

    var modelTransform;

    beforeEach(angular.mock.module('common'));

    // Get references to the object we want to test from the context.
    beforeEach(angular.mock.inject([
        'common.domain.ModelTransformService',
        function( _modelTransform_) {
            modelTransform = _modelTransform_;
        }]));

    describe('#model transform service', function() {
        it('toStringFilter should trim and set undefined for empty strings', function(){
            expect(modelTransform.toStringFilter(' asfasf ')).toBe('asfasf');
            expect(modelTransform.toStringFilter(' ')).toBeUndefined();
            expect(modelTransform.toStringFilter('')).toBeUndefined();
        });
    });
});
