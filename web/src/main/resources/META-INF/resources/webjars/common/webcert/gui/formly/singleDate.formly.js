angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'singleDate',
        defaultOptions: {
            className: 'fold-animation'
        },
        templateUrl: '/web/webjars/common/webcert/gui/formly/singleDate.formly.html',
        controller: ['$scope', '$timeout', 'common.UtkastValidationService', 'common.AtticHelper',
            function($scope, $timeout, UtkastValidationService, AtticHelper) {
                $scope.validate = function() {
                    // When a date is selected from the date popup a blur event is sent.
                    // In the current version of Angular UI this blur event is sent before utkast model is updated
                    // This timeout ensures we get the new value in $scope.model
                    $timeout(function() {
                        UtkastValidationService.validate($scope.model);
                    });
                };

                // Restore data model value form attic if exists
                AtticHelper.restoreFromAttic($scope.model, $scope.options.key);

                // Clear attic model and destroy watch on scope destroy
                AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key);

            }]
    });

});
