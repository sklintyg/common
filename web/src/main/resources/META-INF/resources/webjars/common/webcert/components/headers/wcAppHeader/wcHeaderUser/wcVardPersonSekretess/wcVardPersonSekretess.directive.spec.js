/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

describe('wcVardPersonSekretess Directive', function() {
    'use strict';

    var $scope;
    var element;
    var $uibModal;
    var UserModel;
    var initialMockedUser = {
        sekretessMarkerad: true,
        anvandarPreference: {}

    };

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));


    beforeEach(angular.mock.inject([ '$compile', '$rootScope', '$uibModal', 'common.UserModel', function($compile, $rootScope, _$uibModal_, _UserModel_) {
        $scope = $rootScope.$new();
        $uibModal = _$uibModal_;
        UserModel = _UserModel_;
        UserModel.setUser(initialMockedUser);


        element = $compile('<wc-vard-person-sekretess></wc-vard-person-sekretess>')($scope);

    } ]));

    it('Should show consent dialog if sekretessMarkering is true and consent not approved', function() {
        UserModel.user.sekretessMarkerad = true;
        UserModel.setAnvandarPreference('wc.vardperson.sekretess.approved', undefined);
        spyOn($uibModal, 'open');

        $scope.$digest();
        expect($(document).find('#wc-vardperson-sekretess-modal-dialog--consent-btn').length).toBe(1);
    });

    it('Should NOT show consent dialog if sekretessMarkering is true and consent already approved', function() {
        UserModel.user.sekretessMarkerad = true;
        UserModel.setAnvandarPreference('wc.vardperson.sekretess.approved', true);
        spyOn($uibModal, 'open');

        $scope.$digest();

        expect($uibModal.open).toHaveBeenCalledTimes(0);
    });
});
