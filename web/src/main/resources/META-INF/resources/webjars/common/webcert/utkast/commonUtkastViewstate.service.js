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
            this.doneLoading = false;
            this.collapsedHeader = false;
            this.saving = false;

            // TODO: should go into intyg
            this.showComplete = false;
            this.hsaInfoMissing = false;
            this.vidarebefordraInProgress = false;
            this.hospName = $stateParams.hospName;
            this.deleted = false;
            this.isSigned = false;
            this.validationSections  = null;
            this.validationMessages  = null;
            this.validationMessagesGrouped  = null;

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
