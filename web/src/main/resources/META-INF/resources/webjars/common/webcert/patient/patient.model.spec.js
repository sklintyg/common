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

describe('PatientModel', function() {
    'use strict';

    var PatientModel;
    var $httpBackend;

    beforeEach(angular.mock.module('common', function($provide) {
    }));

    beforeEach(angular.mock.inject([
        'common.PatientModel', '$httpBackend',
        function(_PatientModel_, _$httpBackend_) {
            PatientModel = _PatientModel_;
            $httpBackend = _$httpBackend_;
        }]));

    describe('#reset', function() {

        it('should create an empty object', function() {
            PatientModel.reset();

            expect(PatientModel.personnummer).toBeNull();
            expect(PatientModel.intygType).toEqual('default');
            expect(PatientModel.fornamn).toBeNull();
            expect(PatientModel.mellannamn).toBeNull();
            expect(PatientModel.efternamn).toBeNull();
            expect(PatientModel.postadress).toBeNull();
            expect(PatientModel.postnummer).toBeNull();
            expect(PatientModel.postort).toBeNull();
        });
    });
});
