angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'singleDate',
        templateUrl: '/web/webjars/common/webcert/gui/formly/singleDate.formly.html',
        controller: ['$scope', '$timeout', 'common.UtkastValidationService',
            function($scope, $timeout, UtkastValidationService) {
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
