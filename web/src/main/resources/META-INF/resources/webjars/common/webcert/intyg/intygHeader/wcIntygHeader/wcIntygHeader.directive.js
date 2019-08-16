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
angular.module('common').directive('wcIntygHeader',
    ['$window', '$state', 'common.moduleService', 'common.IntygHeaderViewState', 'common.UserModel',
      function($window, $state, moduleService, IntygHeaderViewState, UserModel) {
        'use strict';

        return {
          restrict: 'E',
          scope: {
            intygViewState: '='
          },
          templateUrl: '/web/webjars/common/webcert/intyg/intygHeader/wcIntygHeader/wcIntygHeader.directive.html',
          link: function($scope) {

            $scope.certificateName = moduleService.getModuleName(IntygHeaderViewState.intygType);
            $scope.backState = $state.$current.parent.data.backState; // backstate is defined in webcert.intyg state data in router.js
            $scope.intygHeaderViewState = IntygHeaderViewState;

            function updatePersonId() {
              if ($scope.intygViewState.intygModel.grundData) {
                $scope.personId = $scope.intygViewState.intygModel.grundData.patient.personId;
                if (UserModel.getIntegrationParam('alternateSsn')) {
                  $scope.oldPersonId = $scope.personId;
                  $scope.personId = UserModel.getIntegrationParam('alternateSsn');
                }
              }
            }

            updatePersonId();
            $scope.$on('intyg.loaded', updatePersonId);
          }
        };
      }]);
