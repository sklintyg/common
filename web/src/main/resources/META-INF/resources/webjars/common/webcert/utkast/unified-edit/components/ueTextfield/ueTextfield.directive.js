angular.module('common').directive('ueTextfield', [ 'common.ObjectHelper', 'common.AtticHelper', 'common.UtkastViewStateService', 'ueUtil',
    function(ObjectHelper, AtticHelper, UtkastViewState, ueUtil) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            form: '=',
            config: '=',
            model: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueTextfield/ueTextfield.directive.html',
        link: function($scope) {

            $scope.validation = UtkastViewState.validation;

            if(!$scope.config.indent) {
                $scope.config.indent = false;
            }

            if(!$scope.config.formType) {
                $scope.config.formType = 'inline';
            }

            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);

            $scope.onBlur = angular.bind(this, ueUtil.updateValidation, $scope.form, $scope.model);
        }
    };
}]);
