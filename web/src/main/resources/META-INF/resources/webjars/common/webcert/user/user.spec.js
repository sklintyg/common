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

describe('User', function() {
    'use strict';

    var $httpBackend;
    var User, UserModel;
    var testUser = {'hsaId':'eva','namn':'Eva Holgersson','lakare':true,'forskrivarkod':'2481632','authenticationScheme':'urn:inera:webcert:fake','vardgivare':[
            {'id':'vastmanland','namn':'Landstinget Västmanland','vardenheter':[
                    {'id':'centrum-vast','namn':'Vårdcentrum i Väst','arbetsplatskod':'0000000','mottagningar':[
                        {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},{'id':'dialys','namn':'Dialys','arbetsplatskod':'0000000'}
                    ]}
                ]
            },
            {'id':'ostergotland','namn':'Landstinget Östergötland','vardenheter':[
                    {'id':'linkoping','namn':'Linköpings Universitetssjukhus','arbetsplatskod':'0000000','mottagningar':[
                        {'id':'lkpg-akuten','namn':'Akuten','arbetsplatskod':'0000000'},
                        {'id':'lkpg-ogon','namn':'Ögonmottagningen','arbetsplatskod':'0000000'}
                    ]}
                ]
            }
        ],
        'specialiseringar':['Kirurgi','Oftalmologi'],
        'titel':'Leg. Ögonläkare',
        'legitimeradeYrkesgrupper':['Läkare'],
        'valdVardenhet':{'id':'centrum-vast','namn':'Vårdcentrum i Väst','arbetsplatskod':'0000000','mottagningar':[
                {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},
                {'id':'dialys','namn':'Dialys','arbetsplatskod':'0000000'}
            ]
        },
        'valdVardgivare':{'id':'vastmanland','namn':'Landstinget Västmanland','vardenheter':[
                {'id':'centrum-vast','namn':'Vårdcentrum i Väst','arbetsplatskod':'0000000','mottagningar':[
                        {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},
                        {'id':'dialys','namn':'Dialys','arbetsplatskod':'0000000'}
                    ]
                }
            ]
        },
        'authorities': {
            'NAVIGERING': {},
            'SIGNERA_INTYG': {'intygstyper': ['fk7263', 'ts-diabetes']},
            'STYRD_AV_ORIGIN': {
                'intygstyper': ['fk7263', 'ts-diabetes'],
                'requestOrigins': [
                    {'name': 'NORMAL', 'intygstyper': ['fk7263']}
                ]
            }
        },
        'features': {
            'HANTERA_FRAGOR': {
                'global': true,
                'intygstyper': ['fk7263']
            }
        },
        'totaltAntalVardenheter':6,
        'roles': {'LAKARE': {'name':'Läkare', 'desc':'Läkare'}},
        'role': 'Läkare',
        'origin': 'NORMAL',
        'anvandarPreference': {}
    };

    beforeEach(angular.mock.module('common', function($provide) {

    }));

    beforeEach(angular.mock.inject(['$httpBackend', 'common.User', 'common.UserModel',
        function(_$httpBackend_, _User_, _UserModel_) {
            User = _User_;
            UserModel = _UserModel_;
            $httpBackend = _$httpBackend_;
        }]));

    describe('#reset', function() {
        it('should set user to null', function() {
            UserModel.setUser(testUser);
            UserModel.reset();
            expect(UserModel.user).toBeNull();
        });
    });

    describe('#getActiveFeatures', function() {
        it('should return currently active features', function() {
            UserModel.setUser(testUser);
            var activeFeatures = UserModel.getActiveFeatures();
            expect(activeFeatures.HANTERA_FRAGOR).not.toBe(undefined);
            expect(activeFeatures.HANTERA_FRAGOR.intygstyper).toContain('fk7263');
        });
    });

    describe('#hasPrivilege - without intygstyp', function() {
        it('should return FALSE for non-matching previligie', function() {
            UserModel.setUser(testUser);
            expect(UserModel.hasPrivilege('DUMMY_PREVILEGIE')).toBeFalsy();
        });

        it('should return TRUE for matching previligie', function() {
            UserModel.setUser(testUser);
            expect(UserModel.hasPrivilege('SIGNERA_INTYG')).toBeTruthy();
        });
    });

    describe('#hasPrivilege - with intygstyp', function() {
        it('should return TRUE for matching previligie (with matching intygstyp)', function() {
            UserModel.setUser(testUser);
            expect(UserModel.hasPrivilege('SIGNERA_INTYG', 'fk7263')).toBeTruthy();
        });

        it('should return FALSE for matching previligie (with non-matching intygstyp)', function() {
            UserModel.setUser(testUser);
            expect(UserModel.hasPrivilege('SIGNERA_INTYG', 'ts-bas')).toBeFalsy();
        });

    });

    describe('#hasPrivilege - with requestOrigin', function() {

        it('should return TRUE for matching previligie and requestOrigin', function() {
            UserModel.setUser(testUser);
            expect(UserModel.hasPrivilege('STYRD_AV_ORIGIN')).toBeTruthy();
        });

        it('should return TRUE for matching previligie/ requestOrigin (with matching intygstyp)', function() {
            UserModel.setUser(testUser);
            expect(UserModel.hasPrivilege('STYRD_AV_ORIGIN', 'fk7263')).toBeTruthy();
        });

        it('should return FALSE for matching previligie / requestOrigin (with non-matching intygstyp)', function() {
            UserModel.setUser(testUser);
            expect(UserModel.hasPrivilege('STYRD_AV_ORIGIN', 'ts-bas')).toBeFalsy();
        });

    });


    describe('#setUser', function() {
        it('should set currently active user context', function() {
            UserModel.setUser(null);
            expect(UserModel.user).toBeUndefined();

            UserModel.setUser(testUser);
            expect(UserModel.user).toEqual(testUser);
        });
    });

    describe('#getVardenhetSelectionList', function() {
        it('should return a list of selectable vardenheter and mottagningar in the selected vardgivare', function() {

            UserModel.setUser(testUser);
            var testSelectionList = [
                { id: 'vastmanland', namn: 'Landstinget Västmanland', vardenheter: [
                        { id: 'centrum-vast', namn: 'Vårdcentrum i Väst' },
                        { id: 'akuten', namn: 'Vårdcentrum i Väst - Akuten', arbetsplatskod: '0000000' },
                        { id: 'dialys', namn: 'Vårdcentrum i Väst - Dialys', arbetsplatskod: '0000000' }
                    ]
                },
                { id: 'ostergotland', namn: 'Landstinget Östergötland', vardenheter: [
                        { id: 'linkoping', namn: 'Linköpings Universitetssjukhus' },
                        { id: 'lkpg-akuten', namn: 'Linköpings Universitetssjukhus - Akuten', arbetsplatskod: '0000000' },
                        { id: 'lkpg-ogon', namn: 'Linköpings Universitetssjukhus - Ögonmottagningen', arbetsplatskod: '0000000' }
                    ]
                }
            ];
            expect(User.getVardenhetSelectionList()).toEqual(testSelectionList);
        });
    });

    describe('#getVardenhetFilterList', function() {
        it('should return a list with the specified vardenhet and its mottagnigar', function() {

            UserModel.setUser(testUser);

            var valdVardenhet = {'id':'centrum-vast','namn':'Vårdcentrum i Väst','arbetsplatskod':'0000000','mottagningar':[
                {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},
                {'id':'dialys','namn':'Dialys','arbetsplatskod':'0000000'}
            ]};

            var testFilterList = [
                { id : 'centrum-vast', namn : 'Vårdcentrum i Väst', arbetsplatskod : '0000000', mottagningar : [
                        { id : 'akuten', namn : 'Akuten', arbetsplatskod : '0000000' },
                        { id : 'dialys', namn : 'Dialys', arbetsplatskod : '0000000' }
                    ]
                },
                { id : 'akuten', namn : 'Akuten', arbetsplatskod : '0000000' },
                { id : 'dialys', namn : 'Dialys', arbetsplatskod : '0000000' }
            ];
            expect(User.getVardenhetFilterList(valdVardenhet)).toEqual(testFilterList);
        });
    });

    describe('#getValdVardgivare', function() {
        it('should return valdVardgivare', function() {
            UserModel.setUser(testUser);
            expect(User.getValdVardgivare()).toEqual({'id':'vastmanland','namn':'Landstinget Västmanland','vardenheter':[
                    {'id':'centrum-vast','namn':'Vårdcentrum i Väst','arbetsplatskod':'0000000','mottagningar':[
                            {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},
                            {'id':'dialys','namn':'Dialys','arbetsplatskod':'0000000'}
                        ]
                    }
                ]
            });
        });
    });

    describe('#getValdVardenhet', function() {
        it('should return valdVardenhet', function() {
            UserModel.setUser(testUser);
            expect(User.getValdVardenhet()).toEqual({'id':'centrum-vast','namn':'Vårdcentrum i Väst','arbetsplatskod':'0000000','mottagningar':[
                    {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},
                    {'id':'dialys','namn':'Dialys','arbetsplatskod':'0000000'}
                ]
            });
        });
    });

    describe('#setValdVardenhet', function() {

        var newUser;

        beforeEach(function() {
            newUser = {'hsaId':'eva','namn':'Eva Holgersson','forskrivarkod':'2481632','authenticationScheme':'urn:inera:webcert:fake','vardgivare':[
                {'id':'vastmanland','namn':'Landstinget Västmanland','vardenheter':[
                    {'id':'centrum-vast','namn':'Vårdcentrum i Väst','arbetsplatskod':'0000000','mottagningar':[
                        {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},{'id':'dialys','namn':'Dialys','arbetsplatskod':'0000000'}
                    ]}
                ]
                },
                {'id':'ostergotland','namn':'Landstinget Östergötland','vardenheter':[
                    {'id':'linkoping','namn':'Linköpings Universitetssjukhus','arbetsplatskod':'0000000','mottagningar':[
                        {'id':'lkpg-akuten','namn':'Akuten','arbetsplatskod':'0000000'},
                        {'id':'lkpg-ogon','namn':'Ögonmottagningen','arbetsplatskod':'0000000'}
                    ]}
                ]
                }
            ],
                'specialiseringar':['Kirurgi','Oftalmologi'],
                'titel':'Leg. Ögonläkare',
                'legitimeradeYrkesgrupper':['Läkare'],
                'valdVardenhet':{'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},
                'valdVardgivare':{'id':'vastmanland','namn':'Landstinget Västmanland','vardenheter':[
                    {'id':'centrum-vast','namn':'Vårdcentrum i Väst','arbetsplatskod':'0000000','mottagningar':[
                        {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'},
                        {'id':'dialys','namn':'Dialys','arbetsplatskod':'0000000'}
                    ]
                    }
                ]
                },
                'features': {
                    'HANTERA_FRAGOR': {
                        'global': true,
                        'intygstyper': ['fk7263']
                    }
                },
                'totaltAntalVardenheter':1,
                'lakare' : true, 'privatLakare' : false, 'tandLakare':false, 'isLakareOrPrivat' : true,
                'roles': {'LAKARE': {'name':'Läkare', 'desc':'Läkare'}},
                'role': 'Läkare',
                'origin': 'NORMAL'
            };
        });

        it('should request to set a new vardenhet as selected and receive a new user context with the new vardenhet as valdvardenhet', function() {

            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');
            $httpBackend.expectPOST('/api/anvandare/andraenhet').respond(200, newUser);

            UserModel.setUser(testUser);
            var valjVardenhet = {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'};
            User.setValdVardenhet(valjVardenhet, onSuccess, onError);
            $httpBackend.flush();

            expect(onSuccess).toHaveBeenCalledWith(newUser);
            expect(onError).not.toHaveBeenCalled();
            expect(User.getValdVardenhet()).toEqual(valjVardenhet);
        });

        it('should request to set a new vardenhet as selected and receive an error if backend responds 500', function() {

            UserModel.setUser(testUser);

            var onSuccess = jasmine.createSpy('onSuccess');
            var onError = jasmine.createSpy('onError');

            $httpBackend.expectPOST('/api/anvandare/andraenhet').respond(500, newUser);

            var valjVardenhet = {'id':'akuten','namn':'Akuten','arbetsplatskod':'0000000'};
            User.setValdVardenhet(valjVardenhet, onSuccess, onError);
            $httpBackend.flush();

            expect(onSuccess).not.toHaveBeenCalled();
            expect(onError).toHaveBeenCalled();
        });

        it('should PUT to store anvandar preference', function() {

            UserModel.setUser(testUser);

            $httpBackend.expectPUT('/api/anvandare/preferences').respond(200, {});

            User.storeAnvandarPreference('key1', 'val1');
            $httpBackend.flush();
        });
    });


});
