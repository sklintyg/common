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
angular.module('common').service('common.ArendeListViewStateService',
    ['$rootScope', 'common.IntygViewStateService', 'common.dynamicLabelService', 'common.UserModel', 'common.ArendeListItemModel',
        function($rootScope, IntygViewStateService, dynamicLabelService, UserModel, ArendeListItemModel) {
            'use strict';

            this.reset = function() {
                this.doneLoading = false;
                this.intygLoaded = false;
                this.activeErrorMessageKey = null;
                this.showTemplate = true;

                this.intyg = {};
                this.arendeList = [];
                this.intygProperties = {
                    isLoaded: false,
                    isSent: false,
                    isRevoked: false,
                    isInteractionEnabled: false,
                    type: undefined,
                    latestChildRelations: {
                        complementedByUtkast: false
                    }
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

            this.setArendeList = function(arendeList) {
                this.arendeList = this.createListItemsFromArenden(arendeList);
                this.updateKompletteringar();
                $rootScope.$broadcast('arenden.updated');
            };

            this.createListItemsFromArenden = function(arendeModelList) {
                var arendeListItemList = [];

                angular.forEach(arendeModelList, function (arendeModel) {
                    arendeListItemList.push(this.createArendeListItem(arendeModel));
                }, this);

                return arendeListItemList;
            };

            this.createArendeListItem = function(arendeModel) {
                var arendeListItem = ArendeListItemModel.build(arendeModel);
                return arendeListItem;
            };

            this.hasUnhandledItems = function() {
                var result = false;

                if (angular.isArray(this.arendeList)) {
                    angular.forEach(this.arendeList, function(arendeListItem) {
                        result = result || arendeListItem.arende.fraga.status !== 'CLOSED';
                    });
                }

                return result;
            };

            this.getKompletteringar = function(key) {
                 if (this.common.kompletteringar[key]) {
                    return this.common.kompletteringar[key];
                 }
                 return [];
            };

            this.updateKompletteringar = function() {
                var kompletteringar = {};
                angular.forEach(this.arendeList, function(arende) {
                    if (arende.arende.fraga.status === 'PENDING_INTERNAL_ACTION') {
                        angular.forEach(arende.arende.fraga.kompletteringar, function(komplettering) {
                            var key = komplettering.jsonPropertyHandle;
                            if (key) {
                                // Uppdatera ämne och status
                                komplettering.amne = arende.arende.fraga.amne;
                                komplettering.status = arende.arende.fraga.status;

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
                            }
                        });
                    }
                });

                this.common.kompletteringar = kompletteringar;
            };

            //Get matching kompletteringar for single arendeModel
            function addMatchingUnhandledFrageKomplettering(result, frageId, arendeModel) {
                if (arendeModel.isKomplettering() && arendeModel.arende.fraga.status === 'PENDING_INTERNAL_ACTION') {
                    angular.forEach(arendeModel.kompletteringar, function(komplettering) {
                        if (parseInt(komplettering.id, 10) === parseInt(frageId, 10)) {
                            result.push(komplettering);
                        }
                    });
                }
            }

            function addMatchingFrageKomplettering(result, frageId, arendeModel) {
                if (arendeModel.isKomplettering()) {
                    angular.forEach(arendeModel.kompletteringar, function(komplettering) {
                        if (parseInt(komplettering.id, 10) === parseInt(frageId, 10)) {
                            result.push(komplettering);
                        }
                    });
                }
            }

            //Return array with all unhandled kompletteringar for the given frageId
            this.getUnhandledKompletteringarForFraga = function(frageId) {
                var result = [];

                angular.forEach(this.arendeList, function(arendeModel) {
                    addMatchingUnhandledFrageKomplettering(result, frageId, arendeModel);
                });

                return result;
            };

            this.getKompletteringarForFraga = function(frageId) {
                var result = [];

                angular.forEach(this.arendeList, function(arendeModel) {
                    addMatchingFrageKomplettering(result, frageId, arendeModel);
                });

                return result;
            };

            this.getUnhandledKompletteringCount = function() {
                var count = 0;
                angular.forEach(this.arendeList, function(arendeModel) {
                    if (arendeModel.isKomplettering() && arendeModel.arende.fraga.status === 'PENDING_INTERNAL_ACTION') {
                        count++;
                    }
                });
                return count;

            };

            this.getUnhandledKompletteringTimestamps = function() {
                var timestamps = [];
                angular.forEach(this.arendeList, function(arendeModel) {
                    if (arendeModel.isKomplettering() && arendeModel.arende.fraga.status === 'PENDING_INTERNAL_ACTION') {
                        timestamps.push(arendeModel.arende.fraga.timestamp);
                    }
                });
                return timestamps;
            };

            this.reset();
        }
    ]
);
