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

describe('ts-diabetesUtkastConfigFactory', function() {
    'use strict';

    var element;
    var UtkastConfigFactory;
    var $scope;

    beforeEach(angular.mock.module('common', 'ts-diabetes'));
    beforeEach(inject(['$compile', '$rootScope', 'ts-diabetes.UtkastConfigFactory.v2', 'ts-diabetes.Domain.IntygModel.v2',
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

    describe('diabetes', function() {
        it('should reset hidden fields when "diabetes.insulin" is set to false', function() {
            $scope.model.diabetes.insulin = true;
            $scope.$digest();

            $scope.model.diabetes.insulinBehandlingsperiod = '2014';
            $scope.model.diabetes.insulin = false;
            $scope.$digest();

            expect($scope.model.diabetes.insulinBehandlingsperiod).toBeUndefined();

            // When reenabled the previously selected values should be remembered
            $scope.model.diabetes.insulin = true;
            $scope.$digest();
            expect($scope.model.diabetes.insulinBehandlingsperiod).toBe('2014');
        });

        it('should reset hidden fields when "diabetes.insulin" is set to false', function() {
            $scope.model.diabetes.insulin = true;
            $scope.model.diabetes.insulinBehandlingsperiod = '2014';
            $scope.$digest();

            $scope.model.diabetes.insulin = false;
            $scope.$digest();

            expect($scope.model.diabetes.insulinBehandlingsperiod).toBeUndefined();
        });
    });

    describe('hypoglykemier', function() {
        it('should reset hidden fields when "teckenNedsattHjarnfunktion" is set to false', function() {
            $scope.model.hypoglykemier.teckenNedsattHjarnfunktion = true;
            $scope.$digest();

            $scope.model.hypoglykemier.saknarFormagaKannaVarningstecken = true;
            $scope.model.hypoglykemier.allvarligForekomst = true;
            $scope.model.hypoglykemier.allvarligForekomstTrafiken = true;

            $scope.model.hypoglykemier.teckenNedsattHjarnfunktion = false;
            $scope.$digest();

            expect($scope.model.hypoglykemier.saknarFormagaKannaVarningstecken).toBeUndefined();
            expect($scope.model.hypoglykemier.allvarligForekomst).toBeUndefined();
            expect($scope.model.hypoglykemier.allvarligForekomstTrafiken).toBeUndefined();

            // When reenabled the previously selected values should be remembered
            $scope.model.hypoglykemier.teckenNedsattHjarnfunktion = true;
            $scope.$digest();
            expect($scope.model.hypoglykemier.saknarFormagaKannaVarningstecken).toBe(true);
            expect($scope.model.hypoglykemier.allvarligForekomst).toBe(true);
            expect($scope.model.hypoglykemier.allvarligForekomstTrafiken).toBe(true);
        });

        it('should reset hidden fields when "allvarligForekomst" is set to false', function() {
            $scope.model.hypoglykemier.teckenNedsattHjarnfunktion = true;
            $scope.model.hypoglykemier.allvarligForekomst = true;
            $scope.$digest();

            $scope.model.hypoglykemier.allvarligForekomstBeskrivning = 'Hello';
            $scope.model.hypoglykemier.allvarligForekomst = false;
            $scope.$digest();

            expect($scope.model.hypoglykemier.allvarligForekomstBeskrivning).toBe(undefined);

            // When reenabled the previously selected values should be remembered
            $scope.model.hypoglykemier.allvarligForekomst = true;
            $scope.$digest();
            expect($scope.model.hypoglykemier.allvarligForekomstBeskrivning).toBe('Hello');
        });

        it('should reset hidden fields when "allvarligForekomstTrafiken" is set to false', function() {
            $scope.model.hypoglykemier.teckenNedsattHjarnfunktion = true;
            $scope.model.hypoglykemier.allvarligForekomstTrafiken = true;
            $scope.$digest();

            $scope.model.hypoglykemier.allvarligForekomstTrafikBeskrivning = 'Hello';
            $scope.model.hypoglykemier.allvarligForekomstTrafiken = false;
            $scope.$digest();

            expect($scope.model.hypoglykemier.allvarligForekomstTrafikBeskrivning).toBe(undefined);

            // When reenabled the previously selected values should be remembered
            $scope.model.hypoglykemier.allvarligForekomstTrafiken = true;
            $scope.$digest();
            expect($scope.model.hypoglykemier.allvarligForekomstTrafikBeskrivning).toBe('Hello');
        });

        it('should reset hidden fields when "allvarligForekomstVakenTid" is set to false', function() {
            getCheckboxForKorkortstyp('C1').selected = true;
            $scope.model.hypoglykemier.allvarligForekomstVakenTid = true;
            $scope.$digest();

            $scope.model.hypoglykemier.allvarligForekomstVakenTidObservationstid = 'Hello';
            $scope.model.hypoglykemier.allvarligForekomstVakenTid = false;
            $scope.$digest();

            expect($scope.model.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe(undefined);

            // When reenabled the previously selected values should be remembered
            $scope.model.hypoglykemier.allvarligForekomstVakenTid = true;
            $scope.$digest();
            expect($scope.model.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe('Hello');
        });
    });

    describe('syn', function() {
        it('should reset hidden fields when "separatOgonlakarintyg" is set to true', function() {

            $scope.model.syn.separatOgonlakarintyg = true;
            $scope.$digest();

            $scope.model.syn.separatOgonlakarintyg = false;
            $scope.$digest();

            $scope.model.syn.hoger.utanKorrektion = '2.0';
            $scope.model.syn.hoger.medKorrektion = '2.0';
            $scope.model.syn.vanster.utanKorrektion = '2.0';
            $scope.model.syn.vanster.medKorrektion = '2.0';
            $scope.model.syn.binokulart.utanKorrektion = '2.0';
            $scope.model.syn.binokulart.medKorrektion = '2.0';
            $scope.model.syn.diplopi = false;

            $scope.model.syn.separatOgonlakarintyg = true;
            $scope.$digest();

            expect($scope.model.syn.hoger.utanKorrektion).toBeUndefined();
            expect($scope.model.syn.hoger.medKorrektion).toBeUndefined();
            expect($scope.model.syn.vanster.utanKorrektion).toBeUndefined();
            expect($scope.model.syn.vanster.medKorrektion).toBeUndefined();
            expect($scope.model.syn.binokulart.utanKorrektion).toBeUndefined();
            expect($scope.model.syn.binokulart.medKorrektion).toBeUndefined();
            expect($scope.model.syn.diplopi).toBeUndefined();

            // When reenabled the previously selected values should be remembered
            $scope.model.syn.separatOgonlakarintyg = false;
            $scope.$digest();
            expect($scope.model.syn.hoger.utanKorrektion).toBe('2.0');
            expect($scope.model.syn.hoger.utanKorrektion).toBe('2.0');
            expect($scope.model.syn.vanster.utanKorrektion).toBe('2.0');
            expect($scope.model.syn.vanster.medKorrektion).toBe('2.0');
            expect($scope.model.syn.binokulart.utanKorrektion).toBe('2.0');
            expect($scope.model.syn.binokulart.medKorrektion).toBe('2.0');
            expect($scope.model.syn.diplopi).toBe(false);
        });
    });

    describe('bedomning', function() {
        it('should reset hidden fields when "form.behorighet" is set to false', function() {

            $scope.model.bedomning.kanInteTaStallning = false;
            $scope.$digest();
            expect($scope.model.bedomning.kanInteTaStallning).toBeFalsy();

            angular.forEach($scope.model.bedomning.korkortstyp, function(korkortstyp) {
                korkortstyp.selected = true;
            });

            $scope.model.bedomning.kanInteTaStallning = true;
            $scope.$digest();

            expect($scope.model.bedomning.kanInteTaStallning).toBeTruthy();
            angular.forEach($scope.model.bedomning.korkortstyp, function(korkortstyp) {
                expect(korkortstyp.selected).toBeFalsy();
            });

            //// When reenabled the previously selected values should be remembered
            $scope.model.bedomning.kanInteTaStallning = false;
            $scope.$digest();

            expect($scope.model.bedomning.kanInteTaStallning).toBeFalsy();
            angular.forEach($scope.model.bedomning.korkortstyp, function(korkortstyp) {
                expect(korkortstyp.selected).toBeTruthy();
            });
        });
    });


    describe('intygAvser', function() {

        function checkKorkortstyp(typ, shouldExist) {
            expect(element.find('#form_hypoglykemier-egenkontrollBlodsocker').length).toBe(0);
            expect(element.find('#form_hypoglykemier-allvarligForekomstVakenTid').length).toBe(0);
            expect(element.find('#form_bedomning-lamplighetInnehaBehorighet').length).toBe(0);
            getCheckboxForKorkortstyp(typ).selected = true;
            $scope.$digest();
            expect(element.find('#form_hypoglykemier-egenkontrollBlodsocker').length).toBe(shouldExist ? 1 : 0);
            expect(element.find('#form_hypoglykemier-allvarligForekomstVakenTid').length).toBe(shouldExist ? 1 : 0);
            expect(element.find('#form_bedomning-lamplighetInnehaBehorighet').length).toBe(shouldExist ? 1 : 0);
            getCheckboxForKorkortstyp(typ).selected = false;
            $scope.$digest();
        }

        it('should show extra fields when some "korkortstyp"-options are selected', function() {
            checkKorkortstyp('C1', true);
            checkKorkortstyp('C1E', true);
            checkKorkortstyp('C', true);
            checkKorkortstyp('CE', true);
            checkKorkortstyp('D1', true);
            checkKorkortstyp('D1E', true);
            checkKorkortstyp('D', true);
            checkKorkortstyp('DE', true);
            checkKorkortstyp('TAXI', true);
            checkKorkortstyp('B', false);
        });

        it('should reset hidden fields when some "korkortstyp"-options are deselected', function() {

            // first check the korkort - restore attic
            getCheckboxForKorkortstyp('C1').selected = true;
            $scope.$digest();

            // set some values
            $scope.model.hypoglykemier.egenkontrollBlodsocker = true;
            // this is watched so we should call digest
            $scope.model.hypoglykemier.allvarligForekomstVakenTid = true;
            $scope.$digest();

            $scope.model.hypoglykemier.allvarligForekomstVakenTidObservationstid = '2014-10-10';
            $scope.model.bedomning.lamplighetInnehaBehorighet = true;
            $scope.$digest();

            // set korkort to false, - update attic
            getCheckboxForKorkortstyp('C1').selected = false;
            $scope.$digest();

            expect($scope.model.hypoglykemier.egenkontrollBlodsocker).toBeUndefined();
            expect($scope.model.hypoglykemier.allvarligForekomstVakenTid).toBeUndefined();
            expect($scope.model.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe(undefined);
            expect($scope.model.bedomning.lamplighetInnehaBehorighet).toBeUndefined();

            // re-enable korkot - restore attic, previous values should be visible
            getCheckboxForKorkortstyp('C1').selected = true;
            $scope.$digest();

            // this one works in the live but not here.. look on monday.
            expect($scope.model.hypoglykemier.egenkontrollBlodsocker).toBe(true);
            expect($scope.model.hypoglykemier.allvarligForekomstVakenTid).toBe(true);
            expect($scope.model.hypoglykemier.allvarligForekomstVakenTidObservationstid).toBe(
                '2014-10-10');
            expect($scope.model.bedomning.lamplighetInnehaBehorighet).toBe(true);
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