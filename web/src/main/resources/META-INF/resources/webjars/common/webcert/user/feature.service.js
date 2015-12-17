/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

angular.module('common').factory('common.featureService',
    [ 'common.UserModel', function(UserModel) {
        'use strict';

        function _isFeatureActive(feature, intygstyp) {
            if (!feature) {
                return false;
            }

            var activeFeatures = UserModel.getActiveFeatures();
            if (!activeFeatures || !activeFeatures.length) {
                return false;
            }

            if (activeFeatures.indexOf(feature) === -1) {
                return false;
            }

            if (intygstyp && activeFeatures.indexOf(feature + '.' + intygstyp) === -1) {
                return false;
            }

            return true;
        }

        return {
            features: {
                HANTERA_FRAGOR: 'hanteraFragor',
                HANTERA_INTYGSUTKAST: 'hanteraIntygsutkast',
                KOPIERA_INTYG: 'kopieraIntyg',
                MAKULERA_INTYG: 'makuleraIntyg',
                SKICKA_INTYG: 'skickaIntyg',
                ARBETSGIVARUTSKRIFT: 'arbetsgivarUtskrift',
                JS_LOGGNING: 'jsLoggning',
                JS_MINIFIED: 'jsMinified'
            },
            isFeatureActive: _isFeatureActive
        };
    }]);
