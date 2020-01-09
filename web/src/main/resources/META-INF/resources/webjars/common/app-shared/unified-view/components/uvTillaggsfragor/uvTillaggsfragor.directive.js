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
angular.module('common').directive('uvTillaggsfragor', [ 'uvUtil', function(uvUtil) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            viewData: '='
        },
        templateUrl: '/web/webjars/common/app-shared/unified-view/components/uvTillaggsfragor/uvTillaggsfragor.directive.html',
        link: function($scope) {

            var categoryType = $scope.config.categoryType ? $scope.config.categoryType : 'uv-kategori';
            var questionType = $scope.config.questionType ? $scope.config.questionType : 'uv-fraga';
            var partQuestionType = $scope.config.partQuestionType ? $scope.config.partQuestionType : 'uv-del-fraga';
            var partQuestionContentUrl = $scope.config.partQuestionContentUrl ? $scope.config.partQuestionContentUrl : null;

            $scope.$watchCollection(function() {
                return uvUtil.getValue($scope.viewData, $scope.config.modelProp);
            }, function() {
                buildFrageConfig();
            });

            function buildFrageConfig() {
                var tillaggsfragor = uvUtil.getValue($scope.viewData, $scope.config.modelProp);
                var kategoriConfig = null;
                if (angular.isArray(tillaggsfragor)) {
                    for (var i = 0; i < tillaggsfragor.length; i++) {

                        // Defer initialization of kategori config until inside
                        // loop so that it only exists if there are tillagsfragor defined for this version of the intyg.
                        kategoriConfig = kategoriConfig || {
                                type: categoryType,
                                labelKey: $scope.config.labelKey,
                                components: []
                            };

                        var frageConfig = {
                            type: questionType,
                            labelKey: 'DFR_' + tillaggsfragor[i].id + '.1.RBK',
                            components: [{
                                type: partQuestionType,
                                contentUrl: partQuestionContentUrl,
                                components: [{
                                    type: 'uv-simple-value',
                                    modelProp: 'tillaggsfragor[' + i + '].svar',
                                    id: 'tillaggsfragor-' + tillaggsfragor[i].id
                                }]

                            }]

                        };
                        kategoriConfig.components.push(frageConfig);

                    }

                    //expose config to template
                    $scope.tillaggsConfig = kategoriConfig;
                }
            }

        }
    };
} ]);
