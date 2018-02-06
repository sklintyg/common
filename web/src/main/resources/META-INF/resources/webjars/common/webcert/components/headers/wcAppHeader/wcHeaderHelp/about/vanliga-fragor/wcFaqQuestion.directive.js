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
angular.module('common').directive('wcFaqQuestion', [ function() {
    'use strict';

    return {
        require: '^^?wcFaqToggler',
        restrict: 'E',
        transclude: true,
        scope: {
            title: '@'
        },
        templateUrl: '/web/webjars/common/webcert/components/headers/wcAppHeader/wcHeaderHelp/about/vanliga-fragor/wcFaqQuestion.directive.html',
        link: function($scope, elem, attrs, wcFaqToggler) {

            //Local open/closed state
            $scope.vm = {
                open: false
            };

            // Local toggle (that also will reset parent's global state)
            $scope.toggle = function() {
                $scope.vm.open = !$scope.vm.open;
                //tell parent wcFaqToggler (if present) that i've toggled, so that it can update it's state
                if (wcFaqToggler) {
                    wcFaqToggler.someChildToggledItself();
                }
            };

            // React to changes made to parent wcFaqToggler's global opened/closed state
            if (wcFaqToggler) {
                $scope.$watch(function() {
                    return wcFaqToggler.getGlobalState();
                }, function(globalExpandState) {
                    if (!angular.isUndefined(globalExpandState)) {
                        $scope.vm.open = globalExpandState;
                    }

                });
            }


        }
    };
} ]);
