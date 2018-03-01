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
/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('fk7263').config(function($stateProvider) {
    'use strict';

    var commonPath = '/web/webjars/common/webcert/';
    var intygsTypPath = '/web/webjars/fk7263/webcert/';

    $stateProvider.
        state('webcert.intyg.fk7263', {
            data: { defaultActive : 'index', intygType: 'fk7263' },
            url:'/intyg/fk7263/:certificateId/:focusOn',
            views: {
                'intyg@webcert.intyg' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'fk7263.ViewCertCtrl',
                    resolve: {
                        supportPanelConfigFactory: 'fk7263.supportPanelConfigFactory'
                    }
                },
                'header@webcert.intyg.fk7263' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'fk7263.IntygController.ViewStateService'
                    }
                }
            }
        }).
        state('webcert.fragasvar.fk7263', {
            data: { defaultActive : 'enhet-arenden', intygType: 'fk7263' },
            url: '/fragasvar/fk7263/:certificateId',
            views: {
                'intyg@webcert.fragasvar' : {
                    templateUrl: intygsTypPath + 'views/intyg/intyg.html',
                    controller: 'fk7263.ViewCertCtrl',
                    resolve: {
                        supportPanelConfigFactory: 'fk7263.supportPanelConfigFactory'
                    }
                },
                'header@webcert.fragasvar.fk7263' : {
                    templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
                    controller: 'common.IntygHeader',
                    resolve: {
                        IntygViewState: 'fk7263.IntygController.ViewStateService'
                    }
                }
            }
        }).state('fk7263-readonly', {
        url: '/intyg-read-only/fk7263/:certificateId',
        views: {
            'content@': {
                templateUrl: commonPath + 'intyg/read-only-view/wcIntygReadOnlyView.template.html',
                controller: 'common.wcIntygReadOnlyViewController',
                resolve: {
                    intygsType: function() {
                        return 'fk7263';
                    },
                    ViewConfigFactory: 'fk7263.viewConfigFactory',
                    DiagnosExtractor: function() {
                        return function (fk7263Model) {
                            return fk7263Model.diagnosKod;
                        };
                    }
                }
            }
        }
    });
});
