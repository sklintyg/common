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

angular.module('common').factory('common.IntygSend',
    [ '$log', 'common.dialogService', 'common.IntygProxy',
        function($log, dialogService, IntygProxy) {
            'use strict';

            // Send dialog setup
            var sendDialog = {
                isOpen: false
            };

            function _sendSigneratIntyg(intygsId, intygsTyp, recipientId, patientConsent, dialogModel, sendDialog, onSuccess) {
                dialogModel.showerror = false;
                dialogModel.acceptprogressdone = false;
                IntygProxy.sendIntyg(intygsId, intygsTyp, recipientId, patientConsent, function(status) {
                    dialogModel.acceptprogressdone = true;
                    sendDialog.close();
                    onSuccess(status);
                }, function(error) {
                    $log.debug('Send intyg failed: ' + error);
                    dialogModel.acceptprogressdone = true;
                    dialogModel.showerror = true;
                });
            }

            function _send(intygId, intygType, recipientId, titleId, bodyTextId, onSuccess) {

                var dialogSendModel ={
                    acceptprogressdone: true,
                    focus: false,
                    errormessageid: 'error.failedtosendintyg',
                    showerror: false,
                    patientConsent: false
                };

                sendDialog = dialogService.showDialog({
                    dialogId: 'send-dialog',
                    titleId: titleId,
                    bodyTextId: bodyTextId,
                    templateUrl: '/web/webjars/common/webcert/intyg/intygSend.dialog.html',
                    model: dialogSendModel,
                    button1click: function() {
                        $log.debug('send intyg from dialog. id:' + intygId + ', intygType:' + intygType + ', recipientId:' + recipientId);
                        _sendSigneratIntyg(intygId, intygType, recipientId, dialogSendModel.patientConsent, dialogSendModel,
                            sendDialog, onSuccess);
                    },
                    button1text: 'common.send',
                    button1id: 'button1send-dialog',
                    button2text: 'common.cancel',
                    autoClose: false
                });

                sendDialog.opened.then(function() {
                    sendDialog.isOpen = true;
                }, function() {
                    sendDialog.isOpen = false;
                });

                return sendDialog;
            }

            // Return public API for the service
            return {
                send: _send
            };
        }]);
