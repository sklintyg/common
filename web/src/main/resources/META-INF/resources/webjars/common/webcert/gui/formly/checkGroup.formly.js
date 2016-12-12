angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        'extends': 'validation-on-change',
        name: 'check-group',
        templateUrl: '/web/webjars/common/webcert/gui/formly/checkGroup.formly.html',
        controller: ['$scope', 'common.AtticHelper', function($scope, AtticHelper) {

            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.options.key);

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key);
        }]
    });

});
