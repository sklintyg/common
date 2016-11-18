angular.module('common').run(['formlyConfig', 'common.dynamicLabelService', function(formlyConfig, dynamicLabelService) {
    'use strict';

    function _onLabelsUpdated(scope, options) {
        if (dynamicLabelService.hasProperty(scope.frageId + '.RBK')) {
            if (dynamicLabelService.hasProperty(options.templateOptions.label + '.RBK')) {
                scope.required = options.templateOptions.required;
                options.templateOptions.required = false;
                scope.showFrageLabel = true;
            } else {
                options.templateOptions.label = scope.frageId;
                options.templateOptions.bold = true;
            }
        }
    }

    formlyConfig.templateManipulators.preWrapper.push(function(template, options, scope) {
        var runOnTypes = ['single-text', 'multi-text', 'boolean', 'checkbox-inline', 'radioGroup', 'checkGroup', 'date', 'singleDate', 'sjukskrivningar'];
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
                        '<span dynamic-label key="{{frageId}}.RBK"></span> {{required ? "*" : ""}}' +
                        '<span wc-help-mark field-dynamic-help-text="{{frageId}}.HLP"></span>' +
                        '</h4>' + template;
                }
            }
        }
        return template;
    });

    // Add an id to every question. This id is used to scroll to the question from arendePanelFraga.directive
    formlyConfig.templateManipulators.preWrapper.push(function(template, options, scope) {
        var key = options.key;
        if (options.templateOptions.id) {
            key = options.templateOptions.id;
        }
        if (key && $.type(key) === 'string') {
            key = key.replace(/\.|\[|\]/g, '_');
            return '<div id="form_' + key + '">' + template + '</div>';
        } else {
            return template;
        }
    });

}]);