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

          $scope.getNbrPages = function() {
            return Math.ceil($scope.listModel.totalCount / $scope.listModel.limit);
          };

          $scope.setChosenPage = function() {
            if ($scope.listModel.chosenPage === undefined || $scope.listModel.chosenPage <= 0 || $scope.listModel.chosenPage > $scope.listModel.nbrOfPages) {
              $scope.listModel.chosenPage = $scope.listModel.DEFAULT_PAGE;
            }
          };

          $scope.isMiddlePageList = function() {
            return $scope.listModel.nbrOfPages > $scope.listModel.DEFAULT_NUMBER_PAGES * $scope.listModel.chosenPageList;
          };

          $scope.isLastPageList = function() {
            return $scope.listModel.nbrOfPages >  $scope.listModel.DEFAULT_NUMBER_PAGES;
          };

          $scope.getNbrPreviousPages = function() {
            return $scope.listModel.DEFAULT_NUMBER_PAGES * ($scope.listModel.chosenPageList - 1);
          };

          $scope.getStartingPoint = function() {
            return ($scope.listModel.chosenPage - 1) * $scope.listModel.limit;
          };

          $scope.getPageFromIndex = function(index) {
            return (index + 1) + ($scope.listModel.chosenPageList - 1) * $scope.listModel.DEFAULT_NUMBER_PAGES;
          };

          $scope.getPages = function() {
            $scope.listModel.pagesList = new Array(0);
            $scope.listModel.nbrOfPages = $scope.getNbrPages();

            $scope.setChosenPage();

            if ($scope.listModel.nbrOfPages >= 1) {
              if (!$scope.isMiddlePageList()) {
                $scope.listModel.pagesList = new Array($scope.listModel.DEFAULT_NUMBER_PAGES);
              } else if($scope.isLastPageList()) {
                $scope.listModel.pagesList = new Array($scope.listModel.nbrOfPages -  $scope.getNbrPreviousPages());
              } else { // is first and only page list
                $scope.listModel.pagesList = new Array($scope.listModel.nbrOfPages);
              }
            }
            $scope.listModel.gettingPage = false;
          };

          $scope.updatePage = function(chosenPage) {
            if(chosenPage !== $scope.listModel.chosenPage) {
              $scope.filterModel.pageSize = $scope.listModel.limit;
              $scope.listModel.chosenPage = chosenPage;
              $rootScope.$broadcast($scope.listModel.LIST_NAME + '.requestListUpdate', {startFrom: $scope.getStartingPoint(), reset: false});
            }
          };

          $scope.isStartingNumber = function() {
            var isFirstPage = $scope.listModel.chosenPage <= $scope.listModel.DEFAULT_NUMBER_PAGES;
            var isStartingNumber = $scope.listModel.chosenPage === $scope.listModel.DEFAULT_NUMBER_PAGES * ($scope.listModel.chosenPageList - 1) + 1;
            return !isFirstPage && isStartingNumber;
          };

          $scope.getPreviousPage = function() {
            if($scope.listModel.chosenPage > $scope.listModel.DEFAULT_PAGE) {
              if($scope.isStartingNumber()) {
                $scope.listModel.gettingPage = true;
                $scope.listModel.chosenPageList--;
              }
              $scope.updatePage($scope.listModel.chosenPage - 1);
            }
          };

          $scope.getNextPage = function() {
            if($scope.listModel.chosenPage < $scope.listModel.nbrOfPages) {
              if($scope.listModel.chosenPage % $scope.listModel.DEFAULT_NUMBER_PAGES === 0) {
                $scope.listModel.gettingPage = true;
                $scope.listModel.chosenPageList++;
              }
              $scope.updatePage($scope.listModel.chosenPage + 1);
            }
          };

          $scope.$on('wcListPageNumbers.getPages', $scope.getPages);
          $scope.getPages();
        }
      };
    }]);
