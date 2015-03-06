/**
 * Show patient has new id message if it differs from the one from the intyg.
 * Broadcast a intyg.loaded event on rootscope when the intyg is loaded to update the message.
 */
angular.module('common').directive('wcNewPersonIdMessage',
    function($stateParams) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            controller: function($scope) {
                $scope.show = false;
                $scope.patientId = $stateParams.patientId;

                $scope.$on('intyg.loaded', function(e, cert) {
                    // also make sure patient ids are valid and in the same format? shouldn't need to since the
                    // source is a journalsystem.
                    if ($stateParams.patientId !== undefined && $stateParams.patientId !== '') {
                        if (cert.grundData.patient.personId !== $stateParams.patientId) {
                            $scope.show = true;
                        }
                    }
                });

            },
            templateUrl: '/web/webjars/common/webcert/js/directives/wcNewPersonIdMessage.html'
        };
    });
