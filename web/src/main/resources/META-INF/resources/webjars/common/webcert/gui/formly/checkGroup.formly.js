angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'check-group',
        templateUrl: '/web/webjars/common/webcert/gui/formly/checkGroup.formly.html',
        controller: ['$scope', 'common.dynamicLabelService', function($scope, dynamicLabelService) {



            model.arbetslivsinriktadeAtgarder = [1,2,3];

            formState.arbetslivsinriktadeAtgarder[1] = true;


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
            }, true);

            $scope.$watch('formState.' + $scope.options.key, function(newVal, oldVal) {
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
            }, true);
        }]
    });

});
