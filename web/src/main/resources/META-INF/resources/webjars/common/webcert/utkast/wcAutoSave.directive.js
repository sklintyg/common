/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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
                    if ($window.autoSave && form.$dirty &&
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
