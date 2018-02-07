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
angular.module('luae_fs').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';

    $stateProvider.
        state('luae_fs-edit', {
            data: { defaultActive : 'index', intygType: 'luae_fs' },
            url : '/luae_fs/edit/:certificateId/:focusOn',
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: 'luae_fs.EditCertCtrl.ViewStateService',
                        FormFactory: 'luae_fs.FormFactory',
                        supportPanelConfigFactory: 'luae_fs.supportPanelConfigFactory'
                    }
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@luae_fs-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@luae_fs-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@luae_fs-edit' : {
                    templateUrl: commonPath + 'utkast/smiUtkastFormly.html',
                    controller: 'smi.EditCert.FormlyCtrl',
                    resolve: {
                        ViewState: 'luae_fs.EditCertCtrl.ViewStateService',
                        FormFactory: 'luae_fs.FormFactory'
                    }
                }
            }
        }).
        state('webcert.intyg.fk.luae_fs', {
            data: { defaultActive : 'index', intygType: 'luae_fs' },
            url:'/intyg/luae_fs/:certificateId/:focusOn',
            views: {
                'intyg@webcert.intyg.fk' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'luae_fs.IntygController.ViewStateService',
                        ViewConfigFactory: 'luae_fs.viewConfigFactory',
                        supportPanelConfigFactory: 'luae_fs.supportPanelConfigFactory'
                    }
                },
                'header@webcert.intyg.fk.luae_fs' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'luae_fs.IntygController.ViewStateService'
                    }
                }
            }
        }).
        state('webcert.fragasvar.luae_fs', {
            data: { defaultActive : 'enhet-arenden', intygType: 'luae_fs'  },
            url: '/fragasvar/luae_fs/:certificateId',
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'luae_fs.IntygController.ViewStateService',
                        ViewConfigFactory: 'luae_fs.viewConfigFactory',
                        supportPanelConfigFactory: 'luae_fs.supportPanelConfigFactory'
                    }
                },
                'header@webcert.fragasvar.luae_fs' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'luae_fs.IntygController.ViewStateService'
                    }
                }
            }
        });
});
