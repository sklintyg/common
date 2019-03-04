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
/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('tstrk1062').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('tstrk1062-view', {
            url :'/tstrk1062/:intygTypeVersion/view/:certificateId',
            templateUrl: '/web/webjars/tstrk1062/minaintyg/views/view-cert.html',
            controller: 'tstrk1062.ViewCertCtrl',
            resolve: {
                viewConfigFactory: function(factoryResolverHelper, $stateParams) {
                    return factoryResolverHelper.resolve('tstrk1062.viewConfigFactory', $stateParams);
                }
            },
            data:{title: 'Läkarintyg Transportstyrelsen Bas', keepInboxTabActive: true,
            breadcrumb: ['inkorg', 'intyg']}
        }).
        state('tstrk1062-visafel', {
            url :'/tstrk1062/visafel/:errorCode',
            templateUrl: '/web/webjars/tstrk1062/minaintyg/views/error.html',
            controller: 'tstrk1062.ErrorCtrl',
            data : { title: 'Fel', backLink: '/web/start' }
        });
});
