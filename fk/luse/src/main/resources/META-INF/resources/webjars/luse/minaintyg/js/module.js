
angular.module('luse', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('luse').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('luse-view', {
            url :'/luse/view/:certificateId',
            templateUrl: '/web/webjars/luse/minaintyg/views/view-cert.html',
            controller: 'luse.ViewCertCtrl',
            data : { title: 'Läkarutlåtande för sjukersättning', keepInboxTabActive: true }
        }).
        state('luse-statushistory', {
            url : '/luse/statushistory',
            templateUrl: '/web/webjars/luse/minaintyg/views/status-history.html',
            controller: 'luse.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('luse-fel', {
            url : '/luse/fel/:errorCode',
            templateUrl: '/web/webjars/luse/minaintyg/views/error.html',
            controller: 'luse.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('luse-visafel', {
            url :'/luse/visafel/:errorCode',
            templateUrl: '/web/webjars/luse/minaintyg/views/error.html',
            controller: 'luse.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('luse').run(['common.messageService', 'luse.messages',
    function(messageService, luseMessages) {
        'use strict';

        messageService.addResources(luseMessages);
    }]);
