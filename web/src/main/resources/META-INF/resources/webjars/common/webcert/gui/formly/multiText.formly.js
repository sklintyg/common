angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'multi-text',
        templateUrl: '/web/webjars/common/webcert/gui/formly/multiText.formly.html',
        controller: ['$scope', 'common.ObjectHelper', 'common.AtticHelper', function($scope, ObjectHelper, AtticHelper) {
            if(!$scope.to.labelColSize) {
                $scope.to.labelColSize = 5;
            }
            if(!$scope.to.indent) {
                $scope.to.indent = false;
            }

            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.options.key);

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key);
        }]
    });
});
