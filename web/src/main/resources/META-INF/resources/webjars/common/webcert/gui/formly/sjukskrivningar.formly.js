angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'sjukskrivningar',
        templateUrl: '/web/webjars/common/webcert/gui/formly/sjukskrivningar.formly.html',
        controller: ['$scope',
            function($scope) {

                $scope.getValidationsForPeriod = function(period) {
                    if (!$scope.formState.viewState.common.validationMessagesByField) {
                        return null;
                    }
                    var key = $scope.options.key + '.period.' + period;
                    return $scope.formState.viewState.common.validationMessagesByField[key.toLowerCase()];
                }

            }
        ]
    });

});
