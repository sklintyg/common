/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('common').directive('ueTextarea', [ '$parse', 'common.ObjectHelper', 'common.AtticHelper', 'common.UtkastViewStateService', 'ueUtil',
    'common.UtkastViewStateService', function($parse, ObjectHelper, AtticHelper, UtkastViewState, ueUtil) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            form: '=',
            config: '=',
            model: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueTextarea/ueTextarea.directive.html',
        link: function($scope) {

            $scope.validation = UtkastViewState.validation;

            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);

            $scope.onBlur = angular.bind(this, ueUtil.updateValidation, $scope.form, $scope.model);

            // This is a ngModel getter/setter function
            // needed for component ue-tillaggsfragor with modelProp like tillaggsfragor[0].svar
            $scope.modelGetterSetter = function(newValue) {
                var getterSetter = $parse($scope.config.modelProp);
                return arguments.length ? (getterSetter.assign($scope.model, newValue)) : getterSetter($scope.model);
            };

        }
    };
}]);
