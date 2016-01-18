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

var iid_GetProperty, iid_Invoke, iid_SetProperty; // jshint ignore:line

describe('UtkastSignService', function() {
    'use strict';

    var UtkastSignService;
    var $document;
    var $httpBackend;
    var $q;
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
        $provide.value('common.messageService',
            jasmine.createSpyObj('common.messageService', [ 'getProperty', 'addResources' ]));
        $provide.value('$stateParams', {});
        $provide.value('common.dialogService',
            jasmine.createSpyObj('common.dialogService', [ 'showDialog', 'showErrorMessageDialog' ]));
        $provide.value('common.statService', jasmine.createSpyObj('common.statService', [ 'refreshStat' ]));
        $provide.value('common.UserModel', { user: { authenticationScheme: null } });
        $provide.value('common.UtkastViewStateService', {});
        $provide.value('common.utkastNotifyService', {});
        $provide.value('common.domain.DraftModel', {});
    }));

    beforeEach(angular.mock.inject(['common.UtkastSignService', '$httpBackend', '$location', '$q', '$stateParams', '$timeout',
        '$document', 'common.dialogService', 'common.User',
        function(_UtkastSignService_, _$httpBackend_, _$location_, _$q_, _$stateParams_, _$timeout_, _$document_,
            _dialogService_, _User_) {
            UtkastSignService = _UtkastSignService_;
            $httpBackend = _$httpBackend_;
            $q = _$q_;
            $location = _$location_;
            $stateParams = _$stateParams_;
            $timeout = _$timeout_;
            $document = _$document_;
            dialogService = _dialogService_;
            User = _User_;

            spyOn($location, 'path').and.callFake(function() {
                return { search: function() {
                } };
            });
        }]));

    describe('#signera server', function() {
        var intygId = 123, biljettId = 12345, version = 5;
        var signModel;

        beforeEach(function() {
            User.getUser().authenticationScheme = 'urn:inera:webcert:fake';

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
            User.getUser().authenticationScheme = null;
        });

        it('should redirect to "visa intyg" if the request to sign was successful', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/' + version + '/signeraserver').
                respond(200, { id: biljettId, status: 'BEARBETAR' });
            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'SIGNERAD' });

            UtkastSignService.__test__.confirmSignera(signModel, 'fk7263', intygId, version, $q.defer());
            $httpBackend.flush();

            expect($location.path).toHaveBeenCalledWith('/intyg/fk7263/' + intygId);
        });

        it('should redirect to "visa intyg" if the request to sign was successful, even if delayed', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/' + version + '/signeraserver').
                respond(200, { id: biljettId, status: 'BEARBETAR' });
            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'BEARBETAR' });

            UtkastSignService.__test__.confirmSignera(signModel, 'fk7263', intygId, version, $q.defer());
            $httpBackend.flush();

            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'SIGNERAD' });
            $timeout.flush();
            $httpBackend.flush();

            expect($location.path).toHaveBeenCalledWith('/intyg/fk7263/' + intygId);
        });

        it('should show an error if the server refuses the request to sign', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/' + version + '/signeraserver').
                respond(500, { errorCode: 'DATA_NOT_FOUND' });

            UtkastSignService.__test__.confirmSignera(signModel, 'fk7263', intygId, version, $q.defer());
            $httpBackend.flush();

            expect($location.path).not.toHaveBeenCalled();
            expect(dialogService.showErrorMessageDialog).toHaveBeenCalled();
        });

        it('should show an error if the server responds with concurrent update error', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/' + version + '/signeraserver').
                respond(500, { errorCode: 'CONCURRENT_MODIFICATION' });

            UtkastSignService.__test__.confirmSignera(signModel, 'fk7263', intygId, version, $q.defer());
            $httpBackend.flush();

            expect($location.path).not.toHaveBeenCalled();
            expect(dialogService.showErrorMessageDialog).toHaveBeenCalled();
        });

        it('should show an error if the server returns an unknown status', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/' + version + '/signeraserver').
                respond(200, { id: biljettId, status: 'ERROR' });
            $httpBackend.expectGET('/moduleapi/utkast/fk7263/' + biljettId + '/signeringsstatus').
                respond(200, { id: biljettId, status: 'ERROR' });

            UtkastSignService.__test__.confirmSignera(signModel, 'fk7263', intygId, version, $q.defer());
            $httpBackend.flush();

            expect($location.path).not.toHaveBeenCalled();
            expect(dialogService.showErrorMessageDialog).toHaveBeenCalled();
        });
    });

    // TODO add a similar test for signera with BankID (which is a combination of client/server-side signing)
    describe('#signera client', function() {
        var intygId = 123, biljettId = 12345, version = 5;
        var $scope;

        beforeEach(function() {
            iid_GetProperty = jasmine.createSpy('iid_GetProperty'); // jshint ignore:line
            iid_Invoke = jasmine.createSpy('invoke'); // jshint ignore:line
            iid_SetProperty = jasmine.createSpy('iid_SetProperty'); // jshint ignore:line

            User.getUser().authenticationScheme = 'urn:oasis:names:tc:SAML:2.0:ac:classes:TLSClient';
            User.getUser().authenticationMethod = 'NET_ID';

            $stateParams.certificateId = intygId;
            $scope = {};
        });

        afterEach(function() {
            User.getUser().authenticationScheme = null;
            User.getUser().authenticationMethod = null;
        });

        it('should redirect to "visa intyg" if the request to sign was successful', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/' + version + '/signeringshash').
                respond(200, { id: biljettId, hash: 'abcd1234' });

            iid_Invoke.and.returnValue(0); // jshint ignore:line
            iid_GetProperty.and.returnValue('4321dcba'); // jshint ignore:line

            UtkastSignService.signera('fk7263', version);

            $httpBackend.flush();

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + biljettId + '/signeraklient').
                respond(200, { id: biljettId, status: 'SIGNERAD' });
            $timeout.flush();
            $httpBackend.flush();

            expect($location.path).toHaveBeenCalledWith('/intyg/fk7263/' + intygId);
        });

        it('should redirect to "visa intyg" if the request to sign was successful, even if delayed', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/' + version + '/signeringshash').
                respond(200, { id: biljettId, hash: 'abcd1234' });

            iid_Invoke.and.returnValue(0); // jshint ignore:line
            iid_GetProperty.and.returnValue('4321dcba'); // jshint ignore:line

            UtkastSignService.signera('fk7263', version);
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

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/' + version +
                '/signeringshash').respond(500, { message: 'Jan Nilsson', errorCode: 'CONCURRENT_MODIFICATION'});

            UtkastSignService.signera('fk7263', version);
            $httpBackend.flush();

            expect(dialogService.showErrorMessageDialog).toHaveBeenCalled();
        });

        it('should show error if the NetID client is not present', function() {

            $httpBackend.expectPOST('/moduleapi/utkast/fk7263/' + intygId + '/' + version + '/signeringshash').
                respond(200, { id: biljettId, hash: 'abcd1234' });

            iid_Invoke.and.returnValue(-1); // jshint ignore:line

            UtkastSignService.signera('fk7263', version);
            $httpBackend.flush();
            $timeout.flush();

            expect(dialogService.showErrorMessageDialog).toHaveBeenCalled();
        });

        xit('should show concurrent_modification error if that code is received from the server', function() {
        });
    });
});
