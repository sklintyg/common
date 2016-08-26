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

    var $scope;
    var element;
    var $compileProvider;
    var $stateParams;
    var messageService;

    var samordningsNummer = '19540187-5769';
    var personNummer1 = '19121212-1212';
    var personNummer2 = '19520617-2339';

    beforeEach(angular.mock.module('htmlTemplates'));
    beforeEach(angular.mock.module('common'));

    beforeEach(module(function(_$compileProvider_) {
        $compileProvider = _$compileProvider_;
    }));

    beforeEach(angular.mock.inject(['$compile', '$rootScope', '$stateParams', 'common.messageService',
        function($compile, $rootScope, _$stateParams_, _messageService_) {
            messageService = _messageService_;
            $stateParams = _$stateParams_;

            $scope = $rootScope.$new();

            $rootScope.lang = 'sv';

            element =
                $compile('<div wc-new-person-id-message></div>')($scope);

            $scope.viewState = {
                intygModel : {
                    grundData : {
                        patient:{}
                    }
                }
            };

        }]));

    it('should not display new personnummer text', function() {
        $stateParams.patientId = personNummer1;
        $scope.viewState.intygModel.grundData.patient.personId = personNummer1;
        $scope.$digest();

        expect($(element).find('span').text()).toBe('');
    });

    it('should display new personnummer text', function() {
        $stateParams.patientId = personNummer1;
        $scope.viewState.intygModel.grundData.patient.personId = personNummer2;
        $scope.$digest();

        expect($(element).find('span').text()).toBe('Patienten har ett nytt personnummer: 19121212-1212');
    });

    it('should display new personnummer text', function() {
        $stateParams.patientId = personNummer1;
        $scope.viewState.intygModel.grundData.patient.personId = samordningsNummer;
        $scope.$digest();

        expect($(element).find('span').text()).toBe('Patienten har ett nytt personnummer: 19121212-1212');
    });

    it('should display new samordningsnummer text', function() {
        $stateParams.patientId = samordningsNummer;
        $scope.viewState.intygModel.grundData.patient.personId = personNummer1;
        $scope.$digest();

        expect($(element).find('span').text()).toBe('Patienten har ett nytt personnummer: '+samordningsNummer);
    });

    it('should display new samordningsnummer text', function() {
        $stateParams.patientId = '555555-5555';
        $scope.viewState.intygModel.grundData.patient.personId = samordningsNummer;
        $scope.$digest();

        expect($(element).find('span').text()).toBe('Patienten har samordningsnummer kopplat till reservnummer: 555555-5555');
    });

});
