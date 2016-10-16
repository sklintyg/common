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

angular.module('common').directive('wcUtkastPatientAddressUpdater',
    ['$timeout', '$log', 'common.PatientProxy',
    function($timeout, $log, PatientProxy) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            patientModel: '=',
            form: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/wcUtkastPatientAddressUpdater.directive.html',
        controller: function($scope) {
            $scope.onUpdateAddressClick = function() {
                if(!(angular.isObject($scope.patientModel) &&
                    angular.isString($scope.patientModel.personId))) {
                    $log.debug('No patientId to do lookup for.');
                    return;
                }

                $scope.fetchingPatientData = true;
                $scope.fetchingPatientDataErrorKey = null;
                $timeout(function() { // delay operation just a bit to make sure the animation is visible to the user
                    PatientProxy.getPatient($scope.patientModel.personId, function(patientResult) {
                        $scope.fetchingPatientData = false;

                        $scope.patientModel.postadress = patientResult.postadress;
                        $scope.patientModel.postnummer = patientResult.postnummer;
                        $scope.patientModel.postort = patientResult.postort;
                        $scope.form.$setDirty();

                    }, function() { // not found
                        $scope.fetchingPatientData = false;
                        $scope.fetchingPatientDataErrorKey = 'common.warning.patientdataupdate.failed';
                    }, function() { // error
                        $scope.fetchingPatientData = false;
                        $scope.fetchingPatientDataErrorKey = 'common.warning.patientdataupdate.failed';
                    });

                }, 500);
            };
        }
    };
} ]);