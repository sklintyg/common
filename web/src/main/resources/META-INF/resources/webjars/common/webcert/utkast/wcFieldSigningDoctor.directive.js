/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

/**
 * Show signing doctor name if it exists in the stateParams.
 * Broadcast a intyg.loaded event on rootscope when the intyg is loaded to update the message.
 */
angular.module('common').directive('wcFieldSigningDoctor',
    [ '$stateParams',
        function($stateParams) {
            'use strict';

            return {
                restrict: 'A',
                controller: function($scope) {
                    $scope.show = $stateParams.hospName !== undefined && $stateParams.hospName !== '';
                    $scope.hospName = $stateParams.hospName;
                },
                templateUrl: '/web/webjars/common/webcert/utkast/wcFieldSigningDoctor.directive.html'
            };
        }]);
