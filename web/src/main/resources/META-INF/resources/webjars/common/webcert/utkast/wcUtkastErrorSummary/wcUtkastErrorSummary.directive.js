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
/**
 * wcUtkastErrorSummary directive. lists validation errors by category with scroll-to-links to
 * the corresponding category input section.
 */
angular.module('common').directive('wcUtkastErrorSummary',
    [ '$filter', 'common.dynamicLabelService', 'common.messageService', 'common.anchorScrollService',
            function($filter, dynamicLabelService, messageService, anchorScrollService) {
                'use strict';

                return {
                    restrict: 'E',
                    templateUrl: '/web/webjars/common/webcert/utkast/wcUtkastErrorSummary/wcUtkastErrorSummary.directive.html',
                    scope: {
                        categories: '=',
                        categoryIds: '='
                    },
                    controller: function($scope) {
                        $scope.lookUpLabel = function(category) {

                            //Get all available categories
                            var keys = Object.keys($scope.categoryIds);

                            for (var i = 0; i < keys.length; i++) {

                                if (!category) {
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
                            return messageService.getProperty('common.label.' + category);
                        };

                        $scope.scrollTo = function(categoryId) {
                            //By convention the ueKategori directive creates an anchor named 'anchor-<categoryId>'
                            // Validation categories are lowercased in utkastValidation.service
                            anchorScrollService.scrollIntygContainerTo('anchor-' + $filter('ueDomIdFilter')(categoryId), parseInt($('#certificate-content-container').offset().top, 10));
                        };

                    }
                };
            } ]);
