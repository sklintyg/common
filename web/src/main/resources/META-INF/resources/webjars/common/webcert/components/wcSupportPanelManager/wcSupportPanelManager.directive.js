/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
    'common.dynamicLabelService', 'common.UserModel', 'common.User',
    function($rootScope, ArendeListViewState, dynamicLabelService, UserModel, UserService) {
        'use strict';

        return {
            restrict: 'E',
            scope: {
                config: '=',
                minimized: '=?'
            },
            templateUrl: '/web/webjars/common/webcert/components/wcSupportPanelManager/wcSupportPanelManager.directive.html',
            link: function($scope, element) {

                if ($scope.config.disableMinimizeMode) {
                    $scope.minimized = false;
                } else {
                    $scope.minimized = UserModel.getAnvandarPreference('wc.sidebarMinimized') === 'true';
                }

                if($scope.minimized) {
                    element.addClass('minimized');
                }

                $scope.onSelect = function(newtab, $event) {
                    $event.preventDefault();

                    $scope.config.tabs.forEach(function(tab) {
                        tab.active = (tab.id === newtab.id);
                    });

                    $scope.$broadcast('panel.activated', newtab.id);
                };

                $scope.onSelectMinimized = function(newtab, $event) {
                    // show sidepanel.
                    $scope.setMinimized(false);
                    $scope.onSelect(newtab, $event);
                };

                var hasArende = false;
                $scope.config.tabs.forEach(function(tab) {

                    setupMinimizedTabConfig(tab);

                    if (tab.id === 'wc-arende-panel-tab') {
                        hasArende = true;

                        $rootScope.$on('totalArendenCount.updated', function(event, arenden) {
                            tab.minimized.notificationCount = arenden.count;
                        });
                    }
                });
                if (!hasArende) {
                    // wcArendePanelTab will reset viewstate, but it will not be loaded with this config.
                    ArendeListViewState.reset();
                    $rootScope.$broadcast('arenden.updated');
                }

                $scope.getText = function(key) {
                    return dynamicLabelService.getProperty(key);
                };

                $scope.setMinimized = function(value) {
                    $scope.minimized = value;
                    UserService.storeAnvandarPreference('wc.sidebarMinimized', value ? 'true' : 'false');
                    element.toggleClass('minimized', value);
                };

                function setupMinimizedTabConfig(tab) {

                    tab.minimized = tab.minimized || {};

                    if(tab.id === 'wc-arende-panel-tab') {
                        tab.minimized.icon = 'forum';
                    } else if(tab.id === 'wc-help-tips-panel-tab') {
                        tab.minimized.icon = 'icon-wc-ikon-24';
                        tab.minimized.iconIsCustom = true;
                    }

                    tab.minimized.notificationCount = 0;
                    tab.minimized.icon = tab.minimized.icon || tab.icon;
                    tab.minimized.tooltip = tab.minimized.tooltip || tab.tooltip + '.minimized';
                    tab.minimized.title = tab.minimized.title || tab.title + '.minimized';
                }
            }
        };
    }
]);
