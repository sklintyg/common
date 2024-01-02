/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('common').directive('ueCheckgroupTs', [ '$parse',  'ueUtil',
    function($parse, ueUtil) {
    'use strict';
    return {
        restrict: 'E',
        scope: {
            form: '=',
            config: '=',
            model: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueCheckgroupTs/ueCheckgroupTs.directive.html',
        link: function($scope) {
            ueUtil.standardSetup($scope);

            var md = 'col-md-2';
            var sm = 'col-sm-4';

            if($scope.config.colSize){
                if($scope.config.colSize.md){
                    md = 'col-md-' + $scope.config.colSize.md;
                }

                if($scope.config.colSize.sm){
                    sm += ' col-sm-' + $scope.config.colSize.sm;
                }
            }

            $scope.colStyle = md + ' ' + sm;
        }
    };

}]);
