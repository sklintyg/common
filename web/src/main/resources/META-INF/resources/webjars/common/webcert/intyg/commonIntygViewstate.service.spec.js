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

describe('commonIntygViewstateService', function() {
    'use strict';

    describe('Testa detektering av ändrat namn eller adress vid djupintegration', function() {
        var commonIntygViewstateService;
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

        angular.mock.inject(['common.IntygViewStateService', 'common.UserModel',
            function(_commonIntygViewstateService_, _UserModel_) {
                commonIntygViewstateService = _commonIntygViewstateService_;
                UserModel = _UserModel_;
                UserModel.setUser({
                    parameters: {
                        fornamn: 'Tolvan',
                        efternamn: 'Tolvansson-changed'
                    }
                });
            }
        ]);

        it('hasNameChanged true if efternamn has changed', function() {
            angular.mock.inject(['common.IntygViewStateService', 'common.UserModel',
                function(_commonIntygViewstateService_, _UserModel_) {
                    commonIntygViewstateService = _commonIntygViewstateService_;
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            fornamn: 'Tolvan',
                            efternamn: 'Tolvansson-changed'
                        }
                    });
                }
            ]);
            expect(commonIntygViewstateService.patient.hasChangedName(intygModel)).toBeTruthy();
        });
        it('hasNameChanged true if fornamn has changed', function() {
            angular.mock.inject(['common.IntygViewStateService', 'common.UserModel',
                function(_commonIntygViewstateService_, _UserModel_) {
                    commonIntygViewstateService = _commonIntygViewstateService_;
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            fornamn: 'Tolvan-changed',
                            efternamn: 'Tolvansson'
                        }
                    });
                }
            ]);
            expect(commonIntygViewstateService.patient.hasChangedName(intygModel)).toBeTruthy();
        });
        it('hasNameChanged false if none has changed', function() {
            angular.mock.inject(['common.IntygViewStateService', 'common.UserModel',
                function(_commonIntygViewstateService_, _UserModel_) {
                    commonIntygViewstateService = _commonIntygViewstateService_;
                    UserModel = _UserModel_;
                    UserModel.setUser({
                        parameters: {
                            fornamn: 'Tolvan',
                            efternamn: 'Tolvansson'
                        }
                    });
                }
            ]);
            expect(commonIntygViewstateService.patient.hasChangedName(intygModel)).toBeFalsy();
        });

        it('hasAddressChanged false if none has changed', function() {
            angular.mock.inject(['common.IntygViewStateService', 'common.UserModel',
                function(_commonIntygViewstateService_, _UserModel_) {
                    commonIntygViewstateService = _commonIntygViewstateService_;
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
            expect(commonIntygViewstateService.patient.hasChangedAddress(intygModel)).toBeFalsy();
        });
        it('hasAddressChanged true if postadress has changed', function() {
            angular.mock.inject(['common.IntygViewStateService', 'common.UserModel',
                function(_commonIntygViewstateService_, _UserModel_) {
                    commonIntygViewstateService = _commonIntygViewstateService_;
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
            expect(commonIntygViewstateService.patient.hasChangedAddress(intygModel)).toBeTruthy();
        });
        it('hasAddressChanged true if postort has changed', function() {
            angular.mock.inject(['common.IntygViewStateService', 'common.UserModel',
                function(_commonIntygViewstateService_, _UserModel_) {
                    commonIntygViewstateService = _commonIntygViewstateService_;
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
            expect(commonIntygViewstateService.patient.hasChangedAddress(intygModel)).toBeTruthy();
        });
        it('hasAddressChanged true if postnummer has changed', function() {
            angular.mock.inject(['common.IntygViewStateService', 'common.UserModel',
                function(_commonIntygViewstateService_, _UserModel_) {
                    commonIntygViewstateService = _commonIntygViewstateService_;
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
            expect(commonIntygViewstateService.patient.hasChangedAddress(intygModel)).toBeTruthy();
        });
    });
});
