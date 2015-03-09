angular.module('common').factory('common.CertViewState',
    function($routeParams) {
        'use strict';

        var viewState = {
            error: {
                activeErrorMessageKey: null,
                saveErrorMessageKey: null
            },
            doneLoading: false,
            showComplete: false,
            collapsedHeader: false,
            hsaInfoMissing: false,
            vidarebefordraInProgress: false,
            hospName: $routeParams.hospName,
            deleted: false
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
