/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

angular.module('common').directive('wcListPageNumbers',
    ['$document', '$window', '$timeout', '$rootScope', function($document, $window, $timeout, $rootScope) {
      'use strict';
      return {
        restrict: 'E',
        transclude: false,
        scope: {
          listModel: '=',
          filterModel: '='
        },
        templateUrl: '/web/webjars/common/webcert/components/wcListPageNumbers/wcListPageNumbers.directive.html',
        link: function($scope, $element, $attrs, $controller) {

          $scope.getPages = function() {
            $scope.listModel.pagesList = new Array(0);
            $scope.listModel.nbrOfPages = Math.ceil($scope.listModel.totalCount / $scope.listModel.limit);

            if ($scope.listModel.chosenPage === undefined || $scope.listModel.chosenPage <= 0 || $scope.listModel.chosenPage > $scope.listModel.nbrOfPages) {
              $scope.listModel.chosenPage = $scope.listModel.DEFAULT_PAGE;
            }

            if ($scope.listModel.nbrOfPages >= 1 && $scope.listModel.limit < $scope.listModel.totalCount) {
              if ($scope.listModel.nbrOfPages > $scope.listModel.DEFAULT_NUMBER_PAGES * $scope.listModel.chosenNumberPage) {
                $scope.listModel.pagesList = new Array($scope.listModel.DEFAULT_NUMBER_PAGES);
              } else if($scope.listModel.nbrOfPages >  $scope.listModel.DEFAULT_NUMBER_PAGES) {
                $scope.listModel.pagesList = new Array($scope.listModel.nbrOfPages -  $scope.listModel.DEFAULT_NUMBER_PAGES * ($scope.listModel.chosenNumberPage - 1));
              } else {
                $scope.listModel.pagesList = new Array($scope.listModel.nbrOfPages);
              }
            }
            $scope.listModel.gettingPage = false;
          };

          $scope.updatePage = function(chosenPage) {
            if(chosenPage !== $scope.listModel.chosenPage) {
              $scope.filterModel.filterForm.pageSize = $scope.listModel.limit;
              $scope.listModel.chosenPage = chosenPage;
              $rootScope.$broadcast('enhetArendenList.requestListUpdate', {startFrom: ($scope.listModel.chosenPage - 1) * $scope.listModel.limit, reset: false});
            }
          };

          $scope.getPreviousPage = function() {
            if($scope.listModel.chosenPage > $scope.listModel.DEFAULT_PAGE) {
              if($scope.listModel.chosenPage === $scope.listModel.DEFAULT_NUMBER_PAGES * ($scope.listModel.chosenNumberPage - 1) + 1 &&
                  $scope.listModel.chosenPage > $scope.listModel.DEFAULT_NUMBER_PAGES) {
                $scope.listModel.gettingPage = true;
                $scope.listModel.chosenNumberPage--;
              }
              $scope.updatePage($scope.listModel.chosenPage - 1);
            }
          };

          $scope.getNextPage = function() {
            if($scope.listModel.chosenPage < $scope.listModel.nbrOfPages) {
              if($scope.listModel.chosenPage % $scope.listModel.DEFAULT_NUMBER_PAGES === 0) {
                $scope.listModel.gettingPage = true;
                $scope.listModel.chosenNumberPage++;
              }
              $scope.updatePage($scope.listModel.chosenPage + 1);
            }
          };

          $scope.getPageFromIndex = function(index) {
            return (index + 1) + ($scope.listModel.chosenNumberPage - 1) * $scope.listModel.DEFAULT_NUMBER_PAGES;
          };

          $scope.$on('wcListPageNumbers.getPages', $scope.getPages);

        }
      };
    }]);
