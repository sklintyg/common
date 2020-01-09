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

angular.module('common').directive('uePrognos', ['$log', '$timeout', 'common.dynamicLabelService', 'common.ArendeListViewStateService',
    'common.AtticHelper', 'common.UtkastValidationService', 'common.UtkastViewStateService', function($log, $timeout, dynamicLabelService,
        ArendeListViewState, AtticHelper, UtkastValidationService, UtkastViewState) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            model: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/uePrognos/uePrognos.directive.html',
        link: function($scope) {

            $scope.validation = UtkastViewState.validation;

            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp + '.typ');

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp + '.typ');

            var chooseOption = {
                id: undefined,
                label: 'Välj tidsperiod'
            };

            $scope.prognosOptions = [];

            function updatePrognosOptions() {
                $scope.prognosOptions = [chooseOption];

                if ($scope.config.prognosDagarTillArbeteTyper) {
                    $scope.config.prognosDagarTillArbeteTyper.forEach(function (prognosDagarTillArbeteTyp) {
                        $scope.prognosOptions.push({
                            'id': prognosDagarTillArbeteTyp,
                            'label': dynamicLabelService.getProperty($scope.config.prognosDagarTillArbeteCode + '.' + prognosDagarTillArbeteTyp + '.RBK')
                        });
                    });
                }
            }

            $scope.hasKompletteringar = function() {
                return ArendeListViewState.hasKompletteringar($scope.config.modelProp);
            };

            $scope.$on('dynamicLabels.updated', function() {
                updatePrognosOptions();
            });

            $scope.validate = function() {
                $timeout(function(){
                    $log.debug('validating');
                    UtkastValidationService.validate($scope.model);
                });
            };

            updatePrognosOptions();

            $scope.$watch('model.prognos.typ', function _prognosTypListener( newValue, oldValue) {
                var model = $scope.model;
                if (newValue === 'ATER_X_ANTAL_DGR') {
                    model.restoreFromAttic('prognos.dagarTillArbete');
                } else {
                    if (oldValue === 'ATER_X_ANTAL_DGR') {
                        model.updateToAttic('prognos.dagarTillArbete');
                    }

                    model.clear('prognos.dagarTillArbete');
                }
            });

            $scope.$on('$destroy', function() {
                $scope.model.updateToAttic('prognos.dagarTillArbete');
                $scope.model.clear('prognos.dagarTillArbete');
            });
        }
    };

}]);
