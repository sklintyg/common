angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'singleDate',
        templateUrl: '/web/webjars/common/webcert/gui/formly/singleDate.formly.html'
    });

});
