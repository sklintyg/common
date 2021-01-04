/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcFragaKomplettering', [ 'common.ArendeListViewStateService', 'common.UtilsService',
    function(ArendeListViewStateService, Utils) {
    'use strict';

    function _updateKompletteringActive(numericFrageId, $element) {
        // lookup if there's an unhandled komplettering for this frage-id
        var list = ArendeListViewStateService.getUnhandledKompletteringarForFraga(numericFrageId);
        if (list.length > 0) {
            // Not using ng-class for performance (IE 11)
            $element.addClass('komplettering-active');
        }
    }

    return {
        restrict: 'E',
        transclude: true,
        scope: {
            frageId: '='
        },
        templateUrl: '/web/webjars/common/webcert/components/wcFragaKomplettering/wcFragaKomplettering.directive.html',
        link: function($scope, $element) {

            var numericFrageId = Utils.extractNumericalFrageId($scope.frageId);
            if (numericFrageId) {
                _updateKompletteringActive(numericFrageId, $element);
                $scope.$on('arenden.updated', function() {
                    _updateKompletteringActive(numericFrageId, $element);
                });
            }

        }
    };
} ]);
