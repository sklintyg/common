/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
    ['$window', '$rootScope', '$uibModal', 'common.UtilsService', 'common.authorityService', 'common.AvtalProxy', 'moduleConfig',
      function($window, $rootScope, $uibModal, UtilsService, authorityService, avtalProxy, moduleConfig) {
        'use strict';

        return {
          restrict: 'E',
          scope: {
            user: '='
          },
          templateUrl: '/web/webjars/common/webcert/components/headers/wcAppHeader/wcHeaderHelp/wcHeaderHelp.directive.html',
          link: function($scope) {

            var aboutModalInstance;
            var aboutModalInstanceOpened;

            $scope.vm = {
              showAbout: angular.isObject($scope.user),
              showCreateAccount: !angular.isObject($scope.user),
              showLogout: angular.isObject($scope.user) && authorityService.isAuthorityActive({
                authority: 'NAVIGERING'
              }),
              expanded: false
            };

            $scope.toggleMenu = function($event) {
              $event.stopPropagation();
              $scope.vm.expanded = !$scope.vm.expanded;

            };

            $scope.onCreateAccountClick = function() {
              $window.location.href = moduleConfig.PP_HOST;
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
                aboutModalInstanceOpened = false;
              }
            });

            //Since rootscope event listeners aren't unregistered automatically when this directives
            //scope is destroyed, let's take care of that.
            // (currently this directive is used only in the wc-header which lives throughout an entire session, so not that critical right now)
            $scope.$on('$destroy', unregisterFn);

            $scope.onAboutClick = function() {
              if (!aboutModalInstanceOpened) {
                aboutModalInstanceOpened = true;
                aboutModalInstance = $uibModal.open({
                  templateUrl: '/web/webjars/common/webcert/components/headers/wcAppHeader/wcHeaderHelp/about/aboutDialog.template.html',
                  size: 'lg',
                  controller: function($scope, $uibModalInstance, user, avtalProxy) {

                    $scope.version = moduleConfig.VERSION;
                    $scope.user = user;

                    avtalProxy.getLatestAvtal(function(avtal) {
                      $scope.avtal = avtal;
                    });

                    $scope.close = function() {
                      $uibModalInstance.close();
                    };

                    $scope.open = {
                      om: false,
                      terms: false,
                      support: false,
                      faq: false,
                      kakor: false,
                      personuppgifter: false
                    };
                  },
                  resolve: {
                    avtalProxy: avtalProxy,
                    user: $scope.user
                  }
                });
                //angular > 1.5 warns if promise rejection is not handled (e.g backdrop-click == rejection)
                aboutModalInstance.result.then(function() {
                  aboutModalInstanceOpened = false;
                }, function() {
                  aboutModalInstanceOpened = false;
                });
              }
            };

          }
        };
      }]);
