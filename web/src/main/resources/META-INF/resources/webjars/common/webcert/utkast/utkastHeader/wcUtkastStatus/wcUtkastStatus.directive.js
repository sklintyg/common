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
angular.module('common').directive('wcUtkastStatus', [
    '$rootScope',
    'common.moduleService', 'common.messageService', 'common.UtkastViewStateService', 'common.IntygStatusService',
    function($rootScope, moduleService, messageService, CommonViewState, IntygStatusService) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                utkastViewState: '='
            },
            templateUrl: '/web/webjars/common/webcert/utkast/utkastHeader/wcUtkastStatus/wcUtkastStatus.directive.html',
            link: function($scope) {

                $scope.intygstatus1 = {};
                $scope.intygstatus2 = {};

                $scope.getIntygStatus1 = function() {
                    // Just in case (should never be here for a signed utkast)
                    if (CommonViewState.isSigned) {
                        return setIntygStatus($scope.intygstatus1, 'is-001');
                    }
                    else if (CommonViewState.intyg.isComplete) {
                        return setIntygStatus($scope.intygstatus1, 'is-013');
                    }
                    return setIntygStatus($scope.intygstatus1, 'is-012');
                };

                $scope.getIntygStatus2 = function() {
                    if (CommonViewState.saving) {
                        return setIntygStatus($scope.intygstatus2, 'is-010');
                    }
                    return setIntygStatus($scope.intygstatus2, 'is-011');
                };

                function setIntygStatus(scopeObj, intygStatus, vars) {
                    scopeObj.code = intygStatus;
                    scopeObj.text = IntygStatusService.getMessageForIntygStatus(intygStatus, vars);
                    scopeObj.modal =  IntygStatusService.intygStatusHasModal(intygStatus);
                    return scopeObj;
                }
            }
        };
    }
]);
