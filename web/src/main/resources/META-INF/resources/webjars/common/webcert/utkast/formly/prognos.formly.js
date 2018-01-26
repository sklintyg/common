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
angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'prognos',
        templateUrl: '/web/webjars/common/webcert/utkast/formly/prognos.formly.html',
        controller: ['$scope', '$log', '$timeout', 'common.dynamicLabelService', 'common.ArendeListViewStateService', 'common.AtticHelper',
            'common.UtkastValidationService',
        function($scope, $log, $timeout, dynamicLabelService, ArendeListViewState, AtticHelper, UtkastValidationService) {

            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.options.key + '.typ');

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key + '.typ');

            var chooseOption = {
                id: undefined,
                label: 'Välj tidsperiod'
            };

            $scope.prognosOptions = [];

            function updatePrognosOptions() {
                $scope.prognosOptions = [chooseOption];

                if ($scope.to.prognosDagarTillArbeteTyper) {
                    $scope.to.prognosDagarTillArbeteTyper.forEach(function (prognosDagarTillArbeteTyp) {
                        $scope.prognosOptions.push({
                            'id': prognosDagarTillArbeteTyp,
                            'label': dynamicLabelService.getProperty($scope.to.prognosDagarTillArbeteCode + '.' + prognosDagarTillArbeteTyp + '.RBK')
                        });
                    });
                }
            }

            $scope.hasKompletteringar = function() {
                return ArendeListViewState.hasKompletteringar($scope.options.key);
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
        }]
    });

});
