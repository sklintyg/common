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
angular.module('common').directive('wcIntygCategory', ['common.IntygViewStateService',
    function(IntygViewStateService) {

        'use strict';

        function _hasCategoryKomplettering(category) {
            var index = -1;
            if (Array.isArray(category)) {
                category.some(function (item, i) {
                    return item.status === 'PENDING_INTERNAL_ACTION' ? (index = i, true) : false;
                });
            }
            return index > -1 ? true : false;
        }

        return {
            restrict: 'A',
            transclude: true,
            replace: true,
            templateUrl: '/web/webjars/common/webcert/intyg/fk/wcIntygCategory.directive.html',
            scope: {
                fieldDynamicLabel: '@',
                fieldLabel: '@',
                filled: '@?',
                categoryKey: '@'
            },
            link: function(scope) {
                if (scope.filled === undefined) {
                    scope.filled = 'true';
                }

                scope.hasCategoryKomplettering = function() {
                    // We need to make sure our model is up to date
                    // with changes to 'kompletteringar' made in the QA view
                    angular.forEach(IntygViewStateService.kompletteringar, function(komplettering) {
                        angular.forEach(komplettering, function(kmplt) {
                            if (kmplt.amne === 'KOMPLT') {
                                var key = kmplt.jsonPropertyHandle;
                                IntygViewStateService.updateCategoryField(scope.categoryKey, key, kmplt.status);
                            }
                        });
                    });

                    // Lookup if category has a field with status PENDING_INTERNAL_ACTION
                    return _hasCategoryKomplettering(IntygViewStateService.getCategory(scope.categoryKey));
                };
            }
        };
    }
]);
