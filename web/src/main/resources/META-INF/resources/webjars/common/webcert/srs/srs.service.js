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

angular.module('common').factory('common.srsService', [
    '$http', '$q', '$log', 'common.srsViewState',
    function($http, $q, $log, viewState) {
        'use strict';

        function _updateDiagnosKod(diagnosKod) {
            console.log('updating diagnosKod: ' + diagnosKod);
            //viewState.diagnosKod = diagnosKod;
            viewState.setDiagnosKod(diagnosKod);
        }

        function _updateHsaId(hsaId) {
            viewState.hsaId = hsaId;
        }

        function _updateIntygsTyp(intygsTyp) {
            viewState.intygsTyp = intygsTyp;
        }

        function _updatePersonnummer(personnummer) {
            viewState.personId = personnummer;
        }

        // Return public API for the service
        return {
            updateDiagnosKod: _updateDiagnosKod,
            updateHsaId: _updateHsaId,
            updateIntygsTyp: _updateIntygsTyp,
            updatePersonnummer: _updatePersonnummer
        };
    }]);
