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
 * Renders a TH element with underline on hover and a tooltip on mouseover
 */
angular.module('common').directive('wcTableHead',
    ['common.messageService',
    function(messageService) {
        'use strict';

        return {
            restrict: 'A',
            templateUrl: '/web/webjars/common/webcert/components/wcTableHead/wcTableHead.directive.html',
            scope: {
                labelKey: '@',
                helpKey: '@',
                sorting: '=',
                sortingCurrent: '=',
                sortingAscending: '='
            },
            link: function($scope, element, attr) {

                $scope.text = messageService.getProperty($scope.labelKey);
                $scope.helpText = messageService.getProperty($scope.helpKey);

            }
        };
    }]);
