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

                if (authorityService.isAuthorityActive({authority: 'HANTERA_PERSONUPPGIFTER'})) {
                    $scope.$watch('personId', function() {
                        lookupPatient($scope.personId);
                    });
                }
            },
            templateUrl: '/web/webjars/common/webcert/patient/wcSecrecyMarkMessage.directive.html'
        };
    }]);
