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
angular.module('common').factory('common.Domain.GrundDataModel',
    [ 'common.Domain.SkapadAvModel', 'common.Domain.PatientModel', 'common.Domain.RelationModel',
        function(SkapadAvModel, PatientModel, RelationModel) {
            'use strict';

            /**
             * Constructor, with class name
             */
            function GrundDataModel() {
                this.skapadAv = SkapadAvModel.build();
                this.patient = PatientModel.build();
                this.relation = RelationModel.build();
                this.isTestIntyg = undefined;
            }

            GrundDataModel.prototype.update = function (grundData) {
                // refresh the model data
                if(grundData === undefined) {
                    return;
                }
                this.skapadAv.update(grundData.skapadAv);
                this.patient.update(grundData.patient);
                this.relation.update(grundData.relation);
                this.testIntyg = grundData.testIntyg;
            };

            GrundDataModel.build = function() {
                return new GrundDataModel();
            };

            /**
             * Return the constructor function GrundDataModel
             */
            return GrundDataModel;

        }
    ]);
