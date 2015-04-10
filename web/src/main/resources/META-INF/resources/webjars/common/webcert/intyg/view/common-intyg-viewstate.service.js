angular.module('common').service('common.IntygViewStateService',
    function() {
        'use strict';

        this.reset = function() {
            this.doneLoading = false;
            this.activeErrorMessageKey =  null;
            this.showTemplate = true;
            this.printStatus = 'notloaded';
            this.defaultRecipient = undefined;
        };

        this.reset();
    }
);
