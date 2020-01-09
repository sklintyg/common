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
angular.module('common').controller('wcVardPersonSekretessDialogCtrl',
    [ '$scope', '$uibModalInstance', '$window', 'common.User', 'common.UserModel', 'preferenceKey',
            function($scope, $uibModalInstance, $window, UserService, UserModel, preferenceKey) {
                'use strict';

                var closeAllowed = false;

                $scope.vm = {
                    approveChecked:  false
                };

                $scope.giveConsent = function() {

                    if (!$scope.vm.approveChecked) {
                        return;
                    }
                    //storeAnvandarPreference will update UserModel AnvandarPreference
                    UserService.storeAnvandarPreference(preferenceKey, true);

                    //close dialog
                    closeAllowed = true;
                    $uibModalInstance.close();

                };

                $scope.check = function() {
                    $scope.vm.approveChecked = !$scope.vm.approveChecked;
                };

                $scope.onCancel = function() {
                    $uibModalInstance.close();
                    $window.location.href = '/error.jsp?reason=sekretessapproval.needed' + (UserModel.isNormalOrigin() ? '&showlogin=true' : '');
                };

                $scope.isVardAdministrator = function() {
                    return UserModel.isVardAdministrator();
                };

                $scope.$on('modal.closing', function(event) {
                    if (!closeAllowed) {
                        event.preventDefault();
                    }
                });
            } ]);
