angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'multi-text',
        templateUrl: '/web/webjars/common/webcert/gui/formly/multiText.formly.html',
        extends: 'atticEnable',
        controller: ['$scope', function($scope) {
            if(!$scope.to.labelColSize) {
                $scope.to.labelColSize = 5;
            }
            if(!$scope.to.indent) {
                $scope.to.indent = false;
            }
        }]
    });
});
