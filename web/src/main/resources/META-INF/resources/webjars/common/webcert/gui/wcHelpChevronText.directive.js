/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
    [ '$log', 'common.messageService', 'common.dynamicLabelService', 'common.ObjectHelper',
        function($log, messageService, dynamicLabelService, ObjectHelper) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                scope: {
                    helpTextKey: '@'
                },
                templateUrl: '/web/webjars/common/webcert/gui/wcHelpChevronText.directive.html',
                controller: function($scope) {

                    $scope.text = '';
                    $scope.isCollapsed = true;

                    $scope.$on('help-chevron-' + $scope.helpTextKey, function(event, data){

                        if(!ObjectHelper.isDefined(data.id) || !ObjectHelper.isDefined($scope.helpTextKey) ||
                            data.id !== $scope.helpTextKey){
                            return;
                        }

                        switch(data.action){
                        case 'setText': setText(data.text); break;
                        case 'toggle':
                            $scope.isCollapsed = !$scope.isCollapsed;
                        }

                    });

                    function setText(text) {
                        if(!ObjectHelper.isEmpty(text)){
                            $scope.text = text;
                        } else {
                            $scope.text = '';
                        }
                    }
                }
            };
        }]);
