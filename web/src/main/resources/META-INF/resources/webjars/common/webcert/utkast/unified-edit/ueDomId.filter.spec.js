/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

describe('Filter: ueDomIdFilter', function() {
    'use strict';

    var _filter;

    beforeEach(angular.mock.module('common'));

    beforeEach(inject(function(_ueDomIdFilterFilter_) {
        _filter = _ueDomIdFilterFilter_;
    }));

    it('should replace non-valid characters', function() {
        expect(_filter('ABC-D:E-[0].svar')).toEqual('ABC-D-E--0--svar');
        expect(_filter('my.prop')).toEqual('my-prop');
    });

    it('should not replace valid charachers', function() {
        expect(_filter('synVinkel1')).toEqual('synVinkel1');
    });

});
