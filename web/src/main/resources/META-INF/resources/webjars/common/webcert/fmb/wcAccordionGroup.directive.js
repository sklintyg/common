/**
 * Do an override of the templateUrl of the original accordionGroupDirective
 * so that we can have custom look for our fmb display
 */
angular.module('common').directive('wcAccordionGroup',
    function (uibAccordionGroupDirective) {
        'use strict';

        return angular.extend({}, uibAccordionGroupDirective[0], {templateUrl: '/web/webjars/common/webcert/fmb/wcAccordionGroup.template.html'});
    });

