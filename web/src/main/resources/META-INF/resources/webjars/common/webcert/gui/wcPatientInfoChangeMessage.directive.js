/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
 * Show info if patient info changed from djupintegration compared to the shown info.
 */
angular.module('common').directive('wcPatientInfoChangeMessage', [
    '$stateParams', '$rootScope', 'common.IntygViewStateService',
    function($stateParams, $rootScope, CommonViewState) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: true,
            controller: function($scope) {

                $scope.patient = {};
                var unbindFastEvent = $rootScope.$on('ViewCertCtrl.load', function (event, intyg, intygProperties) {
                    // Listen when a certificate is loaded and make a
                    // control if a patient's name or address has been changed.
                    $scope.patient.changedName = CommonViewState.patient.hasChangedName(intyg, $stateParams);
                    $scope.patient.changedAddress = CommonViewState.patient.hasChangedAddress(intyg, $stateParams);
                });
                $scope.$on('$destroy', unbindFastEvent);

            },
            templateUrl: '/web/webjars/common/webcert/gui/wcPatientInfoChangeMessage.directive.html'
        };
    }]);
