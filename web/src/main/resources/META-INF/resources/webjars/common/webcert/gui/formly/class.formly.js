angular.module('common').run(function(formlyConfig) {
    'use strict';

    formlyConfig.templateManipulators.preWrapper.push(function(template, options, scope) {
        if (options.key && $.type(options.key) === 'string') {
            var key = options.key;
            var index = options.key.lastIndexOf('.');
            if (index >= 0) {
                key = options.key.substring(index + 1);
            }
            return '<div class="form-' + key + '">' + template + '</div>';
        } else {
            return template;
        }
    });

});