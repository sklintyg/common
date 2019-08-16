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
angular.module('common').directive('ueBefattningSpecialitet', ['common.UserModel',
  function(UserModel) {
    'use strict';

    return {
      restrict: 'E',
      scope: {
        form: '=',
        config: '=',
        model: '='
      },
      templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueBefattningSpecialitet/ueBefattningSpecialitet.directive.html',
      link: function($scope) {
        //Make a printable list of Befattningar (which as of yet consists of un-readable codes...)
        $scope.befattningar = UserModel.user.befattningar;

        //Make a printable list of Specialiteter
        $scope.specialiteter = UserModel.user.specialiseringar.join(', ');
      }
    };
  }]);
