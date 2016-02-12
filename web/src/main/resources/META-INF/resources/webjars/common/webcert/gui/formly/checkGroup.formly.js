angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'check-group',
        templateUrl: '/web/webjars/common/webcert/gui/formly/checkGroup.formly.html',
        controller: ['$scope', '$log', function($scope, $log) {

            $scope.$watch('model.' + $scope.options.key, function(newVal, oldVal) {

            }, true);
        }]
    });

});
