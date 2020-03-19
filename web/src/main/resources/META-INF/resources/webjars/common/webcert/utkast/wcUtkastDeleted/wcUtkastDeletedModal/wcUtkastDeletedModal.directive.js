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

angular.module('common').directive('wcUtkastDeletedModal',
    ['$document', '$window', '$timeout', '$uibModal', 'common.UtkastViewStateService', 'common.UserModel',
      function($document, $window, $timeout, $uibModal, CommonViewState, UserModel) {
        'use strict';
        return {
          restrict: 'E',
          transclude: false,
          scope: {
          },
          link: function($scope) {

            var modalInstance;
            $scope.showModal = function() {
              $timeout(function() {
                modalInstance = $uibModal.open({
                  modalId: 'deleted-dialog-modal',
                  templateUrl: '/web/webjars/common/webcert/utkast/wcUtkastDeleted/wcUtkastDeletedModal/wcUtkastDeletedModal.directive.html',
                  controller: 'utkastDeletedModalCtrl',
                  autoClose: false,
                  backdrop: true,
                  size: 'md'
                });
              }, 500);
            };

            $scope.init = function() {
              if (CommonViewState.deletedDraft && !UserModel.isDjupintegration()) {
                $scope.showModal();

                setTimeout(function() {
                  if (modalInstance) {
                    modalInstance.close();
                  }
                },2000);

                CommonViewState.deletedDraft = false;
              }
            };

            $scope.init();

          }
        };
}]);
