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

angular.module('common').directive('miIntygLabel',
    ['$log', 'common.dynamicLabelService',
        function($log, dynamicLabelService) {
            'use strict';

            return {
                restrict: 'A',
                replace: true,
                scope: {
                    miIntygLabel: '='
                },
                templateUrl: '/web/webjars/common/minaintyg/intyg/miIntygLabel.directive.html',
                link: function(scope, element, attrs) {
                    if (typeof scope.miIntygLabel !== 'string') {
                        $log.debug('miIntygLabel argument is not a string');
                        return;
                    }
                    scope.h5Label = scope.miIntygLabel + '.RBK';

                    if (scope.miIntygLabel.substring(0, 4) === 'DFR_') {
                        var questionIds = scope.miIntygLabel.substring(4).split('.');
                        if (questionIds.length === 2 && questionIds[1] === '1') {
                            var frageId = 'FRG_' + questionIds[0];
                            scope.$on('dynamicLabels.updated', function() {
                                if (dynamicLabelService.hasProperty(frageId + '.RBK')) {
                                    scope.h4Label = frageId + '.RBK';
                                    if (!dynamicLabelService.hasProperty(scope.miIntygLabel + '.RBK')) {
                                        scope.h5Label = null;
                                    }
                                }
                                else {
                                    scope.h4Label = scope.h5Label;
                                    scope.h5Label = null;
                                }
                            });
                        }
                    }
                    else if (scope.miIntygLabel.substring(0, 4) === 'FRG_') {
                        scope.h4Label = scope.h5Label;
                        scope.h5Label = null;
                    }
                }
            };
        }]);
