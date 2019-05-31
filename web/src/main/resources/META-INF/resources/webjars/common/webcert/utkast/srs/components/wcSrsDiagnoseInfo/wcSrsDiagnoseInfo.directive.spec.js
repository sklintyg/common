describe('Directive: wcSrsDiagnoseInfo', function() {
    'use strict';
    var $compile;
    var $scope;
    var $httpBackend;

    var element;
    var elementScope;

    //load common module, and do some overrides
    beforeEach(module('common', function($provide) {
        var messageServiceStub = {
            //We don't want to test the actual messageService, so we just echo back the
            // key, and make our assertions against 'key' value that wer'e actually interested in.
            getProperty: function(key) {
                return key;
            },
            addResources: function() {
            },
            propertyExists: function() {
                return true;
            }
        };
        $provide.value('common.messageService', messageServiceStub);
    }));

    beforeEach(module('htmlTemplates'));

    beforeEach(inject(function(_$compile_, $rootScope, _$httpBackend_) {
        $compile = _$compile_;
        $scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;

    }));

    function arrangeMocks(diagnose, response, responsecode) {
        $scope.config = {
            diagnoseCode: diagnose
        };
        element = $compile(angular.element('<wc-srs-diagnose-info config="config"/>'))($scope);

        $httpBackend.expectGET('/api/srs/atgarder/' + diagnose).respond(responsecode || 200, response);

        $scope.$digest();
        $httpBackend.flush();
        elementScope = element.isolateScope() || element.scope();
    }

    it('should handle load error', function() {

        //Arrange
        arrangeMocks('M18', {}, 500);

        // Assert
        expect(elementScope.viewState.mainMessageKey.key).toEqual('srs.srsfordiagnose.load.error');

    });

    it('should handle no-data', function() {

        //Arrange
        arrangeMocks('M18', {
            'diagnosisCode': 'P01',
            'atgarderStatusCode': 'INFORMATION_SAKNAS',
            'atgarderObs': [],
            'atgarderRek': [],
            'statistikStatusCode': 'STATISTIK_SAKNAS'

        });

        // Assert
        expect(elementScope.viewState.mainMessageKey.key).toEqual('srs.srsfordiagnose.load.nodata');

    });

    it('should handle DIAGNOSKOD_PA_HOGRE_NIVA', function() {

        //Arrange
        arrangeMocks('M18', {
            'diagnosisCode': 'P01',
            'atgarderStatusCode': 'DIAGNOSKOD_PA_HOGRE_NIVA',
            'atgarderObs': [ 'hej' ],
            'atgarderRek': [ 'hå' ],
            'statistikStatusCode': 'DIAGNOSKOD_PA_HOGRE_NIVA'

        });

        // Assert
        expect(elementScope.viewState.atgarderStatusMessage).toContain('srs.srsfordiagnose.atgarder.highercode');
        expect(elementScope.viewState.statistikStatusMessage).toBeNull();

    });

    it('should handle OK with normal payload',
            function() {
                var srsResponse = {
                    'diagnosisCode': 'M18',
                    'atgarderStatusCode': 'OK',
                    'atgarderObs': [ 'Atgardsforslag OBS 1 för diagnos M18', 'Atgardsforslag OBS 2 för diagnos M18',
                            'Atgardsforslag OBS 3 för diagnos M18' ],
                    'atgarderRek': [ 'Atgardsforslag REK 1 för diagnos M18', 'Atgardsforslag REK 2 för diagnos M18',
                            'Atgardsforslag REK 3 för diagnos M18' ],
                    'statistikBild': '/services/stubs/srs-statistics-stub/M18.jpg',
                    'statistikStatusCode': 'OK'
                };
                //Arrange
                arrangeMocks('M18', srsResponse);

                // Assert
                expect(elementScope.viewState.atgarderStatusMessage).toBeNull();
                expect(elementScope.viewState.statistikStatusMessage).toBeNull();
                expect(elementScope.viewState.mainMessageKey).toBeNull();
                expect(elementScope.viewState.srsdata).toEqual(srsResponse);
                expect(elementScope.viewState.statistikLink).toContain('http');
                expect(elementScope.viewState.atgarderLink).toContain('http');

            });

});
