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

describe('authorityService', function() {
    'use strict';

    var authorityService;
    var UserModel;

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
        //'features': ['arbetsgivarUtskrift', 'arbetsgivarUtskrift.fk7263'],
        'features': {
            'ARBETSGIVARUTSKRIFT': {
                'global': true,
                'name': 'ARBETSGIVARUTSKRIFT',
                'intygstyper': ['fk7263']
            }
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
        'origin': 'NORMAL',
        'totaltAntalVardenheter': 6,
        'roles': {'LAKARE': {'name': 'Läkare', 'desc': 'Läkare'}},
        'role': 'Läkare'
    };

    beforeEach(angular.mock.module('common', function($provide) {

    }));

    beforeEach(angular.mock.inject(['common.authorityService', 'common.UserModel',
        function(_authorityService_, _UserModel_) {
            authorityService = _authorityService_;
            UserModel = _UserModel_;
            UserModel.setUser(testUser);
        }
    ]));

    describe('#AuthorityActive - features checking', function() {

        it ('feature should be ACTIVE when base feature exists', function () {
            expect(authorityService.isAuthorityActive({feature:'ARBETSGIVARUTSKRIFT'})).toBeTruthy();
        });

        it ('feature should be INACTIVE when intygstyp context is given - but missing in feature-config', function () {
            expect(authorityService.isAuthorityActive({feature:'ARBETSGIVARUTSKRIFT', intygstyp:'ts-bas'})).toBeFalsy();
        });

         it ('feature should be ACTIVE when intygstyp context is given - AND it exists in feature-config', function () {
            expect(authorityService.isAuthorityActive({feature:'ARBETSGIVARUTSKRIFT', intygstyp:'fk7263'})).toBeTruthy();
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

    describe('#AuthorityService - privilege checking', function() {

        it ('should be false when user does not have privilege', function () {
            expect(authorityService.isAuthorityActive({authority:'DUMMY_PREVILEDGE'})).toBeFalsy();
        });

        it ('should be false when user only have base privilege', function () {
            expect(authorityService.isAuthorityActive({authority:'SIGNERA_INTYG', intygstyp:'ts-bas'})).toBeFalsy();
        });


        it ('should be true when user have both base AND intygstyp privilege', function () {
            expect(authorityService.isAuthorityActive({authority:'SIGNERA_INTYG', intygstyp:'fk7263'})).toBeTruthy();
        });

        it('should be true when user have both base privilege AND correct requestOrigin', function() {
            expect(authorityService.isAuthorityActive({authority: 'STYRD_AV_ORIGIN'})).toBeTruthy();
        });

        it('should be true when user have both base AND intygstyp privilege AND correct requestOrigin AND requestOrigin.intygstyp',
            function() {
                expect(authorityService.isAuthorityActive(
                    {authority: 'STYRD_AV_ORIGIN', intygstyp: 'fk7263'})).toBeTruthy();
            });

        it('should be false when user have both base AND intygstyp privilege AND correct requestOrigin BUT not requestOrigin.intygstyp',
            function() {
                expect(authorityService.isAuthorityActive(
                    {authority: 'STYRD_AV_ORIGIN', intygstyp: 'ts-diabetes'})).toBeFalsy();
            });
    });

    describe('#AuthorityService - requestOrigin checking', function() {

        it('should be FALSE when user does not have requestOrigin', function() {
            expect(authorityService.isAuthorityActive({requestOrigin: 'DUMMY_ORIGIN'})).toBeFalsy();
        });

        it('should be TRUE when user does have requestOrigin', function() {
            expect(authorityService.isAuthorityActive({requestOrigin: 'NORMAL'})).toBeTruthy();
        });
    });

    describe('#AuthorityService - Combination checking', function() {

        it ('should be false when failing 1 criteria', function () {
            expect(authorityService.isAuthorityActive({feature:'ARBETSGIVARUTSKRIFT', role:'LAKARE', authority:'SIGNERA_INTYG', intygstyp:'DUMMY_TYPE'})).toBeFalsy();
        });

        it ('should be true when meeting all criteria', function () {
            expect(authorityService.isAuthorityActive({
                feature: 'ARBETSGIVARUTSKRIFT',
                role: 'LAKARE',
                authority: 'SIGNERA_INTYG',
                requestOrigin: 'NORMAL',
                intygstyp: 'fk7263'
            })).toBeTruthy();
        });
    });
});
