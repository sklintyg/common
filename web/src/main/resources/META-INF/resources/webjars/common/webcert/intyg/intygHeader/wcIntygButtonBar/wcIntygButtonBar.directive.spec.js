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

describe('wcIntygButtonBar', function() {
    'use strict';

    var $rootScope;
    var $scope;
    var $controller;
    var $state;
    var $compile;
    var UserModel;
    var element;
    var CommonIntygViewState;

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));
    beforeEach(angular.mock.module(function($provide) {
    }));

    beforeEach(angular.mock.inject([
        '$rootScope', '$controller', '$state', '$compile', 'common.UserModel', 'common.featureService', 'common.UtkastProxy', 'common.IntygViewStateService',
        function(_$rootScope_, _$controller_, _$state_, _$compile_, _UserModel_, _featureService_, _UtkastProxy_, _IntygViewStateService_) {
        $rootScope = _$rootScope_;
        $scope = _$rootScope_.$new();
        $controller = _$controller_;
        $state = _$state_;
        UserModel = _UserModel_;
        CommonIntygViewState = _IntygViewStateService_;
        $compile = _$compile_;

        UserModel.setUser({parameters:{}});

        // Instantiate directive.
        // gotcha: Controller and link functions will execute.
        element = $compile('<wc-intyg-button-bar view-state="viewState"></wc-intyg-button-bar>')($scope);
        $rootScope.$digest();

        // Grab controller instance
        $controller = element.controller('wcIntygButtonBar');

        // Grab scope. Depends on type of scope.
        // See angular.element documentation.
        $scope = element.isolateScope();
    }]));

    describe('header show button logic', function() {

        beforeEach(function() {

            $state.current.data = { intygType: 'lisjp'};
            $scope.utskrift = false;
            $scope.arbetsgivarUtskrift = false;
            UserModel.setUser({parameters:{}});
        });

        describe('skicka button', function() {
            it('should show skicka button if intyg is not sent, revoked and patient is alive', function() {
                CommonIntygViewState.isIntygOnSendQueue = false;
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isSent = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;
                expect($scope.showSkickaButton()).toBeTruthy();
            });
            it('should not show skicka button if intyg is sent, not revoked and patient is alive', function() {
                CommonIntygViewState.isIntygOnSendQueue = true;
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isSent = true;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;
                expect($scope.showSkickaButton()).toBeFalsy();

                CommonIntygViewState.isIntygOnSendQueue = true;
                CommonIntygViewState.intygProperties.isSent = false;
                expect($scope.showSkickaButton()).toBeFalsy();

                CommonIntygViewState.isIntygOnSendQueue = false;
                CommonIntygViewState.intygProperties.isSent = true;
                expect($scope.showSkickaButton()).toBeFalsy();
            });
        });

        describe('print button', function() {
            it('should be shown if allowed as feature and employer button is not shown', function() {
                $scope.utskrift = true;
                $scope.arbetsgivarUtskrift = false;
                expect($scope.showPrintBtn()).toBeTruthy();
            });
            it('should not be shown if allowed as feature and employer button is shown', function() {
                $scope.utskrift = true;
                $scope.arbetsgivarUtskrift = true;
                CommonIntygViewState.intygProperties.isRevoked = false;
                expect($scope.showPrintBtn()).toBeFalsy();
            });
            it('should be shown if allowed as feature and intyg is revoked', function() {
                $scope.utskrift = true;
                $scope.arbetsgivarUtskrift = true;
                CommonIntygViewState.intygProperties.isRevoked = true;
                expect($scope.showPrintBtn()).toBeTruthy();
            });
            it('should not be shown if not allowed as feature', function() {
                $scope.utskrift = false;
                expect($scope.showPrintBtn()).toBeFalsy();
            });
        });

        describe('employer print button', function() {
            it('should be shown if allowed as feature', function() {
                $scope.arbetsgivarUtskrift = true;
                expect($scope.showEmployerPrintBtn()).toBeTruthy();
            });
            it('should not be shown if not allowed as feature', function() {
                $scope.arbetsgivarUtskrift = false;
                expect($scope.showEmployerPrintBtn()).toBeFalsy();
            });
            it('should not be shown if allowed as feature but intyg is revoked', function() {
                $scope.arbetsgivarUtskrift = true;
                CommonIntygViewState.intygProperties.isRevoked = true;
                expect($scope.showEmployerPrintBtn()).toBeFalsy();
            });
        });

        describe('fornya button', function() {
            it('should be shown if intyg type is fk7263 and copying is allowed', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;
                UserModel.user = {};

                $scope.intygstyp = 'fk7263';
                expect($scope.showFornyaButton()).toBeTruthy();

                $scope.intygstyp = 'lisjp';
                expect($scope.showFornyaButton()).toBeTruthy();

                $scope.intygstyp = 'luse';
                expect($scope.showFornyaButton()).toBeTruthy();

                $scope.intygstyp = 'luae_fs';
                expect($scope.showFornyaButton()).toBeTruthy();

                $scope.intygstyp = 'luae_na';
                expect($scope.showFornyaButton()).toBeTruthy();
            });

            it('should not be shown if intyg type is ts', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;
                UserModel.user = {};

                $scope.intygstyp = 'ts-bas';
                $scope.fornya = false;
                expect($scope.showFornyaButton()).toBe(false);

                $scope.intygstyp = 'ts-diabetes';
                $scope.fornya = false;
                expect($scope.showFornyaButton()).toBe(false);
            });

            it('should not be shown if makulerat or patient deceased', function() {
                CommonIntygViewState.intygProperties.isRevoked = true;
                expect($scope.showFornyaButton()).toBeFalsy();

                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = true;
                expect($scope.showFornyaButton()).toBeFalsy();

            });
            it('should not be shown if unit is inactive', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;
                UserModel.user = {parameters: {inactiveUnit: true}};

                $scope.intygstyp = 'fk7263';
                expect($scope.showFornyaButton()).toBeFalsy();
            });

        });

    });
});
