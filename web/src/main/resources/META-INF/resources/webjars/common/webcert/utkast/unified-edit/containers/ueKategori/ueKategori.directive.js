/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
angular.module('common').directive('ueKategori', ['$parse', '$compile', '$timeout',
    function($parse, $compile, $timeout) {
        'use strict';

        var componentsTemplate = '<ue-render-components class="ue-kategori__body" form="::form" config="::config.components" model="::model" />';
        var compileQueue = [];
        var timeoutHandle;

        return {
            restrict: 'E',
            scope: {
                form: '=',
                config: '=',
                model: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/unified-edit/containers/ueKategori/ueKategori.directive.html',
            link: function($scope, $element) {
                if ($scope.config.label.required) {
                    $scope.hasUnfilledRequirements = function() {
                        var reqProp = $scope.config.label.requiredProp;
                        if (reqProp) {
                            var req;
                            if (angular.isArray(reqProp)) {
                                for (var i = 0; i < reqProp.length; i++) {
                                    req = $parse(reqProp[i])($scope.model);
                                    if(!$scope.form[reqProp[i]] && (req === null || req === undefined || req === false)) {
                                        continue;
                                    }
                                    return false;
                                }
                                return true;
                            } else {
                                req = $parse(reqProp)($scope.model);
                                if(req === null || req === undefined || req === false) {
                                    return true;
                                }
                            }
                        } else {
                            return true;
                        }
                    };
                }

                // Function to replace the spinner with our subcomponents
                var compileFunction = function() {
                    var element = $element.find('.ue-kategori__spinner');
                    if (element) {
                        element.replaceWith($compile(componentsTemplate)($scope))
                    }
                    // If there are still items in the queue, schedule the next compile call
                    if (compileQueue.length > 0 && !timeoutHandle) {
                        timeoutHandle = $timeout(function() {
                            timeoutHandle = null;
                            // Async call, check if queue still has items
                            if (compileQueue.length > 0) {
                                compileQueue.shift()();
                            }
                        });
                    }
                };

                // Add our compileFunction to the queue
                compileQueue.push(compileFunction);

                // If this is the first item in the queue, start the compile process by calling the first item in the queue
                if (compileQueue.length == 1 && !timeoutHandle) {
                    timeoutHandle = $timeout(function() {
                        timeoutHandle = null;
                        // Async call, check if queue still has items
                        if (compileQueue.length > 0) {
                            compileQueue.shift()();
                        }
                    });
                }

                // If this kategori compileFunction is in compileQueue when scope is destroyed, remove it
                $scope.$on('$destroy', function() {
                    var index = compileQueue.indexOf(compileFunction);
                    if (index !== -1) {
                        compileQueue.splice(index, 1);
                    }
                });
            }
        };
    }]);
