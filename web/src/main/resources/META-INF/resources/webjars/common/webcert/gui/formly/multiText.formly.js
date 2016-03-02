angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'multi-text',
        templateUrl: '/web/webjars/common/webcert/gui/formly/multiText.formly.html'
    });
});
