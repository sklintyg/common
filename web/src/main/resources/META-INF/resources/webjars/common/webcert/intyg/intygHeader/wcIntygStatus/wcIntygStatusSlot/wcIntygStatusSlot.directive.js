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
angular.module('common').directive('wcIntygStatusSlot', [
    '$uibModal', 'common.messageService', 'common.IntygHeaderViewState', 'common.IntygStatusService',
    function($uibModal, messageService, IntygHeaderViewState, IntygStatusService) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                intygstatus: '<'
            },
            templateUrl: '/web/webjars/common/webcert/intyg/intygHeader/wcIntygStatus/wcIntygStatusSlot/wcIntygStatusSlot.directive.html',
            link: function($scope) {

                $scope.openModal = function() {
                    var statusCode = $scope.intygstatus.code.toLowerCase();
                    var modalInstance = $uibModal.open({
                        templateUrl: '/web/webjars/common/webcert/intyg/intygHeader/wcIntygStatus/wcIntygStatusSlot/wcIntygStatusModal.template.html',
                        size: 'md',
                        controller: function($scope) {
                            var vars = {
                                recipient: IntygHeaderViewState.recipientText
                            };
                            $scope.header = messageService.getProperty(IntygStatusService.getMessageKeyForIntyg('.modalheader.intygstatus.' + statusCode), vars);
                            $scope.body = messageService.getProperty(IntygStatusService.getMessageKeyForIntyg('.modalbody.intygstatus.' + statusCode), vars);
                        }
                    });
                    //angular > 1.5 warns if promise rejection is not handled (e.g backdrop-click == rejection)
                    modalInstance.result.catch(function () {}); //jshint ignore:line
                };

            }
        };
    }
]);
