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
angular.module('common').directive('wcOriginNormalWarning',
    ['$uibModal', '$window', 'common.UserModel', 'common.featureService',
      function($uibModal, $window, UserModel, featureService) {
        'use strict';
        if (!UserModel.user.valdVardgivare) {
          return;
        }
        var shouldOriginNormalWarningDialogBeDisplayed = function() {
          return UserModel.user.origin === 'NORMAL' &&
              featureService.isFeatureActive(featureService.features.VARNING_FRISTAENDE);
        };
        var careProviderName = UserModel.user.valdVardgivare.namn;
        return {
          templateUrl:
              '/web/webjars/common/webcert/components/wcOriginNormalWarning/wcOriginNormalWarning.directive.html',
          restrict: 'E',
          scope: {},
          controller: function($scope) {
            $scope.careProviderName = careProviderName;
            $scope.displayOriginNormalWarningBanner = function() {
              return shouldOriginNormalWarningDialogBeDisplayed();
            };
          }
        };
      }]);