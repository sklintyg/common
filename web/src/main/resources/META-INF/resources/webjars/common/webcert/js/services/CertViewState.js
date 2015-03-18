angular.module('common').factory('common.CertViewState',
    function($stateParams) {
        'use strict';

        var viewState = {
            error: {
                activeErrorMessageKey: null,
                saveErrorMessageKey: null
            },
            intyg: {
                isComplete: false
            },
            doneLoading: false,
            showComplete: false,
            collapsedHeader: false,
            hsaInfoMissing: false,
            vidarebefordraInProgress: false,
            hospName: $stateParams.hospName,
            deleted: false,
            isSigned : false,
            validationMessages : null,
            validationMessagesGrouped : null
        };

        function _toggleShowComplete() {
            viewState.showComplete = !viewState.showComplete;
            return viewState.showComplete;
        }

        function _toggleCollapsedHeader() {
            viewState.collapsedHeader = !viewState.collapsedHeader;
        }

        return {
            viewState: viewState,
            toggleShowComplete: _toggleShowComplete,
            toggleCollapsedHeader: _toggleCollapsedHeader
        };
    }
);
