/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
angular.module('doi').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';

    var editViewState = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('doi.EditCertCtrl.ViewStateService', $stateParams);
    };

    var utkastConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('doi.UtkastConfigFactory', $stateParams);
    };


    var viewConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('doi.viewConfigFactory', $stateParams);
    };

    $stateProvider.
        state('doi', {
            url: '/doi'
        }).
        state('doi.utkast', {
            data: { defaultActive : 'index', intygType: 'doi', useFmb: false },
            url : '/:intygTypeVersion/edit/:certificateId/:focusOn?:error&:ticket',
            params: {
                focusOn: ''
            },
            resolve: {
                ViewState: editViewState,
                UtkastConfigFactory: utkastConfig,
                supportPanelConfigFactory: 'doi.supportPanelConfigFactory'
            },
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl'
                },
    
                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },
    
                'header@doi.utkast' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },
    
                'footer@doi.utkast' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },
    
                'utkast@doi.utkast' : {
                    templateUrl: commonPath + 'utkast/smiUtkastUE.html',
                    controller: 'smi.EditCert.UECtrl'
                }
            }
        }).
        state('webcert.intyg.doi', {
            data: { defaultActive : 'index', intygType: 'doi' },
            url:'/intyg/doi/:intygTypeVersion/:certificateId/',
            resolve: {
                ViewState: 'doi.IntygController.ViewStateService',
                ViewConfigFactory: viewConfig,
                supportPanelConfigFactory: 'doi.supportPanelConfigFactory',
                IntygViewState: 'doi.IntygController.ViewStateService'
            },
            views: {
                'intyg@webcert.intyg' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv'
                },
                'header@webcert.intyg.doi' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader'
                }
            }
        });
});
