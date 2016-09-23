angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'label-vardenhet',
        templateUrl: '/web/webjars/common/webcert/gui/formly/labelVardenhet.formly.html',
        controller: ['$scope', 'common.UtkastViewStateService', function($scope, UtkastViewStateService) {
            $scope.commonViewState = UtkastViewStateService;
        }]
    });

});
