/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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
module.exports = function(grunt) {
    'use strict';

    var npmDir = grunt.option('npmDir');
    if(!npmDir){
        npmDir = '../';
    }

    var cwd = process.cwd();
    process.chdir(npmDir);

    var sass = require('node-sass');

    var autoprefixer = require('autoprefixer')({
        browsers: [ 'last 2 versions' ]
    });

    require('time-grunt')(grunt);
    require('jit-grunt')(grunt, {
        bower: 'grunt-bower-task',
        configureProxies: 'grunt-connect-proxy',
        ngtemplates: 'grunt-angular-templates',
        postcss: 'grunt-postcss'
    });

    process.chdir(cwd);

    var SRC_DIR = 'src/main/resources/META-INF/resources/';
    var DEST_DIR = (grunt.option('outputDir') || 'build/') +  'resources/main/META-INF/resources/';
    var TEST_OUTPUT_DIR = (grunt.option('outputDir') || 'build/karma/');
    var RUN_COVERAGE = grunt.option('run-coverage') !== undefined ? grunt.option('run-coverage') : false;

    var minaintyg = grunt.file.expand({cwd:SRC_DIR}, ['webjars/common/app-shared/**/*.js', 'webjars/common/minaintyg/**/*.js', '!**/*.spec.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/common/minaintyg/module-deps.json',
                     JSON.stringify(minaintyg.map(function(file){ return '/web/'+file; }).concat('/web/webjars/common/minaintyg/templates.js'), null, 4));
    minaintyg = [SRC_DIR + 'webjars/common/minaintyg/module.js', DEST_DIR + 'webjars/common/minaintyg/templates.js'].concat(minaintyg.map(function(file){
        return SRC_DIR + file;
    }));

    var webcert = grunt.file.expand({cwd:SRC_DIR}, ['webjars/common/app-shared/**/*.js', 'webjars/common/webcert/**/*.js', '!**/*.spec.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/common/webcert/module-deps.json',
                     JSON.stringify(webcert.map(function(file){ return '/web/'+file; }).concat('/web/webjars/common/webcert/templates.js'), null, 4));
    webcert = [SRC_DIR + 'webjars/common/webcert/module.js', DEST_DIR + 'webjars/common/webcert/templates.js'].concat(webcert.map(function(file){
        return SRC_DIR + file;
    }));

    grunt.initConfig({
        config: {
            // configurable paths
            srcRoot: SRC_DIR + 'webjars/common/',
            destRoot: DEST_DIR + 'webjars/common/'
        },

        bower: {
            install: {
                options: {
                    copy: false
                }
            }
        },

        ngtemplates : {
            minaintyg: {
                cwd: SRC_DIR,
                src: ['webjars/common/app-shared/**/*.html', 'webjars/common/minaintyg/**/*.html'],
                dest: DEST_DIR + 'webjars/common/minaintyg/templates.js',
                options: {
                    module: 'common',
                    url: function(url) {
                        return '/web/' + url;
                    }
                }
            },
            webcert: {
                cwd: SRC_DIR,
                src: ['webjars/common/app-shared/**/*.html', 'webjars/common/webcert/**/*.html'],
                dest: DEST_DIR + 'webjars/common/webcert/templates.js',
                options: {
                    module: 'common',
                    url: function(url) {
                        return '/web/' + url;
                    }
                }
            }
        },

        concat: {
            minaintyg: {
                src: minaintyg,
                dest: DEST_DIR + 'webjars/common/minaintyg/module.min.js'
            },
            webcert: {
                src: webcert,
                dest: DEST_DIR + 'webjars/common/webcert/module.min.js'
            }
        },

        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/common/minaintyg/module.min.js',
                dest: DEST_DIR + 'webjars/common/minaintyg/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/common/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/common/webcert/module.min.js'
            }
        },

        uglify: {
            options: {
                mangle: false
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/common/minaintyg/module.min.js',
                dest: DEST_DIR + 'webjars/common/minaintyg/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/common/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/common/webcert/module.min.js'
            }
        },

        injector: {
            options: {
                lineEnding: grunt.util.linefeed,
                addRootSlash: false
            },

            // Inject component scss into mi-common.scss
            minaintyg: {
                options: {
                    transform: function(filePath) {
                        filePath = filePath.replace(SRC_DIR + 'webjars/common/minaintyg/', '');
                        filePath = filePath.replace(SRC_DIR + 'webjars/common/app-shared/', '../app-shared/');
                        return '@import \'' + filePath + '\';';
                    },
                    starttag: '// injector',
                    endtag: '// endinjector'
                },
                files: {
                    '<%= config.srcRoot %>minaintyg/mi-common.scss': [
                        '<%= config.srcRoot %>app-shared/**/*.scss',
                        '<%= config.srcRoot %>minaintyg/components/**/!(_variables).{scss,sass}'
                    ]
                }
            },

            // Inject component scss into wc-common.scss
            webcert: {
                options: {
                    transform: function(filePath) {
                        filePath = filePath.replace(SRC_DIR + 'webjars/common/webcert/', '');
                        filePath = filePath.replace(SRC_DIR + 'webjars/common/app-shared/', '../app-shared/');
                        return '@import \'' + filePath + '\';';
                    },
                    starttag: '// injector',
                    endtag: '// endinjector'
                },
                files: {
                    '<%= config.srcRoot %>webcert/wc-common.scss': [
                        '<%= config.srcRoot %>app-shared/**/*.scss',
                        '<%= config.srcRoot %>webcert/**/!(_variables).{scss,sass}',
                        '!<%= config.srcRoot %>webcert/**/mixins/*.{scss,sass}',
                        '!<%= config.srcRoot %>/webcert/wc-common.{scss,sass}'
                    ]
                }
            }
        },

        // Compiles Sass to CSS
        sass: {
            options: {
                implementation: sass,
                sourceMap: true
            },
            dist: {
                files: [ {
                    expand: true,
                    cwd: '<%= config.srcRoot %>css/',
                    src: [ '*.scss' ],
                    dest: '<%= config.destRoot %>css/',
                    ext: '.css'
                }, {
                    expand: true,
                    cwd: '<%= config.srcRoot %>minaintyg/',
                    src: [ '*.scss' ],
                    dest: '<%= config.destRoot %>minaintyg/',
                    ext: '.css'
                }, {
                    expand: true,
                    cwd: '<%= config.srcRoot %>webcert',
                    src: [ '*.scss' ],
                    dest: '<%= config.destRoot %>webcert/',
                    ext: '.css'
                } ]
            }
        },

        postcss: {
            options: {
                map: false,
                processors: [
                    autoprefixer, // add vendor prefixes
                    require('cssnano')({
                        zindex: false,
                        reduceIdents: false
                        //discardComments: { removeAllButFirst: true }
                    }) // minify the result
                ]
            },
            minaintyg: {
                src: '<%= config.destRoot %>/minaintyg/*.css'
            },
            webcert: {
                src: '<%= config.destRoot %>/webcert/*.css'
            }
        },

        wiredep: {
            options: {
                devDependencies: true
            },
            webcert: {
                src: ['karma-webcert.conf.js']
            },
            minaintyg: {
                src: ['karma-minaintyg.conf.js']
            }
        },

        karma: {
            minaintyg: {
                configFile: 'karma-minaintyg.conf.ci.js',
                coverageReporter: {
                    type : 'lcovonly',
                    dir : TEST_OUTPUT_DIR + 'minaintyg/',
                    subdir: '.'
                }
            },
            webcert: {
                configFile: 'karma-webcert.conf.ci.js',
                coverageReporter: {
                    type : 'lcovonly',
                    dir : TEST_OUTPUT_DIR + 'webcert/',
                    subdir: '.'
                }
            }
        },

        lcovMerge: {
            options: {
                outputFile: TEST_OUTPUT_DIR + 'merged_lcov.info'
            },
            src: [TEST_OUTPUT_DIR + 'webcert/*.info', TEST_OUTPUT_DIR + 'minaintyg/*.info']
        },

        sasslint: {
            options: {
                //configFile: 'config/.sass-lint.yml' //For now we use the .sass-lint.yml that is packaged with sass-lint
            },
            target: [SRC_DIR + '**/*.scss']
        },

        jshint: {
            options: {
                jshintrc: 'build/build-tools/jshint/jshintrc',
                reporterOutput: '',
                force: false,
                ignores: ['**/templates.js', '**/vendor/**']
            },
            minaintyg: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/common/app-shared/**/*.js', SRC_DIR + 'webjars/common/minaintyg/**/*.js' ]
            },
            webcert: {
                src: [ 'Gruntfile.js', SRC_DIR + '!webjars/common/webcert/**/vendor/*.js', SRC_DIR + 'webjars/common/app-shared/**/*.js', SRC_DIR + 'webjars/common/webcert/**/*.js' ]
            }
        }

    });

    //grunt.log.subhead('======================= Autoprefixer settings =====================');
    //grunt.log.ok(autoprefixer.info());
    grunt.registerTask('default', [ 'ngtemplates', 'concat', 'ngAnnotate', 'uglify', 'injector', 'sass', 'postcss' ]);
    grunt.registerTask('lint-minaintyg', [ 'jshint:minaintyg' ]);
    grunt.registerTask('lint-webcert', [ 'jshint:webcert' ]);
    grunt.registerTask('lint', [ 'jshint' ]);
    grunt.registerTask('test-minaintyg', [ 'bower:minaintyg', 'wiredep:minaintyg', 'karma:minaintyg' ]);
    grunt.registerTask('test-webcert', [ 'bower:webcert', 'wiredep:webcert', 'karma:webcert' ]);
    grunt.registerTask('test', [ 'bower', 'wiredep', 'karma' ].concat(RUN_COVERAGE?['lcovMerge']:[]));
};
