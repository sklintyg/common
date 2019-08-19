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
angular.module('fk7263').service('fk7263.IntygController.ViewStateService',
    ['$log', 'common.IntygViewStateService', 'common.ObjectHelper', 'common.messageService',
        function($log, CommonViewState, ObjectHelper, messageService) {
            'use strict';

            this.common = CommonViewState;
            this.intygModel = {};

            this.reset = function() {
                this.common.reset();
                this.common.intygProperties.type = 'fk7263';
            };

            // Fix for Angular 1.4 / WEBCERT-2236
            this.has8a = function() {
                if (ObjectHelper.isFalsy(this.intygModel.nuvarandeArbetsuppgifter) &&
                    ObjectHelper.isFalsy(this.intygModel.arbetsloshet) &&
                    ObjectHelper.isFalsy(this.intygModel.foraldrarledighet)) {
                    return 'false';
                } else {
                    return 'true';
                }
            };

            this.shouldArbeteSpawnObservandum = function () {
                return !this.intygModel.arbetsloshet;
            };

            this.calculateNedsattMedDuration = function () { // jshint ignore:line

                var nedsattMedLevels = ['25', '50', '75', '100'];

                var isAtleastOneLevelValid = false;
                var i = 0;
                for(; i < nedsattMedLevels.length; i++){
                    if(this.intygModel['nedsattMed' + nedsattMedLevels[i]]){
                        isAtleastOneLevelValid = true;
                        break;
                    }
                }

                if(!isAtleastOneLevelValid){
                    return 0;
                }

                var duration;

                var startDate = null;
                var endDate = null;

                for(i = 0; i < nedsattMedLevels.length; i++){
                    var sjukskrivning = this.intygModel['nedsattMed' + nedsattMedLevels[i]];
                    if(!sjukskrivning){
                        continue;
                    }

                    var from = new moment (sjukskrivning.from);
                    if(startDate === null || from.isBefore(startDate)) {
                        startDate = from;
                    }
                    var tom = new moment (sjukskrivning.tom);
                    if(endDate === null || tom.isAfter(endDate)) {
                        endDate = tom;
                    }
                }

                if(startDate === null || endDate === null) {
                    return 0;
                }

                duration = moment.duration(endDate.diff(startDate));
                duration = duration.days() + 1;

                return duration;
            };

            /**
             * Visa observandum om:
             * Perioden intyget avser är kortare eller lika med 7 dagar
             * Alternativet Arbetslöshet är EJ valt.
             */
            this.getObservandumId = function() {

                var duration = this.calculateNedsattMedDuration();
                var shouldSysselsattningSpawnObservandum = this.shouldArbeteSpawnObservandum();

                if (duration <= 7 && shouldSysselsattningSpawnObservandum) {
                    return 'sjukpenning.label.send.obs.short.duration';
                }

                return null;
            };

            /**
             Lägg på text om observandum ska visas och returnera hela modellen
             */
            this.getSendContent = function(intygType) {

                var sendContentModel = {
                    observandumId: this.getObservandumId(),
                    bodyText: messageService.getProperty(intygType + '.label.send.body')
                };

                if(sendContentModel.observandumId) {
                    sendContentModel.bodyText = messageService.getProperty('common.label.send.body') + messageService.getProperty(intygType + '.label.send.body');
                }

                return sendContentModel;
            };

            this.reset();
        }]);
