angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'single-text',
        templateUrl: '/web/webjars/common/webcert/gui/formly/signleText.formly.html'
    });

});
