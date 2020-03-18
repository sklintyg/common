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

angular.module('common').controller('utkastDeletedModalCtrl', function($scope) {
  'use strict';

    $scope.init = function() {
      $scope.isBlocked = true;
      setTimeout(function() {
        $scope.isBlocked = false;
      }, 1000);
    };

    $scope.$on('modal.closing', function(event, reason, closed) {
      if($scope.isBlocked && !closed) {
        event.preventDefault();
      }
    });

    $scope.init();
  });