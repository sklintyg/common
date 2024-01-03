/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('common').directive('message',
    [ '$log', '$rootScope', 'common.messageService',
        function($log, $rootScope, messageService) {
            'use strict';

            return {
                restrict: 'EA',
                scope: {
                    'key': '@',
                    'param': '=',
                    'params': '='
                },
                replace: true,
                template: '<span ng-bind-html="resultValue"></span>',
                link: function(scope, element, attr) {
                    var result;
                    // observe changes to interpolated attribute
                    attr.$observe('key', function(interpolatedKey) {
                        var normalizedKey = angular.lowercase(interpolatedKey);
                        var useLanguage;
                        if (typeof attr.lang !== 'undefined') {
                            useLanguage = attr.lang;
                        } else {
                            useLanguage = $rootScope.lang;
                        }

                        var parameters = [];
                        if (typeof scope.param !== 'undefined') {
                            parameters.push(scope.param);
                        }
                        else if (typeof scope.params !== 'undefined') {
                            parameters.push.apply(parameters, scope.params);
                        }

                        result = messageService.getProperty(normalizedKey, parameters, useLanguage, attr.fallback, (typeof attr.fallbackDefaultLang !== 'undefined'));



                        // now get the value to display..
                        scope.resultValue = result;
                    });
                }
            };
        }]);
