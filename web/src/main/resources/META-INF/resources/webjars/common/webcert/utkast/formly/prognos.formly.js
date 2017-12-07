angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'prognos',
        templateUrl: '/web/webjars/common/webcert/utkast/formly/prognos.formly.html',
        controller: ['$scope', '$log', '$timeout', 'common.dynamicLabelService', 'common.ArendeListViewStateService', 'common.AtticHelper',
            'common.UtkastValidationService',
        function($scope, $log, $timeout, dynamicLabelService, ArendeListViewState, AtticHelper, UtkastValidationService) {

            // Restore data model value form attic if exists
            AtticHelper.restoreFromAttic($scope.model, $scope.options.key + '.typ');

            // Clear attic model and destroy watch on scope destroy
            AtticHelper.updateToAttic($scope, $scope.model, $scope.options.key + '.typ');

            var chooseOption = {
                id: undefined,
                label: 'Välj tidsperiod'
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

            $scope.validate = function() {
                $timeout(function(){
                    $log.debug('validating');
                    UtkastValidationService.validate($scope.model);
                });
            };

            updatePrognosOptions();
        }]
    });

});
