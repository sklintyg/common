/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
angular.module('common').directive('wcFmbHelpDisplay', ['common.ObjectHelper', 'common.fmbService', 'common.fmbViewState', '$rootScope',
    function(ObjectHelper, fmbService, fmbViewState, $rootScope) {
        'use strict';

        return {
            restrict: 'E',
            transclude: true,
            scope: {
                fieldName: '@',
                relatedFormId: '@'
            },
            link: function(scope, element, attrs) {
                scope.status = {
                    open: false
                };

                scope.toggleFMB = function(){
                    scope.status.open = !scope.status.open;

                    // Close srs by sending event
                    if(scope.status.open) {
                        $rootScope.$broadcast('closeSrs');
                    }
                };

                scope.$on('closeFmb', function(){
                    scope.status.open = false;
                });

                scope.fmbViewState = fmbViewState;

                function updateFMBAvailable() {
                    if(ObjectHelper.isDefined(scope.fieldName) && ObjectHelper.isDefined(scope.relatedFormId)){
                        scope.fmbAvailable = fmbService.isAnyFMBDataAvailable(fmbViewState);
                    } else {
                        scope.fmbAvailable = false;
                    }
                }

                scope.$watch('fmbViewState', function(newVal, oldVal) {
                    updateFMBAvailable();
                }, true);

                updateFMBAvailable();
            },
            templateUrl: '/web/webjars/common/webcert/utkast/fmb/wcFmbHelpDisplay.directive.html'
        };
    }]);
