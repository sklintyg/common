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
/**
 * Enable help mark with multiple labels with tooltip for other components than wcFields
 */
angular.module('common').directive('wcHelpMarkGroup',
    ['$log', 'common.messageService', 'common.dynamicLabelService', 'common.ObjectHelper',
        function($log, messageService, dynamicLabelService, ObjectHelper) {
            'use strict';

            return {
                restrict: 'A',
                transclude: true,
                scope: {
                    fieldDynamicLabelGroup: '=',
                    fieldTooltipPlacement: '@'
                },
                controller: function($scope) {
                    $scope.text = '';
                    $scope.showHelp = false;
                    var labelTexts = '';

                    if ($scope.fieldTooltipPlacement === undefined) {
                        $scope.placement = 'bottom';
                    } else {
                        $scope.placement = $scope.fieldTooltipPlacement;
                    }

                    function getLabelText(id) {
                        var imported = dynamicLabelService.getProperty(id);

                        // if we any empty string or only extension, dont render the ? icon in GUI.
                        if (!imported || imported.split('.')[0] === '') {
                            $scope.showHelp = false;
                        } else {
                            $scope.showHelp = true;
                            labelTexts += '<h5>'+ id +'</h5>';
                            labelTexts += '<p>'+ imported +'</p>';
                        }
                    }

                    function updateLabels() {
                        if ($scope.fieldDynamicLabelGroup.length > 0 && $scope.fieldDynamicLabelGroup[0] !== '') {

                            for (var i = 0; i < $scope.fieldDynamicLabelGroup.length; i++) {
                                    getLabelText($scope.fieldDynamicLabelGroup[i]);
                            }
                            $scope.text = labelTexts;
                            labelTexts = '';
                        }
                    }

                    // Either texts have already loaded and the function below will do the job
                    // or
                    // texts failed to update first but they are updated on the dynamicLabels.updated event sent when utkast AND texts have been loaded.

                    $scope.$on('dynamicLabels.updated', function() {
                        updateLabels();
                    });
                    updateLabels();
                },
                templateUrl: '/web/webjars/common/webcert/components/wcHelpMarkGroup/wcHelpMarkGroup.directive.html'

            };
        }]);


