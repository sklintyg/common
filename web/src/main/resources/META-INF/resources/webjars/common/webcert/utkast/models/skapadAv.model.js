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
angular.module('common').factory('common.Domain.SkapadAvModel',
    ['common.Domain.VardenhetModel',
        function(VardenhetModel) {
            'use strict';

            /**
             * Constructor, with class name
             */
            function SkapadAv() {
                this.personId = undefined;
                this.fullstandigtNamn = undefined;
                this.forskrivarKod = undefined;
                this.vardenhet = VardenhetModel.build();
            }

            SkapadAv.prototype.update = function(skapadAv) {
                // refresh the model data
                if(skapadAv === undefined){
                    return;
                }
                this.personId = skapadAv.personId;
                this.fullstandigtNamn = skapadAv.fullstandigtNamn;
                this.forskrivarKod = skapadAv.forskrivarkod;
                this.vardenhet.update(skapadAv.vardenhet);
            };

            SkapadAv.build = function() {
                return new SkapadAv();
            };

            /**
             * Return the constructor function SkapadAv
             */
            return SkapadAv;

        }]);
