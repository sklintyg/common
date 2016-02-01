angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'single-text',
        templateUrl: '/web/webjars/common/webcert/gui/formly/singleText.formly.html',
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
