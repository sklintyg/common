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

angular.module('common').directive('uvTable', [
    'common.dynamicLabelService',
    function(dynamicLabelService) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                config: '=',
                viewData: '='
            },
            templateUrl: '/web/webjars/common/minaintyg/components/unified-view/components/uvTable/uvTable.directive.html',
            link: function($scope) {

                $scope.viewModel = {
                    modelProp: 'sjukskrivning',
                    headers: [],
                    rows: [
                        {valueProps: ['25%', 'value11', 'value12']},
                        {valueProps: ['50%', 'value21', 'value22']},
                        {valueProps: ['75%', 'value31', 'value32']},
                        {valueProps: ['100%', 'value41', 'value42']}
                    ]
                };

                $scope.$watch('viewData', function(current, previous){

                    if (angular.isDefined(current[$scope.config.modelProp])) {

                        // Convert headers config to viewModel values
                        $scope.viewModel.headers = [];
                        angular.forEach($scope.config.headers, function(header, key) {

                            if(typeof header === 'function'){
                                // Generate value from config function
                                this.push(header(key));
                                return;
                            } else {
                                // Generate value from dynamic label if it existed, otherwise assume supplied value is what we want
                                var dynamicLabel = dynamicLabelService.getProperty(header);

                                if(dynamicLabel !== ''){
                                    if(angular.isDefined(dynamicLabel)){
                                        this.push(dynamicLabel);
                                    } else {
                                        this.push(header);
                                    }
                                }
                            }

                        }, $scope.viewModel.headers);

                        // Check if valueProps are dot-paths or a function or just a simple value and resolve viewData accordingly
                        angular.forEach($scope.config.valueProps, function() {

                        });
                    }
                });
            }

        };
    }]);
