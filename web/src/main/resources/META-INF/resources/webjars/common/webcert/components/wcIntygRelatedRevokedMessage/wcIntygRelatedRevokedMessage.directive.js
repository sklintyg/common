/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
/**
 * Show message that this intyg has a newer replacement and a link to that intyg..
 * Broadcast a intyg.loaded event on rootscope when the intyg is loaded to update the message.
 */
angular.module('common').directive('wcIntygRelatedRevokedMessage', [
    '$location', 'common.IntygProxy',
    function($location, IntygProxy) {
        'use strict';

        var CommonIntygViewState;
        var intygProperties;
        return {
            restrict: 'E',
            scope: {
                intygViewState: '='
            },
            link: function($scope, $element, $attributes) {

                CommonIntygViewState = $scope.intygViewState.common;
                intygProperties = CommonIntygViewState.intygProperties;

                var onSuccess = function(result) {
                    if (result !== null && result !== '') {
                        intygProperties.parent.states = result.statuses;
                    }
                };

                var onError = function(result) {
                    CommonIntygViewState.updateActiveError(result, intygProperties.parent.status);
                };

                function loadStates() {
                    IntygProxy.getIntyg(intygProperties.parent.intygsId, intygProperties.type, onSuccess, onError);
                }

                var updateRelation = function() {
                    if (intygProperties.parent && intygProperties.parent.relationKod === 'ERSATT') {
                        loadStates();
                    }
                };

                // intyg data may be loaded now, or it may be loaded later.
                $scope.$on('intyg.loaded', updateRelation);
                updateRelation();
            },
            controller: function($scope) {
                $scope.gotoIntyg = function($event) {
                    if ($event) {
                        $event.preventDefault();
                    }
                    if (intygProperties.parent) {
                        $location.path(
                            '/intyg/' + intygProperties.type + '/' + intygProperties.parent.intygsId + '/');
                    }
                };

                $scope.isRevoked = function() {
                    return intygProperties.isRevoked || CommonIntygViewState.isIntygOnRevokeQueue;
                };

                $scope.showConfirmedMessage = function() {
                    return intygProperties.isRevoked && (!CommonIntygViewState.isIntygOnRevokeQueue || CommonIntygViewState.isIntygOnRevokeQueue === 'undefined');
                };

                $scope.showRequestedMessage = function() {
                    return CommonIntygViewState.isIntygOnRevokeQueue;
                };

                $scope.showMessage = function() {
                    return intygProperties.parent && intygProperties.parent.states && intygProperties.parent.states[0].type !== 'CANCELLED';
                };

                $scope.buildKeyBaseForRevoked = function() {
                    var intygstyp = intygProperties.type;
                    if(intygstyp === 'db' || intygstyp === 'doi') {
                        return intygstyp;
                    } else {
                        return 'intyg';
                    }
                };

            },
            templateUrl: '/web/webjars/common/webcert/components/wcIntygRelatedRevokedMessage/wcIntygRelatedRevokedMessage.directive.html'
        };
    }]);
