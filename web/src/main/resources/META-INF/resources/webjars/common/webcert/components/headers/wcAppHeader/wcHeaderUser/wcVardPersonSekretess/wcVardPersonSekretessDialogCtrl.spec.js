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

describe('wcVardPersonSekretessDialog Controller', function() {
    'use strict';

    var $scope;
    var $controller;
    var $uibModalInstanceSpy;
    var UserModel;
    var UserService;
    var preferenceKey;
    var $windowMock;

    var initialMockedUser = {
        sekretessMarkerad: true,
        origin: 'DJUPINTEGRERAD',
        anvandarPreference: {}

    };

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.inject([ '$rootScope', '$controller', '$window', 'common.User', 'common.UserModel',
            function($rootScope, _$controller_, _$window_, _UserService_, _UserModel_) {
                $scope = $rootScope.$new();
                $controller = _$controller_;
                $uibModalInstanceSpy = jasmine.createSpyObj('$uibModalInstance', [ 'close', 'dismiss' ]);

                UserModel = _UserModel_;
                UserService = _UserService_;
                preferenceKey = 'test-key';
                UserModel.setUser(initialMockedUser);

                $windowMock = {
                    location: {
                        href: ''
                    }
                };

                $controller('wcVardPersonSekretessDialogCtrl', {
                    $scope: $scope,
                    $window: $windowMock,
                    $uibModalInstance: $uibModalInstanceSpy,
                    preferenceKey: preferenceKey
                });

            } ]));

    it('Should save consent preferenceKey and close dialog when approved', function() {
        spyOn(UserService, 'storeAnvandarPreference');

        $scope.check();
        $scope.giveConsent();
        $scope.$digest();

        expect(UserService.storeAnvandarPreference).toHaveBeenCalledWith(preferenceKey, true);
        expect($uibModalInstanceSpy.close).toHaveBeenCalled();

    });

    function doCancel() {
        spyOn(UserService, 'storeAnvandarPreference');

        $scope.onCancel();
        $scope.$digest();

        expect(UserService.storeAnvandarPreference).toHaveBeenCalledTimes(0);
        expect($uibModalInstanceSpy.close).toHaveBeenCalled();
        expect($windowMock.location.href).toContain('reason=sekretessapproval.needed');
    }
    it('Should close dialog and redirect to error page when clicking cancel', function() {

        doCancel();
        //Expect specific behaviour when not NORMAL origin
        expect($windowMock.location.href).not.toContain('showlogin=true');

    });

    it('Should close dialog and redirect to error page with login option when clicking cancel in NORMAL origin', function() {
        UserModel.user.origin = 'NORMAL';

        doCancel();
        //Expect specific behaviour when not NORMAL origin
        expect($windowMock.location.href).toContain('showlogin=true');

    });

});
