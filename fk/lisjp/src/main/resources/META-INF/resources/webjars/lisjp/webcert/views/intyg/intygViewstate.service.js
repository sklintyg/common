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
angular.module('lisjp').service('lisjp.IntygController.ViewStateService',
    ['$log', 'common.IntygViewStateService', 'common.messageService',
        function($log, CommonViewState, messageService) {
            'use strict';

            this.common = CommonViewState;
            this.intygModel = {};

            this.reset = function() {
                this.common.reset();
                this.common.intygProperties.type = 'lisjp';
            };

            this.shouldSysselsattningSpawnObservandum = function () {

                var occupationList = this.intygModel.sysselsattning;

                if(!occupationList){
                    return false;
                }

                var areCriteraForObservandumMet = true;
                occupationList.forEach(function (occupation) {
                    if (occupation.typ === 'ARBETSSOKANDE') {
                        areCriteraForObservandumMet = false;
                    } else if (occupation.typ === 'STUDIER') {
                        occupationList.forEach(function (occupation) {
                            if (occupation.typ  === 'NUVARANDE_ARBETE') {
                                areCriteraForObservandumMet = false;
                            }
                        });

                    }
                });
                return areCriteraForObservandumMet;
            };

            this.showEmployerPrintBtn = function() {
                if (this.intygModel.avstangningSmittskydd) {
                    return false;
                }

                return true;
            };

            this.calculateSjukskrivningDuration = function () {

                if(!this.intygModel.sjukskrivningar){
                    return 0;
                }

                var duration;

                var startDate = null;
                var endDate = null;

                this.intygModel.sjukskrivningar.forEach(function(sjukskrivning) {
                    var from = new moment (sjukskrivning.period.from);
                    if(startDate === null || from.isBefore(startDate)) {
                        startDate = from;
                    }
                    var tom = new moment (sjukskrivning.period.tom);
                    if(endDate === null || tom.isAfter(endDate)) {
                        endDate = tom;
                    }
                });

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
             * Alternativet Arbetssökande är EJ valt.
             * Alternativen Studerande och Nuvarande arbete är EJ valda samtidigt
             */
            this.getObservandumId = function() {

                var duration = this.calculateSjukskrivningDuration();
                var shouldSysselsattningSpawnObservandum = this.shouldSysselsattningSpawnObservandum();

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
