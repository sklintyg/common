/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcApproveReceiversButton', [
    'common.authorityService', 'common.UserModel', 'common.receiverService',
    function(authorityService, UserModel, receiverService) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                intygtyp: '=',
                intygid: '=',
                vardenhet: '@'
            },
            templateUrl: '/web/webjars/common/webcert/components/receiver/wcApproveReceiversButton/wcApproveReceiversButton.directive.html',
            link: function($scope) {

                $scope.vm = {
                  hasBasicAuth: authorityService.isAuthorityActive({
                      authority: UserModel.privileges.GODKANNA_MOTTAGARE,
                      intygstyp: $scope.intygtyp }),
                   hasMultipleReceivers : undefined
                };


                if ($scope.vm.hasBasicAuth) {
                    //If not even basic auth, no need to check further
                    receiverService.getApprovedReceivers($scope.intygtyp, $scope.intygid, true).then(
                        function done(approvedList) {
                            $scope.vm.hasMultipleReceivers = angular.isDefined(approvedList) && approvedList.length > 1;
                        },
                        function fail(data) {

                            //button will not be shown.
                        });
                }



                $scope.isEnabled = function() {
                    return $scope.vm.hasBasicAuth && $scope.vm.hasMultipleReceivers === true;
                };

                $scope.openDialog = function() {
                    receiverService.openConfigDialogForIntyg($scope.intygtyp, $scope.intygid, true);
                };

            }

        };
    } ]);
