/**
 * Show patient has new id message if it differs from the one from the intyg.
 * Broadcast a intyg.loaded event on rootscope when the intyg is loaded to update the message.
 */
angular.module('common').directive('wcNewPersonIdMessage',
    function($routeParams) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            controller: function($scope) {
                $scope.show = false;
                $scope.patientId = $routeParams.patientId;

                $scope.$on('intyg.loaded', function(e, cert) {
                    // also make sure patient ids are valid and in the same format? shouldn't need to since the
                    // source is a journalsystem.
                    if ($routeParams.patientId !== undefined && $routeParams.patientId !== '') {
                        if (cert.grundData.patient.personId !== $routeParams.patientId) {
                            $scope.show = true;
                        }
                    }
                });

            },
            templateUrl: '/web/webjars/common/webcert/js/directives/wcNewPersonIdMessage.html'
        };
    });
