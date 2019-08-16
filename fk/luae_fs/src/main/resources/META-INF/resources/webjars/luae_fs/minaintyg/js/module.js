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

angular.module('luae_fs', ['ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('luae_fs').config(function($stateProvider) {
  'use strict';

  $stateProvider.state('luae_fs-view', {
    url: '/luae_fs/:intygTypeVersion/view/:certificateId',
    templateUrl: '/web/webjars/common/minaintyg/intyg/viewCert.html',
    controller: 'common.ViewCertCtrl',
    resolve: {
      viewConfigFactory: function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('luae_fs.viewConfigFactory', $stateParams);
      },
      viewFactory: function(factoryResolverHelper, $stateParams) {
        return factoryResolverHelper.resolve('luae_fs.viewFactory', $stateParams);
      }
    },
    data: {
      title: 'Läkarutlåtande för aktivitetsersättning vid förlängd skolgång', keepInboxTabActive: true,
      breadcrumb: ['inkorg', 'intyg']
    }
  });
});

// Inject language resources
angular.module('luae_fs').run(['common.messageService', 'luae_fs.messages',
  function(messageService, luaeFsMessages) {
    'use strict';

    messageService.addResources(luaeFsMessages);
  }]);
