/**
 * Listen to intyg loaded event and present a message that the user is marked for secrecy (sekretessmarkerad) if he is.
 */
angular.module('common').directive('wcSecrecyMarkMessage', [
    'common.PatientProxy', 'common.ViewStateService', 'common.featureService',
    function(PatientProxy, ViewStateService, featureService) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: true,
            controller: function($scope) {

                $scope.viewstate = ViewStateService;

                /*
                 * Lookup patient to check for sekretessmarkering
                 */
                function lookupPatient(content) {
                    ViewStateService.sekretessmarkering = false;
                    ViewStateService.sekretessmarkeringError = false;

                    var onSuccess = function(resultPatient) {
                        ViewStateService.sekretessmarkering = resultPatient.sekretessmarkering;
                    };

                    var onNotFound = function() {
                        ViewStateService.sekretessmarkeringError = true;
                    };

                    var onError = function() {
                        ViewStateService.sekretessmarkeringError = true;
                    };

                    if (content.grundData && content.grundData.patient && content.grundData.patient.personId) {
                        PatientProxy.getPatient(content.grundData.patient.personId, onSuccess, onNotFound, onError);
                    }
                }

                if (!featureService.isFeatureActive('franJournalsystem')) {
                    if ($scope.viewState.intygModel) {
                        lookupPatient($scope.viewState.intygModel);
                    }
                    $scope.$on('intyg.loaded', function(event, content) {
                        lookupPatient(content);
                    });
                }
            },
            templateUrl: '/web/webjars/common/webcert/patient/wcSecrecyMarkMessage.directive.html'
        };
    }]);
