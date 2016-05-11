angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'boolean',
        templateUrl: '/web/webjars/common/webcert/gui/formly/boolean.formly.html',
        controller: ['$scope', 'common.AtticHelper', function($scope, AtticHelper) {
            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.options.key);

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key);
        }]
    });

});
