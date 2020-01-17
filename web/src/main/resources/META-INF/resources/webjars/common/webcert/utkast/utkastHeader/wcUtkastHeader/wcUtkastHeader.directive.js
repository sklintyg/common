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
angular.module('common').directive('wcUtkastHeader',
    ['$window', '$state', 'common.moduleService', 'common.UtkastHeaderViewState', 'common.UserModel',
      function($window, $state, moduleService, UtkastHeaderViewState, UserModel) {
        'use strict';

        return {
          restrict: 'E',
          scope: {
            utkastViewState: '=',
            certForm: '='
          },
          templateUrl: '/web/webjars/common/webcert/utkast/utkastHeader/wcUtkastHeader/wcUtkastHeader.directive.html',
          link: function($scope) {

            $scope.certificateName = moduleService.getModuleName(UtkastHeaderViewState.intygType);
            $scope.backState = 'webcert.create-choose-certtype-index';

            // $scope.oldPersonId = UserModel.getIntegrationParam('beforeAlternateSsn');

            /**
             * First have to make sure if grundData has been loaded to the IntygModel.
             *
             * If beforeAlternateSsn has a patient id - It means that the backend has updated it based on passed alternateSsn
             * and beforeAlternateSsn has the old patient id.
             *
             * If beforeAlternateSsn is missing patient id but alternateSsn has one - It means that the EHR-system have called
             * webcert with a new patient id but the backend haven't stored it (based on user authorization). Then display
             * alternateSsn as the new and the patient id on the draft as old.
             *
             * If both beforeAlternateSsn and alternateSsn are missing value, then give just use the patient id on the draft.
             */
            function updatePersonId() {
              if ($scope.utkastViewState.intygModel.grundData) {
                var beforeAlternateSsn = UserModel.getIntegrationParam('beforeAlternateSsn');
                var alternateSsn = UserModel.getIntegrationParam('alternateSsn');

                if (beforeAlternateSsn) {
                  $scope.oldPersonId = beforeAlternateSsn;
                  $scope.personId = alternateSsn;
                } else if (alternateSsn) {
                  $scope.oldPersonId = $scope.utkastViewState.intygModel.grundData.patient.personId;
                  $scope.personId = alternateSsn;
                } else {
                  $scope.personId = $scope.utkastViewState.intygModel.grundData.patient.personId;
                }
              }
            }

            updatePersonId();
            $scope.$on('intyg.loaded', updatePersonId);
          }
        };
      }]);
