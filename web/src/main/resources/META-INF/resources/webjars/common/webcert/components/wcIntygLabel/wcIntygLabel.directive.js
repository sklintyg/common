/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

angular.module('common').directive('wcIntygLabel',
    ['$log', 'common.dynamicLabelService',
        function($log, dynamicLabelService) {
            'use strict';

            function _onLabelsUpdated(scope, frageId, label) {
                if (dynamicLabelService.hasProperty(frageId + '.RBK')) {
                    scope.h4Label = frageId + '.RBK';
                    if (!dynamicLabelService.hasProperty(label + '.RBK')) {
                        scope.h5Label = null;
                    }
                }
                else {
                    scope.h4Label = scope.h5Label;
                    scope.h5Label = null;
                }
            }

            return {
                restrict: 'A',
                replace: true,
                scope: {
                    field: '=',
                    forceNoH4Before: '=',
                    forceNoH5After: '='
                },
                templateUrl: '/web/webjars/common/webcert/components/wcIntygLabel/wcIntygLabel.directive.html',
                link: function(scope, element, attrs) { // jshint ignore:line

                    if (!scope.field) {
                        $log.debug('field argument is not defined');
                        return;
                    }

                    scope.noH4Before = false;
                    scope.noH5After = false;

                    scope.staticLabelId = scope.field.templateOptions && scope.field.templateOptions.staticLabelId;

                    if (scope.field.type === 'check-multi-text') {
                        scope.h4Label = scope.field.templateOptions && 'FRG_' + scope.field.templateOptions.frgId + '.RBK';
                        scope.h5Label = scope.field.templateOptions && 'DFR_' + scope.field.templateOptions.frgId + '.1.RBK';
                    } else {
                        var wcIntygLabel = scope.field.templateOptions && scope.field.templateOptions.label;
                        if (typeof wcIntygLabel !== 'string') {
                            $log.debug('wcIntygLabel is not a string');
                            return;
                        }

                        scope.h5Label = wcIntygLabel + '.RBK';

                        if (wcIntygLabel.substring(0, 4) === 'DFR_') {
                            var questionIds = wcIntygLabel.substring(4).split('.');
                            if (questionIds.length === 2 && questionIds[1] === '1') {
                                var frageId = 'FRG_' + questionIds[0];
                                scope.$on('dynamicLabels.updated', angular.bind(this, _onLabelsUpdated, scope, frageId, wcIntygLabel));
                            }
                        }
                        else if (wcIntygLabel.substring(0, 4) === 'FRG_') {
                            scope.h4Label = scope.h5Label;
                            scope.h5Label = null;
                        }
                    }

                    if (scope.forceNoH4Before === true) {
                        scope.noH4Before = false;
                    }
                    else if (!scope.h4Label && scope.h5Label) {
                        scope.noH4Before = true;
                    }

                    if (scope.forceNoH5After === true) {
                        scope.noH5After = false;
                    }
                    else if (scope.h4Label && !scope.h5Label) {
                        scope.noH5After = true;
                    }

                }
            };
        }]);
