/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcAutoSave', ['$timeout', '$window', '$log', 'common.UtkastService', 'common.UtkastViewStateService',
    function($timeout, $window, $log, UtkastService, UtkastViewState) {
        'use strict';

        return {
            require: ['^form'],
            scope: {
                'saveEnabled': '<wcAutoSave'
            },
            link: function($scope, $element, $attrs, $ctrls) {

                var SAVE_DELAY = 1 * 800; // Time to wait before autosaving after a user action.
                var MIN_SAVE_DELTA = 5 * 1000; // Minimum time to wait between two autosaves.

                var form = $ctrls[0];
                var savePromise = null;
                var lastSave = null;

                var save = function(extras) {
                    if (form.$dirty) {
                        return UtkastService.save(extras);
                    }
                    return true;
                };

                $window.save = UtkastService.save;

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

                $scope.$watch(
                    function () {
                        return $window.autoSave;
                    }, function(n,o){
                        $log.debug('autoSave changed from ' + o + ', to :' + n);
                        if(n){
                            $log.debug('Disabling auto save');
                        } else {
                            $log.debug('Enabling auto save');
                        }
                    }
                );

                $scope.$watch(function() {
                    if ($window.autoSave && form.$dirty && $scope.saveEnabled &&
                        UtkastViewState.error.saveErrorCode !== 'CONCURRENT_MODIFICATION' &&
                        UtkastViewState.error.saveErrorCode !== 'DATA_NOT_FOUND') {
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

                // When leaving the view cancel any outstanding save request.
                $scope.$on('$destroy', function() {
                    $timeout.cancel(savePromise);
                });

                // Whenever a ui-router state change is initialized, we make sure to cancel any ongoing auto-save promise
                // and if the form is dirty, we issue a new save.
                // The reason for using $stateChangeStart and not $destroy is that the $destroy is used by various
                // components including the attic handler, which on destroy for a given DOM element will also null
                // the underlying field in our model, resulting in an empty utkast being saved effectively destroying
                // all content of the utkast. $stateChangeStart runs _before_ any $destroy events are published so as
                // long as we can issue our save request here (if dirty), it's going to be unaffected by the $destroy
                // teardown of the viewstate model.
                $scope.$on('$stateChangeStart',
                    function(){
                        $timeout.cancel(savePromise);
                        if(form.$dirty) {
                            UtkastService.save({autoSave: true});
                        }
                    });
            }
        };
    }]
);
