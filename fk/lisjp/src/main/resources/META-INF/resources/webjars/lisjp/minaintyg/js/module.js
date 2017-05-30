angular.module('lisjp', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('lisjp').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('lisjp-view', {
            url :'/lisjp/view/:certificateId',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/view-cert.html',
            controller: 'lisjp.ViewCertCtrl',
            data : { title: 'Läkarintyg för sjukpenning', keepInboxTabActive: true,
            breadcrumb: ['inkorg', 'intyg']}
        }).
        state('lisjp-customize', {
            url: '/lisjp/customize/:certificateId',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/customize-pdf.html',
            controller: 'lisjp.CustomizePdfCtrl',
            data: {title: 'Anpassa intyget till arbetsgivare', keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg', 'anpassa']}
        }).
        state('lisjp-customize-summary', {
            url: '/lisjp/customize/:certificateId/summary',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/customize-pdf-summary.html',
            controller: 'lisjp.CustomizePdfSummaryCtrl',
            data: {title: 'Summering anpassa intyget till arbetsgivare', keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg', 'anpassa']}
        }).
        state('lisjp-fel', {
            url : '/lisjp/fel/:errorCode',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/error.html',
            controller: 'lisjp.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('lisjp-visafel', {
            url :'/lisjp/visafel/:errorCode',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/error.html',
            controller: 'lisjp.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('lisjp').run(['common.messageService', 'lisjp.messages',
    function(messageService, lisjpMessages) {
        'use strict';

        messageService.addResources(lisjpMessages);
    }]);
