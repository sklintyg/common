/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('common').service('common.UtkastViewStateService',
    ['common.User', 'common.UtkastValidationViewState', 'common.UserModel', 'common.IntygHeaderViewState',
        function(commonUser, utkastValidationViewState, UserModel, IntygHeaderViewState) {
        'use strict';

        this.reset = function() {
            this.error = {
                activeErrorMessageKey : null,
                saveErrorMessage : null,
                saveErrorCode : null
            };
            this.intyg = {
                isComplete : false,
                type : undefined
            };
            //some drafts will be presented using uv-framwork, and need the "raw" utlatande-json as input.
            this.__utlatandeJson = null;

            // should go into intyg above
            this.showComplete = false;
            this.hsaInfoMissing = false;
            this.vidarebefordraInProgress = false;
            this.hospName = UserModel.getIntegrationParam('responsibleHospName');
            this.deleted = false;
            this.isSigned = false;
            this.textVersionUpdated = false;
            this.validPatientAddressAquiredFromPU = false;

            this.doneLoading = false;
            this.collapsedHeader = false;
            this.showHideButtonText = 'Dölj meny';
            this.saving = false;
            this.today = new Date();
            this.today.setHours(0, 0, 0, 0); // reset time to increase comparison accuracy (using new Date() also sets time)

            this.validation = utkastValidationViewState;
            this.validation.reset();

            IntygHeaderViewState.reset();
        };

        this.update = function(draftModel, data) {
            /* Note: data corresponds to DraftHolder dto in backend, which is also used as a wrapper for intyg not just utkast.
             * To find equivalent DTO-transformation for intyg instead of utkast, see  commonIntygViewstate.service.js */
            if(draftModel){
                draftModel.update(data);
                this.__utlatandeJson = data;
                this.error.activeErrorMessageKey = null;
                this.error.saveErrorMessage = null;
                this.error.saveErrorCode = null;

                this.isSigned = draftModel.isSigned();
                this.intyg.isComplete = draftModel.isSigned() || draftModel.isDraftComplete();

                this.validPatientAddressAquiredFromPU = data.validPatientAddressAquiredFromPU;

                // Check if new text version is available
                if (data.latestTextVersion &&
                    data.latestTextVersion !== draftModel.content.textVersion) {
                    // Update textversion to latest
                    draftModel.content.textVersion = data.latestTextVersion;
                    // Set flag to indicate text version has been updated
                    this.textVersionUpdated = true;
                }

                this.hsaInfoMissing = checkHsaInfo(data);
            }
        };

        function checkHsaInfo(data) {
            var vardenhetData = commonUser.getUser().valdVardenhet;
            if (vardenhetData.mottagningar !== undefined) {
                for (var enhetIndex = 0; enhetIndex < vardenhetData.mottagningar.length; enhetIndex++) {
                    if (vardenhetData.mottagningar[enhetIndex].id === data.content.grundData.skapadAv.vardenhet.enhetsid) {
                        vardenhetData = vardenhetData.mottagningar[enhetIndex];
                        break;
                    }
                }
            }

            if (vardenhetData.id === data.content.grundData.skapadAv.vardenhet.enhetsid) {
                var properties = ['postadress', 'postnummer', 'postort', 'telefonnummer'];
                for(var i = 0; i < properties.length; i++) {
                    var field = vardenhetData[properties[i]];
                    if(field === undefined || field === '') {
                        return true;
                    }
                }
            }
            return false;
        }

        this.setShowComplete = function(showComplete) {
            this.showComplete = showComplete;
            return this.showComplete;
        };

        this.toggleCollapsedHeader = function() {
            this.collapsedHeader = !this.collapsedHeader;
            if(this.collapsedHeader){
                this.showHideButtonText = 'Visa meny';
            } else {
                this.showHideButtonText = 'Dölj meny';
            }
        };

        this.setDoneLoading = function(val){
            this.doneLoading = val;
        };

        this.reset();
    }
    ]);
