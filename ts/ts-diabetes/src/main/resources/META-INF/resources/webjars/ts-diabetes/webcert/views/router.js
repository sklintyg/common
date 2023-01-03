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
/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-diabetes').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';
    //var intygsTypPath = '/web/webjars/ts-diabetes/webcert/';

    var editViewState = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('ts-diabetes.UtkastController.ViewStateService', $stateParams);
    };

    var utkastConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('ts-diabetes.UtkastConfigFactory', $stateParams);
    };

    var viewConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('ts-diabetes.viewConfigFactory', $stateParams);
    };

    var supportPanelConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('ts-diabetes.supportPanelConfigFactory', $stateParams);
    };

    $stateProvider.
        state('ts-diabetes', {
            url: '/ts-diabetes'
        }).
        state('ts-diabetes.utkast', {
            data: { defaultActive : 'index', intygType: 'ts-diabetes' },
            url: '/:intygTypeVersion/edit/:certificateId/:focusOn?:error&:ticket',
            params: {
                focusOn: ''
            },
            resolve: {
                ViewState: editViewState,
                UtkastConfigFactory: utkastConfig,
                supportPanelConfigFactory: supportPanelConfig
            },
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl'
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@ts-diabetes.utkast' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@ts-diabetes.utkast' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'utkast@ts-diabetes.utkast' : {
                    templateUrl: commonPath + 'utkast/smiUtkastUE.html',
                    controller: 'smi.EditCert.UECtrl'
                }
            }
        }).
        state('webcert.intyg.ts-diabetes', {
            data: { defaultActive: 'index', intygType: 'ts-diabetes' },
            url: '/intyg/ts-diabetes/:intygTypeVersion/:certificateId/:focusOn?:signed',
            params: {
                focusOn: ''
            },
            resolve: {
                ViewState: 'ts-diabetes.IntygController.ViewStateService',
                ViewConfigFactory: viewConfig,
                supportPanelConfigFactory: supportPanelConfig,
                IntygViewState: 'ts-diabetes.IntygController.ViewStateService'
            },
            views: {
                'intyg@webcert.intyg': {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv'
                },
                'header@webcert.intyg.ts-diabetes': {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader'
                }
            }
        });
});
