angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.setType({
        name: 'sjukskrivningar',
        templateUrl: '/web/webjars/common/webcert/gui/formly/sjukskrivningar.formly.html',
        controller: ['$scope', 'common.DateUtilsService', 'common.dynamicLabelService',
            function($scope, dateUtils, dynamicLabelService) {

            }
        ]
    });

});
