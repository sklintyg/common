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
angular.module('common').service('common.fmbDiagnosInfoModel',
        function() {
            'use strict';

            function FmbDiagnosInfoModel() {
                this.formData = [];
                this.diagnosKod = undefined;
                this.diagnosTitle = undefined;
                this.relatedDiagnoses = undefined;
                this.diagnosBeskrivning = undefined;
                this.originalDiagnosKod = undefined;
                this.referenceDescription = undefined;
                this.referenceLink = undefined;
                this.hasInfo = false;
            }

            var transformFormData = function transformFormData(formData) {
                var transformedFormData = {};
                if (formData.forms) {
                    formData.forms.forEach(function(item) {
                        transformedFormData[item.name] = item.content;
                    });
                }

                return transformedFormData;
            };

            FmbDiagnosInfoModel.prototype.setState = function(formData, originalDiagnosKod, originalDiagnosBeskrivning){
                this.formData = transformFormData(formData);
                this.diagnosKod = formData.icd10Code;
                this.diagnosBeskrivning = formData.icd10Description || originalDiagnosBeskrivning;
                this.originalDiagnosKod = originalDiagnosKod;
                this.diagnosTitle = formData.diagnosTitle;
                this.relatedDiagnoses = formData.relatedDiagnoses;
                this.referenceDescription = formData.referenceDescription;
                this.referenceLink = formData.referenceLink;
                this.hasInfo = Object.keys(this.formData).length > 0;
                this.originalDiagnosBeskrivning = originalDiagnosBeskrivning;
            };

            FmbDiagnosInfoModel.prototype.getReference = function() {
                return {
                    desc: this.referenceDescription,
                    link: this.referenceLink
                };
            };

            FmbDiagnosInfoModel.prototype.getFormData = function(formKey, headingId) {
                var result;
                var form = this.formData[formKey];
                if (form) {
                    angular.forEach(form, function(data) {
                        if (data.heading === headingId) {
                            result = data;
                        }
                    });
                }
                return result;
            };

            FmbDiagnosInfoModel.build = function() {
                return new FmbDiagnosInfoModel();
            };

            return FmbDiagnosInfoModel;
        });
