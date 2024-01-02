/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
 * Enable help marks with tooltip for other components than wcFields
 */
angular.module('common').directive('wcHelpChevronText',
    [ '$rootScope', '$compile', '$timeout', 'common.ObjectHelper',
        function($rootScope, $compile, $timeout, ObjectHelper) {
            'use strict';

            var animationCount = 0;

            return {
                restrict: 'AE',
                scope: {
                    helpTextKey: '@'
                },
                templateUrl: '/web/webjars/common/webcert/components/wcHelpChevronText/wcHelpChevronText.directive.html',
                link: function(scope, element){

                    scope.text = '';

                    function setText(text) {
                        if(!ObjectHelper.isEmpty(text)){
                            scope.text = text;
                        } else {
                            scope.text = '';
                        }
                    }

                    scope.$on('help-chevron-' + scope.helpTextKey, function(event, data){

                        if(!ObjectHelper.isDefined(data.id) || !ObjectHelper.isDefined(scope.helpTextKey) ||
                            data.id !== scope.helpTextKey){
                            return;
                        }

                        switch(data.action){
                            case 'setText': setText(data.text); break;
                        }

                    });
                },
                controller: function($scope) {

                    $scope.isCollapsed = true;

                    // Apparently animation end is called but not animation start when we start with isCollapsed=true
                    animationCount++;

                    $scope.$on('help-chevron-' + $scope.helpTextKey, function(event, data){

                        if(!ObjectHelper.isDefined(data.id) || !ObjectHelper.isDefined($scope.helpTextKey) ||
                            data.id !== $scope.helpTextKey){
                            return;
                        }

                        switch(data.action){
                        case 'toggle':
                            $scope.isCollapsed = !$scope.isCollapsed;
                        }

                    });

                    // These events will prevent wcHeaderHeightSync to match the header size while uib-collapse is animating
                    $scope.start = function() {
                        if (animationCount === 0) {
                            $rootScope.$broadcast('wcAnimationStart');
                        }
                        animationCount++;
                    };
                    $scope.stop = function() {
                        animationCount--;
                        if (animationCount === 0) {
                            $rootScope.$broadcast('wcAllAnimationsEnd');
                        }
                    };

                    $scope.stopEvents = function($event) {
                        $event.preventDefault();
                        $event.stopPropagation();
                    };
                }
            };
        }]);
