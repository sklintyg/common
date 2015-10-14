describe('IntygService', function() {
    'use strict';

    var IntygService;
    var $document;
    var $httpBackend;
    var $q;
    var $location;
    var $stateParams;
    var $timeout;
    var dialogService;
    var User;
    var $cookieStore;

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('$state', jasmine.createSpyObj('$state', [ 'reload' ]));
        $provide.value('common.messageService',
            jasmine.createSpyObj('common.messageService', [ 'getProperty', 'addResources' ]));
        $provide.value('$stateParams', {});
        $provide.value('common.statService', jasmine.createSpyObj('common.statService', [ 'refreshStat' ]));
        $provide.value('common.UserModel', { userContext: { authenticationScheme: null }, getActiveFeatures: function() {}, hasPrivilege: function() {} });
        $provide.value('common.UtkastViewStateService', {});
        $provide.value('common.utkastNotifyService', {});
        $provide.value('common.domain.DraftModel', {});
    }));

    beforeEach(angular.mock.inject(['common.IntygService', '$cookieStore', '$httpBackend', '$location', '$q', '$stateParams', '$timeout',
        '$document', 'common.dialogService', 'common.User',
        function(_IntygService_, _$cookieStore_, _$httpBackend_, _$location_, _$q_, _$stateParams_, _$timeout_, _$document_,
            _dialogService_, _User_) {
            IntygService = _IntygService_;
            $cookieStore = _$cookieStore_;
            $httpBackend = _$httpBackend_;
            $q = _$q_;
            $location = _$location_;
            $stateParams = _$stateParams_;
            $timeout = _$timeout_;
            $document = _$document_;
            dialogService = _dialogService_;
            User = _User_;
        }]));

    describe('#copy', function() {

        var $scope;
        var cert;

        beforeEach(function() {
            $scope = {
                viewState: {
                    activeErrorMessageKey: null,
                    inlineErrorMessageKey: null
                },
                dialog: {
                    showerror: false,
                    acceptprogressdone: false,
                    errormessageid: null
                }
            };
            cert = {
                'intygId': 'intyg-1', 'source': 'IT', 'intygType': 'fk7263', 'status': 'SENT', 'lastUpdatedSigned': '2011-03-23T09:29:15.000', 'updatedSignedBy': 'Eva Holgersson', 'vidarebefordrad': false,
                'grundData' : { 'patient' : { 'personId': '19121212-1212'}, 'skapadAv' : {'vardenhet' : {'enhetsid' : '1234'} } }
            };

            spyOn(dialogService, 'showDialog').and.callFake(function(options) {
                options.button1click();

                return {
                    opened: { then: function() {} },
                    close: function() {}
                };
            });

            spyOn($location, 'path').and.callThrough();

        });

        it('should immediately request a utkast copy of cert if the copy cookie is set', function() {

            $cookieStore.put(IntygService.COPY_DIALOG_COOKIE, true);

            $httpBackend.expectPOST('/api/intyg/' + cert.intygType + '/' + cert.intygId +'/kopiera/').respond(
                {'intygsUtkastId':'nytt-utkast-id','intygsTyp':'fk7263'}
            );
            IntygService.copy($scope.viewState, cert);
            $httpBackend.flush();
            $timeout.flush();
            expect(dialogService.showDialog).not.toHaveBeenCalled();
            expect($location.path).toHaveBeenCalledWith('/fk7263/edit/nytt-utkast-id', true);

            $cookieStore.remove(IntygService.COPY_DIALOG_COOKIE);
        });

        it('should show the copy dialog if the copy cookie is not set', function() {

            $cookieStore.remove(IntygService.COPY_DIALOG_COOKIE);
            $httpBackend.expectPOST('/api/intyg/' + cert.intygType + '/' + cert.intygId +'/kopiera/').respond(
                {'intygsUtkastId':'nytt-utkast-id','intygsTyp':'fk7263'}
            );
            IntygService.copy($scope.viewState, cert);
            $httpBackend.flush();
            $timeout.flush();

            expect(dialogService.showDialog).toHaveBeenCalled();

        });
    });

    describe('_createCopyDraft', function() {

        var cert;
        beforeEach(function() {
            cert = {
                'intygId': 'intyg-1', 'source': 'IT', 'intygType': 'fk7263', 'status': 'SENT', 'lastUpdatedSigned': '2011-03-23T09:29:15.000', 'updatedSignedBy': 'Eva Holgersson', 'vidarebefordrad': false,
                'grundData' : { 'patient' : { 'personId': '19121212-1212'}}
            };
        });

        it('should request a copy and redirect to the edit page', function() {

            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');

            $httpBackend.expectPOST('/api/intyg/' + cert.intygType + '/' + cert.intygId +'/kopiera/').respond(
                {'intygsUtkastId':'nytt-utkast-id','intygsTyp':'fk7263'}
            );
            IntygService.__test__.createCopyDraft(cert, onSuccess, onError);
            $httpBackend.flush();

            expect(onSuccess).toHaveBeenCalledWith({'intygsUtkastId':'nytt-utkast-id','intygsTyp':'fk7263'});
            expect(onError).not.toHaveBeenCalled();
        });
    });

});
