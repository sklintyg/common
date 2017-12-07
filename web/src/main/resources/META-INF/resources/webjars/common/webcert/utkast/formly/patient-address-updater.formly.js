angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'patient-address-updater',
        templateUrl: '/web/webjars/common/webcert/utkast/formly/patient-address-updater.formly.html'
    });
});
