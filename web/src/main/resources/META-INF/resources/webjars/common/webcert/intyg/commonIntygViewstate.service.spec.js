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

describe('commonIntygViewstateService', function() {
    'use strict';

    describe('Testa detektering av ändrat namn eller adress vid djupintegration', function() {
        var commonIntygViewstateService;
        var intygModel = {
            grundData: {
                patient: {
                    fornamn:'Tolvan',
                    efternamn:'Tolvansson',
                    postadress: 'Blomstervägen 13',
                    postort: 'Småmåla',
                    postnummer: '123 45'
                }
            }
        };

        beforeEach(angular.mock.inject(['common.IntygViewStateService',
            function(_commonIntygViewstateService_) {
                commonIntygViewstateService = _commonIntygViewstateService_;
            }
        ]));

        it('hasNameChanged true if efternamn has changed', function() {
            expect(commonIntygViewstateService.patient.hasChangedName(intygModel, {'fornamn':'Tolvan','efternamn':'Tolvansson-changed'})).toBeTruthy();
        });
        it('hasNameChanged true if fornamn has changed', function() {
            expect(commonIntygViewstateService.patient.hasChangedName(intygModel, {'fornamn':'Tolvan-changed','efternamn':'Tolvansson'})).toBeTruthy();
        });
        it('hasNameChanged false if none has changed', function() {
            expect(commonIntygViewstateService.patient.hasChangedName(intygModel, {'fornamn':'Tolvan','efternamn':'Tolvansson'})).toBeFalsy();
        });

        it('hasAddressChanged false if none has changed', function() {
            expect(commonIntygViewstateService.patient.hasChangedAddress(intygModel, {postadress: 'Blomstervägen 13', postort: 'Småmåla', postnummer: '123 45'})).toBeFalsy();
        });
        it('hasAddressChanged true if postadress has changed', function() {
            expect(commonIntygViewstateService.patient.hasChangedAddress(intygModel, {postadress: 'Blomstervägen 12', postort: 'Småmåla', postnummer: '123 45'})).toBeTruthy();
        });
        it('hasAddressChanged true if postort has changed', function() {
            expect(commonIntygViewstateService.patient.hasChangedAddress(intygModel, {postadress: 'Blomstervägen 13', postort: 'Stormåla', postnummer: '123 45'})).toBeTruthy();
        });
        it('hasAddressChanged true if postnummer has changed', function() {
            expect(commonIntygViewstateService.patient.hasChangedAddress(intygModel, {postadress: 'Blomstervägen 13', postort: 'Småmåla', postnummer: '54 321'})).toBeTruthy();
        });
    });
});