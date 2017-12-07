angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'date',
        templateUrl: '/web/webjars/common/webcert/utkast/formly/date.formly.html',
        controller: ['$scope', '$timeout', 'common.DateUtilsService', 'common.dynamicLabelService', 'common.AtticHelper', 'common.UtkastValidationService',
            function($scope, $timeout, dateUtils, dynamicLabelService, AtticHelper, UtkastValidationService) {

            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.options.key);

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key);

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
                        $scope.validate();
                    }
                } else if (oldVal !== undefined) {
                    // Clear date if check is unchecked
                    if ($scope.model[$scope.options.key] !== undefined) {
                        $scope.model[$scope.options.key] = undefined;
                        $scope.validate();
                    }
                }
            });

            $scope.getDynamicText = function(key) {
                return dynamicLabelService.getProperty(key);
            };

            $scope.validate = function() {
                // When a date is selected from the date popup a blur event is sent.
                // In the current version of Angular UI this blur event is sent before utkast model is updated
                // This timeout ensures we get the new value in $scope.model
                $timeout(function() {
                    UtkastValidationService.validate($scope.model);
                });
            };
        }]
    });

});
