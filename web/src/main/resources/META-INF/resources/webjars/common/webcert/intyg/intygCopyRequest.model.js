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

/**
 * Created by BESA on 2015-02-09.
 */

angular.module('common').factory('common.IntygCopyRequestModel',
    [function() {
        'use strict';

        /**
         * Constructor
         */
        function IntygCopyRequestModel(intygId, intygType, patientPersonnummer, nyttPatientPersonnummer) {
            this.intygId = intygId;
            this.intygType = intygType;
            this.patientPersonnummer = patientPersonnummer;
            this.nyttPatientPersonnummer = nyttPatientPersonnummer;
        }

        IntygCopyRequestModel.build = function(data) {
            return new IntygCopyRequestModel(data.intygId, data.intygType, data.patientPersonnummer, data.nyttPatientPersonnummer);
        };

        return IntygCopyRequestModel;
    }
]);
