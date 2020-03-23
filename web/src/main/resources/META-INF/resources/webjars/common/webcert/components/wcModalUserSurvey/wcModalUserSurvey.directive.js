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

angular.module('common').directive('wcModalUserSurvey',
    ['$document', '$window', '$timeout', 'common.dialogService', 'common.User', 'common.UserModel', 'common.messageService', 'moduleConfig',
      function($document, $window, $timeout, dialogService, UserService, UserModel, messageService, moduleConfig) {
      'use strict';
    return {
      restrict: 'E',
      transclude: false,
      scope: {
        appLabel: '='
      },
      link: function($scope, $element, $attrs, $controller) {

        $scope.init = function () {
          $scope.url = moduleConfig.WEBCERT_USER_SURVEY_URL;
          $scope.version = moduleConfig.WEBCERT_USER_SURVEY_VERSION;
          $scope.dateTo = moduleConfig.WEBCERT_USER_SURVEY_DATE_TO;
          $scope.dateFrom = moduleConfig.WEBCERT_USER_SURVEY_DATE_FROM;

          $scope.preferenceKey = $scope.appLabel + '-survey-asked-' + $scope.version;

          $scope.today = new Date().getTime();
          $scope.timeTo = new Date($scope.dateTo).getTime();
          $scope.timeFrom = new Date($scope.dateFrom).getTime();
        };

        $scope.openSurvey = function () {
          $window.open($scope.url, '_blank');
        };

        $scope.isModalActive = function () {
          return $scope.today <=  $scope.timeTo && $scope.today >= $scope.timeFrom;
        };

        $scope.isUserAsked = function () {
          if(UserModel) {
            return UserModel.getAnvandarPreference($scope.preferenceKey);
          } else {
            return true;
          }
        };

        $scope.parametersDefined = function () {
          return moduleConfig.WEBCERT_USER_SURVEY_URL &&
          moduleConfig.WEBCERT_USER_SURVEY_VERSION &&
          moduleConfig.WEBCERT_USER_SURVEY_DATE_TO &&
          moduleConfig.WEBCERT_USER_SURVEY_DATE_FROM;
        };

        $scope.checkSurvey = function () {
          if($scope.isModalActive() && !$scope.isUserAsked() && !UserModel.isReadOnly()) {
            $scope.showModal();
          }
        };

        $scope.setUserAsked = function () {
          UserService.storeAnvandarPreference($scope.preferenceKey, true);
        };

        var dialogInstance;

        $scope.showModal = function() {
          $timeout(function() {
            $scope.setUserAsked();
            dialogInstance = dialogService.showDialog({
            dialogId: $scope.appLabel + '-survey-modal',
              titleId: $scope.appLabel + '.modal.survey.title',
              bodyText: messageService.getProperty($scope.appLabel + '.modal.survey.text'),
              button1text: 'common.yes',
              button2text: 'common.no',
            button1click: function() {
              $scope.openSurvey();
              dialogInstance.close();
            },
            button2click: function() {
              dialogInstance.close();
            },
            autoClose: false,
            size: 'md'
          });
        }, 500);
        };

        if($scope.parametersDefined()) {
          $scope.init();
          $scope.checkSurvey();
        }
      }
    };
}]);
