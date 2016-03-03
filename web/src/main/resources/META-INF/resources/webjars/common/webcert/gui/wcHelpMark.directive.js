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
 * Enable help marks with tooltip for other components than wcFields
 */
angular.module('common').directive('wcHelpMark',
    [ '$log', 'common.messageService', 'common.dynamicLabelService', 'common.ObjectHelper',
        function($log, messageService, dynamicLabelService, ObjectHelper) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                scope: {
                    fieldHelpText: '@',
                    fieldDynamicHelpText: '@',
                    fieldTooltipPlacement: '@'
                },
                controller: function($scope) {

                    $scope.text = '';
                    $scope.showHelp = false;

                    if ($scope.fieldTooltipPlacement === undefined) {
                        $scope.placement = 'right';
                    } else {
                        $scope.placement = $scope.fieldTooltipPlacement;
                    }

                    function updateMessage() {
                        if(!ObjectHelper.isEmpty($scope.fieldDynamicHelpText)) {
                            $scope.text = dynamicLabelService.getProperty($scope.fieldDynamicHelpText);
                            if($scope.text === ''){
                                $scope.showHelp = false;
                            } else{
                                $scope.showHelp = true;
                            }
                            //$log.debug('new help text dynamic:' + $scope.fieldDynamicHelpText + ', actual:' + $scope.text);
                        } else if(!ObjectHelper.isEmpty($scope.fieldHelpText)) {
                            $scope.text = messageService.getProperty($scope.fieldHelpText);
                            $scope.showHelp = true;
                            //$log.debug('new help text static:' + $scope.fieldHelpText + ', actual:' + $scope.text);
                        } else {
                            $scope.showHelp = false;
                            //$log.debug('disable help');
                        }
                    };

                    // Either texts have already loaded and the function below will do the job
                    // or
                    // texts failed to update first but they are updated on the dynamicLabels.updated event sent when utkast AND texts have been loaded.
                    $scope.$on('dynamicLabels.updated', function() {
                        //$log.debug('updating from intyg.loaded message');
                        updateMessage();
                    });

                    updateMessage();
                },
                templateUrl: '/web/webjars/common/webcert/gui/wcHelpMark.directive.html'
            };
        }]);
