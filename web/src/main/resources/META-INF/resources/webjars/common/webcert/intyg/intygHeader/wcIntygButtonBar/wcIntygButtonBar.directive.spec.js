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
    var IntygHeaderViewStateService;
    var ResourceLinkService;
    var userTemplate = {

        'features': {

            'FORNYA_INTYG': {
                'global': true,
                'intygstyper': ['luse', 'lisjp', 'luae_na', 'luae_fs']
            },
            'UTSKRIFT': {
                'global': true,
                'intygstyper': [ 'fk7263', 'ts-bas', 'ts-diabetes', 'luse', 'lisjp', 'luae_na', 'luae_fs', 'db', 'doi' ]
            },
            'MAKULERA_INTYG': {
                'global': true,
                'intygstyper': [ 'fk7263', 'ts-bas', 'ts-diabetes', 'luse', 'lisjp', 'luae_na', 'luae_fs', 'db', 'doi' ]
            },
            'HANTERA_INTYGSUTKAST_AVLIDEN': {
                'global': true,
                'intygstyper': [ 'lisjp' ]
            }

        },

        'authorities': {

            'FORNYA_INTYG': {
                'intygstyper': ['lisjp', 'luse', 'luae_na', 'luae_fs'],
                'requestOrigins': [{
                    'name': 'NORMAL',
                    'intygstyper': ['lisjp', 'luse', 'luae_na', 'luae_fs']
                }, {
                    'name': 'DJUPINTEGRATION',
                    'intygstyper': []
                }]
            },
            'MAKULERA_INTYG': {
                'intygstyper': ['lisjp', 'luse', 'luae_na', 'luae_fs'],
                'requestOrigins': [{
                    'name': 'NORMAL',
                    'intygstyper': ['lisjp', 'luse', 'luae_na', 'luae_fs']
                }]
            },
            'ERSATTA_INTYG': {
                'intygstyper': ['lisjp', 'luse', 'luae_na', 'luae_fs'],
                'requestOrigins': [{
                    'name': 'NORMAL',
                    'intygstyper': ['lisjp', 'luse', 'luae_na', 'luae_fs']
                }]
            }
        },
        'origin': 'NORMAL'
    };

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));
    beforeEach(angular.mock.module(function($provide) {
        $provide.value('common.moduleService', {getModule: function(){return {
            defaultRecipient: 'FKASSA'
        };}, getModuleName: function() { return 'Intygsnamn'; }});
        $provide.value('common.ResourceLinkService', {
            isLinkTypeExists: function(links, type) {
                return true;
            }
        });
    }));

    beforeEach(angular.mock.inject([
        '$rootScope', '$controller', '$state', '$compile', 'common.UserModel', 'common.featureService', 'common.UtkastProxy',
        'common.IntygViewStateService', 'common.IntygHeaderViewState', 'common.ResourceLinkService',
        function(_$rootScope_, _$controller_, _$state_, _$compile_, _UserModel_, _featureService_, _UtkastProxy_, _IntygViewStateService_, _IntygHeaderViewStateService_, _ResourceLinkService_) {
        $rootScope = _$rootScope_;
        $scope = _$rootScope_.$new();
        $controller = _$controller_;
        $state = _$state_;
        UserModel = _UserModel_;
        CommonIntygViewState = _IntygViewStateService_;
        $compile = _$compile_;
        IntygHeaderViewStateService = _IntygHeaderViewStateService_;
        ResourceLinkService = _ResourceLinkService_;
        $scope.viewState = {};

        // Fake that intyg is already loaded so following tests only test the other requirements for buttons to show
        IntygHeaderViewStateService.intygLoaded =  true;

        UserModel.setUser(userTemplate);

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
        });

        describe('skicka button', function() {
            it('should show skicka button if intyg is not sent, revoked and patient is alive', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isSent = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;
                expect($scope.showSkickaButton()).toBeTruthy();
            });
            it('should not show skicka button if intyg is sent, not revoked and patient is alive', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isSent = true;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;
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
            it('should not be shown if allowed as feature and intyg is revoked', function() {
                $scope.utskrift = true;
                $scope.arbetsgivarUtskrift = true;
                CommonIntygViewState.intygProperties.isRevoked = true;
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
            it('should be shown if allowed by auth', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;

                $scope.intygType = 'lisjp';
                expect($scope.showFornyaButton()).toBe(true);

                $scope.intygType = 'luse';
                expect($scope.showFornyaButton()).toBe(true);

                $scope.intygType = 'luae_fs';
                expect($scope.showFornyaButton()).toBe(true);

                $scope.intygType = 'luae_na';
                expect($scope.showFornyaButton()).toBe(true);
            });


            it('should not be shown if makulerat or patient deceased', function() {
                $scope.intygType = 'lisjp';
                CommonIntygViewState.intygProperties.isRevoked = true;
                expect($scope.showFornyaButton()).toBe(false);

                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;
                expect($scope.showFornyaButton()).toBe(true);

            });
        });

        describe('ersatt button', function() {
            it('should be shown for living patients', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;
                UserModel.user.parameters = {inactiveUnit : false };

                $scope.intygType = 'lisjp';
                expect($scope.showErsattButton()).toBe(true);
            });

            it('should be shown for dead patients ONLY if allowed for intygstype feature ', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = true;
                UserModel.user.parameters = {inactiveUnit : false };

                $scope.intygType = 'lisjp';
                expect($scope.showErsattButton()).toBe(true);
            });
        });

        describe('makulera button', function() {
            it('should be shown if allowed by auth', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;

                $scope.intygType = 'lisjp';
                expect($scope.showMakuleraButton()).toBe(true);

            });

            it('should not be shown if already revoked', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                CommonIntygViewState.intygProperties.isPatientDeceased = false;

                $scope.intygType = 'lisjp';
                expect($scope.showMakuleraButton()).toBe(true);

                CommonIntygViewState.intygProperties.isRevoked = true;
                expect($scope.showMakuleraButton()).toBe(false);

            });



        });

    });
});
