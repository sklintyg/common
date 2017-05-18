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
angular.module('common').directive('wcIntygRelatedOtherIntygMessage', [
    '$location',
    function($location) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                textBeforeRelation: '@',
                viewState: '='
            },
            link: function($scope, $element, $attributes) {

                var completePathToRelationOnScope = 'viewState.common.intygProperties.' + $attributes.relation;

                var updateMessage = function() {
                    $scope.relation = $scope.$eval(completePathToRelationOnScope);
                    $scope.showMessage = angular.isObject($scope.relation);
                };

                // intyg data may be loaded now, or it may be loaded later.
                $scope.$watch(completePathToRelationOnScope, updateMessage);
                updateMessage();
            },
            controller: function($scope) {

                $scope.relation = undefined;
                $scope.showMessage = false;

                $scope.gotoIntyg = function($event) {
                    if ($event) {
                        $event.preventDefault();
                    }
                    //Go to either edit or view intyg    TODO MUST FIX!!!
                    if ($scope.relation.status === 'DRAFT_INCOMPLETE' || $scope.relation.status === 'DRAFT_COMPLETE') {
                        $location.path(
                            '/' + $scope.viewState.common.intygProperties.type + '/edit/' + $scope.relation.intygsId);
                    } else {
                        $location.path(
                            '/intyg/' + $scope.viewState.common.intygProperties.type + '/' + $scope.relation.intygsId);
                    }
                };

            },
            templateUrl: '/web/webjars/common/webcert/gui/wcIntygRelatedOtherIntygMessage.directive.html'
        };
    }]);
