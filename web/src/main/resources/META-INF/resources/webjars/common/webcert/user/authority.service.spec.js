describe('authorityService', function() {
    'use strict';

    var authorityService;
    var User, UserModel, featureService;

    var testUser = {
        'hsaId': 'eva',
        'namn': 'Eva Holgersson',
        'lakare': true,
        'forskrivarkod': '2481632',
        'authenticationScheme': 'urn:inera:webcert:fake',
        'vardgivare': [
            {
                'id': 'vastmanland', 'namn': 'Landstinget Västmanland', 'vardenheter': [
                {
                    'id': 'centrum-vast', 'namn': 'Vårdcentrum i Väst', 'arbetsplatskod': '0000000', 'mottagningar': [
                    {'id': 'akuten', 'namn': 'Akuten', 'arbetsplatskod': '0000000'},
                    {'id': 'dialys', 'namn': 'Dialys', 'arbetsplatskod': '0000000'}
                ]
                }
            ]
            }
        ],
        'aktivaFunktioner': ['arbetsgivarUtskrift', 'arbetsgivarUtskrift.fk7263'],
        'authorities': {
            'NAVIGERING': {},
            'SIGNERA_INTYG': {}
        },
        'totaltAntalVardenheter': 6,
        'roles': {'LAKARE': {'name': 'Läkare', 'authorizedIntygsTyper': ['fk7263', 'ts-bas', 'ts-diabetes']}},
        'role': 'Läkare',
        'intygsTyper': ['fk7263', 'ts-bas', 'ts-diabetes']
    };

    beforeEach(angular.mock.module('common', function($provide) {

    }));

    beforeEach(angular.mock.inject(['common.authorityService','common.featureService', 'common.User', 'common.UserModel',
        function(_authorityService_, _featureService_, _User_, _UserModel_) {
            authorityService = _authorityService_;
            featureService = _featureService_;
            User = _User_;
            UserModel = _UserModel_;
            UserModel.setUser(testUser);
        }
    ]));

    describe('#AuthorityActive - features checking', function() {

        it ('feature should be ACTIVE when base feature exists', function () {
            expect(authorityService.isAuthorityActive({feature:'arbetsgivarUtskrift'})).toBeTruthy();
        });

        it ('feature should be INACTIVE when intygstyp context is given - but missing in feature-config', function () {
            expect(authorityService.isAuthorityActive({feature:'arbetsgivarUtskrift', intygstyp:'ts-bas'})).toBeFalsy();
        });

         it ('feature should be ACTIVE when intygstyp context is given - AND it exists in feature-config', function () {
            expect(authorityService.isAuthorityActive({feature:'arbetsgivarUtskrift', intygstyp:'fk7263'})).toBeTruthy();
        });
    });

    describe('#AuthorityService - role checking', function() {

        it ('should be false when user does not have role', function () {
            expect(authorityService.isAuthorityActive({role:'DUMMY_ROLE'})).toBeFalsy();
        });

        it ('should be true when user have role', function () {
            expect(authorityService.isAuthorityActive({role:'LAKARE'})).toBeTruthy();
        });
    });

    describe('#AuthorityService - previledge checking', function() {

        it ('should be false when user does not have previledge', function () {
            expect(authorityService.isAuthorityActive({authority:'DUMMY_PREVILEDGE'})).toBeFalsy();
        });

        it ('should be true when user have previledge', function () {
            expect(authorityService.isAuthorityActive({authority:'SIGNERA_INTYG'})).toBeTruthy();
        });
    });

    describe('#AuthorityService - intygstyp checking', function() {

        it ('should be false when user does not have intygstyp (via role)', function () {
            expect(authorityService.isAuthorityActive({intygstyp:'DUMMY_TYPE'})).toBeFalsy();
        });

        it ('should be true when user does have intygstyp (via role)', function () {
            expect(authorityService.isAuthorityActive({intygstyp:'fk7263'})).toBeTruthy();
        });
    });

    describe('#AuthorityService - Combination checking', function() {

        it ('should be false when failing 1 criteria', function () {
            expect(authorityService.isAuthorityActive({feature:'arbetsgivarUtskrift', role:'LAKARE', authority:'SIGNERA_INTYG', intygstyp:'DUMMY_TYPE'})).toBeFalsy();
        });

        it ('should be true when meeting all criteria', function () {
            expect(authorityService.isAuthorityActive({feature:'arbetsgivarUtskrift', role:'LAKARE', authority:'SIGNERA_INTYG', intygstyp:'fk7263'})).toBeTruthy();
        });
    });
});
