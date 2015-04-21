angular.module('common').service('common.IntygViewStateService',
    function() {
        'use strict';

        this.reset = function() {
            this.doneLoading = false;
            this.activeErrorMessageKey = null;
            this.inlineErrorMessageKey = null;
            this.showTemplate = true;

            this.deleted = false;

            this.intyg = {
                type: undefined,
                defaultRecipient: undefined,
                isSent: false,
                isRevoked: false,
                printStatus: 'notloaded', // used in intyg-header.html only and set in intyg view controllers: 'signed' or 'revoked'
                newPatientId: false // FK only for now. Consider making specific viewState services for each intyg as with utkast
            };
        };

        this.reset();
    }
);
