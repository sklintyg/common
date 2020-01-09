/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
 * Created by BESA on 2015-03-05.
 */

/**
 * arendeVidarebefordra directive. Component for Vidarebefordra button, checkbox and loading animation
 */
angular.module('common').directive('arendeVidarebefordra',
    ['common.authorityService', 'common.UserModel',
        function(authorityService, UserModel) {
            'use strict';

            return {
                restrict: 'E',
                templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcArendePanelTab/vidarebefordra/arendeVidarebefordra.directive.html',
                scope: {
                    panelId: '@',
                    arendeListItem: '=',
                    parentViewState: '='
                },
                controller: function($scope, $element, $attrs) {
                    $scope.forwardInProgress = false;

                    $scope.showVidarebefordra = function() {
                        var hasAuthPermission = authorityService.isAuthorityActive({
                            authority: UserModel.privileges.VIDAREBEFORDRA_FRAGASVAR,
                            intygstyp: $scope.parentViewState.intygProperties.type });
                        return hasAuthPermission &&
                            $scope.parentViewState.intygProperties.isInteractionEnabled;
                    };
                }
            };
        }]);
