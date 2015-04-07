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