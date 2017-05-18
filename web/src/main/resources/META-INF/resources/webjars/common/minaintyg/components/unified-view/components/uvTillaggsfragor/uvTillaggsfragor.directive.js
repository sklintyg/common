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

angular.module('common').directive('uvTillaggsfragor', [ 'uvUtil', function(uvUtil) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            viewData: '='
        },
        templateUrl: '/web/webjars/common/minaintyg/components/unified-view/components/uvTillaggsfragor/uvTillaggsfragor.directive.html',
        link: function($scope) {

            $scope.$watchCollection(function() {
                return uvUtil.getValue($scope.viewData, $scope.config.modelProp);
            }, function() {
                buildFrageConfig();
            });

            function buildFrageConfig() {
                var tillaggsfragor = uvUtil.getValue($scope.viewData, $scope.config.modelProp);
                var kategoriConfig = null;
                for (var i = 0; i < tillaggsfragor.length; i++) {

                    // Defer initialization of kategori config until inside
                    // loop so that it only exists if there are tillagsfragor defined for this version of the intyg.
                    kategoriConfig = kategoriConfig || {
                        labelKey: $scope.config.labelKey,
                        components: []
                    };

                    var frageConfig = {
                        type: 'uv-fraga',
                        labelKey: 'DFR_' + tillaggsfragor[i].id + '.1.RBK',
                        components: [ {
                            type: 'uv-del-fraga',
                            components: [ {
                                type: 'uv-simple-value',
                                modelProp: 'tillaggsfragor[' + i + '].svar'
                            } ]

                        } ]

                    };
                    kategoriConfig.components.push(frageConfig);

                }
                //return kategoriConfig;
                $scope.tillaggsConfig = kategoriConfig;
            }

        }
    };
} ]);
