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

angular.module('common').directive('ueTillaggsfragor', ['$compile', 'common.ueFactoryTemplatesHelper', function($compile, ueFactoryTemplates) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            form: '=',
            config: '=',
            model: '='
        },
        link: function($scope, $element) {

            function buildTillaggsfragor() {
                $scope.tillaggsfragorConfig = {};
                if ($scope.model.tillaggsfragor && $scope.model.tillaggsfragor.length > 0) {

                    var fragor = [];

                    for (var i = 0; i < $scope.model.tillaggsfragor.length; i++) {
                        var tillaggsfraga = $scope.model.tillaggsfragor[i];
                        fragor.push(ueFactoryTemplates.fraga(
                            tillaggsfraga.id,
                            'DFR_' + tillaggsfraga.id + '.1.RBK',
                            'DFR_' + tillaggsfraga.id + '.1.HLP',
                            {},
                            [{
                                type: 'ue-textarea',
                                modelProp: 'tillaggsfragor[' + i + '].svar',
                                id: 'tillaggsfragor_' + tillaggsfraga.id
                            }]
                        ));
                    }

                    $scope.tillaggsfragorConfig = [ueFactoryTemplates.kategori(
                        'tillaggsfragor',
                        'KAT_9999.RBK',
                        'KAT_9999.HLP',
                        {
                            hideExpression: 'model.avstangningSmittskydd'
                        },
                        fragor)];

                    // ue uses one-time-binding, this will force a redraw
                    var template = '<ue-render-components form="::form" config="tillaggsfragorConfig" model="::model" />';
                    $element.empty();
                    $element.append($compile(template)($scope));
                }
            }

            buildTillaggsfragor();
            $scope.$on('intyg.loaded', function () {
                buildTillaggsfragor();
            });
        }
    };

}]);
