/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcFaqQuestion', ['smoothScroll', function(smoothScrollService) {
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

            var scrollContainerId = 'wc-about-modal-body';
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
                if ($scope.vm.open) {
                    var offset = Math.min(30, Math.floor($('#'+ scrollContainerId).height() / 2));
                    var options = {
                        containerId: scrollContainerId,
                        duration: 500,
                        easing: 'easeInOutQuart',
                        offset: offset
                    };
                    //scroll to this questions panel heading, centered vertically
                    var elementToScrollTo = elem.find('.panel-heading')[0];
                    smoothScrollService(elementToScrollTo, options);
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
