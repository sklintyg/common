/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
angular.module('common').controller('approveReceiversDialogController', ['$scope', '$log', 'common.ReceiversProxy', 'common.receiverService', 'dlgConfig',
    function($scope, $log, ReceiversProxy, receiverService, dlgConfig) {
        'use strict';
        $scope.vm = {
            loading: true,
            saving: false,
            errorCode: null,
            model: null,
            dlgConfig: dlgConfig
        };

        //Get current approval status for intyg
        receiverService.getApprovedReceivers(dlgConfig.intygtyp, dlgConfig.intygid, false).then(
            function done(data) {
                $scope.vm.model = data;
        }).finally(function() { // jshint ignore:line
           $scope.vm.loading = false;
        });


        //Have all items been configured?
        $scope.allAnswered = function() {
          var allAnswered = true;
          angular.forEach($scope.vm.model, function(item) {
              allAnswered = (allAnswered && item.approvalStatus !== 'UNDEFINED');
          });
          return allAnswered;
        };

        function _getAllApproved() {
            var approved = [];
            angular.forEach($scope.vm.model, function(item) {
                if (item.approvalStatus === 'YES') {
                    approved.push(item.id);
                }
            });
            return approved;
        }
        //Save current config
        $scope.saveChanges = function() {
            $scope.vm.saving = true;
            $scope.vm.errorCode = null;

            ReceiversProxy.setApprovedReceivers(dlgConfig.intygtyp, dlgConfig.intygid, _getAllApproved()).then(
                function success(response) {
                    $scope.$dismiss();
                },
                function fail(response) {
                    $log.error('Failed to save mottagare:' + response);
                    $scope.vm.errorCode = 'common.receivers.save.error';
                }).finally(function() { // jshint ignore:line
                $scope.vm.saving = false;
            });
        };


    }]);
