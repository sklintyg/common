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

/**
 * wcField directive. Used to abstract common layout for full-layout form fields in intyg modules
 */
angular.module('common').directive('wcUtkastErrorSummaryFk',
    ['common.dynamicLabelService', 'common.messageService', 'common.UtkastViewStateService', 'common.anchorScrollService',
        function(dynamicLabelService, messageService, viewState, anchorScrollService) {
            'use strict';

            return {
                restrict: 'A',
                replace: true,
                templateUrl: '/web/webjars/common/webcert/utkast/wcUtkastErrorSummaryFK.directive.html',
                scope: true,
                controller: function($scope) {
                    $scope.lookUpLabel = function(category) {

                        var keys = Object.keys($scope.categoryNames);

                        for(var i = 0; i < keys.length; i++) {
                            if (category === $scope.categoryNames[keys[i]].toLowerCase()) {
                                var result = dynamicLabelService.getProperty('KAT_' + keys[i] + '.RBK');
                                if (result) {
                                    return result;
                                }
                                return 'KAT_' + i + '.RBK';
                            }
                        }
                        return messageService.getProperty('common.label.'+category);
                    };

                    $scope.scrollTo = function(message) {
                        anchorScrollService.scrollTo('anchor-' + message);
                    };
                }
            };
        }]);
