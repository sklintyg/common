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
angular.module('common').directive('miViewIntygErrorMessage',
        [ '$state', 'common.IntygListService', 'common.dialogService', 'common.messageService', function($state, IntygListService, dialogService, messageService) {
            'use strict';

            return {
                restrict: 'E',
                scope: {
                    msgKey: '@',
                    certId: '='
                },
                templateUrl: '/web/webjars/common/minaintyg/components/miViewIntygErrorMessage/miViewIntygErrorMessage.directive.html',
                controller: function($scope) {

                    if ($scope.msgKey === 'error.certarchived') {
                        $scope.showRestoreLink = true;
                    }
                    if($scope.msgKey === 'error.certnotfound') {
                        $scope.msgText = messageService.getProperty('error.certnotfound', {intygsId: $scope.certId});
                    }

                    $scope.restoreAndReload = function() {
                        IntygListService.restoreCertificate({id: $scope.certId}, function(fromServer) {
                            if (fromServer !== null) {
                                $state.reload();
                            } else {
                                // show error view
                                dialogService.showDialog($scope, {
                                    dialogId: 'restore-error-dialog',
                                    titleId: 'error.generictechproblem.title',
                                    bodyText: messageService.getProperty('error.modal.couldnotarchivecert', {intygsId: $scope.certId}),
                                    button1text: 'common.close',
                                    templateUrl: '/web/webjars/common/minaintyg/components/miViewIntygErrorMessage/error-dialog.html',
                                    autoClose: true
                                });
                            }
                        });
                    };
                }
            };
        } ]);
