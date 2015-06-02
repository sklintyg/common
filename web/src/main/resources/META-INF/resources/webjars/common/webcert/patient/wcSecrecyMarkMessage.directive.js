/**
 * Listen to intyg loaded event and present a message that the user is marked for secrecy (sekretessmarkerad) if he is.
 */
angular.module('common').directive('wcSecrecyMarkMessage', [
    'common.PatientProxy',
    function(PatientProxy) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: true,
            controller: function($scope) {

                /*
                 * Lookup patient to check for sekretessmarkering
                 */
                $scope.$on('intyg.loaded', function(event, content) {

                    $scope.sekretessmarkering = false;
                    $scope.sekretessmarkeringError = false;

                    var onSuccess = function(resultPatient) {
                        $scope.sekretessmarkering = resultPatient.sekretessmarkering;
                    };

                    var onNotFound = function() {
                        $scope.sekretessmarkeringError = true;
                    };

                    var onError = function() {
                        $scope.sekretessmarkeringError = true;
                    };

                    PatientProxy.getPatient(content.grundData.patient.personId, onSuccess, onNotFound, onError);
                });
            },
            templateUrl: '/web/webjars/common/webcert/patient/wcSecrecyMarkMessage.directive.html'
        };
    }]);
