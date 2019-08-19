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
angular.module('common').directive('ueRenderComponentsSpinner',
    function($browser, $compile, $timeout, $animate) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                form: '=',
                config: '=',
                model: '='
            },
            link: function($scope, $element) {

                if ($scope.config.length === 0) {
                    return;
                }

                var spinners = [];
                var timeoutPromise;

                function addSpinner() {
                    var template = '<div class="card"><wc-spinner show-spinner="true"></wc-spinner></div>';
                    var spinner = $compile(template)($scope);
                    $element.append(spinner);
                    return spinner;
                }

                function replaceSpinner(i) {
                    var template = '<ue-dynamic-component form="::form" config="::config[' + i + ']" model="::model" />';
                    spinners[i].replaceWith($compile(template)($scope));

                    if (i + 1 < $scope.config.length) {
                        timeoutReplaceSpinner(i + 1);
                    }
                    else {
                        $animate.enabled(true);
                    }
                }

                function timeoutReplaceSpinner(i) {
                    timeoutPromise = $timeout(function() {
                        timeoutPromise = null;
                        replaceSpinner(i);
                    });
                }

                $animate.enabled(false);
                for (var i = 0; i < $scope.config.length; i++) {
                    spinners[i] = addSpinner();
                }
                timeoutReplaceSpinner(0);

                $scope.$on('$destroy', function() {
                    $animate.enabled(true);
                    if (timeoutPromise) {
                        $timeout.cancel(timeoutPromise);
                    }
                });
            }
        };
    });
