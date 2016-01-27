angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setWrapper({
        name: 'wc-field-static',
        templateUrl: '/web/webjars/common/webcert/gui/formly/wcFieldStatic.formly.html'
    });

});
