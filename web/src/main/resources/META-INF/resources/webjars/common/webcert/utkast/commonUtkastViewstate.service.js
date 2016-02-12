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

angular.module('common').service('common.UtkastViewStateService',
    ['$stateParams', '$window', 'common.ViewStateService', function($stateParams, $window, commonViewStateService) {
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

            // should go into intyg above
            this.showComplete = false;
            this.hsaInfoMissing = false;
            this.vidarebefordraInProgress = false;
            this.hospName = $stateParams.hospName;
            this.deleted = false;
            this.isSigned = false;
            this.textVersionUpdated = false;
            this.textVersionConfirmed = false;
            this.validationSections  = null;
            this.validationMessages  = null;
            this.validationMessagesGrouped  = null;

            this.doneLoading = false;
            this.collapsedHeader = false;
            this.saving = false;
            this.headerSize = {width:0, height: 250};
            this.today = new Date();
            this.today.setHours(0, 0, 0, 0); // reset time to increase comparison accuracy (using new Date() also sets time)

            this.common = commonViewStateService;
            this.common.reset();
        };

        this.update = function(draftModel, data) {
            if(draftModel){
                draftModel.update(data);
                this.error.activeErrorMessageKey = null;
                this.error.saveErrorMessage = null;
                this.error.saveErrorCode = null;

                this.isSigned = draftModel.isSigned();
                this.intyg.isComplete = draftModel.isSigned() || draftModel.isDraftComplete();

                // Check if new text version is available
                if (data.latestTextVersion &&
                    data.latestTextVersion !== draftModel.content.textVersion) {
                    // Update textversion to latest
                    draftModel.content.textVersion = data.latestTextVersion;
                    // Set flag to indicate text version has been updated
                    this.textVersionUpdated = true;
                }

                // check if all info is available from HSA. If not, display the info message that someone needs to update it
                this.hsaInfoMissing = false;
                var vardenhetData = draftModel.content.grundData.skapadAv.vardenhet;
                var properties = ['postadress', 'postnummer', 'postort', 'telefonnummer'];
                for(var i = 0; i < properties.length; i++) {
                    var field = vardenhetData[properties[i]];
                    if(field === undefined || field === '') {
                        this.hsaInfoMissing = true;
                        break;
                    }
                }
            }
        };

        this.toggleShowComplete = function() {
            this.showComplete = !this.showComplete;
            return this.showComplete;
        };

        this.toggleCollapsedHeader = function() {
            this.collapsedHeader = !this.collapsedHeader;
        };

        this.setDoneLoading = function(val){
            this.doneLoading = val;
            $window.doneLoading = val;
        };

        this.reset();
    }
]);
