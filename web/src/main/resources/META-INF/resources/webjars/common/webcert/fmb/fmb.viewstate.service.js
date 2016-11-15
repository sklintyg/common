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

angular.module('common').service('common.fmbViewState', [
    'common.fmbDiagnosInfoModel', 'common.ObjectHelper',
    function(fmbModel, ObjectHelper) {
        'use strict';

        this.diagnoses = {
            // The following properties are created and deleted on demand depending on FMB info available.
            // main: fmbModel.build(),
            // bi1: fmbModel.build(),
            // bi2: fmbModel.build()
        };
        this.activeDiagnos = 1;

        this.setState = function(diagnosType, formData, originalDiagnosKod) {

            if(!ObjectHelper.isEmpty(originalDiagnosKod) && !angular.isObject(this.diagnoses[diagnosType])){
                this.diagnoses[diagnosType] = fmbModel.build();
            }

            this.diagnoses[diagnosType].setState(formData, originalDiagnosKod);
        };

        this.reset = function(diagnosType, formData) {
            delete this.diagnoses[diagnosType];
        };
    }]);
