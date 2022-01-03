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
/**
 * Service for searching and storing information about whether certain data fields are prefilled for an utkast.
 */
angular.module('common').factory('common.PrefilledUserDataService',
    [
        function() {
            'use strict';

            function isPrefilledValue(value) {
                return angular.isString(value) && value !== '';
            }

            function _searchForPrefilledPatientData(patient) {
                if(angular.isUndefined(patient)) {
                    return undefined;
                }

                var prefilled = {};
                // Adress, enligt INTYG-5354.
                prefilled.completeAddress = isPrefilledValue(patient.postadress) &&
                    isPrefilledValue(patient.postnummer) &&
                    isPrefilledValue(patient.postort);
                return prefilled;
            }

            return {
                searchForPrefilledPatientData: _searchForPrefilledPatientData
                //getPrefilledFields: _getPrefilledFields
            };
        }]);
