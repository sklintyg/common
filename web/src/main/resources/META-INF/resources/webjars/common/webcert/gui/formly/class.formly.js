angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.templateManipulators.preWrapper.push(function(template, options, scope) {
        if (options.key && $.type(options.key) === 'string') {
            var key = options.key.replace('.','_').replace('[','_').replace(']','_');
            return '<div id="form_' + key + '">' + template + '</div>';
        } else {
            return template;
        }
    });

});