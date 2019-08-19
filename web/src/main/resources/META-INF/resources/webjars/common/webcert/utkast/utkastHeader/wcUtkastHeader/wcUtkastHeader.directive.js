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
            $scope.oldPersonId = UserModel.getIntegrationParam('beforeAlternateSsn');
          }
        };
      }]);
