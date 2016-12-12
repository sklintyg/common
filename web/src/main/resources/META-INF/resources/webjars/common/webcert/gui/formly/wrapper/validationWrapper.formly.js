angular.module('common').run(['formlyConfig', function(formlyConfig) {
    'use strict';

    formlyConfig.setWrapper({
        templateUrl: '/web/webjars/common/webcert/gui/formly/wrapper/validationWrapper.formly.html',
        types: ['boolean', 'check-group', 'checkbox-inline', 'check-multi-text', 'date', 'multi-text', 'radio-group', 'singleDate', 'single-text', 'single-text-vertical']
    });

}]);