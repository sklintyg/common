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
                aktivaFunktioner : ['hanteraIntygsutkast']
            };
            expect(featureService.isFeatureActive('hanteraFragor')).toBeFalsy();
        });

        it ('should be true if the feature is available', function () {
            UserModel.user = {
                aktivaFunktioner : ['hanteraIntygsutkast', 'hanteraFragor']
            };
            expect(featureService.isFeatureActive('hanteraFragor')).toBeTruthy();
        });

        it ('should be true if the feature is available in a module', function () {
            UserModel.user = {
                aktivaFunktioner : ['hanteraIntygsutkast', 'hanteraFragor', 'hanteraFragor.fk7263']
            };
            expect(featureService.isFeatureActive('hanteraFragor', 'fk7263')).toBeTruthy();
        });

        it ('should be false if the feature is not available a module', function () {
            UserModel.user = {
                aktivaFunktioner : ['hanteraIntygsutkast', 'hanteraFragor']
            };
            expect(featureService.isFeatureActive('hanteraFragor', 'fk7263')).toBeFalsy();
        });
    });
});
