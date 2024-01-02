/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('fk7263', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common' ]);

angular.module('fk7263').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('fk7263-view', {
            url :'/fk7263/:intygTypeVersion/view/:certificateId',
            templateUrl: '/web/webjars/common/minaintyg/intyg/viewCert.html',
            controller: 'common.ViewCertCtrl',
            resolve: {
                viewConfigFactory: ['fk7263.viewConfigFactory', function(viewConfigFactory) {
                    return viewConfigFactory;
                }],
                viewFactory: ['fk7263.viewFactory', function(viewFactory) {
                    return viewFactory;
                }]
            },
            data : { title: 'Läkarintyg FK7263', keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg']}
    });
});

// Inject language resources
angular.module('fk7263').run(['common.messageService', 'fk7263.messages',
    function(messageService, fk7263Messages) {
        'use strict';

        messageService.addResources(fk7263Messages);
    }]);
