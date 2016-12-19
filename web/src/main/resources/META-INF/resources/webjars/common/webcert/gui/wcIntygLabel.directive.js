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

            function _onLabelsUpdated(scope, frageId) {
                if (dynamicLabelService.hasProperty(frageId + '.RBK')) {
                    scope.h4Label = frageId + '.RBK';
                    if (!dynamicLabelService.hasProperty(scope.wcIntygLabel + '.RBK')) {
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
                    wcIntygLabel: '=',
                    staticLabelId: '=',
                    forceNoH5After: '=',
                    forceNoH4Before: '='
                },
                templateUrl: '/web/webjars/common/webcert/gui/wcIntygLabel.directive.html',
                link: function(scope, element, attrs) {
                    if (typeof scope.wcIntygLabel !== 'string') {
                        $log.debug('wcIntygLabel argument is not a string');
                        return;
                    }

                    scope.h5Label = scope.wcIntygLabel + '.RBK';
                    scope.noH5After = false;
                    if (scope.forceNoH5After === true) {
                        scope.noH5After = true;
                    }

                    if (scope.wcIntygLabel.substring(0, 4) === 'DFR_') {
                        var questionIds = scope.wcIntygLabel.substring(4).split('.');
                        if (questionIds.length === 2 && questionIds[1] === '1') {
                            var frageId = 'FRG_' + questionIds[0];
                            scope.$on('dynamicLabels.updated', angular.bind(this, _onLabelsUpdated, scope, frageId));
                        }
                    }
                    else if (scope.wcIntygLabel.substring(0, 4) === 'FRG_') {
                        scope.h4Label = scope.h5Label;
                        scope.h5Label = null;
                        scope.noH5After = true;
                        if (scope.forceNoH5After === false) {
                            scope.noH5After = false;
                        }
                    }

                    if (scope.forceNoH4Before === true) {
                        scope.noH4Before = true;
                    } else if (!scope.h4Label && scope.h5Label) {
                        scope.noH4Before = true;
                    }

                }
            };
        }]);
