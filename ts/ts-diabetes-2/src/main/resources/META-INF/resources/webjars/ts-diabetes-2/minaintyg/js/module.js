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
angular.module('ts-diabetes-2', ['ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('ts-diabetes-2').config(function($stateProvider) {
    'use strict';

    $stateProvider.state('ts-diabetes-2-view', {
        url: '/ts-diabetes-2/view/:certificateId',
        templateUrl: '/web/webjars/ts-diabetes-2/minaintyg/views/view-cert.html',
        controller: 'ts-diabetes-2.ViewCertCtrl',
        data: {
            title: 'Läkarintyg för sjukpenning',
            keepInboxTabActive: true,
            breadcrumb: ['inkorg', 'intyg']
        }
    }).state('ts-diabetes-2-fel', {
        url: '/ts-diabetes-2/fel/:errorCode',
        templateUrl: '/web/webjars/ts-diabetes-2/minaintyg/views/error.html',
        controller: 'ts-diabetes-2.ErrorCtrl',
        data: {
            title: 'Fel'
        }
    }).state('ts-diabetes-2-visafel', {
        url: '/ts-diabetes-2/visafel/:errorCode',
        templateUrl: '/web/webjars/ts-diabetes-2/minaintyg/views/error.html',
        controller: 'ts-diabetes-2.ErrorCtrl',
        data: {
            title: 'Fel',
            backLink: '/web/start'
        }
    });
});

// Inject language resources
angular.module('ts-diabetes-2').run(['common.messageService', 'ts-diabetes-2.messages', function(messageService, tsDiabetes2Messages) {
    'use strict';

    messageService.addResources(tsDiabetes2Messages);
}]);
