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

                    function isDefined(value) {
                        return !(value === null || value === undefined || value === '' || value === false);
                    }

                    var _onLabelsUpdated = function () {
                        var whitespaceBreak = '';

                        if(scope.config.whitespaceBreak === false){
                            whitespaceBreak = ' class="control-label white-space-no-wrap"';
                        }

                        var template = '<' + scope.config.labelType + whitespaceBreak + (scope.config.key ? ' id="' + ueDomIdFilter(scope.config.key) + '" ': '')  + '>\n';
                        if (scope.config.required) {
                            template = generateRequired(template);
                        }
                        if (scope.config.materialIcon) {
                            template += '<i class="material-icons label-icon' +
                                (scope.config.isLocked ? 'is-locked' : '') + 
                                '">' + scope.config.materialIcon + '</i>';
                        }
                        if (scope.config.key) {
                            template += '<span class="label-slot">' + dynamicLabelService.getProperty(scope.config.key) + '</span>\n';
                        }
                        if (scope.config.helpKey) {
                            template += '<wc-help help-key="' + scope.config.helpKey + '" variable-label-key="' + 
                            scope.config.variableLabelKey + '" ' + (scope.config.hideHelpExpression ? 
                            'ng-if="!config.hideHelpExpression || !$eval(config.hideHelpExpression)"' : '') +
                            '></wc-help>\n';
                        }
                        template += '</' + scope.config.labelType + '>\n';
                        element.empty();
                        element.append($compile(template)(scope));
                    };

                    function generateRequired(template) {
                        var requiredMode = 'OR';
                        if (scope.config.requiredMode) {
                            requiredMode = scope.config.requiredMode;
                        }
                        template += '<span class="required icon-wc-ikon-38"';
                        if (scope.config.requiredProp) {
                            if (angular.isArray(scope.config.requiredProp)) {
                                scope.required = function() {
                                    var defined = 0, reqPropLength = scope.config.requiredProp.length;
                                    for (var i = 0; i < reqPropLength; i++) {
                                        var req = $parse(scope.config.requiredProp[i])(scope.model);
                                        if (isDefined(req)) {
                                            defined++;
                                        }
                                    }
                                    if (defined === 0 || requiredMode === 'AND' && defined !== reqPropLength) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                };
                                template += ' ng-if="required()"';
                            } else if (angular.isFunction(scope.config.requiredProp)) {
                                scope.required = scope.config.requiredProp;
                                template += ' ng-if="required(model)"';
                            } else {
                                template += ' ng-if="model.' + scope.config.requiredProp + ' === undefined || model.' +
                                scope.config.requiredProp + ' === \'\' || model.' + scope.config.requiredProp + ' === null "';
                            }
                        }
                        template += '></span>\n';
                        return template;
                    }

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
