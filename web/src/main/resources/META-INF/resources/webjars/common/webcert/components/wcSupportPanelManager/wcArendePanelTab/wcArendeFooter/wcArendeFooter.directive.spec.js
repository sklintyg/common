/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

describe('wArendeFooter', function() {
    'use strict';

    var $scope;
    var $rootScope;
    var $window;
    var authorityService;
    var ArendeListViewState;
    var UserModel;
    var ArendeProxy;
    var DialogService;

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.dialogService', jasmine.createSpyObj('common.dialogService',
            [ 'showErrorMessageDialog']));

        $provide.value('common.ArendeProxy', jasmine.createSpyObj('common.ArendeProxy', ['setVidarebefordradState']));
        $provide.value('$window', {location:{
            protocol:'protocol',
            hostname:'hostname',
            port:'port'
        }});
        $provide.value('common.authorityService', jasmine.createSpyObj('common.authorityService', ['isAuthorityActive']));
    }));

    beforeEach(angular.mock.module('htmlTemplates'));

    beforeEach(angular.mock.inject(['$controller', '$compile', '$rootScope', '$window',
        'common.authorityService', 'common.ArendeListViewStateService', 'common.UserModel',
        'common.ArendeProxy', 'common.dialogService',
        function($controller, $compile, _$rootScope_, _$window_,
            _authorityService_, _ArendeListViewState_, _UserModel_, _ArendeProxy_, _DialogService_) {
            $rootScope = _$rootScope_;
            $window = _$window_;
            ArendeListViewState = _ArendeListViewState_;
            ArendeProxy = _ArendeProxy_;
            authorityService = _authorityService_;
            authorityService.isAuthorityActive.and.returnValue(true);
            UserModel = _UserModel_;
            DialogService = _DialogService_;

            $scope = $rootScope.$new();
            $scope.arendeList = [{
                arende:{
                    fraga:{
                        internReferens: 'ID111',
                        intygId:'ID112',
                        enhetsnamn:'enhet1',
                        vardgivarnamn:'vardgivare1'
                    },
                    svar:{}
                }
            }];

            UserModel.user = {
                valdVardenhet:{ namn: 'VE' },
                valdVardgivare:{ namn: 'VG' }
            };

            ArendeListViewState.intyg.id = 'testIntygId';
            ArendeListViewState.intygProperties.type = 'lisjp';

            var tpl = angular.element(
                '<wc-arende-footer arende-list="arendeList"></wc-arende-footer>'
            );
            var element = $compile(tpl)($scope);
            $scope.$digest();
            $scope = element.isolateScope();
        }]));


    it('should open mail dialog', function() {
        $scope.openMailDialog();

        expect($window.location).toEqual(
            'mailto:?subject=Ett%20arende%20ska%20besvaras%20i%20Webcert%20pa%20enhet%20VE%20for%20vardgivare%20VG'+
            '&body=Klicka%20pa%20lanktexten%20for%20att%20besvara%20arende%3A%0Aprotocol%2F%2Fhostname%3Aport%2Fwebcert%2Fweb'+
            '%2Fuser%2Fbasic-certificate%2Flisjp%2FtestIntygId%2Fquestions%0A%0AOBS!%20Satt%20i%20ditt%20SITHS-kort%20innan%20du'+
            '%20klickar%20pa%20lanken.');
    });

    describe('#vidarebefordra', function() {
        it('should setVidarebefordradState when forward state is changed with onVidarebefordrad', function() {

            ArendeProxy.setVidarebefordradState.and.callFake(function(a,fn) {
                fn({
                    fraga: {
                        vidarebefordrad: true
                    }
                });
            });

            $scope.onVidarebefordradChange();

            expect(ArendeProxy.setVidarebefordradState).toHaveBeenCalled();
        });

        it('should show error message if request fails', function() {

            ArendeProxy.setVidarebefordradState.and.callFake(function(a,fn) {
                fn('');
            });

            $scope.onVidarebefordradChange();

            expect(DialogService.showErrorMessageDialog).toHaveBeenCalled();
        });
    });

});
