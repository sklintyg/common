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
angular.module('lisjp', ['ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('lisjp').config(function($stateProvider) {
  'use strict';

  $stateProvider.state('lisjp-view', {
    url: '/lisjp/:intygTypeVersion/view/:certificateId',
    templateUrl: '/web/webjars/common/minaintyg/intyg/viewCert.html',
    controller: 'common.ViewCertCtrl',
    resolve: {
      viewConfigFactory: function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('lisjp.viewConfigFactory', $stateParams);
      },
      viewFactory: function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('lisjp.viewFactory', $stateParams);
      }
    },
    data: {
      title: 'Läkarintyg för sjukpenning',
      keepInboxTabActive: true,
      breadcrumb: ['inkorg', 'intyg']
    }
  }).state('lisjp-customize', {
    abstract: true, // jshint ignore:line
    url: '/:type/:intygTypeVersion/customize-lisjp/:certificateId',
    templateUrl: '/web/webjars/lisjp/minaintyg/views/customize-pdf.html',
    controller: 'lisjp.CustomizePdfCtrl',
    data: {
      title: 'Anpassa intyget till arbetsgivare',
      keepInboxTabActive: true,
      breadcrumb: ['inkorg', 'intyg', 'anpassa'], backState: 'history-back'
    }

  }).state('lisjp-customize.step1', {
    url: '/step1',
    views: {
      'header@lisjp-customize': {
        templateUrl: '/web/webjars/lisjp/minaintyg/views/step1.header.html'
      },
      'body@lisjp-customize': {
        templateUrl: '/web/webjars/lisjp/minaintyg/views/step1.body.html'
      }
    },
    data: {
      index: 0
    }

  }).state('lisjp-customize.step2', {
    url: '/step2',
    views: {
      'header@lisjp-customize': {
        templateUrl: '/web/webjars/lisjp/minaintyg/views/step2.header.html'
      },
      'body@lisjp-customize': {
        templateUrl: '/web/webjars/lisjp/minaintyg/views/step2.body.html'
      }
    },
    data: {
      index: 1
    }

  }).state('lisjp-customize.step3', {
    url: '/step3',
    views: {
      'header@lisjp-customize': {
        templateUrl: '/web/webjars/lisjp/minaintyg/views/step3.header.html'
      },
      'body@lisjp-customize': {
        templateUrl: '/web/webjars/lisjp/minaintyg/views/step3.body.html'
      }
    },
    data: {
      index: 2
    }

  });
});

// Inject language resources
angular.module('lisjp').run(['common.messageService', 'lisjp.messages', function(messageService, lisjpMessages) {
  'use strict';

  messageService.addResources(lisjpMessages);
}]);