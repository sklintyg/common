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

angular.module('common').directive('wcVardPersonSekretess',
        [ '$rootScope', '$uibModal', 'common.UserModel', function($rootScope, $uibModal, UserModel) {
            'use strict';

            return {
                restrict: 'E',
                scope: {},
                templateUrl: '/web/webjars/common/webcert/gui/wcVardPersonSekretess/wcVardPersonSekretess.directive.html',
                link: function($scope) {

                    var _SEKRETESSWARNING_APPROVED = 'wc.vardperson.sekretess.approved';

                    var infoDialogInstance, confirmDialogInstance;

                    $scope.user = UserModel.user;

                    //The confirmation dialog is only displayed if sekretess and not already given consent.
                    var _showSekretessConfirmationDialog = function() {
                        confirmDialogInstance = $uibModal.open({
                            templateUrl: '/web/webjars/common/webcert/gui/wcVardPersonSekretess/wcVardPersonSekretessDialog.html',
                            controller: 'wcVardPersonSekretessDialogCtrl',
                            size: 'md',
                            id: 'SekretessConsentDialog',
                            backdrop: 'static',
                            keyboard: false,
                            resolve: {
                                preferenceKey: function() {
                                    return _SEKRETESSWARNING_APPROVED;
                                }
                            }
                        });
                    };

                    //To make sure we close any open dialog we spawned, register a listener to state changes
                    // so that we can make sure we close them
                    var unregisterFn = $rootScope.$on('$stateChangeStart', function() {
                        if (infoDialogInstance) {
                            infoDialogInstance.close();
                            infoDialogInstance = undefined;
                        }
                    });

                    //Since rootscope event listeners aren't unregistered automatically when this directives
                    //scope is destroyed, let's take care of that.
                    // (currently this directive is used only in the wc-header which lives throughout an entire session, so not that critical right now)
                    $scope.$on('$destroy', unregisterFn);

                    //The info dialog is triggered by the users themselves via link in the template
                    $scope.showSekretessInfoMessage = function() {
                        infoDialogInstance = $uibModal.open({
                            templateUrl: '/web/webjars/common/webcert/gui/wcVardPersonSekretess/wcVardPersonSekretess.infodialog.html',
                            size: 'md',
                            id: 'SekretessInfoMessage'
                        });
                    };


                    //Should we launch the approval dialog?
                    if (UserModel.user.sekretessMarkerad && UserModel.getAnvandarPreference(_SEKRETESSWARNING_APPROVED) === undefined) {
                        _showSekretessConfirmationDialog();
                    }

                }

            };
        } ]);
