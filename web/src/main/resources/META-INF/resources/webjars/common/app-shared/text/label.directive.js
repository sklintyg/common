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
angular.module('common').directive('dynamicLabel',
    [ '$compile', '$log', '$rootScope', 'common.dynamicLabelService', 'common.messageService',
        function($compile, $log, $rootScope, dynamicLabelService, messageService) {
            'use strict';

            return {
                restrict: 'EA',
                scope: {
                    'key': '@',
                    'params': '<',
                    'fallbackValue': '@',
                    'supportExternalLink': '<'
                },
                replace: true,
                link: function(scope, element, attr) {
                    var result;

                    function updateText(interpolatedKey) {
                        // Try to find the key in the messageService first
                        result = messageService.propertyExists(angular.lowercase(interpolatedKey));

                        if (!result) {
                            result = dynamicLabelService.getProperty(interpolatedKey, scope.supportExternalLink);
                        } else {
                            result = messageService.getProperty(angular.lowercase(interpolatedKey), scope.params);
                        }

                        if (!result && scope.fallbackValue) {
                            result = scope.fallbackValue;
                        }

                        element.empty();
                        element.append(result);
                        $compile(element.contents())(scope);
                    }

                    scope.$on('dynamicLabels.updated', function() {
                        updateText(attr.key);
                    });

                    // observe changes to interpolated attribute
                    attr.$observe('key', updateText);
                }
            };
        }]);