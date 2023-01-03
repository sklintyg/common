/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('ts-bas').service('ts-bas.UtkastController.ViewStateService.v7',
    ['$log', 'ts-bas.Domain.IntygModel.v7', 'common.UtkastViewStateService',
        function($log, IntygModel, CommonViewState) {
            'use strict';

            this.common = CommonViewState;

            this.intygModel = undefined;
            this.draftModel = undefined;

            this.clearModel = function() {
                this.intygModel = undefined;
                this.draftModel = undefined;
            };

            this.setDraftModel = function(draftModel) {
                this.draftModel = draftModel;
                this.intygModel = draftModel.content;
            };

            // STATIC CONSTANT: Input limits on text fields
            this.inputLimits = {
                'funktionsnedsattning': 180,
                'beskrivningRiskfaktorer': 180,
                'medvetandestorning': 180,
                'lakemedelOchDos': 180,
                'medicinering': 180,
                'kommentar': 500,
                'lakareSpecialKompetens': 130,
                'sjukhusvardtidpunkt': 40,
                'sjukhusvardvardinrattning': 40,
                'sjukhusvardanledning': 50
            };

            this.reset = function() {

                CommonViewState.reset();
                CommonViewState.intyg.type = 'ts-bas';
                this.setDraftModel(IntygModel._members.build());
                return this;
            };

            /* jshint ignore:start */
            // This is not so pretty, but necessary? Can it be improved?
            // this could be moved into the model as a linked property, although thats alot of properties ...
            // maybe introduce an after update so the update of the linked property happens at
            // the end of update cycle instead of each individual property change.
            function noKravYtterligareUnderlagFieldsFilled(intygModel) {
                return (!intygModel.syn && !intygModel.horselBalans && !intygModel.funktionsnedsattning &&
                    !intygModel.hjartKarl && !intygModel.diabetes && !intygModel.neurologi &&
                    !intygModel.medvetandestorning && !intygModel.njurar && !intygModel.kognitivt &&
                    !intygModel.somnVakenhet && !intygModel.narkotikaLakemedel && !intygModel.psykiskt &&
                    !intygModel.utvecklingsstorning && !intygModel.sjukhusvard && !intygModel.medicinering);
            }

            function anyKravYtterligareUnderlagFieldsFilled(intygModel) {
                return (intygModel.syn.synfaltsdefekter === true || intygModel.syn.nattblindhet === true ||
                    intygModel.syn.progressivOgonsjukdom === true || intygModel.syn.diplopi === true ||
                    intygModel.syn.nystagmus === true || intygModel.horselBalans.balansrubbningar === true ||
                    intygModel.horselBalans.svartUppfattaSamtal4Meter === true ||
                    intygModel.funktionsnedsattning.funktionsnedsattning === true ||
                    intygModel.funktionsnedsattning.otillrackligRorelseformaga === true ||
                    intygModel.hjartKarl.hjartKarlSjukdom === true ||
                    intygModel.hjartKarl.hjarnskadaEfterTrauma === true ||
                    intygModel.hjartKarl.riskfaktorerStroke === true ||
                    intygModel.diabetes.harDiabetes === true || intygModel.neurologi.neurologiskSjukdom === true ||
                    intygModel.medvetandestorning.medvetandestorning === true ||
                    intygModel.njurar.nedsattNjurfunktion === true ||
                    intygModel.kognitivt.sviktandeKognitivFunktion === true ||
                    intygModel.somnVakenhet.teckenSomnstorningar === true ||
                    intygModel.narkotikaLakemedel.teckenMissbruk === true ||
                    intygModel.narkotikaLakemedel.foremalForVardinsats === true ||
                    intygModel.narkotikaLakemedel.provtagningBehovs === true ||
                    intygModel.narkotikaLakemedel.lakarordineratLakemedelsbruk ||
                    intygModel.psykiskt.psykiskSjukdom === true ||
                    intygModel.utvecklingsstorning.psykiskUtvecklingsstorning === true ||
                    intygModel.utvecklingsstorning.harSyndrom === true ||
                    intygModel.sjukhusvard.sjukhusEllerLakarkontakt === true ||
                    intygModel.medicinering.stadigvarandeMedicinering === true);
            }

            this.updateKravYtterligareUnderlag = function() {
                this.kravYtterligareUnderlag = false;
                if (!this.intygModel || noKravYtterligareUnderlagFieldsFilled(this.intygModel)) {
                    this.kravYtterligareUnderlag = false;
                } else if(anyKravYtterligareUnderlagFieldsFilled(this.intygModel)) {
                    this.kravYtterligareUnderlag = true;
                }
            };
            /* jshint ignore:end */

            this.getValidationErrors = function(field) {
                if (!this.common.validation.messagesByField) {
                    return null;
                }
                return this.common.validation.messagesByField[field.toLowerCase()];
            };

            this.reset();
        }]);
