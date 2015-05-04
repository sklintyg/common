angular.module('common').directive('wcAutoSave', function($timeout) {
    'use strict';

    return {
        require: ['^form'],
        link: function($scope, $element, $attrs, $ctrls) {

            var SAVE_DELAY = 1 * 1000; // Time to wait before autosaving after a user action.
            var MIN_SAVE_DELTA = 5 * 1000; // Minimum time to wait between two autosaves.

            var form = $ctrls[0];
            var savePromise = null;
            var lastSave = null;
            var expression = $attrs.wcAutoSave || 'true';

            var save = function() {
                return $scope.$eval(expression);
            }

            var saveFunction = function() {
                savePromise = null;
                var result = save()
                if (result) {
                    lastSave = (new Date()).getTime();
                } else {
                    // Retry save
                    savePromise = $timeout(saveFunction, MIN_SAVE_DELTA);
                }
            };

            $scope.$watch(function() {
                if (form.$dirty &&
                    $scope.error.saveErrorCode !== 'CONCURRENT_MODIFICATION' &&
                    $scope.error.saveErrorCode !== 'DATA_NOT_FOUND') {
                    var wait = SAVE_DELAY;
                    if (lastSave !== null) {
                        var lastSaveDelta = (new Date()).getTime() - lastSave;
                        wait =  MIN_SAVE_DELTA - lastSaveDelta;
                        if (wait < SAVE_DELAY) {
                            wait = SAVE_DELAY;
                        }
                        if (wait > MIN_SAVE_DELTA) {
                            wait = MIN_SAVE_DELTA;
                        }
                    }
                    if (savePromise) {
                        $timeout.cancel(savePromise);
                    }
                    savePromise = $timeout(saveFunction, wait);
                }
            });

            // When leaving the view perform save if needed and cancel any outstanding save request.
            $scope.$on('$destroy', function() {
                if (form.$dirty) {
                    save();
                }
                $timeout.cancel(savePromise);
            });
        }
    };
});