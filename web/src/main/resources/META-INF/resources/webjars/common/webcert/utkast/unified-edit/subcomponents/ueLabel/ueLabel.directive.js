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

angular.module('common').directive('ueLabel',
    [ '$log', '$compile', '$rootScope', 'common.dynamicLabelService', 'ueDomIdFilterFilter', '$parse',
        function($log, $compile, $rootScope, dynamicLabelService, ueDomIdFilter, $parse) {
            'use strict';

            return {
                restrict: 'EA',
                scope: {
                    'config': '=',
                    'model': '='
                },
                replace: true,
                link: function(scope, element) {

                    var _onLabelsUpdated = function () {

                        var whitespaceBreak = '';

                        if(scope.config.whitespaceBreak === false){
                            whitespaceBreak = ' class="control-label white-space-no-wrap"';
                        }

                        var template = '<' + scope.config.labelType + whitespaceBreak + (scope.config.key ? ' id="' + ueDomIdFilter(scope.config.key) + '" ': '')  + '>\n';
                        if (scope.config.required) {
                            if (scope.config.requiredProp) {
                                if (angular.isArray(scope.config.requiredProp)) {
                                    scope.allRequiredUndefined = function() {
                                        for (var i = 0; i < scope.config.requiredProp.length; i++) {
                                            var req = $parse(scope.config.requiredProp[i])(scope.model);
                                            if(req === null || req === undefined || req === '' || req === false) {
                                                continue;
                                            }
                                            return false;
                                        }
                                        return true;
                                    };
                                    template += '<span class="required icon-wc-ikon-38" ng-if="allRequiredUndefined()"></span>\n';
                                } else {
                                    template += '<span class="required icon-wc-ikon-38" ng-if="model.' + scope.config.requiredProp + ' === undefined || model.' +
                                    scope.config.requiredProp + ' === \'\' || model.' + scope.config.requiredProp + ' === null "></span>\n';
                                }
                            } else {
                                template += '<span class="required icon-wc-ikon-38"></span>\n';
                            }
                        }
                        if (scope.config.key) {
                            template += dynamicLabelService.getProperty(scope.config.key) + '\n';
                        }
                        if (scope.config.helpKey) {
                            template += '<span wc-help-chevron help-text-key="' + scope.config.helpKey + '"></span>\n';
                        }
                        template += '</' + scope.config.labelType + '>\n';
                        if (scope.config.helpKey) {
                            template += '<span wc-help-chevron-text help-text-key="' + scope.config.helpKey + '"></span>\n';
                        }
                        element.empty();
                        element.append($compile(template)(scope));
                    };

                    if (scope.config) {
                        if (!scope.config.labelType) {
                            scope.config.labelType = 'h5';
                        }
                        scope.$on('dynamicLabels.updated', _onLabelsUpdated);
                        _onLabelsUpdated();
                    }
                }
            };
        }]);
