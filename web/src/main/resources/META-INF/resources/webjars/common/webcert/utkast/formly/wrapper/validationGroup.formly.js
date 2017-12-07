angular.module('common').run(['formlyConfig', function(formlyConfig) {
    'use strict';

    formlyConfig.setWrapper({
        name: 'validationGroup',
        templateUrl: '/web/webjars/common/webcert/utkast/formly/wrapper/validationGroup.formly.html'
    });

}]);