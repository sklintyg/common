/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

angular.module('common').directive('wcDisableKeyDown',
    function($rootScope, $timeout) {
        'use strict';
        var removeEnter = function(element, scope, attrs){
            element.bind('keydown', function(event) {
                var code = event.keyCode || event.which;
                if (code === 13 && !event.shiftKey) {
                    event.preventDefault();
                    scope.$apply(attrs.enterSubmit);
                }
            });
        };
        return {
            restrict: 'EA',
            scope: {
                elements: '@wcDisableKeyDownElements',
                doneLoading: '=wcDisableKeyDownDoneLoading'
            },
            link: function (scope, elem, attrs, ctrl, transclude) {
                if (!scope.elements) {
                    removeEnter(elem, scope, attrs);
                } else {
                    // postpone until the dom is rendered
                    scope.$watch('doneLoading', function(doneLoading) {
                        if (doneLoading) {
                            var elements = scope.elements.split(',');
                            angular.forEach(elements, function(value) {
                                removeEnter(elem.parents('form').find(value), scope, attrs);
                            });
                        }
                    });

                }

            }
        };
    });
