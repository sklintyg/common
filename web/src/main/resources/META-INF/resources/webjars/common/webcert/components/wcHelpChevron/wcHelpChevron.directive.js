/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcHelpChevron',
    [ '$log', '$rootScope', 'common.messageService', 'common.dynamicLabelService', 'common.ObjectHelper',
        function($log, $rootScope, messageService, dynamicLabelService, ObjectHelper) {
            'use strict';

            return {
                restrict: 'AE',
                scope: {
                    helpTextKey: '@',
                    variableLabelKey: '@'
                },
                templateUrl: '/web/webjars/common/webcert/components/wcHelpChevron/wcHelpChevron.directive.html',
                link: function($scope, element, attr) {

                    $scope.isCollapsed = true;

                    $scope.toggleHelp = function(){
                        $scope.isCollapsed = !$scope.isCollapsed;
                        $rootScope.$broadcast('help-chevron-' + $scope.helpTextKey, { id: $scope.helpTextKey, action: 'toggle'});
                    };

                    function setText(text) {
                        if(!ObjectHelper.isEmpty(text)){
                            $rootScope.$broadcast('help-chevron-' + $scope.helpTextKey, { id: $scope.helpTextKey, action: 'setText', text: text});
                            $scope.showHelp = true;
                        } else {
                            // if we have empty string, dont render the ? icon in GUI.
                            $scope.showHelp = false;
                        }
                    }

                    function updateMessage() {
                        if(!ObjectHelper.isEmpty($scope.helpTextKey)) {
                            var helpText = dynamicLabelService.getProperty($scope.helpTextKey, true);
                            if ($scope.variableLabelKey) {
                                var statLabel;
                                if (messageService.propertyExists($scope.variableLabelKey)) {
                                    statLabel = messageService.getProperty($scope.variableLabelKey);
                                } else {
                                    statLabel = dynamicLabelService.getProperty($scope.variableLabelKey, true);
                                }
                                if (statLabel) {
                                    helpText = helpText.replace('{0}', statLabel);
                                }
                            }
                            setText(helpText);
                        }
                    }

                    // Either texts have already loaded and the function below will do the job
                    // or
                    // texts failed to update first but they are updated on the dynamicLabels.updated event sent when utkast AND texts have been loaded.
                    $scope.$on('dynamicLabels.updated', function() {
                        $log.debug('updating from intyg.loaded message');
                        updateMessage();
                    });

                    attr.$observe('helpTextKey', updateMessage);

                    updateMessage();
                }
            };
        }]);
