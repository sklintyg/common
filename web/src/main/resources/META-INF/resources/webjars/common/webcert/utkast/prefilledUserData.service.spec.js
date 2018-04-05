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
        var patient;

        beforeEach(function() {
            patient = {};
            patient.postadress = 'Foovägen';
            patient.postnummer = '12345';
            patient.postort = 'Barsala';
        });

        it('should return positive when all parts of are present', function() {
            prefilledUserDataService.searchForPrefilledPatientData(patient);
            expect(prefilledUserDataService.getPrefilledFields().completeAddress).toBe(true);
        });

        it('should return negative if any part are ', function() {

            describe('missing', function() {
                delete patient.postort;
                prefilledUserDataService.searchForPrefilledPatData(patient);
                expect(prefilledUserDataService.getPrefilledFields().completeAddress).toBe(false);
            });

            describe('empty', function() {
                patient.postort = '';
                prefilledUserDataService.searchForPrefilledPatientData(patient);
                expect(prefilledUserDataService.getPrefilledFields().completeAddress).toBe(false);
            });
        });

        it('should return undefined if not initialized by calling searchForPrefilledPatientData', function() {
            expect(prefilledUserDataService.getPrefilledFields().completeAddress).toBe(undefined);
        });
    });
});

