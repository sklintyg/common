/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of statistik (https://github.com/sklintyg/statistik).
 *
 * statistik is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * statistik is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

fdescribe('Filter: PersonIdFormatter', function () {
    'use strict';

    // load the controller's module
    beforeEach(module('common'));

    it('has a PersonIdFormatter filter', inject(function($filter) {
        expect($filter('PersonIdFormatter')).not.toBeNull();
    }));

    it('should return pnr with dash for non-dashed one', inject(function (PersonIdFormatterFilter) {
        expect(PersonIdFormatterFilter('191212121212')).toEqual('19121212-1212');
    }));

    it('should return pnr with dash for shortone', inject(function (PersonIdFormatterFilter) {
        expect(PersonIdFormatterFilter('1212121212')).toEqual('20121212-1212');
    }));

    it('should return same as supplied when not a valid pnr', inject(function (PersonIdFormatterFilter) {
        expect(PersonIdFormatterFilter('abc-123-def-456')).toEqual('abc-123-def-456');
    }));
});
