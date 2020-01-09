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

describe('Directive: wcUtkastPatientAdddressUpdater', function() {
    'use strict';
    var $compile;
    var $scope;
    var $timeout;

    var element;
    var elementScope;

    var PatientProxy;

    // load the controller's module
    beforeEach(module('common', function($provide) {
        PatientProxy = {
            getPatient: function() {
            }
        };
        $provide.value('common.PatientProxy', PatientProxy);
    }));
    beforeEach(module('htmlTemplates'));

    // Store references to $rootScope and $compile
    // so they are available to all tests in this describe block
    beforeEach(inject(function(_$compile_, $rootScope, _$timeout_) {
        $compile = _$compile_;
        $scope = $rootScope.$new();
        $timeout = _$timeout_;

        $scope.patientModel = {
            personId: '191212121212',
            postadress: '',
            postnummer: '11111',
            postort: 'Visby'
        };
        $scope.form = jasmine.createSpyObj('', [ '$setDirty' ]);

        element = $compile(angular.element('<wc-utkast-patient-address-updater patient-model="patientModel" form="form"/>'))($scope);
        $scope.$digest();

        elementScope = element.isolateScope() || element.scope();
    }));

    it('should update patientModel with data from backend', function() {
        //Arrange
        PatientProxy.getPatient = function(input, successHandler) {
            successHandler({
                postadress: 'Nygatan 4',
                postnummer: '22222',
                postort: 'Stockholm'
            });
        };

        //Act
        elementScope.onUpdateAddressClick();
        $timeout.flush();

        // Assert
        expect(elementScope.patientModel.postadress).toEqual('Nygatan 4');
        expect(elementScope.patientModel.postnummer).toEqual('22222');
        expect(elementScope.patientModel.postort).toEqual('Stockholm');
        expect($scope.form.$setDirty).toHaveBeenCalled();

    });

});
