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

describe('UtkastService', function() {
    'use strict';

    var dynamicLabelService;
    var utkastService;
    var commonViewState;
    var messageService;
    var viewState;
    var $httpBackend;
    var $location;
    var $rootScope;
    var $stateParams;
    var $timeout;
    var utkastContent;

    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('$stateParams', {});
        $provide.value('common.messageService', {
            addResources: function() {},
            getProperty: function(prop) { return prop; }
        });
        $provide.value('common.dynamicLabelService', {
            updateDynamicLabels: function(){}
        });
    }));

    beforeEach(angular.mock.inject(['common.dynamicLabelService', 'common.UtkastService', 'common.UtkastViewStateService',
        'common.messageService', '$httpBackend', '$location', '$rootScope', '$stateParams', '$timeout',
        function(_dynamicLabelService_, _utkastService_, _commonViewState_, _messageService_, _$httpBackend_, _$location_, _$rootScope_, _$stateParams_, _$timeout_) {
            dynamicLabelService = _dynamicLabelService_;
            utkastService = _utkastService_;
            messageService = _messageService_;
            commonViewState = _commonViewState_;
            $httpBackend = _$httpBackend_;
            $location = _$location_;
            $rootScope = _$rootScope_;
            $stateParams = _$stateParams_;
            $timeout = _$timeout_;

            commonViewState.reset();

            utkastContent = {
                grundData:{
                    skapadAv: {
                        vardenhet: {
                            postadress: null,
                            postnummer: null,
                            postort: null,
                            telefonnummer: null
                        }
                    }
                },
                id: 'testIntygId',
                typ: 'testIntyg',
                textVersion: '1.0'
            };

            viewState = {
                common: commonViewState,
                draftModel: {
                    isSigned: function() {},
                    isDraftComplete: function() {},
                    update: function(data) { viewState.draftModel.content = data.content; },
                    content: utkastContent,
                    version: 1
                }
            };
            viewState.intygModel = viewState.draftModel.content;
            viewState.intygModel.toSendModel = function() { return viewState.intygModel; };
            commonViewState.intyg.type = 'testIntyg';
        }
    ]));

    describe('load', function() {

        it ('utkast load isSigned should forward to intyg view', function () {
            spyOn(viewState.draftModel,'isSigned').and.callFake(function() {return true;});
            spyOn($location,'url');

            $stateParams.certificateId = 'testIntygId';
            var promise = utkastService.load(viewState);
            var resultData;
            promise.then(function(data) {
                resultData = data;
            });

            utkastContent.braIntygsData = 'bra';
            var response = {
                relations: [],
                content: utkastContent
            };
            $httpBackend.expectGET('/moduleapi/utkast/testIntyg/testIntygId?sjf=false').respond(200, response);
            $httpBackend.flush();
            $timeout.flush();

            expect($location.url).toHaveBeenCalledWith('/intyg/testIntyg/testIntygId');
        });

        it ('successful utkast load', function () {
            spyOn(dynamicLabelService, 'updateDynamicLabels');
            spyOn($rootScope,'$broadcast').and.callThrough();

            $stateParams.certificateId = 'testIntygId';
            var promise = utkastService.load(viewState);
            var resultData;
            promise.then(function(data) {
                resultData = data;
            });

            utkastContent.braIntygsData = 'bra';
            var response = {
                relations: [],
                content: utkastContent
            };
            $httpBackend.expectGET('/moduleapi/utkast/testIntyg/testIntygId?sjf=false').respond(200, response);
            $httpBackend.flush();
            expect(viewState.common.doneLoading).toBeFalsy();
            $timeout.flush();

            expect(viewState.common.doneLoading).toBeTruthy();
            expect(resultData.braIntygsData).toBe('bra');
            expect($rootScope.$broadcast.calls.count()).toBe(5);
            expect($rootScope.$broadcast.calls.argsFor(2)).toEqual(['intyg.loaded', response.content]);
            expect($rootScope.$broadcast.calls.argsFor(3)).toEqual(['testIntyg.loaded', response.content]);
            expect($rootScope.$broadcast.calls.argsFor(4)).toEqual(['wcFocusOn', 'focusFirstInput']);
        });

        it ('successful utkast load with sjf', function () {
            spyOn(dynamicLabelService, 'updateDynamicLabels');

            $stateParams.certificateId = 'testIntygId';
            $stateParams.sjf = true;
            var promise = utkastService.load(viewState);
            var resultData;
            promise.then(function(data) {
                resultData = data;
            });

            utkastContent.braIntygsData = 'bra';
            var response = {
                relations: [],
                content: utkastContent
            };
            $httpBackend.expectGET('/moduleapi/utkast/testIntyg/testIntygId?sjf=true').respond(200, response);
            $httpBackend.flush();
            $timeout.flush();

            expect(resultData.braIntygsData).toBe('bra');
        });

        it ('unsuccessful utkast load', function () {
            spyOn(dynamicLabelService, 'updateDynamicLabels');
            spyOn($rootScope,'$broadcast').and.callThrough();

            $stateParams.certificateId = 'testIntygId';
            var error;
            var promise = utkastService.load(viewState);
            promise.then(function() {
            },function(data) {
                error = data;
            });

            utkastContent.braIntygsData = 'bra';
            $httpBackend.expectGET('/moduleapi/utkast/testIntyg/testIntygId?sjf=false').respond(500, {errorCode:'AUTHORIZATION_PROBLEM'});
            expect(viewState.common.doneLoading).toBeFalsy();
            $httpBackend.flush();

            expect(viewState.common.doneLoading).toBeTruthy();
            expect(viewState.common.error.activeErrorMessageKey).toBe('common.error.authorization_problem');
            expect(error).toEqual({ errorCode: 'AUTHORIZATION_PROBLEM' });
        });

    });

    describe('save', function() {
        var formFailStateSet;
        beforeEach(function() {
            formFailStateSet = false;

            $rootScope.$on('saveRequest', function($event, saveDeferred) {
                var intygState = {
                    viewState: viewState,
                    formFail: function() {
                        formFailStateSet = true;
                    }
                };
                saveDeferred.resolve(intygState);
            });

        });

        it ('successful utkast save and draft incomplete', function () {
            var saveResponse = {
                'version':55,
                'status':'DRAFT_INCOMPLETE',
                'messages':[{'field':'errorField','type':'EMPTY','message':'error.message'}]
            };

            $httpBackend.expectPUT('/moduleapi/utkast/testIntyg/testIntygId/1').respond(200, saveResponse);

            var extras = { destroy:function(){}};
            spyOn(extras, 'destroy');
            utkastService.save(extras);

            $rootScope.$apply();
            $httpBackend.flush();

            expect(extras.destroy).toHaveBeenCalled();
            expect(formFailStateSet).toBeFalsy();
            expect(viewState.common.error.saveErrorMessage).toBe(null);
            expect(viewState.common.intyg.isComplete).toBeFalsy();
            expect(viewState.common.validationMessagesGrouped).toEqual({});
            expect(viewState.common.validationMessages).toEqual([]);
            expect(viewState.common.validationSections).toEqual([]);

            expect(viewState.common.saving).toBeTruthy();
            $timeout.flush();
            expect(viewState.common.saving).toBeFalsy();
            expect(viewState.draftModel.version).toBe(55);
        });

        it ('successful utkast save and draft incorrect format', function () {
            var saveResponse = {
                'version':1,
                'status':'DRAFT_INCOMPLETE',
                'messages':[{'field':'errorField','type':'INCORRECT_FORMAT','message':'error.message'},
                            {'field':'errorField2','type':'EMPTY','message':'error.message2'}]
            };

            $httpBackend.expectPUT('/moduleapi/utkast/testIntyg/testIntygId/1').respond(200, saveResponse);

            utkastService.save();

            $rootScope.$apply();
            $httpBackend.flush();

            expect(formFailStateSet).toBeFalsy();
            expect(viewState.common.error.saveErrorMessage).toBe(null);
            expect(viewState.common.intyg.isComplete).toBeFalsy();
            expect(viewState.common.validationMessagesGrouped).toEqual(
                {errorfield:[{'field':'errorField','type':'INCORRECT_FORMAT','message':'error.message'}]});
            expect(viewState.common.validationMessages).toEqual([
                {'field':'errorField','type':'INCORRECT_FORMAT','message':'error.message'}]);
            expect(viewState.common.validationSections).toEqual(['errorfield']);
        });

        it ('successful utkast save and draft incorrect format, showComplete is true', function () {
            viewState.common.showComplete = true;
            var saveResponse = {
                'version':1,
                'status':'DRAFT_INCOMPLETE',
                'messages':[{'field':'errorField','type':'INCORRECT_FORMAT','message':'error.message'},
                    {'field':'errorField2','type':'EMPTY','message':'error.message2'}]
            };

            $httpBackend.expectPUT('/moduleapi/utkast/testIntyg/testIntygId/1').respond(200, saveResponse);

            utkastService.save();

            $rootScope.$apply();
            $httpBackend.flush();

            expect(formFailStateSet).toBeFalsy();
            expect(viewState.common.error.saveErrorMessage).toBe(null);
            expect(viewState.common.intyg.isComplete).toBeFalsy();
            expect(viewState.common.validationMessagesGrouped).toEqual({
                errorfield:[{'field':'errorField','type':'INCORRECT_FORMAT','message':'error.message'}],
                errorfield2:[{'field':'errorField2','type':'EMPTY','message':'error.message2'}]});
            expect(viewState.common.validationMessages).toEqual([
                {'field':'errorField','type':'INCORRECT_FORMAT','message':'error.message'},
                {'field':'errorField2','type':'EMPTY','message':'error.message2'}]);
            expect(viewState.common.validationSections).toEqual(['errorfield','errorfield2']);
        });

        it ('successful utkast save and draft complete', function () {
            var saveResponse = {
                'version':1,
                'status':'DRAFT_COMPLETE',
                'messages':[]
            };

            $httpBackend.expectPUT('/moduleapi/utkast/testIntyg/testIntygId/1').respond(200, saveResponse);

            utkastService.save();

            $rootScope.$apply();
            $httpBackend.flush();

            expect(formFailStateSet).toBeFalsy();
            expect(viewState.common.error.saveErrorMessage).toBe(null);
            expect(viewState.common.intyg.isComplete).toBeTruthy();
            expect(viewState.common.validationMessagesGrouped).toEqual({});
            expect(viewState.common.validationMessages).toEqual([]);
            expect(viewState.common.validationSections).toEqual([]);
        });

        it ('unsuccessful utkast save', function () {
            $httpBackend.expectPUT('/moduleapi/utkast/testIntyg/testIntygId/1').respond(500);

            var extras = { destroy:function(){}};
            spyOn(extras, 'destroy');
            utkastService.save(extras);

            $rootScope.$apply();
            $httpBackend.flush();

            expect(extras.destroy).toHaveBeenCalled();
            expect(formFailStateSet).toBeTruthy();
            expect(viewState.common.error.saveErrorMessage).toBe('common.error.save.noconnection');
            expect(viewState.common.error.saveErrorCode).toBe('cantconnect');
        });

        it ('concurrent modification utkast save', function () {
            $httpBackend.expectPUT('/moduleapi/utkast/testIntyg/testIntygId/1').respond(500, {errorCode:'CONCURRENT_MODIFICATION'});

            utkastService.save();

            $rootScope.$apply();
            $httpBackend.flush();

            expect(formFailStateSet).toBeTruthy();
            expect(viewState.common.error.saveErrorMessage).toBe('common.error.save.concurrent_modification');
            expect(viewState.common.error.saveErrorCode).toBe('CONCURRENT_MODIFICATION');
        });

    });

});
