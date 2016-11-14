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
 * Display FMB help texts
 */
angular.module('common').directive('wcFmbHelpDisplay', ['common.ObjectHelper',
    function(ObjectHelper) {
        'use strict';

        return {
            restrict: 'E',
            transclude: true,
            scope: {
                fmbStates: '=',
                fieldName: '@',
                relatedFormId: '@'
            },
            link: function(scope, element, attrs) {
                scope.status = {
                    open: true
                };

                function checkDiagnos(diagnos) {
                    if (angular.isObject(diagnos) && !ObjectHelper.isEmpty(diagnos.diagnosKod) &&
                        Object.keys(diagnos.formData).length > 1) {
                        scope.fmbAvailable = true;
                        return true;
                    }
                    return false;
                }

                function updateFMBAvailable() {
                    scope.fmbAvailable = false;
                    if (angular.isObject(scope.fmbStates)) {
                        if (checkDiagnos(scope.fmbStates.main) ||
                            checkDiagnos(scope.fmbStates.bi1) ||
                            checkDiagnos(scope.fmbStates.bi2)) {
                            scope.fmbAvailable = true;
                        }
                    }
                }

                scope.$watch('fmbStates', function(newVal, oldVal) {
                    updateFMBAvailable();
                }, true);

                updateFMBAvailable();
            },
            templateUrl: '/web/webjars/common/webcert/fmb/wcFmbHelpDisplay.directive.html'
        };
    }]);
