/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('fk7263').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';
    var intygsTypPath = '/web/webjars/fk7263/webcert/';

    $stateProvider.
    state('fk7263', {
        url: '/fk7263'
    }).
    state('fk7263.utkast', {
        data: { defaultActive : 'index', intygType: 'fk7263'},
        url : '/:intygTypeVersion/edit/:certificateId/:focusOn',
        params: {
            focusOn: ''
        },
        resolve: {
            ViewState: 'fk7263.EditCertCtrl.ViewStateService'
        },
        views : {
            'header@': {
                templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
            },
            'content@': {
                templateUrl: commonPath + 'intyg/smiIntygUv.html',
                controller: 'fk7263.EditCertCtrl'
            },

            'header@fk7263.utkast': {
                templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                controller: 'common.UtkastHeader'
            }
        }
    }).
        state('webcert.intyg.fk7263', {
            data: { defaultActive : 'index', intygType: 'fk7263' },
            url:'/intyg/fk7263/:intygTypeVersion/:certificateId/:focusOn?:signed',
            params: {
                focusOn: ''
            },
            resolve: {
                supportPanelConfigFactory: 'fk7263.supportPanelConfigFactory',
                IntygViewState: 'fk7263.IntygController.ViewStateService'
            },
            views: {
                'intyg@webcert.intyg' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'fk7263.ViewCertCtrl'
                },
                'header@webcert.intyg.fk7263' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).
        state('webcert.fragasvar.fk7263', {
            data: { defaultActive : 'index', intygType: 'fk7263' },
            url: '/fragasvar/fk7263/:intygTypeVersion/:certificateId',
            resolve: {
                supportPanelConfigFactory: 'fk7263.supportPanelConfigFactory',
                IntygViewState: 'fk7263.IntygController.ViewStateService'
            },
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'fk7263.ViewCertCtrl'
                },
                'header@webcert.fragasvar.fk7263' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).state('fk7263-readonly', {
        data: { intygType: 'fk7263' },
        url: '/intyg-read-only/fk7263/:intygTypeVersion/:certificateId',
        resolve: {
            ViewState: 'fk7263.IntygController.ViewStateService',
            ViewConfigFactory: 'fk7263.viewConfigFactory',
            DiagnosExtractor: function() {
                return function (fk7263Model) {
                    return fk7263Model.diagnosKod;
                };
            }
        },
        views: {
            'content@': {
                templateUrl: commonPath + 'intyg/read-only-view/wcIntygReadOnlyView.template.html',
                controller: 'common.wcIntygReadOnlyViewController'
            }
        }
    });
});
