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
angular.module('ag7804', ['ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('ag7804').config(function($stateProvider) {
  'use strict';

  $stateProvider.state('ag7804-view', {
    url: '/ag7804/:intygTypeVersion/view/:certificateId',
    templateUrl: '/web/webjars/common/minaintyg/intyg/viewCert.html',
    controller: 'common.ViewCertCtrl',
    resolve: {
      viewConfigFactory: function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('ag7804.viewConfigFactory', $stateParams);
      },
      viewFactory: function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('ag7804.viewFactory', $stateParams);
      }
    },
    data: {
      title: 'Läkarintyg om arbetsförmåga – arbetsgivaren',
      keepInboxTabActive: true,
      breadcrumb: ['inkorg', 'intyg']
    }
  }).state('ag7804-customize', {
    abstract: true, // jshint ignore:line
    url: '/:type/:intygTypeVersion/customize-ag7804/:certificateId',
    templateUrl: '/web/webjars/ag7804/minaintyg/views/customize-pdf.html',
    controller: 'ag7804.CustomizePdfCtrl',
    data: {
      title: 'Anpassa intyget till arbetsgivare',
      keepInboxTabActive: true,
      breadcrumb: ['inkorg', 'intyg', 'anpassa'], backState: 'history-back'
    }

  }).state('ag7804-customize.step1', {
    url: '/step1',
    views: {
      'header@ag7804-customize': {
        templateUrl: '/web/webjars/ag7804/minaintyg/views/step1.header.html'
      },
      'body@ag7804-customize': {
        templateUrl: '/web/webjars/ag7804/minaintyg/views/step1.body.html'
      }
    },
    data: {
      index: 0
    }

  }).state('ag7804-customize.step2', {
    url: '/step2',
    views: {
      'header@ag7804-customize': {
        templateUrl: '/web/webjars/ag7804/minaintyg/views/step2.header.html'
      },
      'body@ag7804-customize': {
        templateUrl: '/web/webjars/ag7804/minaintyg/views/step2.body.html'
      }
    },
    data: {
      index: 1
    }

  }).state('ag7804-customize.step3', {
    url: '/step3',
    views: {
      'header@ag7804-customize': {
        templateUrl: '/web/webjars/ag7804/minaintyg/views/step3.header.html'
      },
      'body@ag7804-customize': {
        templateUrl: '/web/webjars/ag7804/minaintyg/views/step3.body.html'
      }
    },
    data: {
      index: 2
    }

  });
});

// Inject language resources
angular.module('ag7804').run(['common.messageService', 'ag7804.messages', function(messageService, ag7804Messages) {
  'use strict';

  messageService.addResources(ag7804Messages);
}]);