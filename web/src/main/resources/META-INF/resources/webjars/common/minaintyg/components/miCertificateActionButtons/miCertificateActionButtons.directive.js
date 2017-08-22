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

angular.module('common').directive('miCertificateActionButtons',
        [ '$log', '$state', 'common.messageService', 'common.IntygListService', 'common.dialogService', function($log, $state, messageService, listCertService, dialogService) {
            'use strict';

            return {
                restrict: 'E',
                scope: {
                    certModel: '=', // certificate model
                    onSend: '&?', // handler for send button. if attribute not present = not enabled
                    enableArchive: '=', // handler for send button. if attribute not present = not enabled
                    enablePrint: '=', // true/false to show print as pdf button
                    onCustomizePdf: '&?'  // handler for customize pdf  button. if attribute not present = not enabled
                },
                templateUrl: '/web/webjars/common/minaintyg/components/miCertificateActionButtons/miCertificateActionButtons.directive.html',
                controller: function($scope) {
                    //expose messageservice so template can present messages
                    $scope.messageService = messageService;

                    //pdf download link requires the certificate to be present
                    $scope.buildPdfLink = function() {
                        if ($scope.certModel) {
                            return '/moduleapi/certificate/' + $scope.certModel.typ + '/' + $scope.certModel.id + '/pdf';
                        } else {
                            return undefined;
                        }
                    };



                    var _dialogInstance;

                    $scope.archiveSelected = function() {
                        var item = $scope.certModel;

                        $log.debug('requesting archiving of certificate ' + item.id);
                        $scope.dialog.acceptprogressdone = false;
                        listCertService.archiveCertificate(item, function(fromServer, oldItem) {
                            $log.debug('archive request callback:' + fromServer);
                            if (fromServer !== null) {
                                $scope.dialog.acceptprogressdone = true;
                                _dialogInstance.close();
                                //Goto inkorgen
                                $state.go('inkorg');
                            } else {
                                // show error view
                                $state.go('fel', {errorCode: 'couldnotarchivecert'});
                            }
                        });
                    };

                    // Archive dialog
                    $scope.onArchiveClicked = function() {
                        _dialogInstance = dialogService.showDialog($scope, {
                            dialogId: 'archive-confirmation-dialog',
                            titleId: 'modules.actionbar.archivedialog.title',
                            bodyTextId: 'modules.actionbar.archivedialog.body',
                            button1click: function() {
                                $log.debug('archive confirmed by user');
                                $scope.archiveSelected();
                            },
                            button1id: 'archive-button',
                            button1text: 'modules.actionbar.archivedialog.archive.button',
                            button1icon: 'icon-box',
                            button1tooltip: messageService.getProperty('button.modal.archive.tooltip'),
                            autoClose: false
                        });
                    };

                }
            };
        } ]);
