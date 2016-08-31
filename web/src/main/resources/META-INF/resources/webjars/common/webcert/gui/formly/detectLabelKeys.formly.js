angular.module('common').run(['formlyConfig', 'common.dynamicLabelService', function(formlyConfig, dynamicLabelService) {
    'use strict';

    function _onLabelsUpdated(scope, options) {
        if (dynamicLabelService.hasProperty(scope.frageId + '.RBK')) {
            if (dynamicLabelService.hasProperty(options.templateOptions.label + '.RBK')) {
                scope.showFrageLabel = true;
            }
            else {
                options.templateOptions.label = scope.frageId;
                options.templateOptions.bold = true;
            }
        }
    }

    formlyConfig.templateManipulators.preWrapper.push(function(template, options, scope) {
        var runOnTypes = ['single-text', 'multi-text', 'boolean', 'checkbox-inline', 'radioGroup', 'checkGroup', 'date', 'singleDate'];
        if (runOnTypes.indexOf(options.type) >= 0) {
            if (options.templateOptions.label && options.templateOptions.label.substring(0, 4) === 'FRG_') {
                options.templateOptions.bold = true;
            }
            // Check labels on all delfraga with id x.1
            if (options.templateOptions.label && options.templateOptions.label.substring(0, 4) === 'DFR_') {
                var questionIds = options.templateOptions.label.substring(4).split('.');
                if (questionIds.length === 2 && questionIds[1] === '1') {
                    scope.showFrageLabel = false;
                    scope.frageId = 'FRG_' + questionIds[0];
                    scope.$on('dynamicLabels.updated', angular.bind(this, _onLabelsUpdated, scope, options));
                    return '<h4 ng-if="showFrageLabel">' +
                        '<span dynamic-label key="{{frageId}}.RBK"></span>' +
                        '<span wc-help-mark field-dynamic-help-text="{{frageId}}.HLP"></span>' +
                        '</h4>' + template;
                }
            }
        }
        return template;
    });

}]);