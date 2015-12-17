/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

describe('utkastHeader', function() {
    'use strict';

    var $httpBackend;
    var $scope;
    var $timeout;
    var controller;
    var testCert = {
        grundData: {
            patient: {
                personId: '191212121212',
                fullstandigtNamn: 'Test Testsson',
                postadress: 'Ställvägen 1',
                postnummer: '12345',
                postort: 'Stället'
            }
        }
    };

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('smoothScroll', {});
    }));

    beforeEach(angular.mock.inject(['$controller', '$rootScope', '$httpBackend', '$timeout',
        function($controller, _$rootScope_, _$httpBackend_, _$timeout_) {
            $scope = _$rootScope_.$new();
            controller = $controller('common.UtkastHeader', { $scope: $scope });
            $httpBackend = _$httpBackend_;
            $timeout = _$timeout_;
        }]));

    describe('#updatePatientData', function() {

        beforeEach(function() {
            $scope.viewState = {
                intygModel: angular.copy(testCert)
            };
        });

        it('should update patient name and adress from pu-service', function() {

            $httpBackend.expectGET('/api/person/191212121212').respond(200, {
                status: 'FOUND',
                person: {
                    fornamn: 'Kalle',
                    efternamn: 'Karlsson',
                    postadress: 'Platsgatan 2',
                    postnummer: '23456',
                    postort: 'Platsen'
                }
            });
            $scope.updatePatientData();
            $timeout.flush();
            $httpBackend.flush();

            expect($scope.viewState.intygModel.grundData.patient.fullstandigtNamn).toBe('Kalle Karlsson');
            expect($scope.viewState.intygModel.grundData.patient.postadress).toBe('Platsgatan 2');
            expect($scope.viewState.intygModel.grundData.patient.postnummer).toBe('23456');
            expect($scope.viewState.intygModel.grundData.patient.postort).toBe('Platsen');
        });

    });
});
