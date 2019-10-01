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
angular.module('common').directive('serviceBanner', function() {
  'use strict';

  return {
    restrict: 'E',
    scope: {
      banners: '='
    },
    templateUrl: '/web/webjars/common/app-shared/servicebanner/serviceBanner.directive.html',
    controller: function($scope) {

      function getSeverity(priority) {
        switch(priority) {
        case 'HOG':
          return 'danger';
        case 'MEDEL':
          return 'warning';
        case 'LAG':
          return 'info';
        }
      }

      var bannersShown = [];
      var i = 0;

      angular.forEach($scope.banners, function(banner) {
        bannersShown.push({
          id: 'serviceBanner' + i++,
          severity: getSeverity(banner.priority),
          text: addExternalIcon(banner.message)
        });
      });

      $scope.bannersShown = bannersShown;
    }
  };

  function addExternalIcon(text) {
    return text.replace(new RegExp('</a>', 'g'), '<i class="material-icons">&#xe895;</i></a>');
  }
});
