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
angular.module('common').factory('common.IntygSend',
    [ '$log', 'common.dialogService', 'common.IntygProxy',
        function($log, dialogService, IntygProxy) {
            'use strict';

            // Send dialog setup
            var sendDialog = {
                isOpen: false
            };

            function _sendSigneratIntyg(intygsId, intygsTyp, recipientId, dialogModel, sendDialog, onSuccess) {
                dialogModel.showerror = false;
                dialogModel.acceptprogressdone = false;
                IntygProxy.sendIntyg(intygsId, intygsTyp, recipientId, function(status) {
                    dialogModel.acceptprogressdone = true;
                    sendDialog.close();
                    onSuccess(status);
                }, function(error) {
                    $log.debug('Send intyg failed: ' + error);
                    dialogModel.acceptprogressdone = true;
                    dialogModel.showerror = true;
                });
            }

            function isObservandumOccupation (occupationList) {
                occupationList.forEach(function (occupation) {
                    if (occupation.typ === 'ARBETSSOKANDE') {
                        return true;
                    } else if (occupation.typ === 'STUDIER') {
                        occupationList.forEach(function (occupation) {
                            if (occupation.typ  === 'NUVARANDE_ARBETE') {
                                return true;
                            }
                        })

                    }
                })
               return false;
            }

            /**
             * Is an observandum if it is of type LIJSP, duration shorter or equal to 7 days
             * and has the occupation arbetssökande or studier AND nuvarande arbete.
             */

            function isObservandum(intygModel) {

                var duration;

                if (intygModel.typ === 'lisjp') {
                    intygModel.sjukskrivningar.forEach(function(sjukskrivning) {
                        var startDate = new moment (sjukskrivning.period.from.split('-'));
                        var endDate = new moment (sjukskrivning.period.tom.split('-'));
                        duration  = moment.duration(endDate.diff(startDate));
                        duration = duration.days() + 1;

                        if (duration <= 7 && isObservandumOccupation(intygModel.sysselsattning)) {
                            return true;
                        }
                    })
                }
                return false;
            }
            
            function _send(intygModel, intygId, intygType, recipientId, titleId, bodyTextId, bodyText, onSuccess) {
             
                var dialogSendModel ={
                    acceptprogressdone: true,
                    focus: false,
                    errormessageid: 'error.failedtosendintyg',
                    showerror: false,
                    patientConsent: false,
                    showObservandum:  isObservandum(intygModel)
                };

                sendDialog = dialogService.showDialog({
                    dialogId: 'send-dialog',
                    titleId: titleId,
                    bodyTextId: bodyTextId,
                    bodyText: bodyText,
                    templateUrl: '/web/webjars/common/webcert/intyg/intygSend.dialog.html',
                    model: dialogSendModel,
                    button1click: function() {
                        $log.debug('send intyg from dialog. id:' + intygId + ', intygType:' + intygType + ', recipientId:' + recipientId);
                        _sendSigneratIntyg(intygId, intygType, recipientId, dialogSendModel,
                            sendDialog, onSuccess);
                    },
                    button2click: function(modalInstance){
                        sendDialog.close();
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
