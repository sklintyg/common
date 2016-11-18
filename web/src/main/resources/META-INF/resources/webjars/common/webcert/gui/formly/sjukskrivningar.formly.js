angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'sjukskrivningar',
        templateUrl: '/web/webjars/common/webcert/gui/formly/sjukskrivningar.formly.html',
        controller: ['$scope', 'common.ArendeListViewStateService', 'common.SjukskrivningarViewStateService',
            function($scope, ArendeListViewState, viewstate) {
                $scope.getValidationsForPeriod = function(period) {
                    if (!$scope.formState.viewState.common.validationMessagesByField) {
                        return null;
                    }
                    var key = $scope.options.key + '.period.' + period;
                    return $scope.formState.viewState.common.validationMessagesByField[key.toLowerCase()];
                };
                $scope.hasKompletteringar = function() {
                    return ArendeListViewState.hasKompletteringar($scope.options.key);
                };

                $scope.viewstate = viewstate.reset();

                $scope.$on('intyg.loaded', function() {
                    viewstate.setModel($scope.model[$scope.options.key]);
                    viewstate.updatePeriods();
                });

                $scope.$watch('model.' + $scope.options.key, function(newValue, oldValue) {
                    viewstate.updatePeriods();
                }, true);

            }
        ]
    });

});
