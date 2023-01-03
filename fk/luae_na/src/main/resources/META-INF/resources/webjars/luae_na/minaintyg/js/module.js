/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('luae_na', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('luae_na').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('luae_na-view', {
            url :'/luae_na/:intygTypeVersion/view/:certificateId',
            templateUrl: '/web/webjars/common/minaintyg/intyg/viewCert.html',
            controller: 'common.ViewCertCtrl',
            resolve: {
                viewConfigFactory: function(factoryResolverHelper, $stateParams) {
                    return factoryResolverHelper.resolve('luae_na.viewConfigFactory', $stateParams);
                },
                viewFactory: function(factoryResolverHelper, $stateParams) {
                    return factoryResolverHelper.resolve('luae_na.viewFactory', $stateParams);
                }
            },
            data : { title: 'Läkarintyg aktivitetsersättning nedsatt arbetsförmåga', keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg'] }
        });
});

// Inject language resources
angular.module('luae_na').run(['common.messageService', 'luae_na.messages',
    function(messageService, luaeNaMessages) {
        'use strict';

        messageService.addResources(luaeNaMessages);
    }]);
