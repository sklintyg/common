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

                var model = $scope.viewData[$scope.config.modelProp];
                $scope.viewModel = {
                    modelProp: '',
                    headers: [
                        // 'headerValue1','headerValue2', 'headerValue3'
                    ],
                    rows: [
                        // {valueProps: ['colValue1', 'colValue2', 'colValue3']},
                    ]
                };

                $scope.$watch('viewData', function(current, previous){
                    $scope.viewModel.modelProp = $scope.config.modelProp;
                    model = current[$scope.config.modelProp];
                    updateTable();
                });

                $scope.$on('dynamicLabels.updated', function(){
                    updateTable();
                });

                function updateTable(){

                    if (angular.isDefined(model)) {

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

                                if(angular.isDefined(dynamicLabel) && dynamicLabel !== ''){
                                    this.push(dynamicLabel);
                                } else if(angular.isDefined(header)) {
                                    this.push(header);
                                }
                            }

                        }, $scope.viewModel.headers);

                        // Check if valueProps are dot-paths or a function or just a simple value and resolve viewData accordingly
                        $scope.viewModel.rows = [];

                        angular.forEach(model, function(modelRow){
                            var row = { valueProps: [] };
                            angular.forEach($scope.config.valueProps, function(prop, key) {

                                if(typeof prop === 'function'){
                                    // Resolve using function
                                    row.valueProps.push(prop(modelRow, key));
                                } else if(prop.indexOf('.') !== -1) {
                                    // Resolve dot-path
                                    var dotPropValue = prop.split('.').reduce(function index(obj, value) {
                                        return obj[value];
                                    }, modelRow);
                                    row.valueProps.push(dotPropValue);
                                } else if(modelRow.hasOwnProperty(prop)) {
                                    // Resolve using property name
                                    row.valueProps.push(modelRow[prop]);
                                }

                            }, row);
                            this.push(row);
                        }, $scope.viewModel.rows);
                    }
                }

            }

        };
    }]);
