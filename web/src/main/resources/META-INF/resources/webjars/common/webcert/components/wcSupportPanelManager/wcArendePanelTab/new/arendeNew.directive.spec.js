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

describe('arendeNew', function() {
    'use strict';

    var $httpBackend;
    var $rootScope;
    var $scope;
    var element;
    var ArendeNewViewState;
    var ArendeListViewState;

    var arende = {
        fraga:{'kompletteringar':[],'internReferens':'ref-1','status':'CLOSED','amne':'OVRIGT','meddelandeRubrik':'Övrigt',
            'sistaDatumForSvar':'2016-09-14','vidarebefordrad':false,'frageStallare':'FK','externaKontakter':[],'meddelande':'Meddelandetext',
            'signeratAv':'Arnold Johansson','svarSkickadDatum':'2016-08-31T16:27:29.898','intygId':'425da3ef-2a24-4a9a-98c4-ace7625c1d4a',
            'enhetsnamn':'NMT vg3 ve1','vardgivarnamn':'NMT vg3','timestamp':'2016-08-31T16:27:29.898','arendeType':'FRAGA','vardaktorNamn':'Arnold Johansson'},
        svar:{'kompletteringar':[],'internReferens':'37637406-b888-467b-a672-62beb7905907','status':'CLOSED','amne':'OVRIGT',
            'meddelandeRubrik':'Övrigt','vidarebefordrad':false,'frageStallare':'WC','externaKontakter':[],'meddelande':'Testsvar',
            'signeratAv':'Arnold Johansson','svarSkickadDatum':'2016-08-31T16:40:35.498','intygId':'425da3ef-2a24-4a9a-98c4-ace7625c1d4a',
            'enhetsnamn':'NMT vg3 ve1','vardgivarnamn':'NMT vg3','timestamp':'2016-08-31T16:40:35.498','arendeType':'SVAR',
            'svarPaId':'f9638b05-1fa0-4cc7-8e1c-177ce670808b','vardaktorNamn':'Arnold Johansson'},
        senasteHandelse:'2016-08-31T16:40:35.498',paminnelser:[]
    };

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.User', {
            getVardenhetFilterList: function() { return []; },
            getUser: function() {
                return {
                    parameters: {
                        sjf: false
                    }
                };
            }
        });

        $provide.value('common.statService', jasmine.createSpyObj('common.statService', [ 'refreshStat']));
        $provide.value('$stateParams', { certificateId: 'intygsid' });
        $provide.value('common.ArendeDraftProxy', { deleteQuestionDraft: function() {}, getDraft: function() {}});
    }));

    beforeEach(angular.mock.module('htmlTemplates'));

    beforeEach(angular.mock.inject(['$controller', '$compile', '$httpBackend', '$rootScope', 'common.ArendeListItemModel',
        'common.ArendeListViewStateService', 'common.ArendeNewViewStateService',
        function($controller, $compile, _$httpBackend_, _$rootScope_, _ArendeListItemModel_, _ArendeListViewStateService_,
            _ArendeNewViewState_) {
            $rootScope = _$rootScope_;
            $httpBackend = _$httpBackend_;
            ArendeNewViewState = _ArendeNewViewState_;
            $scope = $rootScope.$new();
            ArendeListViewState = _ArendeListViewStateService_;

            $scope.parentViewState = ArendeListViewState.reset();
            ArendeListViewState.setArendeList([arende]);
            $scope.parentViewState.setIntygType('intygstyp');
            element = $compile('<arende-new arende-list="arendeList" parent-view-state="parentViewState"></arende-new>')($scope);
            $scope.$digest();
            $scope = element.isolateScope();
        }]));

    it('Should send new Ärende', function() {
        expect(ArendeListViewState.arendeList.length).toBe(1);
        $scope.arendeNewModel.frageText = 'Fråga';
        $scope.arendeNewModel.chosenTopic = 'KONTKT';
        $scope.$digest();
        $scope.sendNewArende();

        $httpBackend.expectPOST('/moduleapi/arende/intygstyp/intygsid').respond(200, arende);
        $httpBackend.flush();

        expect(ArendeListViewState.arendeList.length).toBe(2);
        expect(ArendeListViewState.arendeList[0].arende).toEqual(arende);
        expect(ArendeListViewState.arendeList[1].arende).toEqual(arende);
    });

    it('Should send new Ärende', function() {
        expect(ArendeListViewState.arendeList.length).toBe(1);
        $scope.arendeNewModel.frageText = 'Fråga';
        $scope.arendeNewModel.chosenTopic = 'KONTKT';
        $scope.$digest();
        $scope.sendNewArende();

        $httpBackend.expectPOST('/moduleapi/arende/intygstyp/intygsid').respond(500);
        $httpBackend.flush();

        expect(ArendeListViewState.arendeList.length).toBe(1);
        expect(ArendeListViewState.arendeList[0].arende).toEqual(arende);
        expect(ArendeNewViewState.activeErrorMessageKey).toBe('unknown');
    });

    it('Should not be able to cancel Ärende before something has been entered', function() {
        expect(ArendeListViewState.arendeList.length).toBe(1);
        $scope.arendeNewModel.chosenTopic = null;
        $scope.arendeNewModel.frageText = null;
        expect($scope.isArendeNonCancellable()).toBe(true);
    });

    it('Should be able to cancel Ärende if something has been entered as topic', function() {
        expect(ArendeListViewState.arendeList.length).toBe(1);
        $scope.arendeNewModel.chosenTopic = 'OVRIGT';
        $scope.arendeNewModel.frageText = null;
        expect($scope.isArendeNonCancellable()).toBe(false);
    });

    it('Should be able to cancel Ärende if something has been entered as question text', function() {
        expect(ArendeListViewState.arendeList.length).toBe(1);
        $scope.arendeNewModel.chosenTopic = null;
        $scope.arendeNewModel.frageText = 'Frågetext';
        expect($scope.isArendeNonCancellable()).toBe(false);
    });
});
