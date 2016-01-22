angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setWrapper({
        name: 'wc-field',
        templateUrl: '/web/webjars/common/webcert/gui/formly/wcField.formly.html'
    });

});
