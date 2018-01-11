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
            restrict: 'E',
            scope: {
                textBeforeRelation: '@',
                viewState: '='
            },
            link: function($scope, $element, $attributes) {

                $scope.intygRelation = {};
                $scope.showMessage = true;

                var scopePathToIntygRelation = 'viewState.common.intygProperties.latestChildRelations.' + $attributes.intygRelation;

                var updateMessage = function() {
                    $scope.intygRelation = $scope.$eval(scopePathToIntygRelation);
                    $scope.showMessage = !!$scope.intygRelation;
                };

                // intyg data may be loaded now, or it may be loaded later.
                $scope.$watch(scopePathToIntygRelation, updateMessage);
                updateMessage();
            },
            controller: function($scope) {
                $scope.gotoIntyg = function($event) {
                    if ($event) {
                        $event.preventDefault();
                    }
                    if ($scope.intygRelation) {
                        $location.path(
                            '/intyg/' + $scope.viewState.common.intygProperties.type + '/' + $scope.intygRelation.intygsId + '/');
                    }
                };

            },
            templateUrl: '/web/webjars/common/webcert/components/wcIntygRelatedOtherIntygMessage/wcIntygRelatedOtherIntygMessage.directive.html'
        };
    }]);
