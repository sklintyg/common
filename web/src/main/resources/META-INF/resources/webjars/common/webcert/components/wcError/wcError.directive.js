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

angular.module('common').directive('wcError',
    ['$stateParams', function($stateParams) {
            'use strict';

            return {
                restrict: 'E',
                transclude: false,
                scope: {
                    error: '=',
                    errorTitle: '@',
                    errorMsg: '@',
                    redirectingLink: '@'
                },
                templateUrl: '/web/webjars/common/webcert/components/wcError/wcError.directive.html',
                link: function($scope, element, attrs, ctrl) {
                    if($scope.error !== 'common.error.authorization_problem_sekretessmarkering_enhet' && $scope.error !== 'common.error.authorization_problem_sekretessmarkering') {
                      $scope.certificateId = $stateParams.certificateId;
                    }
                }
            };
        }]);
