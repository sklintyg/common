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

describe('PrefilledUserDataService', function() {
    'use strict';

    var prefilledUserDataService;

    beforeEach(angular.mock.inject(['common.PrefilledUserDataService',
        function(_prefilledUserDataService_) {
            prefilledUserDataService = _prefilledUserDataService_;
        }]));

    describe('For postadress', function() {
        var viewState;

        beforeEach(function() {
            viewState = {intygModel: {grundData: {patient: {}}}};
            viewState.intygModel.grundData.patient.postadress = 'Foovägen';
            viewState.intygModel.grundData.patient.postnummer = '12345';
            viewState.intygModel.grundData.patient.postort = 'Barsala';
        });

        it('should return positive when all parts of are present', function() {
            prefilledUserDataService.searchForPrefilledData(viewState);
            expect(prefilledUserDataService.getPrefilledFields().completeAddress).toBe(true);
        });

        it('should return negative if any part are ', function() {

            describe('missing', function() {
                delete viewState.intygModel.grundData.patient.postort;
                prefilledUserDataService.searchForPrefilledData(viewState);
                expect(prefilledUserDataService.getPrefilledFields().completeAddress).toBe(false);
            });

            describe('empty', function() {
                viewState.intygModel.grundData.patient.postort = '';
                prefilledUserDataService.searchForPrefilledData(viewState);
                expect(prefilledUserDataService.getPrefilledFields().completeAddress).toBe(false);
            });
        });

        it('should return undefined if not initialized by calling searchForPrefilledData', function() {
            expect(prefilledUserDataService.getPrefilledFields().completeAddress).toBe(undefined);
        });
    });
});

