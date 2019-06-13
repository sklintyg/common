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
angular.module('common').factory('common.srsLinkCreator', [function() {
    'use strict';
    var baseUrl = 'http://rattsjukskrivning.se/';

    function _createAtgardsekommendationLink(diagnosKod) {
        if (!angular.isString(diagnosKod)) {
            return undefined;
        }
        return baseUrl + diagnosKod.replace('.', '').toLowerCase();
    }

    function _createStatistikLink(diagnosKod) {
        if (!angular.isString(diagnosKod)) {
            return undefined;
        }
        return baseUrl + diagnosKod.replace('.', '').toLowerCase() + '-statistik';
    }

    function _createSamtyckeLink() {
        return baseUrl + 'samtycke';
    }

    function _createPrediktionsModellLink() {
        return baseUrl + 'prediktionsmodell';
    }

    return {
        createAtgardsrekommendationLink: _createAtgardsekommendationLink,
        createStatistikLink: _createStatistikLink,
        createSamtyckeLink: _createSamtyckeLink(),
        createPrediktionsModellLink: _createPrediktionsModellLink()
    };
}]);