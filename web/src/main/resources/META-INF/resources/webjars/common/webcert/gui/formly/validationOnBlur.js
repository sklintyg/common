angular.module('common.formlyBaseTypes').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'validation-on-blur',
        controller: ['$scope', 'common.UtkastValidationService', function($scope, UtkastValidationService) {
            $scope.onBlur = function() {
                UtkastValidationService.validate($scope.model);
            };
        }],
        defaultOptions:  {
            templateOptions: {
                onBlur: 'onBlur()'
            }
        }
    });
});
