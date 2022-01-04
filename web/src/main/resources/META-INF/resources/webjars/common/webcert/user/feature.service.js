/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

        function _isFeatureActive(featureName, intygstyp) {
            if (!featureName) {
                return false;
            }

            var activeFeatures = UserModel.getActiveFeatures();

            if (!activeFeatures) {
                return false;
            }

            var feature = activeFeatures[featureName];

            if (feature === undefined || !feature.global)  {
                return false;
            }

            if (intygstyp && feature.intygstyper && feature.intygstyper.indexOf(intygstyp) === -1) {
                return false;
            }

            return true;
        }

        return {
            features: {
                HANTERA_FRAGOR: 'HANTERA_FRAGOR',
                HANTERA_INTYGSUTKAST: 'HANTERA_INTYGSUTKAST',
                HANTERA_INTYGSUTKAST_AVLIDEN: 'HANTERA_INTYGSUTKAST_AVLIDEN',
                FORNYA_INTYG: 'FORNYA_INTYG',
                MAKULERA_INTYG: 'MAKULERA_INTYG',
                MAKULERA_INTYG_KRAVER_ANLEDNING: 'MAKULERA_INTYG_KRAVER_ANLEDNING',
                SKAPA_NYFRAGA: 'SKAPA_NYFRAGA',
                SKICKA_INTYG: 'SKICKA_INTYG',
                SIGNERA_SKICKA_DIREKT: 'SIGNERA_SKICKA_DIREKT',
                UTSKRIFT: 'UTSKRIFT',
                ARBETSGIVARUTSKRIFT: 'ARBETSGIVARUTSKRIFT',
                JS_LOGGNING: 'JS_LOGGNING',
                JS_MINIFIED: 'JS_MINIFIED',
                UNIKT_INTYG: 'UNIKT_INTYG',
                UNIKT_INTYG_INOM_VG: 'UNIKT_INTYG_INOM_VG',
                UNIKT_UTKAST_INOM_VG: 'UNIKT_UTKAST_INOM_VG',
                SRS: 'SRS',
                IDP_CONNECTIVITY_CHECK: 'IDP_CONNECTIVITY_CHECK',
                BLOCKERA_FRISTAENDE: 'BLOCKERA_FRISTAENDE',
                VARNING_FRISTAENDE: 'VARNING_FRISTAENDE',
                SKRIV_UT_I_IFRAME: 'SKRIV_UT_I_IFRAME'
            },
            isFeatureActive: _isFeatureActive
        };
    }]);
