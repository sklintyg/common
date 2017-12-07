/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

    $stateProvider.
        state('doi-edit', {
            data: { defaultActive : 'index', intygType: 'doi', useFmb: false },
            url : '/doi/edit/:certificateId/',
            views : {
                'content@' : {
                    templateUrl: commonPath + 'utkast/smiUtkast.html',
                    controller: 'smi.EditCertCtrl',
                    resolve: {
                        ViewState: 'doi.EditCertCtrl.ViewStateService',
                        FormFactory: 'doi.FormFactory'
                    }
                },

                'wcHeader@doi-edit' : {
                    templateUrl: commonPath + 'components/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },

                'header@doi-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@doi-edit' : {
                    templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@doi-edit' : {
                    templateUrl: commonPath + 'utkast/smiUtkastFormly.html',
                    controller: 'smi.EditCert.FormlyCtrl',
                    resolve: {
                        ViewState: 'doi.EditCertCtrl.ViewStateService',
                        FormFactory: 'doi.FormFactory'
                    }
                }
            }
        }).
        state('webcert.intyg.fk.doi', {
            data: { defaultActive : 'index', intygType: 'doi' },
            url:'/intyg/doi/:certificateId/',
            views: {
                'intyg@webcert.intyg.fk' : {
                    templateUrl: commonPath + 'intyg/smiIntygUv.html',
                    controller: 'smi.ViewCertCtrlUv',
                    resolve: {
                        ViewState: 'doi.IntygController.ViewStateService',
                        ViewConfigFactory: 'doi.viewConfigFactory'
                    }
                },
                'header@webcert.intyg.fk.doi' : {
                    templateUrl: commonPath + 'intyg/intyg-header/intyg-header.html',
                    controller: 'common.IntygHeader'
                }
            }
        });
});
