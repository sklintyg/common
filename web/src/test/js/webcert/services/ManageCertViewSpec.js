var iid_GetProperty, iid_Invoke, iid_SetProperty;

describe('ManageCertView', function() {
    'use strict';

    var ManageCertView;
    var $document;
    var $httpBackend;
    var $location;
    var $routeParams;
    var $timeout;
    var dialogService;
    var User;

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('$document', [
            {}
        ]);
        $provide.value('$route', jasmine.createSpyObj('$route', [ 'reload' ]));
        $provide.value('$location', jasmine.createSpyObj('$location', [ 'path', 'replace' ]));
        $provide.value('common.messageService',
            jasmine.createSpyObj('common.messageService', [ 'getProperty', 'addResources' ]));
        $provide.value('$routeParams', {});
        $provide.value('common.dialogService',
            jasmine.createSpyObj('common.dialogService', [ 'showDialog', 'showErrorMessageDialog' ]));
        $provide.value('common.statService', jasmine.createSpyObj('common.statService', [ 'refreshStat' ]));
        $provide.value('common.User', { userContext: { authenticationScheme: null } });
    }));

    beforeEach(angular.mock.inject(['common.ManageCertView', '$httpBackend', '$location', '$routeParams', '$timeout',
        '$document', 'common.dialogService', 'common.User',
        function(_ManageCertView_, _$httpBackend_, _$location_, _$routeParams_, _$timeout_, _$document_, _dialogService_, _User_) {
            ManageCertView = _ManageCertView_;
            $httpBackend = _$httpBackend_;
            $location = _$location_;
            $routeParams = _$routeParams_;
            $timeout = _$timeout_;
            $document = _$document_;
            dialogService = _dialogService_;
            User = _User_;
        }]));

    describe('#signera server', function() {
        var intygId = 123, biljettId = 12345;
        var $scope;

        beforeEach(function() {
            User.userContext.authenticationScheme = 'urn:inera:webcert:fake';

            $routeParams.certificateId = intygId;
            $scope = { dialog: {} };
        });

        afterEach(function() {
            User.userContext.authenticationScheme = null;
        });

        it('should open confirm dialog for fake login', function() {

            ManageCertView.signera($scope);

            expect(dialogService.showDialog).toHaveBeenCalledWith($scope, jasmine.any(Object));
        });


        it('should redirect to "visa intyg" if the request to sign was successful', function() {

            var confirmDialog = jasmine.createSpyObj('confirmDialog', [ 'close' ]);

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeraserver').
                respond(200, { id: biljettId, status: 'BEARBETAR' });
            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'SIGNERAD' });

            ManageCertView.__test__.confirmSignera($scope, 'fk7263', intygId, confirmDialog);
            $httpBackend.flush();

            expect(confirmDialog.close).toHaveBeenCalled();
            expect($location.path).toHaveBeenCalledWith('/intyg/fk7263/' + intygId);
        });

        it('should redirect to "visa intyg" if the request to sign was successful, even if delayed', function() {

            var confirmDialog = jasmine.createSpyObj('confirmDialog', [ 'close' ]);

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeraserver').
                respond(200, { id: biljettId, status: 'BEARBETAR' });
            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'BEARBETAR' });

            ManageCertView.__test__.confirmSignera($scope, 'fk7263', intygId, confirmDialog);
            $httpBackend.flush();

            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'SIGNERAD' });
            $timeout.flush();
            $httpBackend.flush();

            expect(confirmDialog.close).toHaveBeenCalled();
            expect($location.path).toHaveBeenCalledWith('/intyg/fk7263/' + intygId);
        });

        it('should show an error if the server refuses the request to sign', function() {

            var confirmDialog = jasmine.createSpyObj('confirmDialog', [ 'close' ]);

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeraserver').
                respond(500, { errorCode: 'DATA_NOT_FOUND' });

            ManageCertView.__test__.confirmSignera($scope, 'fk7263', intygId, confirmDialog);
            $httpBackend.flush();

            expect(confirmDialog.close).not.toHaveBeenCalled();
            expect($location.path).not.toHaveBeenCalled();
            expect($scope.dialog.acceptprogressdone).toBeTruthy();
            expect($scope.dialog.showerror).toBeTruthy();
        });

        it('should show an error if the server returns an unknown status', function() {

            var confirmDialog = jasmine.createSpyObj('confirmDialog', [ 'close' ]);

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeraserver').
                respond(200, { id: biljettId, status: 'ERROR' });
            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'ERROR' });

            ManageCertView.__test__.confirmSignera($scope, 'fk7263', intygId, confirmDialog);
            $httpBackend.flush();

            expect(confirmDialog.close).not.toHaveBeenCalled();
            expect($location.path).not.toHaveBeenCalled();
            expect($scope.dialog.acceptprogressdone).toBeTruthy();
            expect($scope.dialog.showerror).toBeTruthy();
        });
    });

    describe('#signera client', function() {
        var intygId = 123, biljettId = 12345;
        var $scope;

        beforeEach(function() {
            iid_GetProperty = jasmine.createSpy('iid_GetProperty');
            iid_Invoke = jasmine.createSpy('invoke');
            iid_SetProperty = jasmine.createSpy('iid_SetProperty');

            User.userContext.authenticationScheme = 'urn:oasis:names:tc:SAML:2.0:ac:classes:TLSClient';

            $routeParams.certificateId = intygId;
            $scope = {};
        });

        afterEach(function() {
            User.userContext.authenticationScheme = null;
        });

        it('should redirect to "visa intyg" if the request to sign was successful', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeringshash').
                respond(200, { id: biljettId, hash: 'abcd1234' });

            iid_Invoke.andReturn(0);
            iid_GetProperty.andReturn('4321dcba');

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + biljettId + '/signeraklient').
                respond(200, { id: biljettId, status: 'SIGNERAD' });

            ManageCertView.signera($scope, 'fk7263');
            $httpBackend.flush();

            expect($location.path).toHaveBeenCalledWith('/intyg/fk7263/' + intygId);
        });

        it('should redirect to "visa intyg" if the request to sign was successful, even if delayed', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeringshash').
                respond(200, { id: biljettId, hash: 'abcd1234' });

            iid_Invoke.andReturn(0);
            iid_GetProperty.andReturn('4321dcba');

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + biljettId + '/signeraklient').
                respond(200, { id: biljettId, status: 'BEARBETAD' });
            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'BEARBETAR' });

            ManageCertView.signera($scope, 'fk7263');
            $httpBackend.flush();

            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'SIGNERAD' });
            $timeout.flush();
            $httpBackend.flush();

            expect($location.path).toHaveBeenCalledWith('/intyg/fk7263/' + intygId);
        });

        it('should show error if unable to get hash', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeringshash').respond(500);

            ManageCertView.signera($scope, 'fk7263');
            $httpBackend.flush();

            expect(dialogService.showErrorMessageDialog).toHaveBeenCalled();
        });

        it('should show error if the NetID client is not present', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeringshash').
                respond(200, { id: biljettId, hash: 'abcd1234' });

            iid_Invoke.andReturn(-1);

            ManageCertView.signera($scope, 'fk7263');
            $httpBackend.flush();

            expect(dialogService.showErrorMessageDialog).toHaveBeenCalled();
        });
    });
});
