/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
angular.module('luae_na').config(function($stateProvider) {
  'use strict';

  var commonPath = '/web/webjars/common/webcert/';

  var editViewState = function(factoryResolverHelper, $stateParams) {
    return factoryResolverHelper.resolve('luae_na.EditCertCtrl.ViewStateService', $stateParams);
  };

  var utkastConfig = function(factoryResolverHelper, $stateParams) {
    return factoryResolverHelper.resolve('luae_na.UtkastConfigFactory', $stateParams);
  };

  var viewConfig = function(factoryResolverHelper, $stateParams) {
    return factoryResolverHelper.resolve('luae_na.viewConfigFactory', $stateParams);
  };

  $stateProvider.state('luae_na', {
    url: '/luae_na'
  }).state('luae_na.utkast', {
    data: {defaultActive: 'index', intygType: 'luae_na'},
    url: '/:intygTypeVersion/edit/:certificateId/:focusOn',
    params: {
      focusOn: ''
    },
    resolve: {
      ViewState: editViewState,
      UtkastConfigFactory: utkastConfig,
      supportPanelConfigFactory: 'luae_na.supportPanelConfigFactory'
    },
    views: {
      'content@': {
        templateUrl: commonPath + 'utkast/smiUtkast.html',
        controller: 'smi.EditCertCtrl'
      },

      'header@': {
        templateUrl: commonPath + 'components/headers/wcHeader.partial.html'
      },

      'header@luae_na.utkast': {
        templateUrl: commonPath + 'utkast/utkastHeader/utkastHeader.html',
        controller: 'common.UtkastHeader'
      },

      'footer@luae_na.utkast': {
        templateUrl: commonPath + 'utkast/utkast-footer/utkastFooter.html',
        controller: 'common.UtkastFooter'
      },
      'utkast@luae_na.utkast': {
        templateUrl: commonPath + 'utkast/smiUtkastUE.html',
        controller: 'smi.EditCert.UECtrl'
      }
    }
  }).state('webcert.intyg.luae_na', {
    data: {defaultActive: 'index', intygType: 'luae_na'},
    url: '/intyg/luae_na/:intygTypeVersion/:certificateId/:focusOn?:signed',
    params: {
      focusOn: ''
    },
    resolve: {
      ViewState: 'luae_na.IntygController.ViewStateService',
      ViewConfigFactory: viewConfig,
      supportPanelConfigFactory: 'luae_na.supportPanelConfigFactory',
      IntygViewState: 'luae_na.IntygController.ViewStateService'
    },
    views: {
      'intyg@webcert.intyg': {
        templateUrl: commonPath + 'intyg/smiIntygUv.html',
        controller: 'smi.ViewCertCtrlUv'
      },
      'header@webcert.intyg.luae_na': {
        templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
        controller: 'common.IntygHeader'
      }
    }
  }).state('webcert.fragasvar.luae_na', {
    data: {defaultActive: 'enhet-arenden', intygType: 'luae_na'},
    url: '/fragasvar/luae_na/:intygTypeVersion/:certificateId',
    resolve: {
      ViewState: 'luae_na.IntygController.ViewStateService',
      ViewConfigFactory: viewConfig,
      supportPanelConfigFactory: 'luae_na.supportPanelConfigFactory',
      IntygViewState: 'luae_na.IntygController.ViewStateService'
    },
    views: {
      'intyg@webcert.fragasvar': {
        templateUrl: commonPath + 'intyg/smiIntygUv.html',
        controller: 'smi.ViewCertCtrlUv'
      },
      'header@webcert.fragasvar.luae_na': {
        templateUrl: commonPath + 'intyg/intygHeader/intygHeader.html',
        controller: 'common.IntygHeader'
      }
    }
  });
});
