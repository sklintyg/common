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

angular.module('common').controller('common.SendCertWizardCtrl',
    [ '$filter', '$location', '$rootScope', '$stateParams', '$scope', 'common.IntygListService',
        'common.IntygSendService',
        function($filter, $location, $rootScope, $stateParams, $scope, intygListService, intygSendService) {
            'use strict';

            // Get module and default recipient from querystring
            var params = $location.search();
            $scope.module = params.module;

            // Initialize default recipient
            $scope.selectedRecipientId = params.defaultRecipient;

            $scope.sendingInProgress = false;
            // Get active certificate from rootscope (passed from previous controller)
            $scope.cert = $rootScope.cert;

            if (!angular.isObject($scope.cert)) {
                $location.path($scope.module + '/fel/certnotfound');
                return;
            }
            // expose calculated static link for pdf download
            $scope.downloadAsPdfLink = '/moduleapi/certificate/' + $scope.cert.id + '/pdf';

            // set selected recipeintID in rootscope to preserve state between
            // controller instance invocations
            $rootScope.selectedRecipientId = $scope.selectedRecipientId;

            $scope.recipientList = [];
            intygSendService.getRecipients($scope.module, function(result) {
                if (result !== null && result.length > 0) {
                    for (var i = 0; i < result.length; ++i) {
                        $scope.recipientList[i] = {
                            'id': result[i].id,
                            'recipientName': result[i].name
                        };
                    }
                } else {
                    // show error view
                    $location.path($scope.module + '/fel/failedreceiverecipients');
                }
            }, function() {
                // show error view
                $location.path($scope.module + '/fel/failedreceiverecipients');
            });

            $scope.getRecipientName = function(id) {
                for (var i = 0; i < $scope.recipientList.length; i++) {
                    var recipient = $scope.recipientList[i];
                    if (recipient.id === id) {
                        return recipient.recipientName;
                    }
                }
            };

            $scope.confirmRecipientSelection = function() {
                // now we have a recipient selected, set the selection in rootscope
                $rootScope.selectedRecipientId = $scope.selectedRecipientId;
                $location.path($scope.module + '/summary');
            };

            $scope.confirmAndSend = function() {
                $scope.sendingInProgress = true;
                intygSendService.sendCertificate($scope.cert.id, $scope.selectedRecipientId, function(result) {
                    $scope.sendingInProgress = false;
                    if (result !== null && result.resultCode === 'sent') {
                        intygListService.emptyCache();
                        $location.path($scope.module + '/sent');
                    } else {
                        // show error view
                        $location.path($scope.module + '/fel/couldnotsend');
                    }
                });
            };

            $scope.backToViewRecipents = function() {
                $location.path($scope.module + '/recipients');
            };

            $scope.alreadySentToRecipient = function() {
                // check if selected recipient exists with SENT status in cert history
                if (angular.isObject($scope.cert.filteredStatuses)) {
                    for (var i = 0; i < $scope.cert.filteredStatuses.length; i++) {
                        if (($scope.cert.filteredStatuses[i].type === 'SENT') &&
                            ($scope.cert.filteredStatuses[i].target === $scope.selectedRecipientId)) {
                            return true;
                        }
                    }
                }
                return false;
            };

            $scope.backToViewCertificate = function() {
                $location.path($scope.module + '/view/' + $scope.cert.id).search({});
            };

            $scope.pagefocus = true;
        }]);
