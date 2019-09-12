/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
    }));

    beforeEach(angular.mock.module('htmlTemplates'));

    beforeEach(angular.mock.inject(['$controller', '$compile', '$rootScope', '$window',
        'common.ArendeListViewStateService', 'common.UserModel',
        'common.ArendeProxy', 'common.dialogService',
        function($controller, $compile, _$rootScope_, _$window_,
            _ArendeListViewState_, _UserModel_, _ArendeProxy_, _DialogService_) {
            $rootScope = _$rootScope_;
            $window = _$window_;
            ArendeListViewState = _ArendeListViewState_;
            ArendeProxy = _ArendeProxy_;

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
            ArendeListViewState.intyg.grundData = { skapadAv:{vardenhet:{enhetsid:'testenhetsid'}}};
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
            'mailto:?subject=Ett%20%C3%A4rende%20ska%20hanteras%20i%20Webcert%20p%C3%A5%20enhet%20VE%20f%C3%B6r%20v%C3%A5rdgivare%20VG'+
            '&body=Klicka%20p%C3%A5%20l%C3%A4nken%20f%C3%B6r%20att%20hantera%20%C3%A4rendet%3A%0Aprotocol%2F%2Fhostname%3Aport%2Fwebcert%2Fweb'+
            '%2Fuser%2Fbasic-certificate%2Flisjp%2FtestIntygId%2Fquestions%0A%0AOBS!%20S%C3%A4tt%20i%20ditt%20SITHS-kort%20innan%20du'+
            '%20klickar%20p%C3%A5%20l%C3%A4nken.');
    });

    describe('#vidarebefordra', function() {
        it('should setVidarebefordradState when forward state is changed with onVidarebefordrad', function() {

            ArendeProxy.setVidarebefordradState.and.callFake(function(a,b,fn) {
                fn([{
                    fraga: {
                        vidarebefordrad: true,
                        status: 'CLOSED'
                    }
                }]);
            });

            $scope.onVidarebefordradChange();

            expect(ArendeProxy.setVidarebefordradState).toHaveBeenCalled();
        });

        it('should show error message if request fails', function() {

            ArendeProxy.setVidarebefordradState.and.callFake(function(a,b,fn) {
                fn('');
            });

            $scope.onVidarebefordradChange();

            expect(DialogService.showErrorMessageDialog).toHaveBeenCalled();
        });
    });

});
