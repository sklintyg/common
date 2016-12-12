angular.module('common.formlyBaseTypes').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'validation-on-change',
        controller: ['$scope', 'common.UtkastValidationService', function($scope, UtkastValidationService) {
            $scope.onChange = function() {
                UtkastValidationService.validate($scope.model);
            };
        }],
        defaultOptions:  {
            templateOptions: {
                onChange: 'onChange()'
            }
        }
    });
});
