angular.module('common').directive('ueTextfield', [ 'common.ObjectHelper', 'common.AtticHelper', 'common.UtkastViewStateService', function(ObjectHelper, AtticHelper, UtkastViewState) {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            config: '=',
            model: '='
        },
        templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/components/ueTextField/ueTextField.directive.html',
        link: function($scope) {

            $scope.validation = UtkastViewState.validation;

            if(!$scope.config.indent) {
                $scope.config.indent = false;
            }

            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.config.modelProp);

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.config.modelProp);
        }
    };
}]);
