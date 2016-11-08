angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'prognos',
        templateUrl: '/web/webjars/common/webcert/gui/formly/prognos.formly.html',
        controller: ['$scope', 'common.dynamicLabelService', 'common.ArendeListViewStateService',
        function($scope, dynamicLabelService, ArendeListViewState) {

            var chooseOption = {
                id: undefined,
                label: 'Välj...'
            };

            $scope.prognosOptions = [];

            function updatePrognosOptions() {
                $scope.prognosOptions = [chooseOption];

                if ($scope.to.prognosDagarTillArbeteTyper) {
                    $scope.to.prognosDagarTillArbeteTyper.forEach(function (prognosDagarTillArbeteTyp) {
                        $scope.prognosOptions.push({
                            'id': prognosDagarTillArbeteTyp,
                            'label': dynamicLabelService.getProperty($scope.to.prognosDagarTillArbeteCode + '.' + prognosDagarTillArbeteTyp + '.RBK')
                        });
                    });
                }
            }

            $scope.hasKompletteringar = function() {
                return ArendeListViewState.hasKompletteringar($scope.options.key);
            };

            $scope.$on('dynamicLabels.updated', function() {
                updatePrognosOptions();
            });

            updatePrognosOptions();
        }]
    });

});
