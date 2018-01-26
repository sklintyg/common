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
angular.module('common').directive('wcFormLabel',
    [ '$log', '$rootScope', 'common.dynamicLabelService',
        function($log, $rootScope, dynamicLabelService) {
            'use strict';

            return {
                restrict: 'EA',
                scope: {
                    'key': '=',
                    'to': '=',
                    'frageId': '@'
                },
                replace: true,
                templateUrl: '/web/webjars/common/webcert/components/wcFormLabel/wcFormLabel.directive.html',
                link: function(scope) {

                    scope.frageId = scope.to.frageId;
                    scope.hlpFrageId = null;

                    if (scope.to.label){
                        var questionIds = scope.to.label.substring(4).split('.');

                        // Setup FrågeID FRG
                        if (!scope.frageId && scope.to.label.substring(0, 4) === 'FRG_') {
                            scope.frageId = 'FRG_' + questionIds[0];
                            scope.hlpFrageId = scope.frageId;
                        }

                        // Setup DelFrågeId DFR
                        if (scope.to.label.substring(0, 4) === 'DFR_') {
                            scope.delFrageId = scope.to.label;
                            if (questionIds.length === 2 && questionIds[1] === '1') {
                                scope.frageId = 'FRG_' + questionIds[0];
                                scope.hlpFrageId = scope.frageId;
                            }
                        }

                        var _onLabelsUpdated = function (scope) {
                            if (!dynamicLabelService.hasProperty(scope.frageId + '.RBK')) {
                                scope.frageId = null;
                            }
                            if (!dynamicLabelService.hasProperty(scope.delFrageId + '.RBK')) {
                                scope.delFrageId = null;
                            }
                        };

                        scope.$on('dynamicLabels.updated', angular.bind(this, _onLabelsUpdated, scope));
                    }
                }
            };
        }]);
