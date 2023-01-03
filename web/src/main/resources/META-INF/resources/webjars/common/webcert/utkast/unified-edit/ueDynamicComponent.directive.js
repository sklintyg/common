/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
angular.module('common').directive('ueDynamicComponent',
    ['$compile', 'common.UtkastViewStateService', function($compile, UtkastViewState) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                form: '=',
                config: '=',
                model: '='
            },
            link: function($scope, $element) {

                if (!$scope.config.type) {
                    return;
                }

                $scope.validation = UtkastViewState.validation;

                if (!$scope.config.id) {
                    $scope.config.id = $scope.config.modelProp;
                }

                var componentTemplate = '<' + $scope.config.type;
                if ($scope.config.id) {
                    componentTemplate += ' ng-attr-id="form_{{::config.id|ueDomIdFilter}}"';
                }
                componentTemplate += ' form="::form" config="::config" model="::model"></' + $scope.config.type +'>';

                if ($scope.config.hideExpression)  {
                    componentTemplate = '<div ng-if="!config.hideExpression || !$eval(config.hideExpression)" class="fold-animation">'+componentTemplate+'</div>';
                }
                if ($scope.config.disabledFunc)  {
                    componentTemplate = '<fieldset ng-disabled="config.disabledFunc(model)">'+componentTemplate+'</fieldset>';
                }

                $element.append($compile(componentTemplate)($scope));
            }
        };
    }]);
