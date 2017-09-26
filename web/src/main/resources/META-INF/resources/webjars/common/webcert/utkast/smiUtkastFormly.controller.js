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
