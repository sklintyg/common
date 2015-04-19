var iid_GetProperty, iid_Invoke, iid_SetProperty;

describe('ManageCertView', function() {
    'use strict';

    var ManageCertView;
    var $document;
    var $httpBackend;
    var $location;
    var $stateParams;
    var $timeout;
    var dialogService;
    var User;

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('$document', [
            {}
        ]);
        $provide.value('$state', jasmine.createSpyObj('$state', [ 'reload' ]));
        var location = {
            path: function() { return { search: function() {} }; },
            replace: function() {}
        };
        $provide.value('common.messageService',
            jasmine.createSpyObj('common.messageService', [ 'getProperty', 'addResources' ]));
        $provide.value('$stateParams', {});
        $provide.value('common.dialogService',
            jasmine.createSpyObj('common.dialogService', [ 'showDialog', 'showErrorMessageDialog' ]));
        $provide.value('common.statService', jasmine.createSpyObj('common.statService', [ 'refreshStat' ]));
        $provide.value('common.UserModel', { userContext: { authenticationScheme: null } });
        $provide.value('common.UtkastViewStateService', {});
        $provide.value('common.domain.DraftModel', {});
    }));

    beforeEach(angular.mock.inject(['common.ManageCertView', '$httpBackend', '$location', '$stateParams', '$timeout',
        '$document', 'common.dialogService', 'common.User',
        function(_ManageCertView_, _$httpBackend_, _$location_, _$stateParams_, _$timeout_, _$document_, _dialogService_, _User_) {
            ManageCertView = _ManageCertView_;
            $httpBackend = _$httpBackend_;
            $location = _$location_;
            $stateParams = _$stateParams_;
            $timeout = _$timeout_;
            $document = _$document_;
            dialogService = _dialogService_;
            User = _User_;

            spyOn($location, 'path').and.callFake(function() { return { search: function() {} }; });
        }]));

    describe('#signera server', function() {
        var intygId = 123, biljettId = 12345;
        var $scope;
        var signModel;

        beforeEach(function() {
            User.getUserContext().authenticationScheme = 'urn:inera:webcert:fake';

            $stateParams.certificateId = intygId;

            signModel = {
                dialog: {
                    model: {
                        acceptprogressdone: false
                    }
                },
                signingWithSITHSInProgress: false
            };
        });

        afterEach(function() {
            User.getUserContext().authenticationScheme = null;
        });

        it('should open confirm dialog for fake login', function() {

            ManageCertView.signera($scope);

            expect(dialogService.showDialog).toHaveBeenCalledWith(jasmine.any(Object));
        });


        it('should redirect to "visa intyg" if the request to sign was successful', function() {

            var confirmDialog = jasmine.createSpyObj('confirmDialog', [ 'close' ]);
            confirmDialog.model = {};

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeraserver').
                respond(200, { id: biljettId, status: 'BEARBETAR' });
            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'SIGNERAD' });

            ManageCertView.__test__.confirmSignera(signModel, 'fk7263', intygId, confirmDialog);
            $httpBackend.flush();

            expect(confirmDialog.close).toHaveBeenCalled();
            expect($location.path).toHaveBeenCalledWith('/intyg/fk7263/' + intygId);
        });

        it('should redirect to "visa intyg" if the request to sign was successful, even if delayed', function() {

            var confirmDialog = jasmine.createSpyObj('confirmDialog', [ 'close' ]);
            confirmDialog.model = {};

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeraserver').
                respond(200, { id: biljettId, status: 'BEARBETAR' });
            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'BEARBETAR' });

            ManageCertView.__test__.confirmSignera(signModel, 'fk7263', intygId, confirmDialog);
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
            confirmDialog.model = {};

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeraserver').
                respond(500, { errorCode: 'DATA_NOT_FOUND' });

            ManageCertView.__test__.confirmSignera(signModel, 'fk7263', intygId, confirmDialog);
            $httpBackend.flush();

            expect(confirmDialog.close).not.toHaveBeenCalled();
            expect($location.path).not.toHaveBeenCalled();
            expect(signModel.dialog.model.acceptprogressdone).toBeTruthy();
            expect(signModel.dialog.model.showerror).toBeTruthy();
        });

        it('should show an error if the server returns an unknown status', function() {

            var confirmDialog = jasmine.createSpyObj('confirmDialog', [ 'close' ]);
            confirmDialog.model = {};

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeraserver').
                respond(200, { id: biljettId, status: 'ERROR' });
            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'ERROR' });

            ManageCertView.__test__.confirmSignera(signModel, 'fk7263', intygId, confirmDialog);
            $httpBackend.flush();

            expect(confirmDialog.close).not.toHaveBeenCalled();
            expect($location.path).not.toHaveBeenCalled();
            expect(signModel.dialog.model.acceptprogressdone).toBeTruthy();
            expect(signModel.dialog.model.showerror).toBeTruthy();
        });
    });

    describe('#signera client', function() {
        var intygId = 123, biljettId = 12345;
        var $scope;

        beforeEach(function() {
            iid_GetProperty = jasmine.createSpy('iid_GetProperty');
            iid_Invoke = jasmine.createSpy('invoke');
            iid_SetProperty = jasmine.createSpy('iid_SetProperty');

            User.getUserContext().authenticationScheme = 'urn:oasis:names:tc:SAML:2.0:ac:classes:TLSClient';

            $stateParams.certificateId = intygId;
            $scope = {};
        });

        afterEach(function() {
            User.getUserContext().authenticationScheme = null;
        });

        it('should redirect to "visa intyg" if the request to sign was successful', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeringshash').
                respond(200, { id: biljettId, hash: 'abcd1234' });

            iid_Invoke.and.returnValue(0);
            iid_GetProperty.and.returnValue('4321dcba');

            ManageCertView.signera('fk7263');

            $httpBackend.flush();

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + biljettId + '/signeraklient').
                respond(200, { id: biljettId, status: 'SIGNERAD' });
            $timeout.flush();
            $httpBackend.flush();

            expect($location.path).toHaveBeenCalledWith('/intyg/fk7263/' + intygId);
        });

        it('should redirect to "visa intyg" if the request to sign was successful, even if delayed', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeringshash').
                respond(200, { id: biljettId, hash: 'abcd1234' });

            iid_Invoke.and.returnValue(0);
            iid_GetProperty.and.returnValue('4321dcba');

            ManageCertView.signera('fk7263');
            $httpBackend.flush();

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + biljettId + '/signeraklient').
                respond(200, { id: biljettId, status: 'BEARBETAD' });
            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'BEARBETAR' });
            $timeout.flush();
            $httpBackend.flush();

            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'SIGNERAD' });
            $timeout.flush();
            $httpBackend.flush();

            expect($location.path).toHaveBeenCalledWith('/intyg/fk7263/' + intygId);
        });

        it('should show error if unable to get hash', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeringshash').respond(500);

            ManageCertView.signera('fk7263');
            $httpBackend.flush();

            expect(dialogService.showErrorMessageDialog).toHaveBeenCalled();
        });

        it('should show error if the NetID client is not present', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/signeringshash').
                respond(200, { id: biljettId, hash: 'abcd1234' });

            iid_Invoke.and.returnValue(-1);

            ManageCertView.signera('fk7263');
            $httpBackend.flush();
            $timeout.flush();

            expect(dialogService.showErrorMessageDialog).toHaveBeenCalled();
        });
    });
});
