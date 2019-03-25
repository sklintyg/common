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
angular.module('ts-diabetes').service('ts-diabetes.PatientHelperService.v3',
        function() {
            'use strict';

            // PS-008: Show for ts utkast
            function _showPatientNameChangedIntegration(isIntyg) {
                return false;
            }
            // PS-004: Show for signed ts
            function _showPatientNameChangedPU(isIntyg) {
                return false;
            }
            // PS-005: Show for signed ts
            function _showPatientAddressChangedPU(isIntyg) {
                return false;
            }
            // INTYG-5146: Show for ts utkast
            function _showMissingAddressParameter(isIntyg) {
                return false;
            }

            return {
                showPatientNameChangedIntegration: _showPatientNameChangedIntegration,
                showPatientNameChangedPU: _showPatientNameChangedPU,
                showPatientAddressChangedPU: _showPatientAddressChangedPU,
                showMissingAddressParameter: _showMissingAddressParameter
            };
        });
