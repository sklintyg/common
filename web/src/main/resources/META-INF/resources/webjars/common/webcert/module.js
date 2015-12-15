/* global commonMessages */
angular.module('common', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize' ]);

// Inject language resources
angular.module('common').run([ 'common.messageService', 'common.dynamicLabelService',
    function(messageService, dynamicLabelService) {
        'use strict';

        messageService.addResources(commonMessages);
        dynamicLabelService.addLabels(sjukersattningDynamicLabelsMock);
    }]);
