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
            scope: true,
            controller: function($scope) {

                var updateShowFlag = function() {
                    // also make sure patient ids are valid and in the same format? shouldn't need to since the
                    // source is a journalsystem.
                    $scope.show = false;
                    $scope.patientId = $stateParams.patientId;
                    if ($stateParams.patientId !== undefined && $stateParams.patientId !== '') {
                        if ($scope.cert && $scope.cert.grundData && $scope.cert.grundData.patient && $scope.cert.grundData.patient.personId !== $stateParams.patientId) {
                            $scope.show = true;
                        }
                    }
                };

                // cert data may be loaded now, or it may be loaded later.
                updateShowFlag();
                $scope.$watch('cert.grundData.patient.personId', updateShowFlag);
            },
            templateUrl: '/web/webjars/common/webcert/js/directives/wcNewPersonIdMessage.html'
        };
    });
