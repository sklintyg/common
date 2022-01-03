/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcListDropdown',
    ['$document', '$window', '$timeout', '$rootScope', function($document, $window, $timeout, $rootScope) {
      'use strict';
      return {
        restrict: 'E',
        transclude: false,
        scope: {
          listModel: '=',
          filterModel: '='
        },
        templateUrl: '/web/webjars/common/webcert/components/wcListDropdown/wcListDropdown.directive.html',
        link: function($scope, $element, $attrs, $controller) {

          $scope.fetchList = function() {
            if($scope.listModel.limit !== $scope.filterModel.pageSize) {
              $scope.listModel.chosenPage = $scope.listModel.DEFAULT_PAGE;
              $scope.listModel.chosenPageList = $scope.listModel.DEFAULT_PAGE;
            }
            $scope.filterModel.pageSize = $scope.listModel.limit;
            $rootScope.$broadcast($scope.listModel.LIST_NAME + '.requestListUpdate', {startFrom: 0, reset: false});
          };

          $scope.getLimits = function() {
            $scope.limitList = [];
            var count = 0;

            if ($scope.listModel.totalCount < $scope.listModel.DEFAULT_PAGE_SIZE) {
              return null;
            }

            if ($scope.listModel.totalCount > 10) {
              $scope.limitList[count++] = {id: 10, label: '10'};
            }
            if ($scope.listModel.totalCount > 25) {
              $scope.limitList[count++] = {id: 25, label: '25'};
            }
            if ($scope.listModel.totalCount > 50) {
              $scope.limitList[count++] = {id: 50, label: '50'};
            }
            $scope.limitList[count] = {id: $scope.listModel.totalCount, label: 'alla'};
          };

          $scope.$on('wcListDropdown.getLimits', $scope.getLimits);
          $scope.getLimits();
        }
      };
    }]);
