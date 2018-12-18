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
angular.module('af00251', ['ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('af00251').config(function($stateProvider) {
    'use strict';

    $stateProvider.state('af00251-view', {
        url: '/af00251/:intygTypeVersion/view/:certificateId',
        templateUrl: '/web/webjars/af00251/minaintyg/views/view-cert.html',
        controller: 'af00251.ViewCertCtrl',
        resolve: {
            viewConfigFactory: function(factoryResolverHelper, $stateParams) {
              return factoryResolverHelper.resolve('af00251.viewConfigFactory', $stateParams);
          }
        },
        data: {
            title: 'Läkarintyg för sjukpenning',
            keepInboxTabActive: true,
            breadcrumb: ['inkorg', 'intyg']
        }
    }).state('af00251-fel', {
        url: '/af00251/fel/:errorCode',
        templateUrl: '/web/webjars/af00251/minaintyg/views/error.html',
        controller: 'af00251.ErrorCtrl',
        data: {
            title: 'Fel'
        }
    }).state('af00251-visafel', {
        url: '/af00251/visafel/:errorCode',
        templateUrl: '/web/webjars/af00251/minaintyg/views/error.html',
        controller: 'af00251.ErrorCtrl',
        data: {
            title: 'Fel',
            backLink: '/web/start'
        }
    });
});

// Inject language resources
angular.module('af00251').run(['common.messageService', 'af00251.messages', function(messageService, af00251Messages) {
    'use strict';

    messageService.addResources(af00251Messages);
}]);
