angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        'extends': 'validation-on-change',
        name: 'radio-group',
        templateUrl: '/web/webjars/common/webcert/gui/formly/radioGroup.formly.html',
        controller: ['$scope', function($scope) {
        }]
    });

});
