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
describe('IntygCopyService', function() {
    'use strict';

    var IntygCopyFornya;
    var $httpBackend;
    var $state;
    var $timeout;
    var dialogService;
    var UserModel;

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.messageService',
            jasmine.createSpyObj('common.messageService', [ 'getProperty', 'addResources' ]));
        $provide.value('$stateParams', {});
        $provide.value('common.statService', jasmine.createSpyObj('common.statService', [ 'refreshStat' ]));
        $provide.value('common.UtkastViewStateService', {});
        $provide.value('common.utkastNotifyService', {});
        $provide.value('common.domain.DraftModel', {});
        $provide.value('common.User', jasmine.createSpyObj('common.User', ['storeAnvandarPreference']));
    }));

    angular.module('common').config(function($stateProvider){
        $stateProvider.state('fk7263-edit', {
        });
    });

    beforeEach(angular.mock.inject(['common.IntygCopyFornya', '$httpBackend', '$state', '$timeout',
        'common.dialogService', 'common.UserModel',
        function(_IntygCopyFornya_, _$httpBackend_, _$state_, _$timeout_, _dialogService_, _UserModel_) {
            IntygCopyFornya = _IntygCopyFornya_;
            $httpBackend = _$httpBackend_;
            $state = _$state_;
            $timeout = _$timeout_;
            dialogService = _dialogService_;
            UserModel = _UserModel_;

            UserModel.setUser({
                anvandarPreference: {}
            });
        }]));

    describe('#copy', function() {

        var $scope;
        var intyg;

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
            intyg = {
                'intygId': 'intyg-1', 'source': 'IT', 'intygType': 'fk7263', 'status': 'SENT',
                'lastUpdatedSigned': '2011-03-23T09:29:15.000', 'updatedSignedBy': 'Eva Holgersson', 'vidarebefordrad': false,
                'grundData' : { 'patient' : { 'personId': '19121212-1212'}, 'skapadAv' : {'vardenhet' : {'enhetsid' : '1234'} } }
            };

            spyOn(dialogService, 'showDialog').and.callFake(function(options) {
                options.button1click();

                return {
                    opened: { then: function() {} },
                    result: { then: function() {} },
                    close: function() {}
                };
            });

            spyOn($state, 'go').and.callThrough();
        });

        it('should immediately request a utkast copy of intyg if the copy preference is set', function() {

            UserModel.setAnvandarPreference(IntygCopyFornya.COPY_DIALOG_PREFERENCE, true);

            $httpBackend.expectPOST('/api/intyg/' + intyg.intygType + '/' + intyg.intygId +'/kopiera/').respond(
                {'intygsUtkastId':'nytt-utkast-id','intygsTyp':'fk7263'}
            );
            IntygCopyFornya.copy($scope.viewState, intyg);
            $httpBackend.flush();
            $timeout.flush();
            expect(dialogService.showDialog).not.toHaveBeenCalled();
            expect($state.go).toHaveBeenCalledWith('fk7263-edit', { certificateId : 'nytt-utkast-id' });

            UserModel.setAnvandarPreference(IntygCopyFornya.COPY_DIALOG_PREFERENCE, false);
        });

        it('should show the copy dialog if the copy preference is not set', function() {

            UserModel.setAnvandarPreference(IntygCopyFornya.COPY_DIALOG_PREFERENCE, false);
            $httpBackend.expectPOST('/api/intyg/' + intyg.intygType + '/' + intyg.intygId +'/kopiera/').respond(
                {'intygsUtkastId':'nytt-utkast-id','intygsTyp':'fk7263'}
            );
            IntygCopyFornya.copy($scope.viewState, intyg);
            $httpBackend.flush();
            $timeout.flush();

            expect(dialogService.showDialog).toHaveBeenCalled();
        });

        it('should immediately request a fornya utkast of intyg if the fornya preference is set', function() {

            UserModel.setAnvandarPreference(IntygCopyFornya.FORNYA_DIALOG_PREFERENCE, true);

            $httpBackend.expectPOST('/api/intyg/' + intyg.intygType + '/' + intyg.intygId +'/fornya/').respond(
                {'intygsUtkastId':'nytt-utkast-id','intygsTyp':'fk7263'}
            );
            IntygCopyFornya.fornya($scope.viewState, intyg);
            $httpBackend.flush();
            $timeout.flush();
            expect(dialogService.showDialog).not.toHaveBeenCalled();
            expect($state.go).toHaveBeenCalledWith('fk7263-edit', { certificateId : 'nytt-utkast-id' });

            UserModel.setAnvandarPreference(IntygCopyFornya.FORNYA_DIALOG_PREFERENCE, false);
        });

        it('should show the fornya dialog if the copy preference is not set', function() {

            UserModel.setAnvandarPreference(IntygCopyFornya.FORNYA_DIALOG_PREFERENCE, false);
            $httpBackend.expectPOST('/api/intyg/' + intyg.intygType + '/' + intyg.intygId +'/fornya/').respond(
                {'intygsUtkastId':'nytt-utkast-id','intygsTyp':'fk7263'}
            );
            IntygCopyFornya.fornya($scope.viewState, intyg);
            $httpBackend.flush();
            $timeout.flush();

            expect(dialogService.showDialog).toHaveBeenCalled();

        });
    });

    describe('_createCopyDraft', function() {

        var intyg;
        beforeEach(function() {
            intyg = {
                'intygId': 'intyg-1', 'source': 'IT', 'intygType': 'fk7263', 'status': 'SENT',
                'lastUpdatedSigned': '2011-03-23T09:29:15.000', 'updatedSignedBy': 'Eva Holgersson', 'vidarebefordrad': false,
                'grundData' : { 'patient' : { 'personId': '19121212-1212'}}
            };
        });

        it('should request a copy and redirect to the edit page', function() {

            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');

            $httpBackend.expectPOST('/api/intyg/' + intyg.intygType + '/' + intyg.intygId +'/kopiera/').respond(
                {'intygsUtkastId':'nytt-utkast-id','intygsTyp':'fk7263'}
            );
            IntygCopyFornya.__test__.createCopyDraft(intyg, onSuccess, onError);
            $httpBackend.flush();

            expect(onSuccess).toHaveBeenCalledWith({'intygsUtkastId':'nytt-utkast-id','intygsTyp':'fk7263'});
            expect(onError).not.toHaveBeenCalled();
        });
    });

});
