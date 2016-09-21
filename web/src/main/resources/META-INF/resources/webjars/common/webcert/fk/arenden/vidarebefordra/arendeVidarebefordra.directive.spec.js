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

describe('arendeVidarebefordra', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var ArendeProxy;
    var authorityService;
    var DialogService;
    var $window;

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.dialogService', jasmine.createSpyObj('common.dialogService',
            [ 'showErrorMessageDialog']));
        $provide.value('common.IntygHelper', { isSentToTarget: function() {} });
        $provide.value('common.User', { getVardenhetFilterList: function() { return []; } });
        $provide.value('common.statService', {});
        $provide.value('common.IntygCopyRequestModel', jasmine.createSpyObj('common.IntygCopyRequestModel',
            [ 'build']));

        $provide.value('common.ArendeProxy', jasmine.createSpyObj('common.ArendeProxy', ['setVidarebefordradState']));
        $provide.value('common.authorityService', jasmine.createSpyObj('common.authorityService', ['isAuthorityActive']));
        $provide.value('$window', {location:{
            protocol:'protocol',
            hostname:'hostname',
            port:'port'
        }});
    }));

    beforeEach(angular.mock.module('htmlTemplates'));

    beforeEach(angular.mock.inject(['$controller', '$compile', '$rootScope', '$window',
        'common.authorityService', 'common.ArendeProxy', 'common.dialogService',
        function($controller, $compile, _$rootScope_, _$window_, _authorityService_, _ArendeProxy_,
            _DialogService_) {
            $rootScope = _$rootScope_;
            authorityService = _authorityService_;
            authorityService.isAuthorityActive.and.returnValue(true);
            ArendeProxy = _ArendeProxy_;
            DialogService = _DialogService_;
            $window = _$window_;

            $scope = $rootScope.$new();
            $scope.arendeListItem = {
                arende:{
                    fraga:{
                        internReferens: 'ID111',
                        intygId:'ID112',
                        enhetsnamn:'enhet1',
                        vardgivarnamn:'vardgivare1'
                    },
                    svar:{}
                }
            };
            $scope.parentViewState = {
                intygProperties: {
                    kompletteringOnly:false,
                    isRevoked:false,
                    type:'testIntyg'
                },
                intyg: { grundData:{skapadAv:{vardenhet:{}}}}
            };

            var tpl = angular.element(
                '<div arende-vidarebefordra arende-list-item="arendeListItem" panel-id="handled" parent-view-state="parentViewState"></div>'
            );
            var element = $compile(tpl)($scope);
            $scope.$digest();
            $scope = element.isolateScope();
        }]));


    it('should open mail dialog', function() {
        $scope.openMailDialog($scope.arendeListItem.arende);

        expect($window.location).toEqual(
            'mailto:?subject=Ett%20arende%20ska%20besvaras%20i%20Webcert%20pa%20enhet%20enhet1%20for%20vardgivare%20vardgivare1'+
            '&body=Klicka%20pa%20lanktexten%20for%20att%20besvara%20arende%3A%0Aprotocol%2F%2Fhostname%3Aport%2Fwebcert%2Fweb'+
            '%2Fuser%2Fbasic-certificate%2FtestIntyg%2FID112%2Fquestions');
    });

    describe('#vidarebefordra', function() {
        it('should setVidarebefordradState when forward state is changed with onVidarebefordrad', function() {

            ArendeProxy.setVidarebefordradState.and.callFake(function(a,b,c,fn) {
                fn({
                    fraga: {
                        vidarebefordrad: true
                    }
                });
            });

            $scope.onVidarebefordradChange();

            expect(ArendeProxy.setVidarebefordradState).toHaveBeenCalled();
            expect($scope.arendeListItem.arende.fraga.vidarebefordrad).toBeTruthy();
        });

        it('should show error message if request fails', function() {

            ArendeProxy.setVidarebefordradState.and.callFake(function(a,b,c,fn) {
                fn('');
            });

            $scope.onVidarebefordradChange();

            expect(DialogService.showErrorMessageDialog).toHaveBeenCalled();
        });
    });

});
