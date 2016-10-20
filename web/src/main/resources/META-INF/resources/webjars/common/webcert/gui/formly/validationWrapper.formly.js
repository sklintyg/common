angular.module('common').run(['formlyConfig', function(formlyConfig) {
    'use strict';

    formlyConfig.setWrapper({
        templateUrl: '/web/webjars/common/webcert/gui/formly/validationWrapper.formly.html',
        types: ['boolean', 'check-group', 'date', 'multi-text', 'radio-group', 'singleDate', 'single-text', 'single-text-vertical']
    });

}]);