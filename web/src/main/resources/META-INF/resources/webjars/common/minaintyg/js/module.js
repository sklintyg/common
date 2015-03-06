/* global commonMessages */
angular.module('common', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize' ]);

// Inject language resources
angular.module('common').run([ 'common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(commonMessages);
    }]);
