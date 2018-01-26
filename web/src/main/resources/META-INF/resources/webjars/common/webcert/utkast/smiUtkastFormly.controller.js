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
angular.module('common').controller('smi.EditCert.FormlyCtrl',
    ['$scope', '$state', 'ViewState', 'FormFactory', 'common.TillaggsfragorHelper', 'common.ArendeListViewStateService',
     'common.fmbViewState',
        function FormlyCtrl($scope, $state, viewState, formFactory, tillaggsfragorHelper, ArendeListViewState, fmbViewState) {

            'use strict';

            $scope.viewState = viewState;

            $scope.model = viewState.intygModel;

            if($state.current.data.useFmb) {
                viewState.fmbViewState = fmbViewState.state;
            }

            // hasKompletteringar needs to be here since a formly wrapper (validationGroup.formly.js) currently can not have a controller
            $scope.options = {
                formState:{viewState:{common:viewState.common}}
            };

            $scope.formFields = formFactory.getFormFields();

            tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, $scope.formFields.length - 1);
            $scope.$on('intyg.loaded', function () {
                tillaggsfragorHelper.buildTillaggsFragor($scope.formFields, viewState.intygModel, $scope.formFields.length - 1);
            });
        }
    ]);
