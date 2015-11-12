describe('fragaSvarCommonService', function() {
    'use strict';

    describe('#isUnhandled', function() {
        var fragaSvarCommonService;

        beforeEach(angular.mock.module('common'), function($provide){
            $provide.value('common.dialogService', {});
            $provide.value('common.User', {});
        });

        beforeEach(angular.mock.inject(['common.fragaSvarCommonService',
            function(_fragaSvarCommonService_) {
                fragaSvarCommonService = _fragaSvarCommonService_;
            }
        ]));

        it ('should be unhandled if qa.status === ANSWERED', function () {
            var qa = {status:'ANSWERED'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeTruthy();
        });

        it ('should be unhandled if qa.status === PENDING_INTERNAL_ACTION && qa.amne === PAMINNELSE', function () {
            var qa = {status:'PENDING_INTERNAL_ACTION', amne : 'PAMINNELSE'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeTruthy();
        });

        it ('should be unhandled if qa.status === PENDING_EXTERNAL_ACTION && qa.amne === MAKULERING', function () {
            var qa = {status:'PENDING_EXTERNAL_ACTION', amne : 'MAKULERING'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeFalsy();
        });

        it ('should be unhandled if qa.status === ANSWERED && qa.amne === *', function () {
            var qa = {status:'ANSWERED', amne : 'PAMINNELSE'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeTruthy();
        });

        it ('should be unhandled if qa.status === PENDING_EXTERNAL_ACTION && qa.amne === MAKULERING', function () {
            var qa = {status:'PENDING_EXTERNAL_ACTION', amne : 'AHHG'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeFalsy();
        });

        it ('should be handled if qa.status != ANSWERED', function () {
            var qa = {status:'CLOSED'};
            expect(fragaSvarCommonService.isUnhandled(qa)).toBeFalsy();
        });
    });

    describe('Mail link', function() {
        var userModelMock = {}, $window;

        beforeEach(module('common'));

        beforeEach(function() {
            module(function ($provide) {
                $provide.value('common.UserModel', userModelMock);
            });
        });

        beforeEach(angular.mock.inject(['$window',
            function(_$window_) {
                $window = _$window_;
            }
        ]));

        var qa = {intygsReferens: {intygsId: 'testid'}, vardperson: {}};

        it('should be correct for uthopp role', inject(['common.fragaSvarCommonService', function(fragaSvarCommonService) {
            userModelMock.isVardAdministratorUthopp = function(){ return true; };
            userModelMock.isLakareUthopp = function(){ return true; };
            var link = fragaSvarCommonService.buildMailToLink(qa);
            expect(link).toEqual('mailto:?subject=En%20fraga-svar%20ska%20besvaras%20i%20Webcert&body=Klicka%20pa%20lanktexten%20for%20att%20besvara%20fraga-svar%3A%0Ahttp%3A%2F%2Flocalhost%3A' +
                $window.location.port + '%2Fwebcert%2Fweb%2Fuser%2Fcertificate%2Ftestid%2Fquestions');
        }]));

        it('should be correct for lakare uthopp role', inject(['common.fragaSvarCommonService', function(fragaSvarCommonService) {
            userModelMock.isVardAdministratorUthopp = function(){ return false; };
            userModelMock.isLakareUthopp = function(){ return true; };
            var link = fragaSvarCommonService.buildMailToLink(qa);
            expect(link).toEqual('mailto:?subject=En%20fraga-svar%20ska%20besvaras%20i%20Webcert&body=Klicka%20pa%20lanktexten%20for%20att%20besvara%20fraga-svar%3A%0Ahttp%3A%2F%2Flocalhost%3A' +
                $window.location.port + '%2Fwebcert%2Fweb%2Fuser%2Fcertificate%2Ftestid%2Fquestions');
        }]));

        it('should be correct for admin uthopp role', inject(['common.fragaSvarCommonService', function(fragaSvarCommonService) {
            userModelMock.isVardAdministratorUthopp = function(){ return true; };
            userModelMock.isLakareUthopp = function(){ return false; };
            var link = fragaSvarCommonService.buildMailToLink(qa);
            expect(link).toEqual('mailto:?subject=En%20fraga-svar%20ska%20besvaras%20i%20Webcert&body=Klicka%20pa%20lanktexten%20for%20att%20besvara%20fraga-svar%3A%0Ahttp%3A%2F%2Flocalhost%3A' +
                $window.location.port + '%2Fwebcert%2Fweb%2Fuser%2Fcertificate%2Ftestid%2Fquestions');
        }]));

        it('should be correct for landsting role', inject(['common.fragaSvarCommonService', function(fragaSvarCommonService) {
            userModelMock.isVardAdministratorUthopp = function(){ return false; };
            userModelMock.isLakareUthopp = function(){ return false; };
            var link = fragaSvarCommonService.buildMailToLink(qa);
            expect(link).toEqual('mailto:?subject=En%20fraga-svar%20ska%20besvaras%20i%20Webcert&body=Klicka%20pa%20lanktexten%20for%20att%20besvara%20fraga-svar%3A%0Ahttp%3A%2F%2Flocalhost%3A' +
                $window.location.port + '%2Fwebcert%2Fweb%2Fuser%2Fbasic-certificate%2Ftestid%2Fquestions');
        }]));

    });


});
