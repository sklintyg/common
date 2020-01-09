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
 Note: This directive is not rendered unless a valid userModel is available, so all access to $scope.userModel can skips such checks.
 */
angular.module('common').directive('wcHeaderUser', ['$rootScope', '$uibModal', '$window', '$location', 'common.User', 'common.UserModel', 'moduleConfig',
    function($rootScope, $uibModal, $window, $location, UserService, UserModel, moduleConfig) {
    'use strict';

    return {
        restrict: 'E',
        scope: {},
        templateUrl: '/web/webjars/common/webcert/components/headers/wcAppHeader/wcHeaderUser/wcHeaderUser.directive.html',
        link: function($scope) {

            $scope.user = UserService.getUser();
            $scope.menu = {
                enabled: UserModel.isPrivatLakare(),
                expanded: false
            };


            $scope.toggleMenu = function($event) {
                $event.stopPropagation();
                $scope.menu.expanded = !$scope.menu.expanded;

            };

            $scope.goToPrivatPortalen = function(){
                var link = moduleConfig.PP_HOST;
                link += '?from=' + window.encodeURIComponent(moduleConfig.DASHBOARD_URL + '#' + $location.path());
                $window.location.href = link;
            };


            // SekretessInfo dialog handling ---------------------------------
            var infoDialogInstance;
            var infoDialogInstanceOpened;
            //The info dialog is triggered by the users themselves via link in the template
            $scope.showSekretessInfoMessage = function() {
                if (!infoDialogInstanceOpened) {
                    infoDialogInstanceOpened = true;
                    var isVardAdministrator = UserModel.isVardAdministrator();
                    infoDialogInstance = $uibModal.open({
                        templateUrl: '/web/webjars/common/webcert/components/headers/wcAppHeader/wcHeaderUser/vardperson-sekretess.infodialog.html',
                        size: 'md',
                        id: 'SekretessInfoMessage',
                        controller: function($scope, $uibModalInstance) {
                            $scope.isVardAdministrator = isVardAdministrator;
                        }
                    });
                    //angular > 1.5 warns if promise rejection is not handled (e.g backdrop-click == rejection)
                    infoDialogInstance.result.then(function () {
                        infoDialogInstanceOpened = false;
                    }, function () {
                        infoDialogInstanceOpened = false;
                    });
                }
            };

            //To make sure we close any open dialog we spawned, register a listener to state changes
            // so that we can make sure we close them

            var unregisterFn = $rootScope.$on('$stateChangeStart', function() {
                if (infoDialogInstance) {
                    infoDialogInstance.close();
                    infoDialogInstance = undefined;
                    infoDialogInstanceOpened = false;
                }
            });

            //Since rootscope event listeners aren't unregistered automatically when this directives
            //scope is destroyed, let's take care of that.
            // (currently this directive is used only in the wc-header which lives throughout an entire session, so not that critical right now)
            $scope.$on('$destroy', unregisterFn);

        }
    };
}]);
