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
angular.module('common').directive('wcExpandableContent', [ '$timeout', 'common.messageService', function($timeout, messageService) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            content: '=',
            thresholdPixelHeight: '@',
            linkDomId: '@'
        },
        templateUrl: '/web/webjars/common/webcert/components/wcExpandableContent/wcExpandableContent.directive.html',
        link: function($scope, element) {

            //Get a handle to the actual element that holds the content
            var contentDiv = element[0].querySelector('.content');

            $scope.vm = {
                showControls: false,
                showsAll: false
            };

            var _setHeightConstraint = function(constrain) {
                angular.element(contentDiv).css('max-height', constrain ? $scope.thresholdPixelHeight : 'none');
                angular.element(contentDiv).css('overflow-y', constrain ? 'hidden' : 'visible');

            };

            var _update = function() {

                //Start by using the specified height constraint
                _setHeightConstraint(true);

                var offsetHeight = contentDiv.offsetHeight;
                var scrollHeight = contentDiv.scrollHeight;
                var contentTruncated = scrollHeight > offsetHeight;

                //Is there isn't any content rendered in the element yet (like first call to update) - do nothing..
                if (offsetHeight > 0) {
                    //If not all content could be displayed withing the constraint - we should enable the show more/less controls
                    if (contentTruncated) {
                        $scope.vm.showControls = contentTruncated;
                    }

                    //Potentially remove constraint if user toggled so that all should be shown
                    _setHeightConstraint(!$scope.vm.showsAll);
                }

            };

            $scope.toggle = function() {
                $scope.vm.showsAll = !$scope.vm.showsAll;
                _update();
            };

            // Sent by wcSupportPanelManager when tab is changed.
            // wcSupportPanelManager uses ng-show, content will be in dom but offsetHeight will be 0 until it is displayed.
            $scope.$on('panel.activated', function() {
                $timeout(function(){
                    _update();
                });
            });

            //Let the DOM render the content and then check if we have overflow..
            $timeout(function(){
                _update();
            });
        }
    };
} ]);
