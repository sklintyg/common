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
angular.module('common').directive('ueDate', [ '$parse', 'ueUtil', 'common.wcDatePeriodShorthandService',
    function($parse, ueUtil, datePeriodShorthandService) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                config: '=',
                model: '=',
                form: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueDate/ueDate.directive.html',
            link: function($scope) {
                ueUtil.standardSetup($scope);

                $scope.onBlur = function() {

                    if ($scope.config.fromModelProp) {
                        var fromValue = $parse($scope.config.fromModelProp)($scope.model);
                        var newTomValue = datePeriodShorthandService.applyToDateCodes(fromValue, $scope.modelGetterSetter());
                        if (newTomValue) {
                            $scope.modelGetterSetter(newTomValue);
                        }
                    }

                    $scope.updateValidation();
                };

            }
        };
    }]);

