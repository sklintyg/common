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
angular.module('common').directive('ueComponentToggler', ['$timeout', 'common.UtkastValidationService', 'common.UtkastValidationViewState',
  function($timeout, UtkastValidationService, UtkastValidationViewState) {
    'use strict';

    return {
      restrict: 'E',
      scope: {
        form: '=',
        config: '=',
        model: '='
      },
      templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/containers/ueComponentToggler/ueComponentToggler.directive.html',
      link: function($scope) {

        $scope.validation = UtkastValidationViewState;

        //Default is to not render the component children..
        $scope.vm = {
          showComponents: false
        };
        //Let the state of modelPropToWatch determine if we should show/hide the components
        $scope.$watch('model.' + $scope.config.modelPropToWatch, function(newVal) {
          $scope.vm.showComponents = angular.isDefined(newVal);
        });
      }
    };
  }]);
