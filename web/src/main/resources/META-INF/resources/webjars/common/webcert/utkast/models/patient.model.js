/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
angular.module('common').factory('common.Domain.PatientModel',
    [function() {
        'use strict';

        /**
         * Constructor, with class name
         */
        function PatientModel() {
            this.personId = undefined;
            this.fullstandigtNamn = undefined;
            this.fornamn = undefined;
            this.mellannamn = undefined;
            this.efternamn = undefined;
            this.postadress = undefined;
            this.postnummer = undefined;
            this.postort = undefined;
            this.addressDetailsSourcePU = undefined;
            this.samordningsNummer = undefined;
            this.sekretessmarkering = undefined;
            this.avliden = undefined;
            this.testIndicator = undefined;
        }


        PatientModel.prototype.update = function(patient) {
            // refresh the model data
            if(patient === undefined) {
                return;
            }
            this.personId = patient.personId;
            this.fullstandigtNamn = patient.fullstandigtNamn;
            this.fornamn = patient.fornamn;
            this.mellannamn = patient.mellannamn;
            this.efternamn = patient.efternamn;
            this.postadress = patient.postadress;
            this.postnummer = patient.postnummer;
            this.postort = patient.postort;
            this.addressDetailsSourcePU = patient.addressDetailsSourcePU;
            this.samordningsNummer = patient.samordningsNummer;
            this.sekretessmarkering = patient.sekretessmarkering;
            this.avliden = patient.avliden;
            this.testIndicator = patient.testIndicator;
        };

        PatientModel.build = function() {
            return new PatientModel();
        };

        /**
         * Return the constructor function PatientModel
         */
        return PatientModel;

    }]);
