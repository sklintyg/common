/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

            var prefilled = {};

            function reset() {
                prefilled = {};
            }

            function isPrefilledValue(value) {
                return angular.isString(value) && value !== '';
            }

            /**
             * Call this before calling getPrefilledFields.
             */
            function _searchForPrefilledData(viewState) {
                reset();
                // Adress, enligt INTYG-5354.
                // Vi vet inte här om adressvärdena kommer från PU eller js, men det spelar ändå ingen roll vilket.
                prefilled.completeAddress = isPrefilledValue(viewState.intygModel.grundData.patient.postadress) &&
                    isPrefilledValue(viewState.intygModel.grundData.patient.postnummer) &&
                    isPrefilledValue(viewState.intygModel.grundData.patient.postort);
            }

            function _getPrefilledFields() {
                return prefilled;
            }

            return {
                searchForPrefilledData: _searchForPrefilledData,
                getPrefilledFields: _getPrefilledFields
            };
        }]);
