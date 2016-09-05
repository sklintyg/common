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

describe('arendeHantera', function() {
    'use strict';

    var $httpBackend;
    var $rootScope;
    var $scope;
    var element;
    var ArendeNewViewState;

    var arende = {
        fraga:{'kompletteringar':[],'internReferens':'ref-1','status':'CLOSED','amne':'OVRIGT','meddelandeRubrik':'Övrigt',
            'sistaDatumForSvar':'2016-09-14','vidarebefordrad':false,'frageStallare':'FK','externaKontakter':[],'meddelande':'Meddelandetext',
            'signeratAv':'Arnold Schwarzenegger','svarSkickadDatum':'2016-08-31T16:27:29.898','intygId':'425da3ef-2a24-4a9a-98c4-ace7625c1d4a',
            'enhetsnamn':'NMT vg3 ve1','vardgivarnamn':'NMT vg3','timestamp':'2016-08-31T16:27:29.898','arendeType':'FRAGA','vardaktorNamn':'Arnold Schwarzenegger'},
        svar:{'kompletteringar':[],'internReferens':'37637406-b888-467b-a672-62beb7905907','status':'CLOSED','amne':'OVRIGT',
            'meddelandeRubrik':'Övrigt','vidarebefordrad':false,'frageStallare':'WC','externaKontakter':[],'meddelande':'Testsvar',
            'signeratAv':'Arnold Schwarzenegger','svarSkickadDatum':'2016-08-31T16:40:35.498','intygId':'425da3ef-2a24-4a9a-98c4-ace7625c1d4a',
            'enhetsnamn':'NMT vg3 ve1','vardgivarnamn':'NMT vg3','timestamp':'2016-08-31T16:40:35.498','arendeType':'SVAR',
            'svarPaId':'f9638b05-1fa0-4cc7-8e1c-177ce670808b','vardaktorNamn':'Arnold Schwarzenegger'},
        senasteHandelse:'2016-08-31T16:40:35.498',paminnelser:[]
    };

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.User', { getVardenhetFilterList: function() { return []; } });
        $provide.value('common.statService', jasmine.createSpyObj('common.statService', [ 'refreshStat']));
        $provide.value('$stateParams', { certificateId: 'intygsid' });
    }));

    beforeEach(angular.mock.module('htmlTemplates'));

    beforeEach(angular.mock.inject(['$controller', '$compile', '$httpBackend', '$rootScope', 'common.ArendeListItemModel',
        'common.ArendenViewStateService', 'common.ArendeNewViewStateService',
        function($controller, $compile, _$httpBackend_, _$rootScope_, _ArendeListItemModel_, _ArendenViewStateService_,
            _ArendeNewViewState_) {
            $rootScope = _$rootScope_;
            $httpBackend = _$httpBackend_;
            ArendeNewViewState = _ArendeNewViewState_;
            $scope = $rootScope.$new();

            $scope.arendeListItem = _ArendeListItemModel_.build(angular.copy(arende));
            $scope.parentViewState = _ArendenViewStateService_.reset();
            $scope.parentViewState.setIntygType('intygstyp');
            element = $compile('<div arende-hantera arende-list-item="arendeListItem" parent-view-state="parentViewState"></div>')($scope);

            $scope.$digest();
            $scope = element.isolateScope();
        }]));

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
