/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
 * Give user a warning message if patient's address is missing (for the
 */
angular.module('common').directive('wcPatientAddressMissingFromIntegrationMessage', ['common.messageService', 'common.UserModel', 'common.ObjectHelper',
    'common.PatientService',
    function(messageService, UserModel, ObjectHelper, PatientService) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                intyg: '=',
                context: '@'
            },
            controller: function($scope) {

                $scope.$on('intyg.loaded', function() {
                    // Intyg is loaded asynchronously
                    $scope.show = PatientService.isMissingRequiredAddressIntegrationParameter($scope.context, $scope.intyg);
                });
            },
            templateUrl: '/web/webjars/common/webcert/gui/wcPatientAddressMissingFromIntegrationMessage.directive.html'
        };
    }]);
