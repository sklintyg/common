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
angular.module('lisjp').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';

    var editViewState = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('lisjp.EditCertCtrl.ViewStateService', $stateParams);
    };

    var utkastConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('lisjp.UtkastConfigFactory', $stateParams);
    };

    var viewConfig = function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('lisjp.viewConfigFactory', $stateParams);
    };

    $stateProvider.
        state('lisjp', {
            url: '/lisjp'
        }).
        state('lisjp.utkast', {
            data: { defaultActive : 'index', intygType: 'lisjp', useFmb: true },
            url : '/:intygTypeVersion/edit/:certificateId/:focusOn',
            params: {
                focusOn: ''
            },
            resolve: {
                ViewState: editViewState,
                UtkastConfigFactory: utkastConfig,
                supportPanelConfigFactory: 'lisjp.supportPanelConfigFactory'
            },
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl'
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@lisjp.utkast' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@lisjp.utkast' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'utkast@lisjp.utkast' : {
                    templateUrl: commonPath + 'utkast/smiUtkastUE.html',
                    controller: 'smi.EditCert.UECtrl'
                 }
            }
        }).state('webcert.intyg.lisjp', {
            data: { defaultActive : 'index', intygType: 'lisjp' },
            url:'/intyg/lisjp/:intygTypeVersion/:certificateId/:focusOn?:signed',
            params: {
                focusOn: ''
            },
            resolve: {
                ViewState: 'lisjp.IntygController.ViewStateService',
                ViewConfigFactory: viewConfig,
                supportPanelConfigFactory: 'lisjp.supportPanelConfigFactory',
                IntygViewState: 'lisjp.IntygController.ViewStateService'
            },
            views: {
                'intyg@webcert.intyg' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv'
                },
                'header@webcert.intyg.lisjp' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).
        state('webcert.fragasvar.lisjp', {
            data: { defaultActive : 'index', intygType: 'lisjp' },
            url: '/fragasvar/lisjp/:intygTypeVersion/:certificateId',
            resolve: {
                ViewState: 'lisjp.IntygController.ViewStateService',
                ViewConfigFactory: viewConfig,
                supportPanelConfigFactory: 'lisjp.supportPanelConfigFactory',
                IntygViewState: 'lisjp.IntygController.ViewStateService'
            },
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv'
                },
                'header@webcert.fragasvar.lisjp' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader'
                }
            }
        }).
        state('lisjp-readonly', {
            data: { intygType: 'lisjp' },
            url: '/intyg-read-only/lisjp/:intygTypeVersion/:certificateId',
            resolve: {
                ViewState: 'lisjp.IntygController.ViewStateService',
                ViewConfigFactory: viewConfig,
                DiagnosExtractor: function() {
                return function (lisjpModel) {
                    return lisjpModel.diagnoser[0].diagnosKod;
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
