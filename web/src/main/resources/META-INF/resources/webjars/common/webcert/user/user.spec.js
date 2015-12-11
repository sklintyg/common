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
        'aktivaFunktioner':['hanteraFragor','hanteraFragor.fk7263'],
        'totaltAntalVardenheter':6,
        'roles': {'LAKARE': {'name':'Läkare', 'desc':'Läkare'}},
        'role': 'Läkare',
        'intygsTyper' : [ 'fk7263', 'ts-bas', 'ts-diabetes' ],
        'requestOrigin' : { 'name': 'NORMAL', 'intygstyper': []}
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
            expect(activeFeatures).toContain('hanteraFragor');
            expect(activeFeatures).toContain('hanteraFragor.fk7263');
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
                'aktivaFunktioner':['hanteraFragor','hanteraFragor.fk7263'],
                'totaltAntalVardenheter':1,
                'lakare' : true, 'privatLakare' : false, 'tandLakare':false, 'isLakareOrPrivat' : true,
                'roles': {'LAKARE': {'name':'Läkare', 'desc':'Läkare'}},
                'role': 'Läkare',
                'intygsTyper' : [ 'fk7263', 'ts-bas', 'ts-diabetes' ],
                'requestOrigin' : { 'name': 'NORMAL', 'intygstyper': []},
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
    });


});
