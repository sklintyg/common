/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
angular.module('afmu', ['ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('afmu').config(function($stateProvider) {
    'use strict';

    $stateProvider.state('afmu-view', {
        url: '/afmu/view/:certificateId',
        templateUrl: '/web/webjars/afmu/minaintyg/views/view-cert.html',
        controller: 'afmu.ViewCertCtrl',
        data: {
            title: 'Läkarintyg för sjukpenning',
            keepInboxTabActive: true,
            breadcrumb: ['inkorg', 'intyg']
        }
    }).state('afmu-customize', {
        abstract: true, // jshint ignore:line
        url: '/:type/customize/:certificateId',
        templateUrl: '/web/webjars/afmu/minaintyg/views/customize-pdf.html',
        controller: 'afmu.CustomizePdfCtrl',
        data: {
            title: 'Anpassa intyget till arbetsgivare',
            keepInboxTabActive: true,
            breadcrumb: ['inkorg', 'intyg', 'anpassa'], backState: 'history-back' }

    }).state('afmu-customize.step1', {
        url: '/step1',
        views: {
            'header@afmu-customize': {
                templateUrl: '/web/webjars/afmu/minaintyg/views/step1.header.html'
            },
            'body@afmu-customize': {
                templateUrl: '/web/webjars/afmu/minaintyg/views/step1.body.html'
            }
        },
        data: {
            index: 0
        }

    }).state('afmu-customize.step2', {
        url: '/step2',
        views: {
            'header@afmu-customize': {
                templateUrl: '/web/webjars/afmu/minaintyg/views/step2.header.html'
            },
            'body@afmu-customize': {
                templateUrl: '/web/webjars/afmu/minaintyg/views/step2.body.html'
            }
        },
        data: {
            index: 1
        }

    }).state('afmu-customize.step3', {
        url: '/step3',
        views: {
            'header@afmu-customize': {
                templateUrl: '/web/webjars/afmu/minaintyg/views/step3.header.html'
            },
            'body@afmu-customize': {
                templateUrl: '/web/webjars/afmu/minaintyg/views/step3.body.html'
            }
        },
        data: {
            index: 2
        }

    }).state('afmu-fel', {
        url: '/afmu/fel/:errorCode',
        templateUrl: '/web/webjars/afmu/minaintyg/views/error.html',
        controller: 'afmu.ErrorCtrl',
        data: {
            title: 'Fel'
        }
    }).state('afmu-visafel', {
        url: '/afmu/visafel/:errorCode',
        templateUrl: '/web/webjars/afmu/minaintyg/views/error.html',
        controller: 'afmu.ErrorCtrl',
        data: {
            title: 'Fel',
            backLink: '/web/start'
        }
    });
});

// Inject language resources
angular.module('afmu').run(['common.messageService', 'afmu.messages', function(messageService, afmuMessages) {
    'use strict';

    messageService.addResources(afmuMessages);
}]);
