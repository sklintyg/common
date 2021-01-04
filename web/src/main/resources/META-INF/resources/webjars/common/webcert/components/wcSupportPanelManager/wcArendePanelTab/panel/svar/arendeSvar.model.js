/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
/**
 * Created by BESA on 2015-02-09.
 */

angular.module('common').factory('common.ArendeSvarModel',
    ['$log', 'common.UserModel', 'common.ObjectHelper',
        function($log, UserModel, ObjectHelper) {
            'use strict';

            /**
             * Constructor
             */
            function ArendeSvarModel(parentViewState, arendeListItem) {
                this.answerKompletteringWithText = false;
                this.draftState = undefined;
                this.update(parentViewState, arendeListItem);
            }

            ArendeSvarModel.prototype.update = function(parentViewState, arendeListItem) {

                this.intygProperties = parentViewState.intygProperties;

                // From intyg
                if (parentViewState.intyg) {
                    this.enhetsId = parentViewState.intyg.grundData.skapadAv.vardenhet.enhetsid;
                }
                else {
                    this.enhetsid = null;
                }

                // From fraga
                this.amne = arendeListItem.arende.fraga.amne;
                this.status = arendeListItem.arende.fraga.status;
                this.frageStallare = arendeListItem.arende.fraga.frageStallare;
                this.fragaInternReferens = arendeListItem.arende.fraga.internReferens;
                this.fragaMeddelande = arendeListItem.arende.fraga.meddelande;

                // From svar
                this.meddelande = arendeListItem.arende.svar.meddelande || arendeListItem.arende.draftText;
                this.internReferens = arendeListItem.arende.svar.internReferens;
                this.svarSkickadDatum = arendeListItem.arende.svar.svarSkickadDatum;
                this.vardaktorNamn = arendeListItem.arende.svar.vardaktorNamn;

                this.answeredWithIntyg = arendeListItem.arende.answeredWithIntyg;
                if(this.answeredWithIntyg){
                    // besvaradmedintyg overrides these fields when available
                    this.vardaktorNamn = this.answeredWithIntyg.namnetPaSkapareAvIntyg;
                    this.svarSkickadDatum = this.answeredWithIntyg.skickatDatum;
                }

                // From ArendeListItem
                this.kompletteringar = angular.copy(arendeListItem.kompletteringar);
            };

            ArendeSvarModel.build = function(parentViewState, arendeListItem) {
                return new ArendeSvarModel(parentViewState, arendeListItem);
            };

            return ArendeSvarModel;
        }
    ]);
