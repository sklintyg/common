/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

angular.module('ag114', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('ag114').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('ag114-view', {
            url :'/ag114/:intygTypeVersion/view/:certificateId',
            templateUrl: '/web/webjars/common/minaintyg/intyg/viewCert.html',
            controller: 'common.ViewCertCtrl',
            resolve: {
                viewConfigFactory: function(factoryResolverHelper, $stateParams) {
                    return factoryResolverHelper.resolve('ag114.viewConfigFactory', $stateParams);
                },
                viewFactory: function(factoryResolverHelper, $stateParams) {
                    return factoryResolverHelper.resolve('ag114.viewFactory', $stateParams);
                }
            },
            data : { title: 'Arbetsgivarintyg ', keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg'] }
        }).state('ag114-customize', {
            abstract: true, // jshint ignore:line
            url: '/:type/:intygTypeVersion/customize-ag114/:certificateId',
            templateUrl: '/web/webjars/ag114/minaintyg/views/customize-pdf.html',
            controller: 'ag114.CustomizePdfCtrl',
            data: {
                title: 'Anpassa intyget till arbetsgivare',
                keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg', 'anpassa'], backState: 'history-back' }

        }).state('ag114-customize.step1', {
            url: '/step1',
            views: {
                'header@ag114-customize': {
                    templateUrl: '/web/webjars/ag114/minaintyg/views/step1.header.html'
                },
                'body@ag114-customize': {
                    templateUrl: '/web/webjars/ag114/minaintyg/views/step1.body.html'
                }
            },
            data: {
                index: 0
            }

        }).state('ag114-customize.step2', {
            url: '/step2',
            views: {
                'header@ag114-customize': {
                    templateUrl: '/web/webjars/ag114/minaintyg/views/step2.header.html'
                },
                'body@ag114-customize': {
                    templateUrl: '/web/webjars/ag114/minaintyg/views/step2.body.html'
                }
            },
            data: {
                index: 1
            }

        }).state('ag114-customize.step3', {
            url: '/step3',
            views: {
                'header@ag114-customize': {
                    templateUrl: '/web/webjars/ag114/minaintyg/views/step3.header.html'
                },
                'body@ag114-customize': {
                    templateUrl: '/web/webjars/ag114/minaintyg/views/step3.body.html'
                }
            },
            data: {
                index: 2
            }

        });
});

// Inject language resources
angular.module('ag114').run(['common.messageService', 'ag114.messages',
    function(messageService, ag114Messages) {
        'use strict';

        messageService.addResources(ag114Messages);
    }]);
