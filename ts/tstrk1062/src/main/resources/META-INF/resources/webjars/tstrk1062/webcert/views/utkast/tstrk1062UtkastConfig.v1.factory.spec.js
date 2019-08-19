/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

describe('tstrk1062UtkastConfigFactory', function() {
    'use strict';

    var element;
    var UtkastConfigFactory;
    var $scope;

    beforeEach(angular.mock.module('common', 'tstrk1062'));
    beforeEach(inject(['$compile', '$rootScope', 'tstrk1062.UtkastConfigFactory.v1', 'tstrk1062.Domain.IntygModel.v1',
        function($compile, $rootScope, _UtkastConfigFactory_, _IntygModel_) {
            UtkastConfigFactory = _UtkastConfigFactory_;

            $scope = $rootScope.$new();
            $scope.model = _IntygModel_._members.build().content;
            $scope.ueConfig = UtkastConfigFactory.getConfig();
            element = angular.element(
                '<form name="certForm"><ue-render-components form="::certForm" config="::ueConfig" model="::model"></ue-render-components></form>');
            $compile(element)($scope);
            $scope.$digest();
        }]));

    describe('lakemedelsbehandling', function() {

        it('should show extra fields when "lakemedelsbehandling.harHaft"-option is selected', function() {

            $scope.model.lakemedelsbehandling.harHaft = false;
            $scope.$digest();

            expect(element.find('#form_lakemedelsbehandling-pagar').length).toBe(0);

            $scope.model.lakemedelsbehandling.harHaft = true;
            $scope.$digest();

            expect(element.find('#form_lakemedelsbehandling-pagar').length).toBe(1);

        });

        it('should show extra fields when "lakemedelsbehandling.pagar"-option is selected', function() {

            $scope.model.lakemedelsbehandling.pagar = false;
            $scope.$digest();

            expect(element.find('#form_lakemedelsbehandling-aktuell').length).toBe(0);
            expect(element.find('#form_lakemedelsbehandling-pagatt').length).toBe(0);
            expect(element.find('#form_lakemedelsbehandling-effekt').length).toBe(0);
            expect(element.find('#form_lakemedelsbehandling-foljsamhet').length).toBe(0);

            expect(element.find('#form_lakemedelsbehandling-avslutadTidpunkt').length).toBe(1);
            expect(element.find('#form_lakemedelsbehandling-avslutadOrsak').length).toBe(1);

            $scope.model.lakemedelsbehandling.pagar = true;
            $scope.$digest();

            expect(element.find('#form_lakemedelsbehandling-aktuell').length).toBe(1);
            expect(element.find('#form_lakemedelsbehandling-pagatt').length).toBe(1);
            expect(element.find('#form_lakemedelsbehandling-effekt').length).toBe(1);
            expect(element.find('#form_lakemedelsbehandling-foljsamhet').length).toBe(1);

            expect(element.find('#form_lakemedelsbehandling-avslutadTidpunkt').length).toBe(0);
            expect(element.find('#form_lakemedelsbehandling-avslutadOrsak').length).toBe(0);

        });

    });

    describe('diagnos', function() {

        var initializeDiagnosKodad = function() {
            var getInitValue = function() {
                return {
                    diagnosKodSystem: 'ICD_10_SE',
                    diagnosKod: undefined,
                    diagnosBeskrivning: undefined,
                    diagnosArtal: undefined
                };
            };

            $scope.model.diagnosKodad = [getInitValue(), getInitValue(), getInitValue(), getInitValue()];
        };

        it('should show extra fields depending on the selection of "diagnosRegistrering.typ"-option', function() {

            initializeDiagnosKodad();

            expect(element.find('#form_diagnosKodad').length).toBe(0);
            expect(element.find('#form_diagnosFritext-diagnosFritext').length).toBe(0);
            expect(element.find('#diagnosFritext-diagnosArtal').length).toBe(0);

            $scope.model.diagnosRegistrering.typ = 'DIAGNOS_KODAD';
            $scope.$digest();

            expect(element.find('#form_diagnosKodad').length).toBe(1);
            expect(element.find('#form_diagnosFritext-diagnosFritext').length).toBe(0);
            expect(element.find('#diagnosFritext-diagnosArtal').length).toBe(0);

            $scope.model.diagnosRegistrering.typ = 'DIAGNOS_FRITEXT';
            $scope.$digest();

            expect(element.find('#form_diagnosKodad').length).toBe(0);
            expect(element.find('#form_diagnosFritext-diagnosFritext').length).toBe(1);
            expect(element.find('#diagnosFritext-diagnosArtal').length).toBe(1);

        });

    });

});
