/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

        return {
          restrict: 'E',
          scope: {},
          template: '',
          link: function() {
            var dialogInstance;
            var careProviderHSAWithoutDash = UserModel.user.valdVardgivare.id.replaceAll('-', '');

            var showOriginNormalWarningDialog = function() {
              var careProviderName = UserModel.user.valdVardgivare.namn;

              $window.sessionStorage[careProviderHSAWithoutDash] = true;

              dialogInstance = $uibModal.open({
                templateUrl:
                    '/web/webjars/common/webcert/components/headers/wcAppHeader/wcOriginNormalWarning/wcOriginNormalWarning.infodialog.html',
                size: 'md',
                id: 'OriginNormalWarningId',
                controller: function($scope) {
                  $scope.careProviderName = careProviderName;
                }
              });
            };

            var shouldOriginNormalWarningDialogBeDisplayed = function() {
              return UserModel.user.origin === 'NORMAL'
                  && featureService.isFeatureActive(featureService.features.VARNING_FRISTAENDE)
                  && !$window.sessionStorage[careProviderHSAWithoutDash];
            }

            if (shouldOriginNormalWarningDialogBeDisplayed()) {
              showOriginNormalWarningDialog();
            }
          }
        };
      }]);