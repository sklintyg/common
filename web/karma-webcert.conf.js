/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* global module */
module.exports = function(config) {
    'use strict';

    var SRC_DIR = 'src/main/resources/META-INF/resources/webjars/common/webcert/';
    var NODE_DIR = '../node_modules/';
    var SRC_SHARED_DIR = 'src/main/resources/META-INF/resources/webjars/common/app-shared/';
    //var TEMPLATE_PATH = ;

    config.set({

        // base path, that will be used to resolve files and exclude
        basePath: '',

        // frameworks to use
        frameworks: [ 'jasmine' ],

        // generate js files from html templates to expose them during testing.
        preprocessors: {
            'src/main/resources/META-INF/resources/webjars/common/app-shared/**/*.html': ['ng-html2js'],
            'src/main/resources/META-INF/resources/webjars/common/webcert/**/*.html': ['ng-html2js']
        },

        ngHtml2JsPreprocessor: {
            // If your build process changes the path to your templates,
            // use stripPrefix and prependPrefix to adjust it.
            stripPrefix: 'src/main/resources/META-INF/resources/',
            prependPrefix: '/web/',

            // the name of the Angular module to create
            moduleName: 'htmlTemplates'
        },

        // list of files / patterns to load in the browser
        files: [

            // Dependencies
            // bower:js
            'bower_components/jquery/dist/jquery.js',
            'bower_components/angular/angular.js',
            'bower_components/angular-animate/angular-animate.js',
            'bower_components/angular-cookies/angular-cookies.js',
            'bower_components/angular-mocks/angular-mocks.js',
            'bower_components/angular-i18n/angular-locale_sv-se.js',
            'bower_components/angular-sanitize/angular-sanitize.js',
            'bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
            'bower_components/angular-ui-router/release/angular-ui-router.js',
            'bower_components/bootstrap/dist/js/bootstrap.js',
            'bower_components/momentjs/moment.js',
            // endbower
            NODE_DIR + '/karma-read-json/karma-read-json.js',
            NODE_DIR + 'tv4/tv4.js',

            // Load these first
            SRC_DIR + 'module.js',
            SRC_DIR + 'messages.js',

            { pattern: SRC_DIR + '**/*.js' },
            { pattern: SRC_DIR + '**/*.html' },
            { pattern: SRC_DIR + '**/*.spec.js' },

            { pattern: SRC_SHARED_DIR + '**/*.js' },
            { pattern: SRC_SHARED_DIR + '**/*.html' },
            { pattern: SRC_SHARED_DIR + '**/*.spec.js' },

            // Test resources
            { pattern: 'test/resources/jsonschema/*', included: false }
        ],

        // web server port
        port: 9876,

        // enable / disable colors in the output (reporters and logs)
        colors: true,

        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_DEBUG,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera (has to be installed with `npm install karma-opera-launcher`)
        // - Safari (only Mac; has to be installed with `npm install karma-safari-launcher`)
        // - PhantomJS
        // - IE (only Windows; has to be installed with `npm install karma-ie-launcher`)
        browsers: [ 'ChromeHeadless' ],

        // If browser does not capture in given timeout [ms], kill it
        captureTimeout: 60000,

        // Continuous Integration mode if true, it capture browsers, run tests and exit
        singleRun: false,

        plugins: [
            'karma-jasmine',
            'karma-chrome-launcher',
            'karma-mocha-reporter',
            'karma-ng-html2js-preprocessor',
            'karma-read-json'
        ],

        reporters: [ 'progress' ]
    });
};
