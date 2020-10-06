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
        $provide.value('common.moduleService', {getModule: function(){return {
            defaultRecipient: 'FKASSA'
        };}, getModuleName: function() { return 'Intygsnamn'; }});
    }));

    beforeEach(angular.mock.inject([
        '$rootScope', 'common.IntygHeaderService', 'common.IntygHeaderViewState', 'common.IntygViewStateService',
        'common.UserModel', 'common.UtkastProxy',
        function(_$rootScope_, _IntygHeaderService_, _IntygHeaderViewState_, _IntygViewStateService_,
            _UserModel_, _UtkastProxy_) {
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
            UserModel.setUser({
                features: {
                    'HANTERA_INTYGSUTKAST': {
                        'name': 'HANTERA_INTYGSUTKAST',
                        'global': true,
                        'intygstyper': [ 'db', 'doi', 'ag7804' ]
                    }
                },
                origin: 'NORMAL',
                parameters: {}
            });
            testIntygViewState = {
                intygModel: {
                    grundData: {
                        patient: {
                            personId: 'test'
                        }
                    }
                }
            };
            IntygHeaderViewState.setIntygViewState(testIntygViewState, 'db');
        });

        describe('skapa <intygstyp> button', function() {
            it('should be shown if intyg type is db and copying is allowed', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;

                expect(IntygHeaderService.showCreateFromTemplate()).toBeTruthy();
            });

            it('should not be shown if makulerat', function() {
                CommonIntygViewState.intygProperties.isRevoked = true;
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();
            });

            it('should be enabled if no previous intyg exists', function() {

                spyOn(UtkastProxy, 'getPrevious').and.callFake(function(patient, undefined, onSuccess) {
                    onSuccess({});
                });

                $rootScope.$broadcast('intyg.loaded', {});

                expect(IntygHeaderService.enableCreateFromTemplate()).toBeTruthy();
            });

            it('should not be enabled if previous intyg exists and feature is enabled', function() {

                IntygHeaderViewState.warningForCreateTemplate = null;

                spyOn(UtkastProxy, 'getPrevious').and.callFake(function(patient, undefined, onSuccess) {
                    onSuccess({
                        intyg: {
                            doi: {
                                sameVardgivare:true
                            }
                        }
                    });
                });

                IntygHeaderService.updatePreviousIntygUtkast(IntygHeaderViewState.intygViewState.intygModel);

                expect(IntygHeaderService.enableCreateFromTemplate()).toBeFalsy();
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();
                expect(IntygHeaderService.showGotoCreatedFromTemplate()).toBeTruthy();
                expect(IntygHeaderViewState.warningForCreateTemplate).not.toBeNull();
            });

            it('should not be shown if intyg type does not support create from template', function() {
                CommonIntygViewState.isIntygOnRevokeQueue = false;
                CommonIntygViewState.intygProperties.isRevoked = false;

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'doi');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'ts-bas');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'ts-diabetes');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'fk7263');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeFalsy();

                IntygHeaderViewState.setIntygViewState(testIntygViewState, 'lisjp');
                expect(IntygHeaderService.showCreateFromTemplate()).toBeTruthy();

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
