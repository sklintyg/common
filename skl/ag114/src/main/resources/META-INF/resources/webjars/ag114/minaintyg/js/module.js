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

angular.module('ag114', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('ag114').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('ag114-view', {
            url :'/ag114/:intygTypeVersion/view/:certificateId',
            templateUrl: '/web/webjars/ag114/minaintyg/views/view-cert.html',
            controller: 'ag114.ViewCertCtrl',
            resolve: {
                viewConfigFactory: function(factoryResolverHelper, $stateParams) {
                    return factoryResolverHelper.resolve('ag114.viewConfigFactory', $stateParams);
                }
            },
            data : { title: 'Läkarutlåtande för aktivitetsersättning vid förlängd skolgång', keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg'] }
        }).
        state('ag114-fel', {
            url : '/ag114/fel/:errorCode',
            templateUrl: '/web/webjars/ag114/minaintyg/views/error.html',
            controller: 'ag114.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('ag114-visafel', {
            url :'/ag114/visafel/:errorCode',
            templateUrl: '/web/webjars/ag114/minaintyg/views/error.html',
            controller: 'ag114.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('ag114').run(['common.messageService', 'ag114.messages',
    function(messageService, ag114Messages) {
        'use strict';

        messageService.addResources(ag114Messages);
    }]);
