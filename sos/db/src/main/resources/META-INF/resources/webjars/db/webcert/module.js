angular.module('db', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common', 'formly' ]);

// Inject language resources
angular.module('db').run(['common.messageService', 'db.messages',
    function(messageService, dbMessages) {
        'use strict';

        messageService.addResources(dbMessages);
    }]);
