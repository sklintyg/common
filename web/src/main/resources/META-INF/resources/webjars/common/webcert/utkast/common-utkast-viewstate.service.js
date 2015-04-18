angular.module('common').service('common.UtkastViewStateService',
    function($stateParams) {
        'use strict';

        this.reset = function() {
            this.error = {
                activeErrorMessageKey : null,
                saveErrorMessageKey : null
            };
            this.intyg = {
                isComplete : false,
                typ : undefined
            };
            this.doneLoading = false;
            this.collapsedHeader = false;

            // TODO: should go into intyg
            this.showComplete = false;
            this.hsaInfoMissing = false;
            this.vidarebefordraInProgress = false;
            this.hospName = $stateParams.hospName;
            this.deleted = false;
            this.isSigned = false;
            this.validationMessages  = null;
            this.validationMessagesGrouped  = null;

            this.draftModel = undefined;
            this.intygModel = undefined;
        };

        this.toggleShowComplete = function() {
            this.showComplete = !this.showComplete;
            return this.showComplete;
        };

        this.toggleCollapsedHeader = function() {
            this.collapsedHeader = !this.collapsedHeader;
        };

        this.reset();
    }
);
