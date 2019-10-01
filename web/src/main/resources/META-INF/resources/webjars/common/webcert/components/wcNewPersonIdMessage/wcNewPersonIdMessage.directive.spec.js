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

describe('wcNewPersonIdMessageDirective', function() {
    'use strict';

    var $compile;
    var $scope;
    var element;
    var UserModel;

    var samordningsNummer = '19540187-5769';
    var personNummer1 = '19121212-1212';
    var personNummer1WithoutDash = '191212121212';
    var personNummer2 = '19520617-2339';

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));

    beforeEach(angular.mock.inject(['$compile', '$rootScope', 'common.UserModel',
        function(_$compile_, $rootScope, _UserModel_) {
            $compile = _$compile_;
            $scope = $rootScope.$new();

            $rootScope.lang = 'sv';

            $scope.patient = {
                personId: '19121212-1212'
            };

            UserModel = _UserModel_;

            UserModel.setUser({parameters: {}});
            
        }]));

    describe('on utkast', function() {

        beforeEach(function() {
            element =
                $compile('<wc-new-person-id-message is-intyg="false"></wc-new-person-id-message>')($scope);
        });

        it('should not display new personnummer text', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            UserModel.user.parameters.beforeAlternateSsn = personNummer1;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('');
        });

        it('should not display new personnummer text', function() {
            UserModel.user.parameters.alternateSsn = personNummer1WithoutDash;
            UserModel.user.parameters.beforeAlternateSsn = personNummer1;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('');
        });

        it('should not display new personnummer text', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            UserModel.user.parameters.beforeAlternateSsn = personNummer1WithoutDash;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('');
        });

        it('should not display any personnummer text when personnummer is empty string', function() {
            UserModel.user.parameters.alternateSsn = '';
            UserModel.user.parameters.beforeAlternateSsn = personNummer1;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('');
        });

        it('should not display any personnummer text when backend has not set beforeAlternateSsn', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            UserModel.user.parameters.beforeAlternateSsn = null;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('');
        });

        it('should not display any personnummer text when backend has not set beforeAlternateSsn', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            UserModel.user.parameters.beforeAlternateSsn = '';
            $scope.$digest();

            expect($(element).find('span').text()).toBe('');
        });

        it('should display new personnummer text', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            UserModel.user.parameters.beforeAlternateSsn = personNummer2;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('Patientens personnummer har ändrats');
        });

        it('should display new personnummer text', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            UserModel.user.parameters.beforeAlternateSsn = samordningsNummer;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('Patientens personnummer har ändrats');
        });

        it('should display new samordningsnummer text', function() {
            UserModel.user.parameters.alternateSsn = samordningsNummer;
            UserModel.user.parameters.beforeAlternateSsn = personNummer1;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('Patientens personnummer har ändrats');
        });

        it('should display new samordningsnummer text for new reservnr for existing samordningnr', function() {
            UserModel.user.parameters.alternateSsn = '555555-5555';
            UserModel.user.parameters.beforeAlternateSsn = samordningsNummer;
            $scope.$digest();

            expect($(element).find('span').text()).toContain(
                'Patienten har samordningsnummer kopplat till reservnummer: 555555-5555.');
        });

        it('should display new samordningsnummer text for new reservnr for existing personnr', function() {
            UserModel.user.parameters.alternateSsn = 'A123456FFFF';
            UserModel.user.parameters.beforeAlternateSsn = personNummer1;
            $scope.$digest();

            expect($(element).find('span').text()).toContain(
                'Patienten har samordningsnummer kopplat till reservnummer: A123456FFFF.');
        });
    });

    describe('on intyg', function() {

        beforeEach(function() {
            element =
                $compile('<wc-new-person-id-message is-intyg="true" patient="patient"></wc-new-person-id-message>')($scope);
        });

        it('should not display new personnummer text', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            $scope.patient.personId = personNummer1;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('');
        });

        it('should not display any personnummer text when personnummer is empty string', function() {
            UserModel.user.parameters.alternateSsn = '';
            $scope.patient.personId = personNummer1;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('');
        });

        it('should not display any personnummer text when intyg has not set patient', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            $scope.patient = null;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('');
        });

        it('should not display any personnummer text when intyg has not set personId', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            $scope.patient.personId = null;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('');
        });

        it('should not display any personnummer text when intyg has not set personId', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            $scope.patient.personId = '';
            $scope.$digest();

            expect($(element).find('span').text()).toBe('');
        });

        it('should display new personnummer text', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            $scope.patient.personId = personNummer2;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('Patientens personnummer har ändrats');
        });

        it('should display new personnummer text', function() {
            UserModel.user.parameters.alternateSsn = personNummer1;
            $scope.patient.personId = samordningsNummer;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('Patientens personnummer har ändrats');
        });

        it('should display new samordningsnummer text', function() {
            UserModel.user.parameters.alternateSsn = samordningsNummer;
            $scope.patient.personId = personNummer1;
            $scope.$digest();

            expect($(element).find('span').text()).toBe('Patientens personnummer har ändrats');
        });

        it('should display new samordningsnummer text for new reservnr for existing samordningnr', function() {
            UserModel.user.parameters.alternateSsn = '555555-5555';
            $scope.patient.personId = samordningsNummer;
            $scope.$digest();

            expect($(element).find('span').text()).toContain(
                'Patienten har samordningsnummer kopplat till reservnummer: 555555-5555.');
        });

        it('should display new samordningsnummer text for new reservnr for existing personnr', function() {
            UserModel.user.parameters.alternateSsn = 'A123456FFFF';
            $scope.patient.personId = personNummer1;
            $scope.$digest();

            expect($(element).find('span').text()).toContain(
                'Patienten har samordningsnummer kopplat till reservnummer: A123456FFFF.');
        });

    });
});
