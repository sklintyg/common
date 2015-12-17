/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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
 * Listen to intyg loaded event and present a message that the user is marked for secrecy (sekretessmarkerad) if he is.
 */
angular.module('common').directive('wcSecrecyMarkMessage', [
    'common.PatientProxy', 'common.ViewStateService', 'common.authorityService',
    function(PatientProxy, ViewStateService, authorityService) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                personId: '=',
                type: '@'
            },
            controller: function($scope) {

                $scope.viewstate = ViewStateService;

                /*
                 * Lookup patient to check for sekretessmarkering
                 */
                function lookupPatient(personId) {
                    ViewStateService.sekretessmarkering = false;
                    ViewStateService.sekretessmarkeringError = false;

                    if (!personId) {
                        return;
                    }

                    var onSuccess = function(resultPatient) {
                        ViewStateService.sekretessmarkering = resultPatient.sekretessmarkering;
                    };

                    var onNotFound = function() {
                        ViewStateService.sekretessmarkeringError = true;
                    };

                    var onError = function() {
                        ViewStateService.sekretessmarkeringError = true;
                    };

                    PatientProxy.getPatient(personId, onSuccess, onNotFound, onError);
                }

                if (authorityService.isAuthorityActive({authority: 'PRIVILEGE_HANTERA_PERSONUPPGIFTER'})) {
                    $scope.$watch('personId', function() {
                        lookupPatient($scope.personId);
                    });
                }
            },
            templateUrl: '/web/webjars/common/webcert/patient/wcSecrecyMarkMessage.directive.html'
        };
    }]);
