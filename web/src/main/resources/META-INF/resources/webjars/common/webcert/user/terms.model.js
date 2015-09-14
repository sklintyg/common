angular.module('common').service('webcert.TermsState',
    function() {
        'use strict';

        this.reset = function() {
            this.termsAccepted = false;
            this.transitioning = false;
        };

        this.reset();
    }
);