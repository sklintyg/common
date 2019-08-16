module.exports = function(grunt) {
  'use strict';

  var npmDir = grunt.option('npmDir');
  var cwd = process.cwd();
  process.chdir(npmDir);

  require('time-grunt')(grunt);
  require('jit-grunt')(grunt, {
    bower: 'grunt-bower-task',
    ngtemplates: 'grunt-angular-templates'
  });

  process.chdir(cwd);

  var sass = require('node-sass');

  var SRC_DIR = 'src/main/resources/META-INF/resources/';
  var TEST_DIR = 'src/test/js/';
  var DEST_DIR = (grunt.option('outputDir') || 'build/') + 'resources/main/META-INF/resources/';
  var TEST_OUTPUT_DIR = (grunt.option('outputDir') || 'build/karma/');
  var RUN_COVERAGE = grunt.option('run-coverage') !== undefined ? grunt.option('run-coverage') : false;
  var MODULE = grunt.option('intygModule');

  var minaintyg = grunt.file.expand(
      {cwd: SRC_DIR},
      ['webjars/' + MODULE + '/app-shared/**/*.js', 'webjars/' + MODULE + '/minaintyg/**/*.js', '!**/*.spec.js', '!**/*.test.js',
        '!**/module.js']).sort();

  // MI Build module-deps.json. URL paths
  grunt.file.write(
      DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module-deps.json',
      JSON.stringify(minaintyg.map(function(file) {
        return '/web/' + file;
      }).concat('/web/webjars/' + MODULE + '/minaintyg/templates.js'), null, 4));

  // MI List of script files
  minaintyg =
      [SRC_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.js', DEST_DIR + 'webjars/' + MODULE + '/minaintyg/templates.js'].concat(
          minaintyg.map(function(file) {
            return SRC_DIR + file;
          }));

  // WC Build module-deps.json. URL paths
  var webcert = grunt.file.expand({cwd: SRC_DIR},
      ['webjars/' + MODULE + '/app-shared/**/*.js', 'webjars/' + MODULE + '/webcert/**/*.js', '!**/*.spec.js', '!**/*.test.js',
        '!**/module.js']).sort();
  grunt.file.write(DEST_DIR + 'webjars/' + MODULE + '/webcert/module-deps.json',
      JSON.stringify(webcert.map(function(file) {
        return '/web/' + file;
      }).concat('/web/webjars/' + MODULE + '/webcert/templates.js'), null, 4));

  // WC List of script files
  webcert = [SRC_DIR + 'webjars/' + MODULE + '/webcert/module.js', DEST_DIR + 'webjars/' + MODULE + '/webcert/templates.js'].concat(
      webcert.map(function(file) {
        return SRC_DIR + file;
      }));

  grunt.initConfig({

    ngtemplates: {
      webcert: {
        cwd: 'src/main/resources/META-INF/resources/webjars/' + MODULE + '/webcert',
        src: ['**/*.html'],
        dest: 'build/resources/main/META-INF/resources/webjars/' + MODULE + '/webcert/templates.js',
        options: {
          module: MODULE,
          url: function(url) {
            return '/web/webjars/' + MODULE + '/webcert/' + url;
          }
        }
      },
      minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/' + MODULE + '/minaintyg',
        src: ['**/*.html'],
        dest: 'build/resources/main/META-INF/resources/webjars/' + MODULE + '/minaintyg/templates.js',
        options: {
          module: MODULE,
          url: function(url) {
            return '/web/webjars/' + MODULE + '/minaintyg/' + url;
          }
        }
      }
    },

    concat: {
      minaintyg: {
        src: minaintyg,
        dest: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.min.js'
      },
      webcert: {
        src: webcert,
        dest: DEST_DIR + 'webjars/' + MODULE + '/webcert/module.min.js'
      }
    },

    ngAnnotate: {
      options: {
        singleQuotes: true
      },
      minaintyg: {
        src: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.min.js',
        dest: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.min.js'
      },
      webcert: {
        src: DEST_DIR + 'webjars/' + MODULE + '/webcert/module.min.js',
        dest: DEST_DIR + 'webjars/' + MODULE + '/webcert/module.min.js'
      }
    },

    uglify: {
      options: {
        mangle: false
      },
      minaintyg: {
        src: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.min.js',
        dest: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.min.js'
      },
      webcert: {
        src: DEST_DIR + 'webjars/' + MODULE + '/webcert/module.min.js',
        dest: DEST_DIR + 'webjars/' + MODULE + '/webcert/module.min.js'
      }
    },

    // Compiles Sass to CSS
    sass: {
      options: {
        implementation: sass
      },
      dist: {
        files: [
          {
            expand: true,
            cwd: SRC_DIR + 'webjars/' + MODULE + '/minaintyg/css/',
            src: ['*.scss'],
            dest: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/css',
            ext: '.css'
          },
          {
            expand: true,
            cwd: SRC_DIR + 'webjars/' + MODULE + '/webcert/css/',
            src: ['*.scss'],
            dest: DEST_DIR + 'webjars/' + MODULE + '/webcert/css',
            ext: '.css'
          }
        ]
      }
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

    karma: {
      minaintyg: {
        configFile: 'karma-minaintyg.conf.ci.js',
        coverageReporter: {
          type: 'lcovonly',
          dir: TEST_OUTPUT_DIR + 'minaintyg/',
          subdir: '.'
        }
      },
      webcert: {
        configFile: 'karma-webcert.conf.ci.js',
        coverageReporter: {
          type: 'lcovonly',
          dir: TEST_OUTPUT_DIR + 'webcert/',
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
        jshintrc: npmDir + '/build/build-tools/jshint/jshintrc',
        reporterOutput: '',
        force: false
      },
      minaintyg: {
        src: ['Gruntfile.js', SRC_DIR + 'webjars/' + MODULE + '/minaintyg/**/*.js', SRC_DIR + 'webjars/' + MODULE + '/app-shared/**/*.js',
          TEST_DIR + 'minaintyg/**/*.js']
      },
      webcert: {
        src: ['Gruntfile.js', SRC_DIR + 'webjars/' + MODULE + '/webcert/**/*.js', SRC_DIR + 'webjars/' + MODULE + '/app-shared/**/*.js',
          TEST_DIR + 'webcert/**/*.js']
      }
    }

  });

  grunt.registerTask('default', ['ngtemplates', 'concat', 'ngAnnotate', 'uglify', 'sass']);
  grunt.registerTask('test-minaintyg', ['bower:minaintyg', 'wiredep:minaintyg', 'karma:minaintyg']);
  grunt.registerTask('test-webcert', ['bower:webcert', 'wiredep:webcert', 'karma:webcert']);
  grunt.registerTask('test', ['bower', 'wiredep', 'karma'].concat(RUN_COVERAGE ? ['lcovMerge'] : []));
  grunt.registerTask('lint-minaintyg', ['jshint:minaintyg']);
  grunt.registerTask('lint-webcert', ['jshint:webcert']);
  grunt.registerTask('lint', ['jshint']);
};
