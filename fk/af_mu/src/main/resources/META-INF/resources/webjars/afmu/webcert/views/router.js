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
angular.module('afmu').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';

    $stateProvider.
        state('afmu-edit', {
            data: { defaultActive : 'index', intygType: 'afmu', useFmb: true },
            url : '/afmu/edit/:certificateId/:focusOn',
                views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: 'afmu.EditCertCtrl.ViewStateService',
                        UtkastConfigFactory: 'afmu.UtkastConfigFactory',
                        supportPanelConfigFactory: 'afmu.supportPanelConfigFactory'
                    }
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@afmu-edit' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader',
                    resolve: {
                        ViewState: 'afmu.EditCertCtrl.ViewStateService'
                    }
                },

                'footer@afmu-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'utkast@afmu-edit' : {
                    templateUrl: commonPath + 'utkast/smiUtkastUE.html',
                    controller: 'smi.EditCert.UECtrl',
                    resolve: {
                        ViewState: 'afmu.EditCertCtrl.ViewStateService',
                        UtkastConfigFactory: 'afmu.UtkastConfigFactory'
                    }
                 }
            }
        }).state('webcert.intyg.afmu', {
            data: { defaultActive : 'index', intygType: 'afmu' },
            url:'/intyg/afmu/:certificateId/:focusOn?:signed',
            views: {
                'intyg@webcert.intyg' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'afmu.IntygController.ViewStateService',
                        ViewConfigFactory: 'afmu.viewConfigFactory',
                        supportPanelConfigFactory: 'afmu.supportPanelConfigFactory'
                    }
                },
                'header@webcert.intyg.afmu' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'afmu.IntygController.ViewStateService'
                    }
                }
            }
        }).
        state('webcert.fragasvar.afmu', {
            data: { defaultActive : 'enhet-arenden', intygType: 'afmu'  },
            url: '/fragasvar/afmu/:certificateId',
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'afmu.IntygController.ViewStateService',
                        ViewConfigFactory: 'afmu.viewConfigFactory',
                        supportPanelConfigFactory: 'afmu.supportPanelConfigFactory'
                    }
                },
                'header@webcert.fragasvar.afmu' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'afmu.IntygController.ViewStateService'
                    }
                }
            }
        }).
        state('afmu-readonly', {
        url: '/intyg-read-only/afmu/:certificateId',
        views: {
            'content@': {
                templateUrl: commonPath + 'intyg/read-only-view/wcIntygReadOnlyView.template.html',
                controller: 'common.wcIntygReadOnlyViewController',
                resolve: {
                    intygsType: function() {
                        return 'afmu';
                    },
                    ViewConfigFactory: 'afmu.viewConfigFactory',
                    DiagnosExtractor: function() {
                        return function (afmuModel) {
                            return afmuModel.diagnoser[0].diagnosKod;
                        };
                    }
                }
            }
        }
    });
});
