describe('UserPreferencesService', function() {
    'use strict';

    var CookieService;

    beforeEach(angular.mock.module('common'), function($provide){

    });

    beforeEach(angular.mock.inject(['common.UserPreferencesService',
        function(_CookieService) {
            CookieService = _CookieService;
        }
    ]));

    describe('#setSkipShowUnhandledDialog', function() {

        it ('skipShowUnhandledDialog should be set in cookie store', function () {
            CookieService.setSkipShowUnhandledDialog(true);
            expect(CookieService.isSkipShowUnhandledDialogSet()).toBeTruthy();
            CookieService.setSkipShowUnhandledDialog(false);
            expect(CookieService.isSkipShowUnhandledDialogSet()).toBeFalsy();
        });
    });

});
