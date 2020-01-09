/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

describe('enhetArendenCommonService', function() {
    'use strict';

    describe('#isUnhandled', function() {
        var enhetArendenCommonService;

        beforeEach(angular.mock.module('common'), function($provide){
            $provide.value('common.dialogService', {});
            $provide.value('common.User', {});
        });

        beforeEach(angular.mock.inject(['common.enhetArendenCommonService',
            function(_enhetArendenCommonService_) {
                enhetArendenCommonService = _enhetArendenCommonService_;
            }
        ]));

        it ('should be unhandled if qa.status === ANSWERED', function () {
            var qa = {status:'ANSWERED'};
            expect(enhetArendenCommonService.isUnhandled(qa)).toBeTruthy();
        });

        it ('should be unhandled if qa.status === PENDING_INTERNAL_ACTION && qa.amne === PAMINNELSE', function () {
            var qa = {status:'PENDING_INTERNAL_ACTION', amne : 'PAMINNELSE'};
            expect(enhetArendenCommonService.isUnhandled(qa)).toBeTruthy();
        });

        it ('should be unhandled if qa.status === PENDING_INTERNAL_ACTION && qa.amne === PAMINN', function () {
            var qa = {status:'PENDING_INTERNAL_ACTION', amne : 'PAMINN'};
            expect(enhetArendenCommonService.isUnhandled(qa)).toBeTruthy();
        });

        it ('should be unhandled if qa.status === PENDING_EXTERNAL_ACTION && qa.amne === MAKULERING', function () {
            var qa = {status:'PENDING_EXTERNAL_ACTION', amne : 'MAKULERING'};
            expect(enhetArendenCommonService.isUnhandled(qa)).toBeFalsy();
        });

        it ('should be unhandled if qa.status === ANSWERED && qa.amne === *', function () {
            var qa = {status:'ANSWERED', amne : 'PAMINNELSE'};
            expect(enhetArendenCommonService.isUnhandled(qa)).toBeTruthy();
        });

        it ('should be unhandled if qa.status === PENDING_EXTERNAL_ACTION && qa.amne === MAKULERING', function () {
            var qa = {status:'PENDING_EXTERNAL_ACTION', amne : 'AHHG'};
            expect(enhetArendenCommonService.isUnhandled(qa)).toBeFalsy();
        });

        it ('should be handled if qa.status != ANSWERED', function () {
            var qa = {status:'CLOSED'};
            expect(enhetArendenCommonService.isUnhandled(qa)).toBeFalsy();
        });
    });

    describe('decorateSingleItem', function() {
        var enhetArendenCommonService;

        var UserModel;

        beforeEach(angular.mock.module('common'), function($provide){
        });

        beforeEach(angular.mock.inject(['common.enhetArendenCommonService', 'common.UserModel',
            function(_enhetArendenCommonService_, _UserModel_) {
                enhetArendenCommonService = _enhetArendenCommonService_;
                UserModel = _UserModel_;
            }
        ]));

        it ('should disable answer if amne is PAMINNELSE', function () {
            var qa = {amne:'PAMINNELSE'};
            enhetArendenCommonService.decorateSingleItem(qa);
            expect(qa.answerDisabled).toBeTruthy();
            expect(qa.svaraMedNyttIntygDisabled).toBeFalsy();
        });

        it ('should disable answer if amne is PAMINN', function () {
            var qa = {amne:'PAMINN'};
            enhetArendenCommonService.decorateSingleItem(qa);
            expect(qa.answerDisabled).toBeTruthy();
            expect(qa.svaraMedNyttIntygDisabled).toBeFalsy();
        });

        it ('should disable answer if it is a komplettering (new KOMPLT) and user does not have that permission', function () {
            var qa = {amne:'KOMPLT'};
            enhetArendenCommonService.decorateSingleItem(qa);
            expect(qa.answerDisabled).toBeTruthy();
            expect(qa.svaraMedNyttIntygDisabled).toBeFalsy();
        });

        it ('should disable answer if it is a komplettering (old KOMPLETTERING_AV_LAKARINTYG) and user does not have that permission', function () {
            var qa = {amne:'KOMPLETTERING_AV_LAKARINTYG'};
            enhetArendenCommonService.decorateSingleItem(qa);
            expect(qa.answerDisabled).toBeTruthy();
            expect(qa.svaraMedNyttIntygDisabled).toBeFalsy();
        });

    });

    describe('vidarebefordrad', function() {

        var $httpBackend;
        var enhetArendenCommonService;

        beforeEach(angular.mock.inject(['$httpBackend', 'common.enhetArendenCommonService',
            function(_$httpBackend_, _enhetArendenCommonService_) {
                $httpBackend = _$httpBackend_;
                enhetArendenCommonService = _enhetArendenCommonService_;
            }
        ]));

        it('success', function() {
            var callback = {done:function(){}};
            spyOn(callback,'done');
            enhetArendenCommonService.setVidareBefordradState('111', 'testIntygTyp', true, callback.done);
            $httpBackend.expectPOST('/moduleapi/arende/111/vidarebefordrad').respond(200,{fragasvar:'aaa'});
            $httpBackend.flush();
            expect(callback.done).toHaveBeenCalledWith({fragasvar:'aaa'});
        });

        it('fail', function() {
            var callback = {done:function(){}};
            spyOn(callback,'done');
            enhetArendenCommonService.setVidareBefordradState('111', 'testIntygTyp', true, callback.done);
            $httpBackend.expectPOST('/moduleapi/arende/111/vidarebefordrad').respond(500,{fragasvar:'aaa'});
            $httpBackend.flush();
            expect(callback.done).toHaveBeenCalledWith(null);
        });

        it('toggle', function() {
            var onVidarebefordradChange = function() {};
            enhetArendenCommonService.handleVidareBefordradToggle(onVidarebefordradChange);
        });
    });

    describe('QAonly dialog', function() {

        var dialogService;
        var enhetArendenCommonService;
        var UserModel;

        beforeEach(angular.mock.inject(['common.enhetArendenCommonService', 'common.dialogService',
            'common.UserModel',
            function(_enhetArendenCommonService_, _dialogService_, _UserModel_) {
                dialogService = _dialogService_;
                enhetArendenCommonService = _enhetArendenCommonService_;
                UserModel = _UserModel_;

                UserModel.setUser({origin: 'UTHOPP'});
            }
        ]));

        it('should not open dialog', function() {

            var spies = {
                preventDefault: function() {},
                unbindEvent: function() {}
            };
            spyOn(spies, 'preventDefault');
            spyOn(spies, 'unbindEvent');
            spyOn(dialogService, 'showDialog').and.callThrough();
            enhetArendenCommonService.checkQAonlyDialog(
                {},
                spies,
                '#/fragasvar/',
                '#/enhet-arenden',
                spies.unbindEvent);

            expect(spies.unbindEvent).toHaveBeenCalled();
            expect(dialogService.showDialog).not.toHaveBeenCalled();
        });

        it('should open dialog', function() {

            var spies = {
                preventDefault: function() {},
                unbindEvent: function() {}
            };
            spyOn(spies, 'preventDefault');
            spyOn(spies, 'unbindEvent');
            spyOn(dialogService, 'showDialog').and.callThrough();
            enhetArendenCommonService.checkQAonlyDialog(
                {},
                spies,
                '#/create/choose-patient/index',
                '#/enhet-arenden',
                spies.unbindEvent);

            expect(spies.preventDefault).toHaveBeenCalled();
            expect(dialogService.showDialog).toHaveBeenCalled();
        });
    });
});
