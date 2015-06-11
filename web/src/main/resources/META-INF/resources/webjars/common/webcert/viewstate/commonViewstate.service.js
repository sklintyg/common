angular.module('common').service('common.ViewStateService',
    function() {
        'use strict';

        this.reset = function() {
            this.sekretessmarkering = false;
            this.sekretessmarkeringError = false;
        };

        this.reset();
    }
);
