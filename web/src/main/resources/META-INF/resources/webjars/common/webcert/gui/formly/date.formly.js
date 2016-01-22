angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'date',
        templateUrl: '/web/webjars/common/webcert/gui/formly/date.formly.html',
        controller: ['$scope', 'common.DateUtilsService', function($scope, dateUtils) {

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
                    console.log($scope.model[$scope.options.key]);
                    $scope.model[$scope.options.key] = undefined;
                }
                console.log(newVal, oldVal);
            });
        }]
    });

});
