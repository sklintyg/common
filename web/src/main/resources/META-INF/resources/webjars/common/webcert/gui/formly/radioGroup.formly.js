angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'radio-group',
        templateUrl: '/web/webjars/common/webcert/gui/formly/radioGroup.formly.html',
        controller: ['$scope', 'common.dynamicLabelService', function($scope, dynamicLabelService) {

            $scope.$watch('model.' + $scope.options.key, function(newVal, oldVal) {
                var formState = $scope.formState;
                if (!formState[$scope.options.key]) {
                    formState[$scope.options.key] = {};
                }
                if (newVal) {
                    formState[$scope.options.key].checked = true;
                } else {
                    formState[$scope.options.key].checked = false;
                }
            });

            $scope.getDynamicText = function(key) {
                return dynamicLabelService.getProperty(key);
            };
        }]
    });

});
