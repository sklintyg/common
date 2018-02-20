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
angular.module('luse').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';

    $stateProvider.
        state('luse-edit-formly', {
            data: { defaultActive : 'index', intygType: 'luse' },
            url : '/luse/edit-formly/:certificateId/:focusOn',
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: 'luse.EditCertCtrl.ViewStateService',
                        FormFactory: 'luse.FormFactory',
                        supportPanelConfigFactory: 'luse.supportPanelConfigFactory'
                    }
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@luse-edit-formly' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@luse-edit-formly' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@luse-edit-formly' : {
                    templateUrl: commonPath + 'utkast/smiUtkastFormly.html',
                    controller: 'smi.EditCert.FormlyCtrl',
                    resolve: {
                        ViewState: 'luse.EditCertCtrl.ViewStateService',
                        FormFactory: 'luse.FormFactory'
                    }
                }
            }
        }).
        state('luse-edit', {
            data: { defaultActive : 'index', intygType: 'luse' },
            url : '/luse/edit/:certificateId/:focusOn',
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: 'luse.EditCertCtrl.ViewStateService',
                        FormFactory: 'luse.FormFactory',
                        supportPanelConfigFactory: 'luse.supportPanelConfigFactory'
                    }
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@luse-edit' : {
                    templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
                    controller: 'common.UtkastHeader',
                    resolve: {
                        ViewState: 'luse.EditCertCtrl.ViewStateService'
                    }
                },

                'footer@luse-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },
                'formly@luse-edit' : {
                    templateUrl: commonPath + 'utkast/smiUtkastUE.html',
                    controller: 'smi.EditCert.UECtrl',
                    resolve: {
                        ViewState: 'luse.EditCertCtrl.ViewStateService',
                        UtkastConfigFactory: 'luse.UtkastConfigFactory'
                    }
                }
            }
        }).
        state('webcert.intyg.luse', {
            data: { defaultActive : 'index', intygType: 'luse' },
            url:'/intyg/luse/:certificateId/:focusOn',
            views: {
                'intyg@webcert.intyg' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'luse.IntygController.ViewStateService',
                        ViewConfigFactory: 'luse.viewConfigFactory',
                        supportPanelConfigFactory: 'luse.supportPanelConfigFactory'
                    }
                },
                'header@webcert.intyg.luse' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'luse.IntygController.ViewStateService'
                    }
                }
            }
        }).
        state('webcert.fragasvar.luse', {
            data: { defaultActive : 'enhet-arenden', intygType: 'luse' },
            url: '/fragasvar/luse/:certificateId',
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'luse.IntygController.ViewStateService',
                        ViewConfigFactory: 'luse.viewConfigFactory',
                        supportPanelConfigFactory: 'luse.supportPanelConfigFactory'
                    }
                },
                'header@webcert.fragasvar.luse' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'luse.IntygController.ViewStateService'
                    }
                }
            }
        });
});
