/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
    '$log', 'uvUtil', 'uvTableService',
    function($log, uvUtil, uvTableService) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                config: '=',
                viewData: '='
            },
            template: '<div ng-include="contentUrl" class="table-wrapper"></div>',
            link: function($scope) {
                if ($scope.config.contentUrl) {
                    $scope.contentUrl = '/web/webjars/common/app-shared/unified-view/components/uvTable/uvTable-' + $scope.config.contentUrl + '.directive.html';
                } else {
                    $scope.contentUrl = '/web/webjars/common/app-shared/unified-view/components/uvTable/uvTable.directive.html';
                }

                if(!$scope.config) {
                    $log.debug('no view config present for uv-table controller');
                    return;
                }

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
                                this.push(header(key)); // Generate value from config function
                            } else if(angular.isDefined(header)) {
                                this.push(uvUtil.getTextFromConfig(header));
                            }
                        }, $scope.viewModel.headers);

                        // Check if valueProps are dot-paths or a function or just a simple value and resolve viewData accordingly
                        $scope.viewModel.rows = [];

                        // Only object and array models is supported
                        if (Array.isArray(model)){
                            $scope.viewModel.rows = uvTableService.createRowsFromArrayModel($scope.config, model);
                        } else if(typeof model === 'object'){
                            $scope.viewModel.rows = uvTableService.createRowsFromObjectModel($scope.config, model);
                        }
                    }

                }
            }
        };
    }]);
