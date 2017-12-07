angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        'extends': 'validation-on-change',
        name: 'radio-group',
        templateUrl: '/web/webjars/common/webcert/utkast/formly/radioGroup.formly.html',
        controller: ['$scope', 'common.AtticHelper', function($scope, AtticHelper) {
            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.options.key);

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key);
        }]
    });

});
