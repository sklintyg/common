/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

angular.module('doi', ['ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('doi').config(function($stateProvider) {
    'use strict';

    $stateProvider.state('doi-view', {
        url: '/doi/view/:certificateId',
        templateUrl: '/web/webjars/doi/minaintyg/views/view-cert.html',
        controller: 'doi.ViewCertCtrl',
        data: {
            title: 'Läkarintyg för sjukpenning',
            keepInboxTabActive: true,
            breadcrumb: ['inkorg', 'intyg']
        }
    }).state('doi-customize', {
        abstract: true, // jshint ignore:line
        url: '/:type/customize/:certificateId',
        templateUrl: '/web/webjars/doi/minaintyg/views/customize-pdf.html',
        controller: 'doi.CustomizePdfCtrl',
        data: {
            title: 'Anpassa intyget till arbetsgivare',
            keepInboxTabActive: true,
            breadcrumb: ['inkorg', 'intyg', 'anpassa'], backState: 'history-back' }

    }).state('doi-customize.step1', {
        url: '/step1',
        views: {
            'header@doi-customize': {
                templateUrl: '/web/webjars/doi/minaintyg/views/step1.header.html'
            },
            'body@doi-customize': {
                templateUrl: '/web/webjars/doi/minaintyg/views/step1.body.html'
            }
        },
        data: {
            index: 0
        }

    }).state('doi-customize.step2', {
        url: '/step2',
        views: {
            'header@doi-customize': {
                templateUrl: '/web/webjars/doi/minaintyg/views/step2.header.html'
            },
            'body@doi-customize': {
                templateUrl: '/web/webjars/doi/minaintyg/views/step2.body.html'
            }
        },
        data: {
            index: 1
        }

    }).state('doi-customize.step3', {
        url: '/step3',
        views: {
            'header@doi-customize': {
                templateUrl: '/web/webjars/doi/minaintyg/views/step3.header.html'
            },
            'body@doi-customize': {
                templateUrl: '/web/webjars/doi/minaintyg/views/step3.body.html'
            }
        },
        data: {
            index: 2
        }

    }).state('doi-fel', {
        url: '/doi/fel/:errorCode',
        templateUrl: '/web/webjars/doi/minaintyg/views/error.html',
        controller: 'doi.ErrorCtrl',
        data: {
            title: 'Fel'
        }
    }).state('doi-visafel', {
        url: '/doi/visafel/:errorCode',
        templateUrl: '/web/webjars/doi/minaintyg/views/error.html',
        controller: 'doi.ErrorCtrl',
        data: {
            title: 'Fel',
            backLink: '/web/start'
        }
    });
});

// Inject language resources
angular.module('doi').run(['common.messageService', 'doi.messages', function(messageService, doiMessages) {
    'use strict';

    messageService.addResources(doiMessages);
}]);
