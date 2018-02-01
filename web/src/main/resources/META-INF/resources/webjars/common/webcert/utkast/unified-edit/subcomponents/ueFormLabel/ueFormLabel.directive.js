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

angular.module('common').directive('ueFormLabel',
    [ '$log', '$compile', '$rootScope', 'common.dynamicLabelService',
        function($log, $compile, $rootScope, dynamicLabelService) {
            'use strict';

            return {
                restrict: 'EA',
                scope: {
                    'config': '='
                },
                replace: true,
                link: function(scope, element) {

                    var _onLabelsUpdated = function () {

                        var whitespaceBreak = '';

                        if(scope.config.whitespaceBreak === false){
                            whitespaceBreak = ' class="control-label white-space-no-wrap"';
                        }

                        var template = '<' + scope.config.type + whitespaceBreak + '>\n';
                        if (scope.config.required) {
                            template += '<span class="required">*</span>\n';
                        }
                        if (scope.config.key) {
                            template += dynamicLabelService.getProperty(scope.config.key) + '\n';
                        }
                        if (scope.config.helpKey) {
                            template += '<span wc-help-chevron help-text-key="' + scope.config.helpKey + '"></span>\n';
                        }
                        template += '</' + scope.config.type + '>\n';
                        if (scope.config.helpKey) {
                            template += '<span wc-help-chevron-text help-text-key="' + scope.config.helpKey + '"></span>\n';
                        }
                        element.empty();
                        element.append($compile(template)(scope));
                    };

                    if (scope.config) {
                        if (!scope.config.type) {
                            scope.config.type = 'h5';
                        }
                        scope.$on('dynamicLabels.updated', _onLabelsUpdated);
                        _onLabelsUpdated();
                    }
                }
            };
        }]);
