/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
describe('ts-basUtkastConfigFactoryV7', function() {
    'use strict';

    var element;
    var UtkastConfigFactory;
    var $scope;

    beforeEach(angular.mock.module('common', 'ts-bas'));
    beforeEach(inject(['$compile', '$rootScope', 'ts-bas.UtkastConfigFactory.v7', 'ts-bas.Domain.IntygModel.v7',
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

    describe('korkortstyp', function() {

        function checkKorkortstyp(typ, shouldExist) {
            expect(element.find('#form_horselBalans-svartUppfattaSamtal4Meter').length).toBe(0);
            expect(element.find('#form_funktionsnedsattning-otillrackligRorelseformaga').length).toBe(0);
            getCheckboxForKorkortstyp(typ).selected = true;
            $scope.$digest();
            expect(element.find('#form_horselBalans-svartUppfattaSamtal4Meter').length).toBe(shouldExist ? 1 : 0);
            expect(element.find('#form_funktionsnedsattning-otillrackligRorelseformaga').length).toBe(shouldExist ? 1 : 0);
            getCheckboxForKorkortstyp(typ).selected = false;
            $scope.$digest();
        }

        it('should show extra fields when some "korkortstyp"-options are selected', function() {
            checkKorkortstyp('IAV5', true);
            checkKorkortstyp('IAV6', true);
            checkKorkortstyp('IAV7', true);
            checkKorkortstyp('IAV8', true);
            checkKorkortstyp('IAV9', true);
            checkKorkortstyp('IAV10', false);
            checkKorkortstyp('IAV1', false);
        });

        it('should reset hidden fields when some "korkortstyp"-options are deselected', function() {

            getCheckboxForKorkortstyp('IAV5').selected = true;
            $scope.$digest();

            $scope.model.horselBalans.svartUppfattaSamtal4Meter = true;
            $scope.model.funktionsnedsattning.otillrackligRorelseformaga = true;
            getCheckboxForKorkortstyp('IAV5').selected = false;
            $scope.$digest();

            expect($scope.model.horselBalans.svartUppfattaSamtal4Meter).toBeUndefined();
            expect($scope.model.funktionsnedsattning.otillrackligRorelseformaga).toBeUndefined();

            // Attic
            getCheckboxForKorkortstyp('IAV5').selected = true;
            $scope.$digest();

            expect($scope.model.horselBalans.svartUppfattaSamtal4Meter).toBeTruthy();
            expect($scope.model.funktionsnedsattning.otillrackligRorelseformaga).toBeTruthy();

            // Check that 2b and 3b still are selected if you select another high körkortstyp after it already is set to high.
            $scope.model.horselBalans.svartUppfattaSamtal4Meter = true;
            $scope.model.funktionsnedsattning.otillrackligRorelseformaga = true;
            getCheckboxForKorkortstyp('IAV6').selected = true;
            $scope.$digest();

            expect($scope.model.horselBalans.svartUppfattaSamtal4Meter).toBeTruthy();
            expect($scope.model.funktionsnedsattning.otillrackligRorelseformaga).toBeTruthy();
        });
    });

    describe('funktionsnedsattning', function() {
        it('should reset hidden fields when "funktionsnedsattning" is set to false', function() {
            $scope.model.funktionsnedsattning.funktionsnedsattning = true;
            $scope.$digest();

            $scope.model.funktionsnedsattning.beskrivning = 'Hello';
            $scope.model.funktionsnedsattning.funktionsnedsattning = false;
            $scope.$digest();

            expect($scope.model.funktionsnedsattning.beskrivning).toBeUndefined();

            // Attic
            $scope.model.funktionsnedsattning.funktionsnedsattning = true;
            $scope.$digest();

            expect($scope.model.funktionsnedsattning.beskrivning).toBe('Hello');
        });
    });

    describe('hjartKarl', function() {
        it('should reset hidden fields when "riskfaktorerStroke" is set to false', function() {
            $scope.model.hjartKarl.riskfaktorerStroke = true;
            $scope.$digest();

            $scope.model.hjartKarl.beskrivningRiskfaktorer = 'Hello';
            $scope.model.hjartKarl.riskfaktorerStroke = false;
            $scope.$digest();

            expect($scope.model.hjartKarl.beskrivningRiskfaktorer).toBeUndefined();

            // Attic
            $scope.model.hjartKarl.riskfaktorerStroke = true;
            $scope.$digest();

            expect($scope.model.hjartKarl.beskrivningRiskfaktorer).toBe('Hello');
        });
    });

    describe('diabetes', function() {
        it('should reset hidden fields when "harDiabetes" is set to false', function() {
            $scope.model.diabetes.harDiabetes = true;
            $scope.$digest();

            $scope.model.diabetes.diabetesTyp = 'DIABETES_TYP_1';
            $scope.model.diabetes.harDiabetes = false;
            $scope.$digest();

            expect($scope.model.diabetes.diabetesTyp).toBeUndefined();

            // Attic
            $scope.model.diabetes.harDiabetes = true;
            $scope.$digest();

            expect($scope.model.diabetes.diabetesTyp).toBe('DIABETES_TYP_1');
        });

        it('should reset hidden fields when "diabetesTyp" is not "DIABETES_TYP_2"', function() {
            $scope.model.diabetes.diabetesTyp = 'DIABETES_TYP_2';
            $scope.$digest();

            $scope.model.diabetes.kost = true;
            $scope.model.diabetes.tabletter = true;
            $scope.model.diabetes.insulin = true;
            $scope.model.diabetes.diabetesTyp = 'DIABETES_TYP_1';
            $scope.$digest();

            expect($scope.model.diabetes.kost).toBeFalsy();
            expect($scope.model.diabetes.tabletter).toBeFalsy();
            expect($scope.model.diabetes.insulin).toBeFalsy();

            // Attic
            $scope.model.diabetes.diabetesTyp = 'DIABETES_TYP_2';
            $scope.$digest();

            expect($scope.model.diabetes.kost).toBeTruthy();
            expect($scope.model.diabetes.tabletter).toBeTruthy();
            expect($scope.model.diabetes.insulin).toBeTruthy();
        });
    });

    describe('medvetandestorning', function() {
        it('should reset hidden fields when "medvetandestorning" is set to false', function() {
            $scope.model.medvetandestorning.medvetandestorning = true;
            $scope.$digest();

            $scope.model.medvetandestorning.beskrivning = 'Hello';
            $scope.model.medvetandestorning.medvetandestorning = false;
            $scope.$digest();

            expect($scope.model.medvetandestorning.beskrivning).toBeUndefined();

            // Attic
            $scope.model.medvetandestorning.medvetandestorning = true;
            $scope.$digest();

            expect($scope.model.medvetandestorning.beskrivning).toBe('Hello');
        });
    });

    describe('narkotikaLakemedel', function() {
        it('should reset hidden fields when "teckenMissbruk" and "foremalForVardinsats" is set to false', function() {
            $scope.model.narkotikaLakemedel.teckenMissbruk = true;
            $scope.model.narkotikaLakemedel.foremalForVardinsats = true;
            $scope.$digest();

            // Set provtagning
            $scope.model.narkotikaLakemedel.provtagningBehovs = true;

            // One true, nothing changes
            $scope.model.narkotikaLakemedel.teckenMissbruk = false;
            $scope.$digest();

            expect($scope.model.narkotikaLakemedel.provtagningBehovs).toBeTruthy();

            // Still one true, nothing changes
            $scope.model.narkotikaLakemedel.teckenMissbruk = true;
            $scope.model.narkotikaLakemedel.foremalForVardinsats = false;
            $scope.$digest();

            expect($scope.model.narkotikaLakemedel.provtagningBehovs).toBeTruthy();

            // Both false, provtagning = true will be saved and cleared because field is invisible
            $scope.model.narkotikaLakemedel.teckenMissbruk = false;
            $scope.$digest();

            expect($scope.model.narkotikaLakemedel.provtagningBehovs).toBeUndefined();

            // Attic
            // One true again, provtagning = true should be restored from attic
            $scope.model.narkotikaLakemedel.teckenMissbruk = true;
            $scope.$digest();

            expect($scope.model.narkotikaLakemedel.provtagningBehovs).toBeTruthy();
        });

        it('should reset hidden fields when "lakarordineratLakemedelsbruk" is set to false', function() {
            $scope.model.narkotikaLakemedel.lakarordineratLakemedelsbruk = true;
            $scope.$digest();

            $scope.model.narkotikaLakemedel.lakemedelOchDos = 'Hello';
            $scope.model.narkotikaLakemedel.lakarordineratLakemedelsbruk = false;
            $scope.$digest();

            expect($scope.model.narkotikaLakemedel.lakemedelOchDos).toBeUndefined();

            // Attic
            $scope.model.narkotikaLakemedel.lakarordineratLakemedelsbruk = true;
            $scope.$digest();

            expect($scope.model.narkotikaLakemedel.lakemedelOchDos).toBe('Hello');
        });
    });

    describe('sjukhusvard', function() {
        it('should reset hidden fields when "sjukhusEllerLakarkontakt" is set to false', function() {
            $scope.model.sjukhusvard.sjukhusEllerLakarkontakt = true;
            $scope.$digest();

            $scope.model.sjukhusvard.tidpunkt = 'Förra veckan';
            $scope.model.sjukhusvard.vardinrattning = 'Sahlgrenska';
            $scope.model.sjukhusvard.anledning = 'Allt';
            $scope.model.sjukhusvard.sjukhusEllerLakarkontakt = false;
            $scope.$digest();

            expect($scope.model.sjukhusvard.tidpunkt).toBeUndefined();
            expect($scope.model.sjukhusvard.vardinrattning).toBeUndefined();
            expect($scope.model.sjukhusvard.anledning).toBeUndefined();

            // Attic
            $scope.model.sjukhusvard.sjukhusEllerLakarkontakt = true;
            $scope.$digest();

            expect($scope.model.sjukhusvard.tidpunkt).toBe('Förra veckan');
            expect($scope.model.sjukhusvard.vardinrattning).toBe('Sahlgrenska');
            expect($scope.model.sjukhusvard.anledning).toBe('Allt');
        });
    });

    describe('medicinering', function() {
        it('should reset hidden fields when "stadigvarandeMedicinering" is set to false', function() {
            $scope.model.medicinering.stadigvarandeMedicinering = true;
            $scope.$digest();

            $scope.model.medicinering.beskrivning = 'Hello';
            $scope.model.medicinering.stadigvarandeMedicinering = false;
            $scope.$digest();

            expect($scope.model.medicinering.beskrivning).toBeUndefined();

            // Attic
            $scope.model.medicinering.stadigvarandeMedicinering = true;
            $scope.$digest();

            expect($scope.model.medicinering.beskrivning).toBe('Hello');
        });
    });

    // Helper methods
    function getCheckboxForKorkortstyp(typ) {
        for (var i = 0; i < $scope.model.intygAvser.korkortstyp.length; i++) {
            if ($scope.model.intygAvser.korkortstyp[i].type === typ) {
                return $scope.model.intygAvser.korkortstyp[i];
            }
        }
        return null;
    }
});
