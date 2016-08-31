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

describe('arendePanelSvar', function() {
    'use strict';

    var element;
    var $httpBackend;
    var $scope;
    var $rootScope;
    var ArendeProxy;
    var deferred;

    // Load the webcert module and mock away everything that is not necessary.
    beforeEach(angular.mock.module('common', function($provide) {
        $provide.value('common.dialogService', {});
        $provide.value('common.IntygService', { isSentToTarget: function() {} });
        $provide.value('common.User', { getVardenhetFilterList: function() { return []; } });
        $provide.value('common.statService', {});
        $provide.value('common.ObjectHelper', jasmine.createSpyObj('common.ObjectHelper',
            [ 'isDefined']));
        $provide.value('common.IntygCopyRequestModel', jasmine.createSpyObj('common.IntygCopyRequestModel',
            [ 'build']));

        ArendeProxy = jasmine.createSpyObj('common.ArendeProxy', ['saveAnswer', 'closeAllAsHandled']);
        $provide.value('common.ArendeProxy', ArendeProxy);
    }));

    beforeEach(angular.mock.module('htmlTemplates'));

    beforeEach(angular.mock.inject(['$controller', '$compile', '$rootScope', '$q', '$httpBackend',
        function($controller, $compile, _$rootScope_, _$q_, _$httpBackend_) {
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();

            $httpBackend = _$httpBackend_;
            $scope.qa = {
                arende:{
                    fraga:{},
                    svar:{}
                }
            };
            $scope.parentViewState = {
                intygProperties: {},
                intyg: { grundData:{skapadAv:{vardenhet:{}}}}
            };
            element = $compile('<div arende-panel-svar arende-list-item="qa" panel-id="handled" parent-view-state="parentViewState"></div>')($scope);
            $scope.$digest();
            $scope = element.isolateScope();
        }]));

    describe('#send answer', function() {
        it('should sendAnswer when "svara" is clicked', function() {

            var fragaSvar = {
                internReferens: 'intyg-1',
                svarsText: 'Att svara eller inte svara. Det är frågan.'
            };

            $scope.sendAnswer(fragaSvar);

            expect(ArendeProxy.saveAnswer).toHaveBeenCalled();
        });

    });

    describe('#updateAnsweredAsHandled', function() {
        it('has no UnhandledQas so shouldnt update qas', function() {
            // ----- arrange
            var qaList = [];

            // ----- act
            $scope.updateAnsweredAsHandled(deferred, qaList);

            // ----- assert
            expect(ArendeProxy.closeAllAsHandled).not.toHaveBeenCalled();
        });

        it('has UnhandledQas so should update qas', function() {
            // ----- arrange
            var qaAnswered = {};
            var qaList = [qaAnswered];

            // ----- act
            $scope.updateAnsweredAsHandled(deferred, qaList);

            // ----- assert
            expect(ArendeProxy.closeAllAsHandled).toHaveBeenCalled();
        });

    });
});
