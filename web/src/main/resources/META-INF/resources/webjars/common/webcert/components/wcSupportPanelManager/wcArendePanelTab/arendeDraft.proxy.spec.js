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

describe('ArendeDraftProxy', function() {
    'use strict';

    var $httpBackend;
    var $rootScope;
    var ArendeDraftProxy;

    beforeEach(module('common', function($provide) {
        $provide.value('common.User', {});
    }));

    beforeEach(angular.mock.inject(['$controller', '$rootScope', '$httpBackend', 'common.ArendeDraftProxy',
        function($controller, _$rootScope_, _$httpBackend_, _ArendeProxy_) {

            $httpBackend = _$httpBackend_;
            $rootScope = _$rootScope_;
            $rootScope.$new();
            ArendeDraftProxy = _ArendeProxy_;
        }]));

    describe('#getDraft', function() {
        it('should call onSuccess callback when called', function() {

            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');

            var intygsId = 'intyg-1';
            var restPath = '/api/arende/draft/' + intygsId;
            $httpBackend.expectGET(restPath).respond(200, {
                'intygId':intygsId,
                'text': 'testText',
                'amne': 'testAmne'
            });

            ArendeDraftProxy.getDraft(intygsId, onSuccess, onError);
            $httpBackend.flush();

            expect(onSuccess).toHaveBeenCalled();
        });
    });

    describe('#saveDraft', function() {
        it('should call onSuccess callback when called', function() {

            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');

            var restPath = '/api/arende/draft';
            $httpBackend.expectPUT(restPath).respond(200);

            ArendeDraftProxy.saveDraft('intygId', 'questionId', 'text', 'amne', onSuccess, onError);
            $httpBackend.flush();

            expect(onSuccess).toHaveBeenCalled();
        });
    });

    describe('#deleteDraft', function() {
        it('should call onSuccess callback when called', function() {

            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');

            var intygId = 'intygId';
            var questionId = 'questionId';
            var restPath = '/api/arende/draft/' + intygId + '/' + questionId;

            $httpBackend.expectDELETE(restPath).respond(200);

            ArendeDraftProxy.deleteDraft(intygId, questionId, onSuccess, onError);
            $httpBackend.flush();

            expect(onSuccess).toHaveBeenCalled();
        });

        it('should not include question in path unless provided', function() {

            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');

            var intygId = 'intygId';
            var restPath = '/api/arende/draft/' + intygId;

            $httpBackend.expectDELETE(restPath).respond(200);

            ArendeDraftProxy.deleteDraft(intygId, undefined, onSuccess, onError);
            $httpBackend.flush();

            expect(onSuccess).toHaveBeenCalled();
        });
    });
    describe('#deleteQuestionDraft', function() {
        it('should call onSuccess callback when called', function() {

            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');

            var intygId = 'intygId';
            var restPath = '/api/arende/draft/' + intygId;

            $httpBackend.expectDELETE(restPath).respond(200);

            ArendeDraftProxy.deleteQuestionDraft(intygId, onSuccess, onError);
            $httpBackend.flush();

            expect(onSuccess).toHaveBeenCalled();
        });
    });
});
