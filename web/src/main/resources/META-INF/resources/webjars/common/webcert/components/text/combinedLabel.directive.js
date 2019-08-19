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
angular.module('common').directive('combinedLabel',
    [ '$log', '$rootScope', 'common.dynamicLabelService', 'common.messageService',
        function($log, $rootScope, dynamicLabelService, staticMessageService) {
            'use strict';

            return {
                restrict: 'EA',
                scope: {
                    'dynKey': '@',
                    'statKey': '@'
                },
                replace: true,
                template: '<span ng-bind-html="resultValue"></span>',
                link: function(scope, element, attr) {
                    var result;

                    function updateText(interpolatedKey) {
                        result = dynamicLabelService.getProperty(interpolatedKey);
                        var statLabel;
                        if (staticMessageService.propertyExists(attr.statKey)) {
                            statLabel = staticMessageService.getProperty(attr.statKey);
                        }
                        else {
                            statLabel = dynamicLabelService.getProperty(attr.statKey);
                        }

                        if (!statLabel) {
                            $log.error('labelKey "' + attr.statKey + '" not found');
                            scope.resultValue = '[Missing label "' + attr.statKey + '"]';
                        }
                        else {
                            var dynLabel = statLabel.replace('{0}', result);
                            scope.resultValue = dynLabel;
                        }
                    }

                    scope.$on('dynamicLabels.updated', function() {
                        updateText(attr.dynKey);
                    });

                    // observe changes to interpolated attribute
                    attr.$observe('dynKey', updateText);
                }
            };
        }]);
