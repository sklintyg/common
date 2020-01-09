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

describe('UtkastNotifyService', function() {
    'use strict';

    var dialogService;
    var utkastNotifyService;
    var utkastViewState;
    var $httpBackend;
    var $timeout;
    var fakeModal;
    var $window;

    beforeEach(angular.mock.module(function($provide){
        var $window = {location:{}};
        $provide.value('$window', $window);
        $provide.value('common.dialogService', jasmine.createSpyObj('common.dialogService', ['showErrorMessageDialog']));

        var fakeModal = {
            open: function(options) { this.options = options; return this; },
            result: {
                then: function() {return fakeModal.deferred.promise;}
            }
        };
        $provide.value('$uibModal', fakeModal);
    }));

    beforeEach(angular.mock.inject(['$httpBackend', '$q', '$timeout', '$uibModal', '$window', 'common.dialogService',
        'common.UtkastNotifyService', 'common.UtkastViewStateService',
        function(_$httpBackend_, $q, _$timeout_, _$uibModal_, _$window_, _dialogService_,
            _utkastNotifyService_, _UtkastViewStateService_) {
            $httpBackend = _$httpBackend_;
            $timeout = _$timeout_;
            fakeModal = _$uibModal_;
            fakeModal.deferred = $q.defer();
            $window = _$window_;
            dialogService = _dialogService_;
            utkastNotifyService = _utkastNotifyService_;
            utkastViewState = _UtkastViewStateService_;
            utkastViewState.reset();
        }
    ]));

    describe('notifyUtkast', function() {

        it ('should mark utkast as vidarebefordrat', function() {
            var utkast = {
                version:4
            };
            utkastNotifyService.notifyUtkast('intygsId','intygsTyp',utkast,utkastViewState);

            var utkastResponse = {
                version: 4,
                vidarebefordrad: false
            };
            $httpBackend.expectGET('/moduleapi/utkast/intygsTyp/intygsId').respond(200, utkastResponse);
            $httpBackend.flush();
            $timeout.flush();

            // Fake user yes click in dialog
            fakeModal.options.resolve.yesCallback()();
            fakeModal.deferred.resolve();
            expect(utkastViewState.vidarebefordraInProgress).toBeTruthy();

            var vidarebefordraResponse = {
                version: 5,
                vidarebefordrad: true
            };
            $httpBackend.expectPUT('/api/intyg/intygsTyp/intygsId/4/vidarebefordra').respond(200, vidarebefordraResponse);
            $httpBackend.flush();
            expect(utkastViewState.vidarebefordraInProgress).toBeFalsy();
            expect(utkast.version).toBe(5);
            expect(utkast.vidarebefordrad).toBeTruthy();
            expect($window.location).toContain('mailto:');
            expect(dialogService.showErrorMessageDialog).not.toHaveBeenCalled();
        });

        it ('should show errormessagedialog if mark utkast as vidarebefordrat fails', function() {
            var utkast = {
                version:4
            };
            utkastNotifyService.notifyUtkast('intygsId','intygsTyp',utkast,utkastViewState);

            var utkastResponse = {
                version: 4,
                vidarebefordrad: false
            };
            $httpBackend.expectGET('/moduleapi/utkast/intygsTyp/intygsId').respond(200, utkastResponse);
            $httpBackend.flush();
            $timeout.flush();

            // Fake user yes click in dialog
            fakeModal.options.resolve.yesCallback()();
            fakeModal.deferred.resolve();
            expect(utkastViewState.vidarebefordraInProgress).toBeTruthy();

            $httpBackend.expectPUT('/api/intyg/intygsTyp/intygsId/4/vidarebefordra').respond(500);
            $httpBackend.flush();
            expect(utkastViewState.vidarebefordraInProgress).toBeFalsy();
            expect(utkast.version).toBe(4);
            expect(utkast.vidarebefordrad).toBeFalsy();
            expect($window.location).toContain('mailto:');
            expect(dialogService.showErrorMessageDialog).toHaveBeenCalled();
        });

        it ('should not mark utkast as vidarebefordrat if user selects no', function() {
            var utkast = {
                version:4
            };
            utkastNotifyService.notifyUtkast('intygsId','intygsTyp',utkast,utkastViewState);

            var utkastResponse = {
                version: 4,
                vidarebefordrad: false
            };
            $httpBackend.expectGET('/moduleapi/utkast/intygsTyp/intygsId').respond(200, utkastResponse);
            $httpBackend.flush();
            $timeout.flush();

            // Fake user yes click in dialog
            fakeModal.options.resolve.noCallback()();
            fakeModal.deferred.resolve();

            expect(utkastViewState.vidarebefordraInProgress).toBeFalsy();
            expect(utkast.version).toBe(4);
            expect(utkast.vidarebefordrad).toBeFalsy();
            expect($window.location).toContain('mailto:');
            expect(dialogService.showErrorMessageDialog).not.toHaveBeenCalled();
        });
    });

    describe('#notificationEncoding', function() {

        it ('should convert åäö to urlencoded characters', function () {
            var intygId = 'intyg-1';
            var intygType = 'fk7263';
            var enhetsNamn = 'Vårdenhet åäöÅÄÖ';
            var vardgivareNamn = 'Vårdgivare åäöÅÄÖ';

            // Urlencoded åäö
            var urlEncoded = '%C3%A5%C3%A4%C3%B6%C3%85%C3%84%C3%96';

            expect(utkastNotifyService.buildNotifyDoctorMailToLink(intygId, intygType, enhetsNamn, vardgivareNamn)).toContain(urlEncoded);
        });

    });

});
