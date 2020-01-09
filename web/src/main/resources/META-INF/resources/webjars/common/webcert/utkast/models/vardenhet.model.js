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
angular.module('common').factory(
    'common.Domain.VardenhetModel',
    [ function() {
        'use strict';

        /**
         * Constructor, with class name
         */
        function VardenhetModel() {
            this.enhetsid = undefined;
            this.enhetsnamn = undefined;
            this.postadress = undefined;
            this.postnummer = undefined;
            this.postort = undefined;
            this.telefonnummer = undefined;
            this.epost = undefined;
            this.vardgivare = {
                vardgivarid: null,
                vardgivarnamn: null
            };
            this.arbetsplatsKod = undefined;
        }


        VardenhetModel.prototype.update = function(vardenhet) {
            // refresh the model data

            if(vardenhet === undefined) {
                return;
            }
            this.enhetsid = vardenhet.enhetsid;
            this.enhetsnamn = vardenhet.enhetsnamn;
            this.postadress = vardenhet.postadress;
            this.postnummer = vardenhet.postnummer;
            this.postort = vardenhet.postort;
            this.telefonnummer = vardenhet.telefonnummer;
            this.epost = vardenhet.epost;
            this.vardgivare = {
                vardgivarid: vardenhet.vardgivare.vardgivarid,
                vardgivarnamn: vardenhet.vardgivare.vardgivarnamn
            };
            this.arbetsplatsKod = vardenhet.arbetsplatsKod;
        };

        function isDefined(data) {
            return typeof data !== 'undefined' && data !== '';
        }

        VardenhetModel.prototype.isMissingInfo = function(){

            var props = [
                'postadress',
                'postnummer',
                'postort',
                'telefonnummer',
                'postadress',
                'postnummer',
                'postort',
                'telefonnummer'
            ];

            var missingInfo = false;
            angular.forEach(props, function(prop) {
                if(!isDefined(this[prop])) {
                    missingInfo = true;
                }
            }, this);

            return missingInfo;
        };

        VardenhetModel.build = function() {
            return new VardenhetModel();
        };

        /**
         * Return the constructor function VardenhetModel
         */
        return VardenhetModel;

    }]);
