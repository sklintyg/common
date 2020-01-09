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

describe('arendeHantera', function() {
    'use strict';

    var $httpBackend;
    var $rootScope;
    var $scope;
    var element;
    var ArendeNewViewState;
    var ResourceLinkService;

    var arende = {
        fraga:{'kompletteringar':[],'internReferens':'ref-1','status':'CLOSED','amne':'OVRIGT','meddelandeRubrik':'Övrigt',
            'sistaDatumForSvar':'2016-09-14','vidarebefordrad':false,'frageStallare':'WC','externaKontakter':[],'meddelande':'Meddelandetext',
            'signeratAv':'Arnold Johansson','svarSkickadDatum':'2016-08-31T16:27:29.898','intygId':'425da3ef-2a24-4a9a-98c4-ace7625c1d4a',
            'enhetsnamn':'NMT vg3 ve1','vardgivarnamn':'NMT vg3','timestamp':'2016-08-31T16:27:29.898','arendeType':'FRAGA','vardaktorNamn':'Arnold Johansson'},
        svar:{'kompletteringar':[],'internReferens':'37637406-b888-467b-a672-62beb7905907','status':'CLOSED','amne':'OVRIGT',
            'meddelandeRubrik':'Övrigt','vidarebefordrad':false,'frageStallare':'FK','externaKontakter':[],'meddelande':'Testsvar',
            'signeratAv':'Arnold Johansson','svarSkickadDatum':'2016-08-31T16:40:35.498','intygId':'425da3ef-2a24-4a9a-98c4-ace7625c1d4a',
            'enhetsnamn':'NMT vg3 ve1','vardgivarnamn':'NMT vg3','timestamp':'2016-08-31T16:40:35.498','arendeType':'SVAR',
            'svarPaId':'f9638b05-1fa0-4cc7-8e1c-177ce670808b','vardaktorNamn':'Arnold Johansson'},
        senasteHandelse:'2016-08-31T16:40:35.498',paminnelser:[]
    };

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.User', { getVardenhetFilterList: function() { return []; } });
        $provide.value('common.statService', jasmine.createSpyObj('common.statService', [ 'refreshStat']));
        $provide.value('$stateParams', { certificateId: 'intygsid' });
        $provide.value('common.ResourceLinkService', {
            isLinkTypeExists: function(links, type) {
                return true;
            }
        });
    }));

    beforeEach(angular.mock.module('htmlTemplates'));

    beforeEach(angular.mock.inject(['$controller', '$compile', '$httpBackend', '$rootScope', 'common.ArendeListItemModel',
        'common.ArendeListViewStateService', 'common.ArendeNewViewStateService', 'common.ResourceLinkService',
        function($controller, $compile, _$httpBackend_, _$rootScope_, _ArendeListItemModel_, _ArendeListViewStateService_,
            _ArendeNewViewState_, _ResourceLinkService_) {
            $rootScope = _$rootScope_;
            $httpBackend = _$httpBackend_;
            ArendeNewViewState = _ArendeNewViewState_;
            ResourceLinkService = _ResourceLinkService_;
            $scope = $rootScope.$new();

            var arendePanelController = {
                getArendePanelSvar: function() {
                    return {
                        hasSvaraDraft: function() { return false; }
                    };
                }
            };

            $scope.arendeListItem = _ArendeListItemModel_.build(angular.copy(arende));
            $scope.parentViewState = _ArendeListViewStateService_.reset();
            $scope.parentViewState.setIntygType('intygstyp');
            element = angular.element('<div><arende-hantera arende-list-item="arendeListItem" parent-view-state="parentViewState"></arende-hantera></div>');
            element.data('$arendePanelController', arendePanelController);
            $compile(element)($scope);

            $scope.$digest();
            $scope = element.find('arende-hantera').isolateScope();
        }]));

    it('Should be possible to change to unhandled status if sent from FK and has no response', function() {
        $scope.arendeListItem.arende.fraga.frageStallare = 'FK';
        $scope.arendeListItem.arende.svar.frageStallare = 'WC';
        $scope.arendeListItem.arende.svar.meddelande = '';
        $scope.$apply();
        expect($scope.showHandleToggle()).toBeTruthy();
        expect(element.find('INPUT').length).toBe(1);
    });

    it('Should not be possible to change to unhandled status if sent from FK and has a response (FS-011)', function() {
        $scope.arendeListItem.arende.fraga.frageStallare = 'FK';
        $scope.arendeListItem.arende.svar.frageStallare = 'WC';
        $scope.$apply();
        expect($scope.showHandleToggle()).toBeFalsy();
        expect(element.find('INPUT').length).toBe(0);
    });

    it('Should not be possible to change to unhandled status if KOMPLETTERING and have answerered with intyg', function() {
        $scope.arendeListItem.arende.fraga.amne = 'KOMPLT';
        $scope.arendeListItem.arende.fraga.status = 'CLOSED';
        $scope.arendeListItem.arende.answeredWithIntyg = {};
        $scope.$apply();
        expect($scope.showHandleToggle()).toBeFalsy();
        expect(element.find('INPUT').length).toBe(0);
    });

    it('Should save unhandled status', function() {
        var responsArende = angular.copy(arende);
        responsArende.fraga.status = 'PENDING_EXTERNAL_ACTION';
        $httpBackend.expectPUT('/moduleapi/arende/intygstyp/ref-1/oppna').respond(200, responsArende);

        element.find('INPUT').click();
        $httpBackend.flush();
        expect($scope.arendeListItem.activeErrorMessageKey).toBe(null);
    });

    it('Should show errormessage if save unhandled status fails', function() {
        $httpBackend.expectPUT('/moduleapi/arende/intygstyp/ref-1/oppna').respond(500);

        element.find('INPUT').click();
        $httpBackend.flush();
        expect($scope.arendeListItem.activeErrorMessageKey).toBe('unknown');
    });

    it('Should save handled status', function() {
        var responsArende = angular.copy(arende);
        $scope.arendeListItem.arende.fraga.status = 'PENDING_EXTERNAL_ACTION';
        $scope.$apply();
        $httpBackend.expectPUT('/moduleapi/arende/intygstyp/ref-1/stang').respond(200, responsArende);

        element.find('INPUT').click();
        $httpBackend.flush();
        expect($scope.arendeListItem.activeErrorMessageKey).toBe(null);
    });

    it('Should show errormessage if save handled status failes', function() {
        $scope.arendeListItem.arende.fraga.status = 'PENDING_EXTERNAL_ACTION';
        $scope.$apply();
        $httpBackend.expectPUT('/moduleapi/arende/intygstyp/ref-1/stang').respond(500);

        element.find('INPUT').click();
        $httpBackend.flush();
        expect($scope.arendeListItem.activeErrorMessageKey).toBe('unknown');
    });

});
