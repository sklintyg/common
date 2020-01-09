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

describe('Filter: miRelevantEvent', function() {
    'use strict';

    var _filter;

    beforeEach(angular.mock.module('common'));

    beforeEach(inject(function(_miRelevantEventFilterFilter_) {
        _filter = _miRelevantEventFilterFilter_;
    }));

    it('should filter non-wanted events', function() {
        var events = [ {
            type: 'OTHER'
        }, {
            type: 'SENT'
        }, {
            type: 'UNKNOWN'
        }, {
            type: 'ERSATT'
        }, {
            type: 'ERSATTER'
        }, {
            type: 'KOMPLETTERAT'
        }, {
            type: 'KOMPLETTERAR'
        } ];
        expect(_filter(events).length).toEqual(5);
        expect(_filter(events)[0].type).toEqual('SENT');
        expect(_filter(events)[1].type).toEqual('ERSATT');
        expect(_filter(events)[2].type).toEqual('ERSATTER');
        expect(_filter(events)[3].type).toEqual('KOMPLETTERAT');
        expect(_filter(events)[4].type).toEqual('KOMPLETTERAR');
    });

    it('should handle no events', function() {
        var events = [];

        expect(_filter(events)).toEqual([]);
        expect(_filter(undefined)).toEqual([]);
    });

});
