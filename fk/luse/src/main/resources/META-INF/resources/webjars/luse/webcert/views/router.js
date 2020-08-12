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
angular.module('luse').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';

    var editViewState = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('luse.EditCertCtrl.ViewStateService', $stateParams);
    };

    var utkastConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('luse.UtkastConfigFactory', $stateParams);
    };

    var viewConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('luse.viewConfigFactory', $stateParams);
    };

    $stateProvider.
        state('luse', {
            url: '/luse'
        }).
        state('luse.utkast', {
            data: { defaultActive : 'index', intygType: 'luse' },
            url : '/:intygTypeVersion/edit/:certificateId/:focusOn',
            params: {
                focusOn: ''
            },
            resolve: {
                ViewState: editViewState,
                UtkastConfigFactory: utkastConfig,
                supportPanelConfigFactory: 'luse.supportPanelConfigFactory'
            },
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl'
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@luse.utkast' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@luse.utkast' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },
                'utkast@luse.utkast' : {
                    templateUrl: commonPath + 'utkast/smiUtkastUE.html',
                    controller: 'smi.EditCert.UECtrl'
                }
            }
        }).
        state('webcert.intyg.luse', {
            data: { defaultActive : 'index', intygType: 'luse' },
            url:'/intyg/luse/:intygTypeVersion/:certificateId/:focusOn?:signed',
            params: {
                focusOn: ''
            },
            resolve: {
                ViewState: 'luse.IntygController.ViewStateService',
                ViewConfigFactory: viewConfig,
                supportPanelConfigFactory: 'luse.supportPanelConfigFactory',
                IntygViewState: 'luse.IntygController.ViewStateService'
            },
            views: {
                'intyg@webcert.intyg' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv'
                },
                'header@webcert.intyg.luse' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).
        state('webcert.fragasvar.luse', {
            data: { defaultActive : 'index', intygType: 'luse' },
            url: '/fragasvar/luse/:intygTypeVersion/:certificateId',
            resolve: {
                ViewState: 'luse.IntygController.ViewStateService',
                ViewConfigFactory: viewConfig,
                supportPanelConfigFactory: 'luse.supportPanelConfigFactory',
                IntygViewState: 'luse.IntygController.ViewStateService'
            },
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv'
                },
                'header@webcert.fragasvar.luse' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).state('luse-readonly', {
        data: {intygType: 'luse'},
        url: '/intyg-read-only/luse/:intygTypeVersion/:certificateId',
        resolve: {
            ViewState: 'luse.IntygController.ViewStateService',
            ViewConfigFactory: viewConfig,
            DiagnosExtractor: function() {
                return function(luseModel) {
                    return luseModel.diagnosKod;
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
