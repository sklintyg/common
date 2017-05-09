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

    require('time-grunt')(grunt);
    require('jit-grunt')(grunt, {
        bower: 'grunt-bower-task',
        configureProxies: 'grunt-connect-proxy',
        ngtemplates: 'grunt-angular-templates',
        postcss: 'grunt-postcss'
    });

    var SRC_DIR = 'src/main/resources/META-INF/resources/';
    var DEST_DIR = (grunt.option('outputDir') || 'build/') +  'resources/main/META-INF/resources/';
    var TEST_OUTPUT_DIR = (grunt.option('outputDir') || 'build/karma/');
    var RUN_COVERAGE = grunt.option('run-coverage') !== undefined ? grunt.option('run-coverage') : false;
        
    var minaintyg = grunt.file.expand({cwd:SRC_DIR}, ['webjars/common/minaintyg/**/*.js', '!**/*.spec.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/common/minaintyg/module-deps.json',
                     JSON.stringify(minaintyg.map(function(file){ return '/web/'+file; }).concat('/web/webjars/common/minaintyg/templates.js'), null, 4));
    minaintyg = [SRC_DIR + 'webjars/common/minaintyg/module.js', DEST_DIR + 'webjars/common/minaintyg/templates.js'].concat(minaintyg.map(function(file){
        return SRC_DIR + file;
    }));

    var webcert = grunt.file.expand({cwd:SRC_DIR}, ['webjars/common/webcert/**/*.js', '!**/*.spec.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/common/webcert/module-deps.json',
                     JSON.stringify(webcert.map(function(file){ return '/web/'+file; }).concat('/web/webjars/common/webcert/templates.js'), null, 4));
    webcert = [SRC_DIR + 'webjars/common/webcert/module.js', DEST_DIR + 'webjars/common/webcert/templates.js'].concat(webcert.map(function(file){
        return SRC_DIR + file;
    }));

    grunt.initConfig({
        config: {
            // configurable paths
            src_root: SRC_DIR + 'webjars/common/',
            dest_root: DEST_DIR + 'webjars/common/'
        },
        bower: {
            install: {
                options: {
                    copy: false
                }
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

        sasslint: {
            options: {
                //configFile: 'config/.sass-lint.yml' //For now we use the .sass-lint.yml that is packaged with sass-lint
            },
            target: [SRC_DIR + '**/*.scss']
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

        jshint: {
            options: {
                jshintrc: 'build/build-tools/jshint/jshintrc',
                reporterOutput: '',
                force: false,
                ignores: ['**/templates.js', '**/vendor/**']
            },
            minaintyg: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/common/minaintyg/**/*.js' ]
            },
            webcert: {
                src: [ 'Gruntfile.js', SRC_DIR + '!webjars/common/webcert/**/vendor/*.js', SRC_DIR + 'webjars/common/webcert/**/*.js' ]
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

        // Compiles Sass to CSS
        sass: {
            options: {
            },
            dist: {
                files: [ {
                    expand: true,
                    cwd: '<%= config.src_root %>css/',
                    src: [ '*.scss' ],
                    dest: '<%= config.dest_root %>css/',
                    ext: '.css'
                }, {
                    expand: true,
                    cwd: '<%= config.src_root %>minaintyg/',
                    src: [ '*.scss' ],
                    dest: '<%= config.dest_root %>minaintyg/',
                    ext: '.css'
                }, {
                    expand: true,
                    cwd: '<%= config.src_root %>webcert/css/',
                    src: [ '*.scss' ],
                    dest: '<%= config.dest_root %>webcert/css/',
                    ext: '.css'
                } ]
            }
        },

        postcss: {
            options: {
                map: false,
                processors: [
                    require('autoprefixer')({browsers: ['last 2 versions', 'ie 9']}), // add vendor prefixes
                    require('cssnano')() // minify the result
                ]
            },
            dist: {
                src: '<%= config.dest_root %>/minaintyg/*.css'
            }
        },

        injector: {
            options: {
                lineEnding: grunt.util.linefeed,
                addRootSlash: false
            },

            // Inject component scss into app.scss
            sass: {
                options: {
                    transform: function(filePath) {
                        filePath = filePath.replace(SRC_DIR + 'webjars/common/minaintyg/', '');
                        return '@import \'' + filePath + '\';';
                    },
                    starttag: '// injector',
                    endtag: '// endinjector'
                },
                files: {
                        '<%= config.src_root %>minaintyg/mi-common.scss': [
                        '<%= config.src_root %>minaintyg/components/**/!(_variables).{scss,sass}'
                    ]
                }
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

        ngtemplates : {
            minaintyg: {
                cwd: SRC_DIR,
                src: ['webjars/common/minaintyg/**/*.html'],
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
                src: ['webjars/common/webcert/**/*.html'],
                dest: DEST_DIR + 'webjars/common/webcert/templates.js',
                options: {
                    module: 'common',
                    url: function(url) {
                        return '/web/' + url;
                    }
                }
            }
        },

        lcovMerge: {
            options: {
                outputFile: TEST_OUTPUT_DIR + 'merged_lcov.info'
            },
            src: [TEST_OUTPUT_DIR + 'webcert/*.info', TEST_OUTPUT_DIR + 'minaintyg/*.info']
        }
    });

    grunt.registerTask('default', [ 'bower', 'ngtemplates', 'concat', 'ngAnnotate', 'uglify', 'injector:sass', 'sass', 'postcss' ]);
    grunt.registerTask('lint-minaintyg', [ 'jshint:minaintyg' ]);
    grunt.registerTask('lint-webcert', [ 'jshint:webcert' ]);
    grunt.registerTask('lint', [ 'jshint' ]);
    grunt.registerTask('test-minaintyg', [ 'wiredep:minaintyg', 'karma:minaintyg' ]);
    grunt.registerTask('test-webcert', [ 'wiredep:webcert', 'karma:webcert' ]);
    grunt.registerTask('test', [ 'wiredep', 'karma' ].concat(RUN_COVERAGE?['lcovMerge']:[]));
};
