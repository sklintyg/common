/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

describe('featureService', function() {
    'use strict';

    var featureService;
    var User, UserModel;

    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.inject(['common.featureService', 'common.User', 'common.UserModel',
        function(_featureService_, _User_, _UserModel_) {
            featureService = _featureService_;
            User = _User_;
            UserModel = _UserModel_;
        }
    ]));

    describe('#isFeatureActive', function() {

        it ('should be false for invalid feature', function () {
            expect(featureService.isFeatureActive('')).toBeFalsy();
            expect(featureService.isFeatureActive(null)).toBeFalsy();
        });

        it ('should be false if no features is set on the user', function () {
            expect(featureService.isFeatureActive('hanteraFragor')).toBeFalsy();
        });

        it ('should be false if the feature is not available', function () {
            UserModel.user = {
                features : ['hanteraIntygsutkast']
            };
            expect(featureService.isFeatureActive('hanteraFragor')).toBeFalsy();
        });

        it ('should be true if the feature is available', function () {
            UserModel.user = {
                features : ['hanteraIntygsutkast', 'hanteraFragor']
            };
            expect(featureService.isFeatureActive('hanteraFragor')).toBeTruthy();
        });

        it ('should be true if the feature is available in a module', function () {
            UserModel.user = {
                features : ['hanteraIntygsutkast', 'hanteraFragor', 'hanteraFragor.fk7263']
            };
            expect(featureService.isFeatureActive('hanteraFragor', 'fk7263')).toBeTruthy();
        });

        it ('should be false if the feature is not available a module', function () {
            UserModel.user = {
                features : ['hanteraIntygsutkast', 'hanteraFragor']
            };
            expect(featureService.isFeatureActive('hanteraFragor', 'fk7263')).toBeFalsy();
        });
    });
});
