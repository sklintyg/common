/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

angular.module('common').directive('wcHeaderHelp',
        [ '$window', '$rootScope', '$uibModal', 'common.UtilsService', 'common.authorityService', 'moduleConfig' , function($window, $rootScope, $uibModal, UtilsService, authorityService, moduleConfig) {
            'use strict';

            return {
                restrict: 'E',
                scope: {
                    user: '='
                },
                templateUrl: '/web/webjars/common/webcert/components/headers/wcAppHeader/wcHeaderHelp/wcHeaderHelp.directive.html',
                link: function($scope) {

                    var aboutModalInstance;
                    $scope.menu = {
                        expanded: false
                    };

                    $scope.toggleMenu = function($event) {
                        $event.stopPropagation();
                        $scope.menu.expanded = !$scope.menu.expanded;

                    };


                    // Logout ---------------------------------------------------------------
                    $scope.showLogout = function() {
                        return authorityService.isAuthorityActive({
                            authority: 'NAVIGERING'
                        });
                    };

                    $scope.onLogoutClick = function() {
                        if (UtilsService.endsWith($scope.user.authenticationScheme, ':fake')) {
                            $window.location = '/logout';
                        } else {
                            // iid is a global object from /vendor/netid.js
                            iid_Invoke('Logout'); // jshint ignore:line
                            $window.location = '/saml/logout/';
                        }
                    };

                    // About ----------------------------------------------------------------


                    //To make sure we close any open dialog we spawned, register a listener to state changes
                    // so that we can make sure we close them if user navigates using browser back etc.
                    var unregisterFn = $rootScope.$on('$stateChangeStart', function() {
                        if (aboutModalInstance) {
                            aboutModalInstance.close();
                            aboutModalInstance = undefined;
                        }
                    });

                    //Since rootscope event listeners aren't unregistered automatically when this directives
                    //scope is destroyed, let's take care of that.
                    // (currently this directive is used only in the wc-header which lives throughout an entire session, so not that critical right now)
                    $scope.$on('$destroy', unregisterFn);

                    $scope.onAboutClick = function() {
                        aboutModalInstance = $uibModal.open({
                            templateUrl: '/web/webjars/common/webcert/components/headers/wcAppHeader/wcHeaderHelp/about/aboutDialog.template.html',
                            controller: function($scope, $uibModalInstance) {

                                $scope.version = moduleConfig.VERSION;

                                $scope.close = function() {
                                    $uibModalInstance.close();
                                };

                            },
                            resolve: {}
                        });
                    };

                }
            };
        } ]);
