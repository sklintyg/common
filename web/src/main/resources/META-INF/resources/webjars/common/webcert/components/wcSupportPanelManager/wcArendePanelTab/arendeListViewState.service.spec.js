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

describe('ArendeListViewState', function() {
    'use strict';

    var ArendeListViewStateService;

    var closed = {
        fraga: {
            status: 'CLOSED'
        }
    };

    var open = {
        fraga: {
            status: 'PENDING_INTERNAL_ACTION'
        }
    };

    beforeEach(angular.mock.inject([ 'common.ArendeListViewStateService', function(_ArendeListViewStateService_) {
        ArendeListViewStateService = _ArendeListViewStateService_;
    } ]));

    describe('hasUnhandledItems', function() {
        it('should return false if no unhandled items', function() {

            ArendeListViewStateService.setArendeList([ closed ]);
            expect(ArendeListViewStateService.hasUnhandledItems()).toBe(false);

        });
        
        it('should return false if no items', function() {

            ArendeListViewStateService.setArendeList([]);
            expect(ArendeListViewStateService.hasUnhandledItems()).toBe(false);

        });

        it('should return true if unhandled items', function() {

            ArendeListViewStateService.setArendeList([ open, closed ]);
            expect(ArendeListViewStateService.hasUnhandledItems()).toBe(true);
        });
    });

});
