/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
/* global printStackTrace */
angular.module('common').factory('common.stacktraceService', function() {
  'use strict';
  return ({
    print: printStackTrace
  });
});

// The error log service is our wrapper around the core error
// handling ability of AngularJS. Notice that we pass off to
// the native "$log" method and then handle our additional
// server-side logging.
angular.module('common').factory('common.errorLogService', ['$log', '$window', 'common.stacktraceService',
  function($log, $window, stacktraceService) {

    'use strict';

    function log(exception, cause) {

      // Pass off the error to the default error handler
      // on the AngualrJS logger. This will output the
      // error to the console (and let the application
      // keep running normally for the user).
      $log.error.apply($log, arguments);

      try {
        var errorMessage = exception.toString();
        var stackTrace = stacktraceService.print({e: exception});

        // Log the JavaScript error to the server.
        $.ajax({
          type: 'POST',
          url: '/api/jslog/debug',
          contentType: 'application/json',
          data: angular.toJson({
            errorUrl: $window.location.href,
            errorMessage: errorMessage,
            stackTrace: stackTrace,
            cause: (cause || '')
          })
        });
      } catch (loggingError) {
        $log.warn('Error logging failed');
        $log.log(loggingError);
      }
    }

    return log;
  }
]);
