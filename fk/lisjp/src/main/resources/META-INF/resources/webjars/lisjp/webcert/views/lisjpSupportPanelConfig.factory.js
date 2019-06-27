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
/**
 * Creates the supportpanel config related to working with lisjp intyg and utkast
 *
 * Created by marced on 2018-01-16.
 */
angular.module('lisjp').factory('lisjp.supportPanelConfigFactory',
    ['common.featureService', 'common.ResourceLinkService', function(featureService, ResourceLinkService) {
        'use strict';

        function _getConfig(id, intygTypeVersion, isSigned, isKompletteringsUtkast, isLocked, links) {

            var config = {
                tabs: [],
                intygContext: {
                    type: 'lisjp',
                    aboutMsgKey: 'FRM_2.RBK',
                    id: id,
                    intygTypeVersion: intygTypeVersion,
                    isSigned: isSigned,
                    isLocked: isLocked
                }
            };

            if (ResourceLinkService.isLinkTypeExists(links, 'LASA_FRAGA') && (isSigned || isKompletteringsUtkast)) {
                config.tabs.push({
                    id: 'wc-arende-panel-tab',
                    title: 'common.supportpanel.arende.title',
                    tooltip: 'common.supportpanel.arende.tooltip',
                    config: {
                        intygContext: config.intygContext
                    },
                    active: isSigned || isKompletteringsUtkast
                });
            }

            //Bara visas i utkastläge men inte låst, default aktiv bara om det inte är ett kompletteringsutkast
            if (!config.intygContext.isSigned && !config.intygContext.isLocked) {
                config.tabs.push({
                    id: 'wc-fmb-panel-tab',
                    title: 'common.supportpanel.fmb.title',
                    icon: 'lightbulb_outline',
                    tooltip: 'common.supportpanel.fmb.tooltip',
                    config: {
                        intygContext: config.intygContext
                    },
                    active: !(isSigned || isKompletteringsUtkast) && !_anyTabActive()
                });
            }

            if (featureService.isFeatureActive(featureService.features.SRS, config.intygContext.type) &&
                    !(isSigned || isKompletteringsUtkast || isLocked)) {
                config.tabs.push({
                    id: 'wc-srs-panel-tab',
                    title: 'common.supportpanel.srs.title',
                    icon: 'lightbulb_outline',
                    tooltip: 'common.supportpanel.srs.tooltip',
                    config: {
                        intygContext: config.intygContext
                    },
                    active: !_anyTabActive()
                });
            }

            //Default aktiv om signerat och inte ännu skickat
            config.tabs.push({
                id: 'wc-help-tips-panel-tab',
                title: 'common.supportpanel.help.title',
                tooltip: 'common.supportpanel.help.tooltip',
                config: {
                    intygContext: config.intygContext
                },
                active: !_anyTabActive()
            });

            function _anyTabActive() {
                var foundActive = false;
                angular.forEach(config.tabs, function(tab) {
                    if (tab.active) {
                        foundActive = true;
                    }
                });
                return foundActive;
            }

            return angular.copy(config);
        }

        return {
            getConfig: _getConfig
        };

    }]);
