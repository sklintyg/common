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
