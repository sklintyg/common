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
angular.module('common').service('common.srsViewState',
    function() {
        'use strict';

        this.consent = '';
        this.opinion = '';
        this.opinionError = null;
        this.diagnosBeskrivning = '';
        this.diagnosKod = '';
        this.personId = '';
        this.hsaId = ''; // vardenhets hsa-id
        this.vardgivareHsaId = '';
        this.intygsTyp = '';
        this.shownFirstTime = false;
        this.clickedFirstTime = false;
        this.diagnosisCodes = null;
        this.diagnosisListFetching = null;
        this.errorMessage = '';
        this.srsApplicable = false;
        this.questions = [];
        this.statistik = {};
        this.atgarder = {};

                    
        this.prediction = {};
        this.prediction.description = '';
        this.prediction.prevalens = null;
        this.allQuestionsAnswered = false;
        this.showVisaKnapp = false;
        this.srsButtonVisible = true; // SRS window should not start in fixed position immediately.
        this.riskImage = '';

        this.consentInfo = '';
        this.consentError = '';

        this.atgarderInfo = '';
        this.atgarderError = '';

        this.statistikInfo = '';
        this.statistikError = '';

        this.prediktionInfo = '';
        this.prediktionError = '';

        this.activeTab = 'atgarder';

        this.userHasSrsFeature = false;

        this.status = {
            open: false
        };

        this.setHsaId = function(hsaId){
            this.hsaId = hsaId;
        };

        this.setVardgivareHsaId = function(vardgivareHsaId) {
            this.vardgivareHsaId = vardgivareHsaId;
        };

        this.setDiagnosKod = function(diagnosKod) {
            this.diagnosKod = diagnosKod;
            this.originalDiagnosKod = diagnosKod;
        };

        this.setDiagnosBeskrivning = function(diagnosBeskrivning){
            this.diagnosBeskrivning = diagnosBeskrivning;
        };

    });
