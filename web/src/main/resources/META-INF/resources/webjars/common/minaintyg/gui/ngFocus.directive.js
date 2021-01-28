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
angular.module('common').directive('ngFocus',
    function($parse, $timeout) {
        'use strict';
        return function(scope, element, attrs) {
            var ngFocusGet = $parse(attrs.ngFocus);
            var ngFocusSet = ngFocusGet.assign;
            if (!ngFocusSet) {
                throw Error('Non assignable expression');
            }

            var abortFocusing = false;
            var unwatch = scope.$watch(attrs.ngFocus, function(newVal) {
                if (newVal) {
                    $timeout(function() {
                        element[0].focus();
                    }, 0);
                } else {
                    $timeout(function() {
                        element[0].blur();
                    }, 0);
                }
            });

            function onBlur() {
                if (abortFocusing) {
                    return;
                }

                $timeout(function() {
                    ngFocusSet(scope, false);
                }, 0);
            }

            element.bind('blur', onBlur);

            var timerStarted = false;
            var focusCount = 0;

            function startTimer() {
                $timeout(function() {
                    timerStarted = false;
                    if (focusCount > 3) {
                        unwatch();
                        abortFocusing = true;
                        throw new Error(
                            'Aborting : ngFocus cannot be assigned to the same variable with multiple elements');
                    }
                }, 200);
            }

            function onFocus() {
                if (abortFocusing) {
                    return;
                }

                if (!timerStarted) {
                    timerStarted = true;
                    focusCount = 0;
                    startTimer();
                }
                focusCount++;

                $timeout(function() {
                    ngFocusSet(scope, true);
                }, 0);
            }

            element.bind('focus', onFocus);

            scope.$on('$destroy', function() {
                element.unbind('blur', onBlur);
                element.unbind('focus', onFocus);
           });
        };
    });
