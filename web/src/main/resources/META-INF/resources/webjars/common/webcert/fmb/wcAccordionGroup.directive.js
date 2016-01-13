/**
 * Do an override of the templateUrl of the original accordionGroupDirective
 * so that we can have custom look for our fmb display
 */
angular.module('common').directive('wcAccordionGroup',
    function (accordionGroupDirective) {
        'use strict';

        return angular.extend({}, accordionGroupDirective[0], {templateUrl: '/web/webjars/common/webcert/fmb/wcAccordionGroup.template.html'});
    });

