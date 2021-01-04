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
angular.module('common').directive('ueRadiogroup', [ 'ueUtil', function(ueUtil) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            model: '=',
            form: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueRadiogroup/ueRadiogroup.directive.html',
        link: function($scope) {

            if (!$scope.config.htmlClass) {
                $scope.config.htmlClass = 'col-lg-12';
            } else {
                var str = $scope.config.htmlClass;
                $scope.classList = str.split(' ');
                for (var i = 0; i < $scope.classList.length; i++) {
                    if ($scope.classList[i] === 'no-padding') {
                        $scope.noPaddingClass = true;
                    } else {
                        $scope.config.htmlClass = $scope.classList[i];
                    }
                }
            }
            ueUtil.standardSetup($scope);
        }
    };

}]);
