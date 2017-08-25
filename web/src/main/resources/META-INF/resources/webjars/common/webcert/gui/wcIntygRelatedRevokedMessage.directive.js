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

/**
 * Show message that this intyg has a newer replacement and a link to that intyg..
 * Broadcast a intyg.loaded event on rootscope when the intyg is loaded to update the message.
 */
angular.module('common').directive('wcIntygRelatedRevokedMessage', [
    '$location', 'common.IntygProxy',
    function($location, IntygProxy) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                viewState: '='
            },
            link: function($scope, $element, $attributes) {

                var scopePathToIntygRelation = 'viewState.common.intygProperties.parent';

                var onSuccess = function(result) {
                    if (result !== null && result !== '') {
                        $scope.intygRelation.states = result.statuses;
                    }
                };

                var onError = function(result) {
                    $scope.viewState.common.updateActiveError(result, $scope.intygRelation.status);
                }

                function loadStates() {
                    IntygProxy.getIntyg($scope.intygRelation.intygsId, $scope.viewState.common.intygProperties.type, onSuccess, onError);
                }

                var updateRelation = function() {
                    $scope.intygRelation = $scope.$eval(scopePathToIntygRelation);
                    if ($scope.intygRelation) {
                        loadStates();
                    }
                };

                // intyg data may be loaded now, or it may be loaded later.
                $scope.$watch(scopePathToIntygRelation, updateRelation);
                updateRelation();

            },
            controller: function($scope) {
                $scope.gotoIntyg = function($event) {
                    if ($event) {
                        $event.preventDefault();
                    }
                    if ($scope.intygRelation) {
                        $location.path(
                            '/intyg/' + $scope.viewState.common.intygProperties.type + '/' + $scope.intygRelation.intygsId);
                    }
                };

                $scope.isRevoked = function() {
                    return $scope.viewState.common.intygProperties.isRevoked || $scope.viewState.common.isIntygOnRevokeQueue;
                };

                $scope.showConfirmedMessage = function() {
                    return $scope.viewState.common.intygProperties.isRevoked && (!$scope.viewState.common.isIntygOnRevokeQueue || $scope.viewState.common.isIntygOnRevokeQueue === 'undefined');
                };

                $scope.showRequestedMessage = function() {
                    return $scope.viewState.common.isIntygOnRevokeQueue;
                };

                $scope.showMessage = function() {
                    return $scope.intygRelation.states && $scope.intygRelation.states[0].type !== 'CANCELLED';
                };

            },
            templateUrl: '/web/webjars/common/webcert/gui/wcIntygRelatedRevokedMessage.directive.html'
        };
    }]);
