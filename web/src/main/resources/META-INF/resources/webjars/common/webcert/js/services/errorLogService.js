/* global printStackTrace */
angular.module('common').factory('common.stacktraceService', function() {
    'use strict';
    return({
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
                var stackTrace = stacktraceService.print({ e: exception });

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