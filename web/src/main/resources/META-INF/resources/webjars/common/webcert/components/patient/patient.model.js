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
angular.module('common').service('common.PatientModel',
    ['$log', 'common.ObjectHelper',
        function($log, ObjectHelper) {
            'use strict';

            this.isValid = function() {
                if (ObjectHelper.isEmpty(this.personnummer) ||
                    ObjectHelper.isEmpty(this.fornamn) ||
                    ObjectHelper.isEmpty(this.efternamn)) {
                    return false;
                }
                return true;
            };

            this.build = function() {
                this.personnummer = null;
                this.sekretessmarkering = null;
                this.intygId = null;
                this.intygType = 'default';
                this.fornamn = null;
                this.mellannamn = null;
                this.efternamn = null;
                this.fullstandigtNamn = null;
                this.postadress = null;
                this.postnummer = null;
                this.postort = null;
                this.avliden = null;
                this.testIndicator = null;
                return this;
            };
            this.update = function(patientResponse) {
                this.personnummer = patientResponse.personnummer;
                this.sekretessmarkering = patientResponse.sekretessmarkering;
                this.intygId = patientResponse.intygId;
                this.intygType = patientResponse.intygType;
                this.fornamn = patientResponse.fornamn;
                this.mellannamn = patientResponse.mellannamn;
                this.efternamn = patientResponse.efternamn;
                this.fullstandigtNamn = patientResponse.fullstandigtNamn;
                this.postadress = patientResponse.postadress;
                this.postnummer = patientResponse.postnummer;
                this.postort = patientResponse.postort;
                this.avliden = patientResponse.avliden;
                this.testIndicator = patientResponse.testIndicator;
                return this.isValid();
            };

            this.build();
        }]);
