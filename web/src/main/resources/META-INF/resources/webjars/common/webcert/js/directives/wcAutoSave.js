angular.module('common').directive('wcAutoSave', ['$timeout', 'common.ManageCertView',
    function($timeout, ManageCertView) {
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

                var save = function(extras) {
                    return ManageCertView.save(extras);
                };

                var saveFunction = function() {
                    savePromise = null;
                    var result = save();
                    if (result) {
                        lastSave = (new Date()).getTime();
                    } else {
                        // Retry save
                        savePromise = $timeout(saveFunction, MIN_SAVE_DELTA);
                    }
                };

                $scope.$watch(function() {
                    if (form.$dirty) {
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
                        save({autoSave: true, destroy:$scope.destroyList});
                    }
                    $timeout.cancel(savePromise);
                });
            }
        };
    }]
);
