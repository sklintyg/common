angular.module('common').factory('common.IntygEditViewStateService',
    function($stateParams) {
        'use strict';

        function CommonViewState() {
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
        }

        CommonViewState.prototype.toggleShowComplete = function() {
            this.showComplete = !this.showComplete;
            return this.showComplete;
        };

        CommonViewState.prototype.toggleCollapsedHeader = function() {
            this.collapsedHeader = !this.collapsedHeader;
        };

        var _viewState = new CommonViewState();

        return _viewState;
    }
);
