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
angular.module('common').directive('arendeDraftAutoSave',
    ['$timeout','$log', 'common.ArendeDraftProxy',
        function($timeout, $log, ArendeDraftProxy) {
            'use strict';

            return {
                restrict: 'A',
                scope: {
                    intygId: '=',
                    questionId: '=',
                    amne: '=',
                    text: '=',
                    saveState: '='
                },
                link: function($scope, $element, $attrs, $ctrls) {

                    var SAVE_DELAY = 800; // Time to wait before autosaving after a user action.
                    var MIN_SAVE_DELTA = 5000; // Minimum time to wait between two autosaves.

                    var savePromise = null;
                    var lastSave = null;
                    var lastSavedText = '';
                    var lastSavedAmne = null;

                    var save = function() {
                        var trimmedQuestionId = ($scope.questionId !== '') ? $scope.questionId : undefined;
                        var trimmedAmne = ($scope.amne !== null) ? $scope.amne : undefined;
                        $scope.saveState = 'saving';
                        ArendeDraftProxy.saveDraft($scope.intygId, trimmedQuestionId, $scope.text, trimmedAmne,
                            function() {
                                $scope.saveState = 'saved';
                            }, function() {
                                $scope.saveState = 'failed';
                            });
                    };

                    var saveFunction = function() {
                        savePromise = null;
                        if ($scope.text !== '' || ($scope.amne !== undefined && $scope.amne !== null)) {
                            save();
                            lastSave = (new Date()).getTime();
                            lastSavedText = $scope.text;
                            lastSavedAmne = $scope.amne;
                        }
                    };

                    $scope.saveState = 'normal';

                    $scope.$watch(function() {
                        if ($scope.intygId !== undefined &&
                            (($scope.text !== undefined && $scope.text !== '' && lastSavedText !== $scope.text) ||
                             ($scope.amne !== undefined && $scope.amne !== null && lastSavedAmne !== $scope.amne))) {
                            $scope.saveState = 'dirty';
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
                    $scope.$on('$stateChangeStart', function(){
                            if (savePromise) {
                                $timeout.cancel(savePromise);
                                save();
                            }
                        });
                }
            };
        }
    ]);
