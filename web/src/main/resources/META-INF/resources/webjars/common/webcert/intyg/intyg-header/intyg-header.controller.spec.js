/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

describe('IntygHeaderCtrl', function() {
    'use strict';

    var $scope;
    var $controller;
    var $state;

    beforeEach(function() {

        module('common', function($provide) {
            $provide.value('common.ObjectHelper', jasmine.createSpyObj('common.ObjectHelper', ['isEmpty']));
        });

        inject(function($rootScope, _$controller_, _$state_) {
            $scope = $rootScope.$new();
            $controller = _$controller_;
            $state = _$state_;
        });
    });

    describe('header show button logic', function() {

        beforeEach(function() {

            $state.current.data = { intygType: 'lisjp'};
            $scope.utskrift = false;
            $scope.arbetsgivarUtskrift = false;
            $scope.viewState = {
                common: {
                    isIntygOnSendQueue: false,
                    isIntygOnRevokeQueue: false,
                    intygProperties:{
                        isRevoked: false,
                        isPatientDeceased: false,
                        isSent: false
                    },
                    common: {
                        sekretessmarkering: false
                    }
                }
            };

            $controller('common.IntygHeader', {$scope: $scope});
        });

        describe('skicka button', function() {
            it('should show skicka button if intyg is not sent, revoked and patient is alive', function() {
                $scope.viewState.common.isIntygOnSendQueue = false;
                $scope.viewState.common.isIntygOnRevokeQueue = false;
                $scope.viewState.common.intygProperties.isRevoked = false;
                $scope.viewState.common.intygProperties.isSent = false;
                $scope.viewState.common.intygProperties.isPatientDeceased = false;
                expect($scope.showSkickaButton()).toBeTruthy();
            });
            it('should not show skicka button if intyg is sent, not revoked and patient is alive', function() {
                $scope.viewState.common.isIntygOnSendQueue = true;
                $scope.viewState.common.isIntygOnRevokeQueue = false;
                $scope.viewState.common.intygProperties.isRevoked = false;
                $scope.viewState.common.intygProperties.isSent = true;
                $scope.viewState.common.intygProperties.isPatientDeceased = false;
                expect($scope.showSkickaButton()).toBeFalsy();

                $scope.viewState.common.isIntygOnSendQueue = true;
                $scope.viewState.common.intygProperties.isSent = false;
                expect($scope.showSkickaButton()).toBeFalsy();

                $scope.viewState.common.isIntygOnSendQueue = false;
                $scope.viewState.common.intygProperties.isSent = true;
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
                expect($scope.showPrintBtn()).toBeFalsy();
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
        });

        describe('kopiera button', function() {
            it('should be shown if not makulerat, sekretessmarkerat or patient deceased', function() {
                $scope.viewState.common.common.sekretessmarkering = false;
                $scope.viewState.common.isIntygOnRevokeQueue = false;
                $scope.viewState.common.intygProperties.isRevoked = false;
                $scope.viewState.common.intygProperties.isPatientDeceased = false;
                expect($scope.showKopieraButton()).toBeTruthy();
            });
            it('should not be shown if makulerat, sekretessmarkerat or patient deceased', function() {
                $scope.viewState.common.common.sekretessmarkering = true;
                $scope.viewState.common.isIntygOnRevokeQueue = false;
                $scope.viewState.common.intygProperties.isRevoked = false;
                $scope.viewState.common.intygProperties.isPatientDeceased = false;
                expect($scope.showKopieraButton()).toBeFalsy();

                $scope.viewState.common.common.sekretessmarkering = false;
                $scope.viewState.common.intygProperties.isRevoked = true;
                expect($scope.showKopieraButton()).toBeFalsy();

                $scope.viewState.common.common.sekretessmarkering = false;
                $scope.viewState.common.intygProperties.isRevoked = false;
                $scope.viewState.common.intygProperties.isPatientDeceased = true;
                expect($scope.showKopieraButton()).toBeFalsy();
            });
        });

        describe('fornya button', function() {
            it('should be shown if intyg type is fk7263 and copying is allowed', function() {
                $scope.viewState.common.common.sekretessmarkering = false;
                $scope.viewState.common.isIntygOnRevokeQueue = false;
                $scope.viewState.common.intygProperties.isRevoked = false;
                $scope.viewState.common.intygProperties.isPatientDeceased = false;

                $scope.intygstyp = 'fk7263';
                expect($scope.showFornyaButton()).toBeTruthy();
            });
            it('should not be shown if makulerat, sekretessmarkerat or patient deceased', function() {
                $scope.intygstyp = 'luae_na';
                expect($scope.showFornyaButton()).toBeFalsy();
            });
        });

        describe('makulerat button', function() {
            it('should be shown if intyg is not already makulerat or on queue to be makulerat', function() {
                $scope.viewState.common.isIntygOnRevokeQueue = false;
                $scope.viewState.common.intygProperties.isRevoked = false;
                expect($scope.makuleratIntyg()).toBeTruthy();
            });
            it('should not be shown if intyg is already makulerat or on queue to be makulerat', function() {
                $scope.viewState.common.isIntygOnRevokeQueue = true;
                $scope.viewState.common.intygProperties.isRevoked = false;
                expect($scope.makuleratIntyg()).toBeFalsy();

                $scope.viewState.common.isIntygOnRevokeQueue = false;
                $scope.viewState.common.intygProperties.isRevoked = true;
                expect($scope.makuleratIntyg()).toBeFalsy();

                $scope.viewState.common.isIntygOnRevokeQueue = true;
                $scope.viewState.common.intygProperties.isRevoked = true;
                expect($scope.makuleratIntyg()).toBeFalsy();
            });
        });
    });
});
