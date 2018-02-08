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
angular.module('db').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';

    $stateProvider.
        state('db-edit', {
            data: { defaultActive : 'index', intygType: 'db', useFmb: false },
            url : '/db/edit/:certificateId/:focusOn',
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: 'db.EditCertCtrl.ViewStateService',
                        FormFactory: 'db.FormFactory',
                        supportPanelConfigFactory: 'db.supportPanelConfigFactory'
                    }
                },

                'header@' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
                },

                'header@db-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@db-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@db-edit' : {
                    templateUrl: commonPath + 'utkast/smiUtkastFormly.html',
                    controller: 'smi.EditCert.FormlyCtrl',
                    resolve: {
                        ViewState: 'db.EditCertCtrl.ViewStateService',
                        FormFactory: 'db.FormFactory'
                    }
                }
            }
        }).
        state('webcert.intyg.db', {
            data: { defaultActive : 'index', intygType: 'db' },
            url:'/intyg/db/:certificateId/:focusOn',
            views: {
                'intyg@webcert.intyg' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'db.IntygController.ViewStateService',
                        ViewConfigFactory: 'db.viewConfigFactory'
                    }
                },
                'header@webcert.intyg.db' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'luse.IntygController.ViewStateService'
                    }
                }
            }
        }).
        state('webcert.fragasvar.db', {
            data: { defaultActive : 'enhet-arenden', intygType: 'db'  },
            url: '/fragasvar/db/:certificateId',
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'db.IntygController.ViewStateService',
                        ViewConfigFactory: 'db.viewConfigFactory',
                        supportPanelConfigFactory: 'db.supportPanelConfigFactory'
                    }
                },
                'fragasvar@webcert.fragasvar' : {
                    templateUrl: commonPath + 'fk/arenden/arendeList.html',
                    controller: 'common.ArendeListCtrl'
                },
                'header@webcert.fragasvar.db' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'luse.IntygController.ViewStateService'
                    }
                }
            }
        });
});
