angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'date',
        templateUrl: '/web/webjars/common/webcert/gui/formly/date.formly.html',
        controller: ['$scope', 'common.DateUtilsService', 'common.dynamicLabelService', function($scope, dateUtils, dynamicLabelService) {

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

            $scope.$watch('formState.' + $scope.options.key + '.checked', function(newVal, oldVal) {
                if (newVal) {
                    if (!$scope.model[$scope.options.key]) {
                        $scope.model[$scope.options.key] = dateUtils.todayAsYYYYMMDD();
                    }
                } else if (oldVal !== undefined) {
                    // Clear date if check is unchecked
                    $scope.model[$scope.options.key] = undefined;
                }
            });

            $scope.getDynamicText = function(key) {
                return dynamicLabelService.getProperty(key);
            };
        }]
    });

});
