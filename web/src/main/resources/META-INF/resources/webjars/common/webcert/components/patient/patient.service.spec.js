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

describe('PatientService', function() {
    'use strict';

    var PatientService;
    var UserModel;
    var intyg;
    var intygProperties;

    beforeEach(angular.mock.module('common', function($provide) {
    }));

    beforeEach(angular.mock.inject([
        'common.PatientService', 'common.UserModel',
        function(_PatientService_, _UserModel_) {
            PatientService = _PatientService_;
            UserModel = _UserModel_;

            intyg = {
                typ: 'ts-bas',
                grundData: {
                    patient: {
                        fornamn: 'Kalle',
                        efternamn: 'Karlsson',
                        postort: null,
                        postadress: null,
                        postnummer: null
                    }
                }
            };

            intygProperties = {
                patientNameChangedInPU: false,
                patientAddressChangedInPU: false
            };
        }]));

    describe('#getPatientDataChanges', function() {

        it('should show NAME change (pu vs intyg) for TS INTYG (but not UTKAST)', function() {

            //////////////////////////////////////////////////////////////////////
            // INTEGRATION
            //////////////////////////////////////////////////////////////////////
            var integration = true;
            spyOn(UserModel, 'isDjupintegration').and.callFake(function(){
                return integration;
            });

            // INTYG
            intygProperties.patientNameChangedInPU = true;
            var patientChangeData = PatientService.getPatientDataChanges(true, intyg, intygProperties);

            expect(patientChangeData.changedNamePu).toBeTruthy();

            // UTKAST
            patientChangeData = PatientService.getPatientDataChanges(false, intyg, intygProperties);

            expect(patientChangeData.changedNamePu).toBeFalsy();

            //////////////////////////////////////////////////////////////////////
            // FRISTÅENDE
            //////////////////////////////////////////////////////////////////////
            integration = false;

            // INTYG
            intygProperties.patientNameChangedInPU = true;
            patientChangeData = PatientService.getPatientDataChanges(true, intyg, intygProperties);

            expect(patientChangeData.changedNamePu).toBeTruthy();

            // UTKAST
            patientChangeData = PatientService.getPatientDataChanges(false, intyg, intygProperties);

            expect(patientChangeData.changedNamePu).toBeFalsy();

        });

        it('should show ADDRESS change (pu vs intyg) for TS INTYG (but not UTKAST)', function() {

            //////////////////////////////////////////////////////////////////////
            // FRISTÅENDE
            //////////////////////////////////////////////////////////////////////
            spyOn(UserModel, 'isDjupintegration').and.callFake(function(){
                return false;
            });

            // INTYG
            intygProperties.patientAddressChangedInPU = true;
            var patientChangeData = PatientService.getPatientDataChanges(true, intyg, intygProperties);

            expect(patientChangeData.changedAddressPu).toBeTruthy();

            // UTKAST
            patientChangeData = PatientService.getPatientDataChanges(false, intyg, intygProperties);

            expect(patientChangeData.changedAddressPu).toBeFalsy();

        });

        it('integration - should show NAME change (pu vs js) for TS UTKAST', function() {

            spyOn(UserModel, 'getIntegrationParam').and.callFake(function(paramName){
                switch(paramName){
                case 'fornamn': return 'Pelle';
                case 'efternamn': return 'Svensson';
                }
            });

            spyOn(UserModel, 'isDjupintegration').and.callFake(function(){
                return true;
            });

            // INTYG
            var patientChangeData = PatientService.getPatientDataChanges(true, intyg, intygProperties);

            expect(patientChangeData.changedNamePuIntegration).toBeFalsy();
            expect(patientChangeData.changedNamePu).toBeFalsy();
            expect(patientChangeData.changedAddressPu).toBeFalsy();

            // UTKAST
            patientChangeData = PatientService.getPatientDataChanges(false, intyg, intygProperties);

            expect(patientChangeData.changedNamePuIntegration).toBeTruthy();
            expect(patientChangeData.changedNamePu).toBeFalsy();
            expect(patientChangeData.changedAddressPu).toBeFalsy();

        });

        it('integration - should show NAME change (pu vs js) for FK INTYG and UTKAST', function() {

            spyOn(UserModel, 'getIntegrationParam').and.callFake(function(paramName){
                switch(paramName){
                case 'fornamn': return 'Pelle';
                case 'efternamn': return 'Svensson';
                }
            });

            spyOn(UserModel, 'isDjupintegration').and.callFake(function(){
                return true;
            });

            // INTYG
            intyg.typ = 'fk7263';
            var patientChangeData = PatientService.getPatientDataChanges(true, intyg, intygProperties);

            expect(patientChangeData.changedNamePuIntegration).toBeTruthy();
            expect(patientChangeData.changedNamePu).toBeFalsy();
            expect(patientChangeData.changedAddressPu).toBeFalsy();

            // UTKAST
            patientChangeData = PatientService.getPatientDataChanges(false, intyg, intygProperties);

            expect(patientChangeData.changedNamePuIntegration).toBeTruthy();
            expect(patientChangeData.changedNamePu).toBeFalsy();
            expect(patientChangeData.changedAddressPu).toBeFalsy();

        });
    });

    describe('Testa detektering av ändrat namn eller adress vid djupintegration', function() {
        var UserModel;
        var intygModel = {
            grundData: {
                patient: {
                    fornamn:'Tolvan',
                    efternamn:'Tolvansson',
                    postadress: 'Blomstervägen 13',
                    postort: 'Småmåla',
                    postnummer: '123 45'
                }
            }
        };

        angular.mock.inject(['common.UserModel',
            function(_UserModel_) {
                UserModel = _UserModel_;
                UserModel.setUser({
                    parameters: {
                        fornamn: 'Tolvan',
                        efternamn: 'Tolvansson-changed'
                    }
                });
            }
        ]);

        it('hasChangedNameInIntegration true if efternamn has changed', function() {
            angular.mock.inject(['common.UserModel',
                function(_UserModel_) {
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            fornamn: 'Tolvan',
                            efternamn: 'Tolvansson-changed'
                        }
                    });
                }
            ]);
            expect(PatientService.hasChangedNameInIntegration(intygModel.grundData)).toBeTruthy();
        });
        it('hasChangedNameInIntegration true if fornamn has changed', function() {
            angular.mock.inject(['common.UserModel',
                function(_UserModel_) {
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            fornamn: 'Tolvan-changed',
                            efternamn: 'Tolvansson'
                        }
                    });
                }
            ]);
            expect(PatientService.hasChangedNameInIntegration(intygModel.grundData)).toBeTruthy();
        });
        it('hasNameChanged false if none has changed', function() {
            angular.mock.inject(['common.UserModel',
                function(_UserModel_) {
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            fornamn: 'Tolvan',
                            efternamn: 'Tolvansson'
                        }
                    });
                }
            ]);
            expect(PatientService.hasChangedNameInIntegration(intygModel.grundData)).toBeFalsy();
        });

        it('hasChangedAddressInIntegration false if none has changed', function() {
            angular.mock.inject(['common.UserModel',
                function(_UserModel_) {
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            postadress: 'Blomstervägen 13',
                            postort: 'Småmåla',
                            postnummer: '123 45'
                        }
                    });
                }
            ]);
            expect(PatientService.hasChangedAddressInIntegration(intygModel.grundData)).toBeFalsy();
        });
        it('hasChangedAddressInIntegration true if postadress has changed', function() {
            angular.mock.inject(['common.UserModel',
                function(_UserModel_) {
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            postadress: 'Blomstervägen 12',
                            postort: 'Småmåla',
                            postnummer: '123 45'
                        }
                    });
                }
            ]);
            expect(PatientService.hasChangedAddressInIntegration(intygModel.grundData)).toBeTruthy();
        });
        it('hasChangedAddressInIntegration true if postort has changed', function() {
            angular.mock.inject(['common.UserModel',
                function(_UserModel_) {
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            postadress: 'Blomstervägen 13',
                            postort: 'Stormåla',
                            postnummer: '123 45'
                        }
                    });
                }
            ]);
            expect(PatientService.hasChangedAddressInIntegration(intygModel.grundData)).toBeTruthy();
        });
        it('hasChangedAddressInIntegration true if postnummer has changed', function() {
            angular.mock.inject(['common.UserModel',
                function(_UserModel_) {
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            postadress: 'Blomstervägen 13',
                            postort: 'Småmåla',
                            postnummer: '54321'
                        }
                    });
                }
            ]);
            expect(PatientService.hasChangedAddressInIntegration(intygModel.grundData)).toBeTruthy();
        });
    });

    describe('Testa detektering av avsaknad adress i integrationsparametrar i ts-intyg för UTKAST', function() {
        var UserModel;
        var intygModel = {
            typ: 'ts-bas',
            grundData: {
                patient: {
                    fornamn:'Tolvan',
                    efternamn:'Tolvansson',
                    postadress: 'Blomstervägen 13',
                    postort: 'Småmåla',
                    postnummer: '123 45'
                }
            }
        };

        it('isMissingRequiredAddressIntegrationParameter true if some parts of address is missing', function() {
            angular.mock.inject(['common.UserModel',
                function(_UserModel_) {
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            postadress: 'Blomstervägen 13',
                            postnummer: '54321'
                        }
                    });
                }
            ]);
            spyOn(UserModel, 'isDjupintegration').and.callFake(function(){
                return true;
            });
            expect(PatientService.isMissingRequiredAddressIntegrationParameter(false, intygModel)).toBeTruthy();
        });

        it('isMissingRequiredAddressIntegrationParameter false if no parts of address are missing', function() {
            angular.mock.inject(['common.UserModel',
                function(_UserModel_) {
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            postadress: 'Blomstervägen 13',
                            postort: 'Småmåla',
                            postnummer: '54321'
                        }
                    });
                }
            ]);
            spyOn(UserModel, 'isDjupintegration').and.callFake(function(){
                return true;
            });
            expect(PatientService.isMissingRequiredAddressIntegrationParameter(false, intygModel)).toBeFalsy();
        });

        it('isMissingRequiredAddressIntegrationParameter false if not in djupintegration', function() {
            angular.mock.inject(['common.UserModel',
                function(_UserModel_) {
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            postadress: 'Blomstervägen 13',
                            postnummer: '54321'
                        }
                    });
                }
            ]);
            spyOn(UserModel, 'isDjupintegration').and.callFake(function(){
                return false;
            });
            expect(PatientService.isMissingRequiredAddressIntegrationParameter(false, intygModel)).toBeFalsy();
        });

        it('isMissingRequiredAddressIntegrationParameter false for INTYG', function() {
            angular.mock.inject(['common.UserModel',
                function(_UserModel_) {
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            postadress: 'Blomstervägen 13',
                            postnummer: '54321'
                        }
                    });
                }
            ]);
            spyOn(UserModel, 'isDjupintegration').and.callFake(function(){
                return true;
            });
            expect(PatientService.isMissingRequiredAddressIntegrationParameter(true, intygModel)).toBeFalsy();
        });

    });
});
