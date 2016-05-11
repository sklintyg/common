angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'boolean',
        templateUrl: '/web/webjars/common/webcert/gui/formly/boolean.formly.html',
        extends: 'atticEnable'
    });

});
