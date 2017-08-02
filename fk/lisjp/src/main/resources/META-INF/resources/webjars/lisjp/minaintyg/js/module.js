angular.module('lisjp', ['ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('lisjp').config(function($stateProvider) {
    'use strict';

    $stateProvider.state('lisjp-view', {
        url: '/lisjp/view/:certificateId',
        templateUrl: '/web/webjars/lisjp/minaintyg/views/view-cert.html',
        controller: 'lisjp.ViewCertCtrl',
        data: {
            title: 'Läkarintyg för sjukpenning',
            keepInboxTabActive: true,
            breadcrumb: ['inkorg', 'intyg']
        }
    }).state('lisjp-customize', {
        abstract: true, // jshint ignore:line
        url: '/:type/customize/:certificateId',
        templateUrl: '/web/webjars/lisjp/minaintyg/views/customize-pdf.html',
        controller: 'lisjp.CustomizePdfCtrl',
        data: {
            title: 'Anpassa intyget till arbetsgivare',
            keepInboxTabActive: true,
            breadcrumb: ['inkorg', 'intyg', 'anpassa'], backState: 'history-back' }

    }).state('lisjp-customize.step1', {
        url: '/step1',
        views: {
            'header@lisjp-customize': {
                templateUrl: '/web/webjars/lisjp/minaintyg/views/step1.header.html'
            },
            'body@lisjp-customize': {
                templateUrl: '/web/webjars/lisjp/minaintyg/views/step1.body.html'
            }
        },
        data: {
            index: 0
        }

    }).state('lisjp-customize.step2', {
        url: '/step2',
        views: {
            'header@lisjp-customize': {
                templateUrl: '/web/webjars/lisjp/minaintyg/views/step2.header.html'
            },
            'body@lisjp-customize': {
                templateUrl: '/web/webjars/lisjp/minaintyg/views/step2.body.html'
            }
        },
        data: {
            index: 1
        }

    }).state('lisjp-customize.step3', {
        url: '/step3',
        views: {
            'header@lisjp-customize': {
                templateUrl: '/web/webjars/lisjp/minaintyg/views/step3.header.html'
            },
            'body@lisjp-customize': {
                templateUrl: '/web/webjars/lisjp/minaintyg/views/step3.body.html'
            }
        },
        data: {
            index: 2
        }

    }).state('lisjp-fel', {
        url: '/lisjp/fel/:errorCode',
        templateUrl: '/web/webjars/lisjp/minaintyg/views/error.html',
        controller: 'lisjp.ErrorCtrl',
        data: {
            title: 'Fel'
        }
    }).state('lisjp-visafel', {
        url: '/lisjp/visafel/:errorCode',
        templateUrl: '/web/webjars/lisjp/minaintyg/views/error.html',
        controller: 'lisjp.ErrorCtrl',
        data: {
            title: 'Fel',
            backLink: '/web/start'
        }
    });
});

// Inject language resources
angular.module('lisjp').run(['common.messageService', 'lisjp.messages', function(messageService, lisjpMessages) {
    'use strict';

    messageService.addResources(lisjpMessages);
}]);