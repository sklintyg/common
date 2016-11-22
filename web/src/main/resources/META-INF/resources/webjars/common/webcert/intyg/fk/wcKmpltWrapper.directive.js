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

angular.module('common').directive('wcKmpltWrapper',
    [ 'common.IntygViewStateService',
        function(IntygViewStateService) {
            'use strict';

            function _hasKomplettering(category) {
                var index = -1;
                if (Array.isArray(category)) {
                    category.some(function (item, i) {
                        return item.status === 'PENDING_INTERNAL_ACTION' ? (index = i, true) : false;
                    });
                }
                return index > -1 ? true : false;
            }

            return {
                restrict: 'AE',
                transclude: true,
                replace: true,
                templateUrl: '/web/webjars/common/webcert/intyg/fk/wcKmpltWrapper.directive.html',
                require: '?^^wcIntygField',
                scope: {
                    categoryKey: '@',
                    fieldKey: '@'
                },
                link: function(scope, element, attr, wcIntygField) {
                    scope.viewState = IntygViewStateService;

                    // Update structure holding field and category mapping
                    scope.viewState.setCategoryField(scope.categoryKey, scope.fieldKey);

                    scope.$watch('viewState.kompletteringar', function() {
                        angular.forEach(scope.viewState.kompletteringar, function(komplettering) {
                            angular.forEach(komplettering, function(kmplt) {
                                if (kmplt.amne === 'KOMPLT') {
                                    var state = scope.viewState;
                                    var key = kmplt.jsonPropertyHandle;
                                    var found = state.hasCategoryField(scope.categoryKey, key);

                                    // If there is a wcField above us in DOM, inform it to show komplettering border
                                    if (wcIntygField && found) {
                                        // update category/field mapping
                                        state.setCategoryField(scope.categoryKey, key, kmplt.status);
                                        // lookup if there's a field having a status PENDING_INTERNAL_ACTION
                                        wcIntygField.setCategoryHasKomplettering(_hasKomplettering(state.getCategory(scope.categoryKey)));
                                    }
                                }
                            });
                        });
                    }, true);
                }
            };
        }]);
