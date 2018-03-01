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
/**
 * wcField directive. Used to abstract common layout for full-layout form fields in intyg modules
 */
angular.module('common').directive('wcUtkastErrorSummaryFk',
    ['common.dynamicLabelService', 'common.messageService',
        'common.UtkastViewStateService', 'common.anchorScrollService', 'common.UtkastValidationViewState',
        function(dynamicLabelService, messageService, CommonViewState,
            anchorScrollService, utkastValidationViewState) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/utkast/wcUtkastErrorSummaryFK/wcUtkastErrorSummaryFK.directive.html',
                scope: true,
                controller: function($scope) {
                    $scope.lookUpLabel = function(category) {

                        var keys = Object.keys($scope.categoryIds);

                        for(var i = 0; i < keys.length; i++) {

                            if(!category){
                                continue;
                            }

                            var categoryLc = category.toLowerCase();
                            var categoryNameLc = $scope.categoryIds[keys[i]].toLowerCase();
                            if (categoryLc === categoryNameLc) {
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
                        anchorScrollService.scrollIntygContainerTo('anchor-' + message);
                    };

                    $scope.hideMissing = function(){
                        CommonViewState.setShowComplete(false);
                        utkastValidationViewState.reset();
                    };
                }
            };
        }]);
