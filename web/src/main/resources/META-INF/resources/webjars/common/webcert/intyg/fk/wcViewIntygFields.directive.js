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

angular.module('common').directive('wcViewIntygFields', ['$rootScope', 'common.ObjectHelper',
    function($rootScope, ObjectHelper) {
        'use strict';

        return {
            restrict: 'A',
            replace: true,
            scope: {
                wcViewIntygFields: '=',
                intygModel: '='
            },
            templateUrl: '/web/webjars/common/webcert/intyg/fk/wcViewIntygFields.directive.html',
            link: function(scope, element, attrs) {
                scope.intygFields = scope.wcViewIntygFields;

                scope.showField = function(field){
                    return !field.templateOptions.hideFromSigned &&
                     (!field.templateOptions.hideWhenEmpty || scope.intygModel[field.key]);
                };

                scope.showFieldLine = function(field, nextField) {

                    var showField = scope.showField(field);

                    if(showField){
                        // No lines after these fields
                        if (field.type === 'info' || field.type === 'headline') {
                            return false;
                        }
                        if (field.templateOptions.label && field.templateOptions.label.indexOf('KV_') === 0) {
                            return false;
                        }
                        var exp = /DFR_[0-9]+\.([2-9]|[1-9][0-9]+)/;
                        if (exp.exec(nextField.templateOptions.label)) {
                            return false;
                        }
                    } else {
                        // Always line on these fields
                        if(field.templateOptions.forceLine){
                            return true;
                        }

                        return false;
                    }

                    return true;
                };

            }
        };
    }]);
