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
angular.module('common').directive('wcSupportPanelManager', ['$rootScope', 'common.ArendeListViewStateService',
    function($rootScope, ArendeListViewState) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                config: '='
            },
            templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSupportPanelManager.directive.html',
            link: function($scope) {

                $scope.onSelect = function(newtab, $event) {
                    $event.preventDefault();

                    $scope.config.tabs.forEach(function(tab) {
                        tab.active = (tab.id === newtab.id);
                    });

                    $scope.$broadcast('panel.activated', newtab.id);
                };

                var hasArende = false;
                $scope.config.tabs.forEach(function(tab) {
                    if (tab.id === 'wc-arende-panel-tab') {
                        hasArende = true;
                    }
                });
                if (!hasArende) {
                    // wcArendePanelTab will reset viewstate, but it will not be loaded with this config.
                    ArendeListViewState.reset();
                    $rootScope.$broadcast('arenden.updated');
                }

            }
        };
    }
]);
