
angular.module('luae_fs', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('luae_fs').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('luae_fs-view', {
            url :'/luae_fs/view/:certificateId',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/view-cert.html',
            controller: 'luae_fs.ViewCertCtrl',
            data : { title: 'Läkarutlåtande för aktivitetsersättning vid förlängd skolgång', keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg'] }
        }).
        state('luae_fs-fel', {
            url : '/luae_fs/fel/:errorCode',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/error.html',
            controller: 'luae_fs.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('luae_fs-visafel', {
            url :'/luae_fs/visafel/:errorCode',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/error.html',
            controller: 'luae_fs.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('luae_fs').run(['common.messageService', 'luae_fs.messages',
    function(messageService, luaeFsMessages) {
        'use strict';

        messageService.addResources(luaeFsMessages);
    }]);
