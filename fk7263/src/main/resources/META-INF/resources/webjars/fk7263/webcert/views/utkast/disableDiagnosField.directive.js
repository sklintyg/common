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
 * wcFieldSingle directive. Used to abstract common layout for single-line form fields in intyg modules
 */
angular.module('fk7263').directive('disableDiagnosisField', function($compile) {
        'use strict';

        return {
            restrict: 'A',
            replace: false,
            scope: {
                model: '@'
            },
            terminal: true,
            priority: 1000,
            link: function(scope, element, attrs) {
                element.attr('ng-disabled', '{{isDisabled()}}');
                element.removeAttr('disable-diagnosis-field');

                $compile(element)(scope);

            },
            controller: function($scope, $element) {

                var scope = $scope;
                var elemId = $element[0].id;

                $scope.isDisabled = function() {

                    if (elemId && (elemId === 'diagnoseCodeOpt1' || elemId === 'diagnoseDescriptionOpt1')) {
                        console.log("1");
                        console.log($scope.model.diagnosKod, $scope.model.diagnosBeskrivning1);
                    }

                    if (elemId && (elemId === 'diagnoseCodeOpt2' || elemId === 'diagnoseDescriptionOpt2')) {
                        console.log("2");
                        console.log($scope.model.diagnosKod2, $scope.model.diagnosBeskrivning2);
                    }

                    // Om
                    return false;
                };
            }
        };
    });
