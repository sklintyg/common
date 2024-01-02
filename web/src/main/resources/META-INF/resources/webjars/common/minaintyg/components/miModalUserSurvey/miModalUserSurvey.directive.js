/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('common').directive('miModalUserSurvey',
    ['$document', '$window', '$timeout', 'common.dialogService', 'MIConfig',
      function($document, $window, $timeout, dialogService, MIConfig) {
      'use strict';
      return {
        restrict: 'E',
        transclude: false,
        scope: {
          appLabel: '=',
          nbrOfIntyg: '='
        },
        link: function($scope, $element, $attrs, $controller) {

          $scope.variablesDefined = function () {
            return MIConfig.miUserSurveyDateTo &&
                MIConfig.miUserSurveyDateFrom &&
                MIConfig.miUserSurveyUrl &&
                MIConfig.miUserSurveyVersion;
          };

          $scope.init = function() {
            $scope.dateTo = MIConfig.miUserSurveyDateTo;
            $scope.dateFrom = MIConfig.miUserSurveyDateFrom;
            $scope.url = MIConfig.miUserSurveyUrl;
            $scope.version = MIConfig.miUserSurveyVersion;

            $scope.cookieKey = $scope.appLabel + '-survey-asked-' + $scope.version;
            $scope.timeTo = new Date($scope.dateTo).getTime();
            $scope.timeFrom = new Date($scope.dateFrom).getTime();
            $scope.today = new Date().getTime();
          };

          $scope.userHasIntyg = function() {
            return $scope.nbrOfIntyg >= 2;
          };

          $scope.openSurvey = function () {
            $window.open($scope.url, '_blank');
          };

          $scope.isModalActive = function () {
            return $scope.today <=  $scope.timeTo && $scope.today >= $scope.timeFrom;
          };

          $scope.isUserAsked = function () {
            return $window.localStorage && $window.localStorage.getItem($scope.cookieKey) === '1';
          };

          $scope.checkSurvey = function () {
            if($scope.isModalActive() && $scope.userHasIntyg() && !$scope.isUserAsked()) {
              $scope.showModal();
            }
          };

          $scope.setUserAsked = function () {
            $window.localStorage.setItem($scope.cookieKey, '1');
          };

          $scope.showModal = function() {
            $scope.setUserAsked();
            var dialogInstance = dialogService.showDialog($scope, {
              dialogId: $scope.appLabel + '-user-survey-dialog',
              titleId: $scope.appLabel + '.survey.modal.title',
              bodyTextId: $scope.appLabel + '.survey.modal.text',
              button1click: function () {
                $scope.openSurvey();
                dialogInstance.close();
              },
              button2click: function() {
                dialogInstance.close();
              },
              button1text: 'common.yes',
              button2text: 'common.no',
              button2visible: true,
              autoClose: false
            });
          };

          if($scope.variablesDefined()) {
            $scope.init();
            $scope.checkSurvey();
          }
        }
      };
    }]);
