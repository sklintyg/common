angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'check-group-description',
        templateUrl: '/web/webjars/common/webcert/gui/formly/checkGroupDescription.formly.html',
        controller: ['$scope', '$log', function($scope) {
            $scope.getDescriptionValidationsForChoice = function(choice) {
                if (!$scope.formState.viewState.common.validationMessagesByField) {
                    return null;
                }
                var key = $scope.options.key + '.' + choice.id + '.beskrivning';
                return $scope.formState.viewState.common.validationMessagesByField[key.toLowerCase()];
            };
        }]
    });

});
