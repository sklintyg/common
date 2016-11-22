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

angular.module('common').service('common.ArendeListViewStateService',
    ['common.IntygViewStateService', function(IntygViewStateService) {
        'use strict';

        this.reset = function() {
            this.doneLoading = false;
            this.activeErrorMessageKey = null;
            this.showTemplate = true;

            this.intyg = {};
            this.intygProperties = {
                isLoaded: false,
                isSent: false,
                isRevoked: false,
                type: undefined,
                relations: []
            };

            // Injecting the IntygViewStateService service so client-side only changes
            // on the intyg page (such as a send/revoke) can trigger GUI updates in the Q&A view.
            this.common = IntygViewStateService;

            // Adding a list of kompletteringar to the IntygViewStateService.
            // Each entry in this list will have another list of kompletteringar
            this.common.kompletteringar = [];

            return this;
        };

        this.setIntygType = function(type) {
            this.intygProperties.type = type;
        };

        this.hasKompletteringar = function(key) {
            if (this.common.kompletteringar[key]) {
                return this.common.kompletteringar[key].length > 0;
            }
            return false;
        };

        this.getKompletteringar = function(key) {
             if (this.common.kompletteringar[key]) {
                return this.common.kompletteringar[key];
             }
             return [];
        };

        this.setKompletteringar = function(kompletteringar) {
            this.common.kompletteringar = kompletteringar;
        };

        this.updateKompletteringar = function(kompletteringar) {
            angular.forEach(kompletteringar, function(komplettering) {
                angular.forEach(komplettering, function(kmplt) {
                    var key = kmplt.jsonPropertyHandle;

                    // Reset and update with latest
                    this.common.kompletteringar[key] = [];
                    this.common.kompletteringar[key].push(kmplt);
                }, this);
            }, this);
        };

        this.updateKompletteringarArende = function(arende) {
            if (arende) {
                // Update kompletteringar in the common intyg view state
                var kompletteringar = {};
                angular.forEach(arende.fraga.kompletteringar, function(komplettering) {
                    komplettering.amne = arende.fraga.amne;
                    komplettering.status = arende.fraga.status;

                    var key = komplettering.jsonPropertyHandle;
                    if (key === 'tillaggsfragor') {
                        var tillaggsfragor = dynamicLabelService.getTillaggsFragor();
                        if (tillaggsfragor) {
                            for (var i = 0; i < tillaggsfragor.length; i++) {
                                if (tillaggsfragor[i].id === komplettering.frageId) {
                                    key += '[' + i + '].svar';
                                }
                            }
                        }
                    }
                    if (!kompletteringar[key]) {
                        kompletteringar[key] = [];
                    }

                    kompletteringar[key].push(komplettering);
                });
                this.updateKompletteringar(kompletteringar);
            }
        };

        this.reset();
    }]
);
