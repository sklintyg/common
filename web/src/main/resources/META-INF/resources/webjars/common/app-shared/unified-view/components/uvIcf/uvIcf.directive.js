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
angular.module('common').directive('uvIcf', [ 'uvUtil', function(uvUtil) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            viewData: '='
        },
        templateUrl: '/web/webjars/common/app-shared/unified-view/components/uvIcf/uvIcf.directive.html',
        link: function($scope) {
            var text = uvUtil.getValue($scope.viewData, $scope.config.modelProp);
            var kategorier = uvUtil.getValue($scope.viewData, $scope.config.kategoriProp);

            var value = '';

            if (kategorier.length > 0 ) {
                if($scope.config.modelProp === 'aktivitetsbegransning') {
                    value += 'Svårigheter som påverkar patientens sysselsättning:' + '\n';
                } else {
                    value += 'Problem som påverkar patientens möjlighet att utföra sin sysselsättning:' + '\n';
                }
                for (var i = 0; i < kategorier.length; i++) {
                    value += kategorier[i];
                    if (i !== kategorier.length - 1) {
                        value += ' - ';
                    }
                }
                value += '\n\n';
            }

            if (!angular.isUndefined(text)) {
                value += text;
            }

            $scope.value = value;

            $scope.hasValue = function() {
                return uvUtil.isValidValue($scope.value);
            };

        }
    };
} ]);
