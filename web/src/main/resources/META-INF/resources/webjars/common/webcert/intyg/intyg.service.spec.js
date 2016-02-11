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

/* globals readJSON */
/* globals tv4 */
describe('IntygService', function() {
    'use strict';

    var IntygService;
    var $document;
    var $httpBackend;
    var $q;
    var $state;
    var $stateParams;
    var $timeout;
    var dialogService;
    var User;
    var $cookies;

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.messageService',
            jasmine.createSpyObj('common.messageService', [ 'getProperty', 'addResources' ]));
        $provide.value('$stateParams', {});
        $provide.value('common.statService', jasmine.createSpyObj('common.statService', [ 'refreshStat' ]));
        $provide.value('common.UserModel', { userContext: { authenticationScheme: null }, getActiveFeatures: function() {}, hasPrivilege: function() {} });
        $provide.value('common.UtkastViewStateService', {});
        $provide.value('common.utkastNotifyService', {});
        $provide.value('common.domain.DraftModel', {});
    }));

    angular.module('common').config(function($stateProvider){
        $stateProvider.state('fk7263-edit', {
        });
    });

    beforeEach(angular.mock.inject(['common.IntygService', '$cookies', '$httpBackend', '$q', '$state', '$stateParams', '$timeout',
        '$document', 'common.dialogService', 'common.User',
        function(_IntygService_, _$cookies_, _$httpBackend_, _$q_, _$state_, _$stateParams_, _$timeout_, _$document_,
            _dialogService_, _User_) {
            IntygService = _IntygService_;
            $cookies = _$cookies_;
            $httpBackend = _$httpBackend_;
            $q = _$q_;
            $state = _$state_;
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
                'intygId': 'intyg-1', 'source': 'IT', 'intygType': 'fk7263', 'status': 'SENT',
                'lastUpdatedSigned': '2011-03-23T09:29:15.000', 'updatedSignedBy': 'Eva Holgersson', 'vidarebefordrad': false,
                'grundData' : { 'patient' : { 'personId': '19121212-1212'}, 'skapadAv' : {'vardenhet' : {'enhetsid' : '1234'} } }
            };

            spyOn(dialogService, 'showDialog').and.callFake(function(options) {
                options.button1click();

                return {
                    opened: { then: function() {} },
                    close: function() {}
                };
            });

            spyOn($state, 'go').and.callThrough();
        });

        it('should immediately request a utkast copy of cert if the copy cookie is set', function() {

            $cookies.putObject(IntygService.COPY_DIALOG_COOKIE, true);

            $httpBackend.expectPOST('/api/intyg/' + cert.intygType + '/' + cert.intygId +'/kopiera/').respond(
                {'intygsUtkastId':'nytt-utkast-id','intygsTyp':'fk7263'}
            );
            IntygService.copy($scope.viewState, cert);
            $httpBackend.flush();
            $timeout.flush();
            expect(dialogService.showDialog).not.toHaveBeenCalled();
            expect($state.go).toHaveBeenCalledWith('fk7263-edit', { certificateId : 'nytt-utkast-id' });

            $cookies.remove(IntygService.COPY_DIALOG_COOKIE);
        });

        it('should show the copy dialog if the copy cookie is not set', function() {

            $cookies.remove(IntygService.COPY_DIALOG_COOKIE);
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
                'intygId': 'intyg-1', 'source': 'IT', 'intygType': 'fk7263', 'status': 'SENT',
                'lastUpdatedSigned': '2011-03-23T09:29:15.000', 'updatedSignedBy': 'Eva Holgersson', 'vidarebefordrad': false,
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

    describe('send', function() {

        it ('should request intyg send with valid json request', function() {

            var data = function(data) {
                data = JSON.parse(data);
                var schema = readJSON('test/resources/jsonschema/webcert-send-intyg-request-schema.json');
                return tv4.validate(data, schema);
            };
            data.toString = function() {
                return tv4.error.toString();
            };

            $httpBackend.expectPOST('/moduleapi/intyg/intygsTyp/intygsId/skicka', data).respond(200);

            spyOn(dialogService, 'showDialog').and.callFake(function(options) {
                $timeout(function() {
                    options.button1click();
                });
                return {
                    opened: { then: function() {} },
                    close: function() {}
                };
            });

            IntygService.send('intygsId', 'intygsTyp', 'recipientId', 'titleId', 'bodyTextId', function() {});

            $timeout.flush();

            $httpBackend.flush();
        });
    });

});
