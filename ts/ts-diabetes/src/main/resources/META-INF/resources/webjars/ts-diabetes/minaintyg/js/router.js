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
/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-diabetes').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('ts-diabetes-view', {
            url :'/ts-diabetes/:intygTypeVersion/view/:certificateId',
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/view-cert.html',
            controller: 'ts-diabetes.ViewCertCtrl',
            resolve: {
                viewConfigFactory: function(factoryResolverHelper, $stateParams) {
                    return factoryResolverHelper.resolve('ts-diabetes.viewConfigFactory', $stateParams);
                }
            },
            data:{title: 'Läkarintyg Transportstyrelsen diabetes', keepInboxTabActive: true,
                breadcrumb: ['inkorg', 'intyg']}
        }).
        state('ts-diabetes-visafel', {
            url :'/ts-diabetes/visafel/:errorCode',
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/error.html',
            controller: 'ts-diabetes.ErrorCtrl',
            data : { title: 'Fel', backLink: '/web/start' }
        });
});
