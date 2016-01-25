angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'label-vardenhet',
        templateUrl: '/web/webjars/common/webcert/gui/formly/labelVardenhet.formly.html',
        controller: function($scope) {
            $scope.$on('intyg.loaded', function() {
                var doesntHaveVardenhet = !$scope.model.grundData || !$scope.model.grundData.skapadAv ||
                    !$scope.model.grundData.skapadAv.vardenhet;

                // check if all info is available from HSA. If not, display the info message that someone needs to update it
                $scope.hsaInfoMissing =
                    doesntHaveVardenhet || $scope.model.grundData.skapadAv.vardenhet.isMissingInfo();
            });
        }
    });

});
