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
 * Creates the supportpanel config related to working with tstrk1062 intyg and utkast
 *
 * Created by marced on 2018-01-26.
 */
angular.module('tstrk1062').factory('tstrk1062.supportPanelConfigFactory', [ function() {
    'use strict';

    function _getConfig(id, intygTypeVersion, isSigned) {

        var config = {
            tabs: [],
            intygContext: {
                type: 'tstrk1062',
                id: id,
                aboutMsgKey: 'FRM_2.RBK',
                intygTypeVersion: intygTypeVersion,
                isSigned: isSigned
            }
        };

        //Always has this
        config.tabs.push({
            id: 'wc-help-tips-panel-tab',
            title: 'common.supportpanel.help.title',
            tooltip: 'common.supportpanel.help.tooltip',
            config: {
                intygContext: config.intygContext
            }
        });

        // First tab of those added should be active by default
        config.tabs[0].active = true;
        return angular.copy(config);
    }

    return {
        getConfig: _getConfig
    };

} ]);
