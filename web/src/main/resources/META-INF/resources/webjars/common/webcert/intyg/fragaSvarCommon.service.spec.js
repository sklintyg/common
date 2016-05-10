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

    describe('decorateSingleItem', function() {
        var fragaSvarCommonService;

        var UserModel;

        beforeEach(angular.mock.module('common'), function($provide){
        });

        beforeEach(angular.mock.inject(['common.fragaSvarCommonService', 'common.UserModel',
            function(_fragaSvarCommonService_, _UserModel_) {
                fragaSvarCommonService = _fragaSvarCommonService_;
                UserModel = _UserModel_;
            }
        ]));

        it ('should disable answer if amne is PAMINNELSE', function () {
            var qa = {amne:'PAMINNELSE'};
            fragaSvarCommonService.decorateSingleItem(qa);
            expect(qa.answerDisabled).toBeTruthy();
            expect(qa.svaraMedNyttIntygDisabled).toBeFalsy();
        });

        it ('should disable answer if it is a komplettering (new KOMPLT) and user does not have that permission', function () {
            var qa = {amne:'KOMPLT'};
            fragaSvarCommonService.decorateSingleItem(qa);
            expect(qa.answerDisabled).toBeTruthy();
            expect(qa.svaraMedNyttIntygDisabled).toBeFalsy();
        });

        it ('should disable answer if it is a komplettering (old KOMPLETTERING_AV_LAKARINTYG) and user does not have that permission', function () {
            var qa = {amne:'KOMPLETTERING_AV_LAKARINTYG'};
            fragaSvarCommonService.decorateSingleItem(qa);
            expect(qa.answerDisabled).toBeTruthy();
            expect(qa.svaraMedNyttIntygDisabled).toBeFalsy();
        });

        it ('should disable answer and show message about not svara med nytt intyg if user is coming from request origin UTHOPP', function () {
            UserModel.setUser({origin: 'UTHOPP'});

            var qa = {amne:'KOMPLETTERING_AV_LAKARINTYG'};
            fragaSvarCommonService.decorateSingleItem(qa);
            expect(qa.answerDisabled).toBeTruthy();
            expect(qa.svaraMedNyttIntygDisabled).toBeTruthy();
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

        var qa = {
            intygsReferens: {
                intygsId: 'testid'
            },
            enhetsnamn: 'Ängården',
            vardgivarnamn: 'Vårdgivare'
        };

        it('should be correct when user\'s request origin is uthopp', inject(['common.fragaSvarCommonService', function(fragaSvarCommonService) {
            userModelMock.isUthopp = function() { return true; };
            var link = fragaSvarCommonService.buildMailToLink(qa);
            expect(link).toEqual(
                'mailto:?subject=En%20fraga-svar%20ska%20besvaras%20i%20Webcert%20pa%20enhet%20Angarden%20for%20vardgivare%20Vardgivare&body=Klicka%20pa%20lanktexten%20for%20att%20besvara%20fraga-svar%3A%0Ahttp%3A%2F%2Flocalhost%3A' +
                $window.location.port + '%2Fwebcert%2Fweb%2Fuser%2Fcertificate%2Ftestid%2Fquestions');
        }]));

        it('should be correct when user\'s request origin is something else than uthopp', inject(['common.fragaSvarCommonService', function(fragaSvarCommonService) {
            userModelMock.isUthopp = function() { return false; };
            var link = fragaSvarCommonService.buildMailToLink(qa);
            expect(link).toEqual(
                'mailto:?subject=En%20fraga-svar%20ska%20besvaras%20i%20Webcert%20pa%20enhet%20Angarden%20for%20vardgivare%20Vardgivare&body=Klicka%20pa%20lanktexten%20for%20att%20besvara%20fraga-svar%3A%0Ahttp%3A%2F%2Flocalhost%3A' +
                $window.location.port + '%2Fwebcert%2Fweb%2Fuser%2Fbasic-certificate%2Ftestid%2Fquestions');
        }]));

        it('should create a link that leads back to the intyg', inject(['common.fragaSvarCommonService', function(fragaSvarCommonService) {
            userModelMock.isUthopp = function() { return false; };
            var link = fragaSvarCommonService.buildMailToLink(qa);
            expect(link).not.toContain('undefined');
        }]));

        it('should create a link that leads back to the intyg', inject(['common.fragaSvarCommonService', function(fragaSvarCommonService) {
            userModelMock.isUthopp = function() { return false; };

            // This is fine. Both are handled in 4.1
            var qaWrong = {
                intygId: 'testid',
                enhetsnamn: 'Ängården',
                vardgivarnamn: 'Vårdgivare'
            };
            var link = fragaSvarCommonService.buildMailToLink(qaWrong);
            expect(link).not.toContain('undefined');
        }]));
    });

});
