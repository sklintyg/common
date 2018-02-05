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

describe('IntygHeaderService', function() {
    'use strict';

    var $rootScope;
    var IntygHeaderService;
    var IntygHeaderViewState;
    var CommonIntygViewState;
    var UserModel;
    var UtkastProxy;
    var testIntygViewState;

    beforeEach(angular.mock.module('common'));
    beforeEach(angular.mock.module(function($provide) {
    }));

    beforeEach(angular.mock.inject([
        '$rootScope', 'common.IntygHeaderService', 'common.IntygHeaderViewState', 'common.IntygViewStateService', 'common.UserModel', 'common.UtkastProxy',/*'$controller', '$state', '$compile', 'common.featureService', */
        function(_$rootScope_, _IntygHeaderService_, _IntygHeaderViewState_, _IntygViewStateService_, _UserModel_, _UtkastProxy_/*_$controller_, _$state_, _$compile_, _featureService_*/) {
            $rootScope = _$rootScope_;
            IntygHeaderService = _IntygHeaderService_;
            IntygHeaderViewState = _IntygHeaderViewState_;
            CommonIntygViewState = _IntygViewStateService_;
            UserModel = _UserModel_;
            UtkastProxy = _UtkastProxy_;
            UserModel.setUser({parameters:{}});
        }]));

    describe('header show button logic', function() {

        beforeEach(function() {
            UserModel.setUser({parameters:{}});
            testIntygViewState = {
                intygModel: {
                    grundData: {
                        patient: {
                            personId: 'test'
                        }
                    }
                }
            }
            IntygHeaderViewState.setIntygViewState(testIntygViewState, 'db');
        });

        describe('skapa <intygstyp> button', function() {
            it('should be shown if intyg type is db and copying is allowed', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                UserModel.user = {};

                expect(IntygHeaderService.showCreateFromTemplate()).toBeTruthy();
            });

            it('should not be shown if makulerat', function() {
                CommonIntygViewState.intygProperties.isRevoked = true;
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();
            });

            it('should be enabled if no previous intyg exists', function() {
                UserModel.user = {};

                spyOn(UtkastProxy, 'getPrevious').and.callFake(function(patient, onSuccess) {
                    onSuccess({});
                });

                $rootScope.$broadcast('intyg.loaded', {});

                expect(IntygHeaderService.enableCreateFromTemplate()).toBeTruthy();
            });

            it('should not be enabled if previous intyg exists and feature is enabled', function() {
                UserModel.user = {};

                IntygHeaderViewState.warningForCreateTemplate = null;

                spyOn(UtkastProxy, 'getPrevious').and.callFake(function(patient, onSuccess) {
                    onSuccess({
                        intyg: {
                            doi: true
                        }
                    });
                });

                IntygHeaderService.updatePreviousIntygUtkast(IntygHeaderViewState.intygViewState.intygModel);

                expect(IntygHeaderService.enableCreateFromTemplate()).toBeFalsy();
                expect(IntygHeaderViewState.warningForCreateTemplate).not.toBeNull();
            });

            it('should not be shown if intyg type is fk, ts or doi', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;
                UserModel.user = {};

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'doi');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'ts-bas');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'ts-diabetes');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'fk7263');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'lisjp');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'luse');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'luae_fs');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'luae_na');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();
            });

        });
    });
});
