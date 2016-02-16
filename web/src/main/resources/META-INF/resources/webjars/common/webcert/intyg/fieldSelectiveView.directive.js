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

angular.module('common').directive('fieldSelectiveView',
    ['$log', '$window', 'common.ObjectHelper', function($log, $window, ObjectHelper) {
        'use strict';

        return {
            restrict: 'A',
            transclude: true,
            replace: true,
            scope: {
                fieldSelectiveView: '=',
                fieldModel: '='
            },
            templateUrl: '/web/webjars/common/webcert/intyg/fieldSelectiveView.directive.html',
            link: function(scope, element, attrs) {

                function recurseCheckField(model, field) {

                    if(!ObjectHelper.isDefined(field) || !ObjectHelper.isDefined(model[field[0]])) {
                        return false;
                    }

                    var nextFieldName = field.shift();
                    if(field.length == 0) {
                        return true;
                    }

                    return recurseCheckField(model[nextFieldName], field);
                }

                scope.$watch('fieldModel', function(model){
                    var field = scope.fieldSelectiveView;
                    var fieldPath = field.key.split('.');
                    if(fieldPath.length == 1) {
                        scope.show = !(field.key && model[field.key] == null);
                    }
                    else if(fieldPath.length > 1) {
                        scope.show = recurseCheckField(model, fieldPath);
                    }
                    else {
                        scope.show = false;
                    }
                }, true);

            }
        };
    }]);
