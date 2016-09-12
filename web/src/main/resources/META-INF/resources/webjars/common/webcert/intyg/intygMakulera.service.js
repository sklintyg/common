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

angular.module('common').factory('common.IntygMakulera',
    [ '$log', 'common.dialogService', 'common.IntygProxy', 'common.ObjectHelper',
        function($log, dialogService, IntygProxy, ObjectHelper) {
            'use strict';

            // Makulera dialog setup
            var makuleraDialog = {
                isOpen: false
            };

            function _revokeSigneratIntyg(intygMakuleraMethod, intyg, dialogModel, makuleraDialog, onSuccess) {
                dialogModel.showerror = false;
                dialogModel.acceptprogressdone = false;

                var revokeMessage = dialogModel.labels[dialogModel.makuleraModel.reason] + '. ' + dialogModel.makuleraModel.clarification;
                revokeMessage.trim();

                intygMakuleraMethod(intyg.id, intyg.intygType, revokeMessage, function() {
                    dialogModel.acceptprogressdone = true;
                    makuleraDialog.close();
                    onSuccess();
                }, function(error) {
                    $log.debug('Revoke failed: ' + error);
                    dialogModel.acceptprogressdone = true;
                    dialogModel.showerror = true;
                });
            }

            function _makulera( intyg, confirmationMessage, onSuccess) {

                function isMakuleraEnabled(model) {
                    return model.acceptprogressdone &&
                        (
                            (ObjectHelper.isDefined(model.makuleraModel.reason) &&
                                model.makuleraModel.reason !== 'OVRIGT') ||
                            (model.makuleraModel.reason === 'OVRIGT' &&
                                !ObjectHelper.isEmpty(model.makuleraModel.clarification))
                        );
                }

                var dialogMakuleraModel = {
                    isMakuleraEnabled: isMakuleraEnabled,
                    acceptprogressdone: true,
                    focus: false,
                    errormessageid: 'error.failedtomakuleraintyg',
                    showerror: false,
                    labels: {
                        'FELAKTIGT_INTYG': 'Intyget har fyllts i felaktigt',
                        'PATIENT_NY_INFO': 'Patienten har kommit med ny information som behöver tillföras',
                        'MIN_BEDOMNING_ANDRAD': 'Min bedömning i intyget har ändrats',
                        'OVRIGT': 'Övrigt'
                    },
                    choices: [],
                    makuleraModel: {
                        reason: undefined,
                        clarification: ''
                    }
                };

                angular.forEach(dialogMakuleraModel.labels, function(label, key) {
                    if(key === 'OVRIGT'){
                        this.push({
                            label: label,
                            value: key,
                            placeholder: 'Ange orsak (obligatoriskt)...'
                        });
                    }
                    else{
                        this.push({
                            label: label,
                            value: key,
                            placeholder: 'Förtydliga vid behov...'
                        });
                    }
                }, dialogMakuleraModel.choices);

                makuleraDialog = dialogService.showDialog({
                    dialogId: 'makulera-dialog',
                    titleId: 'label.makulera',
                    templateUrl: '/web/webjars/common/webcert/intyg/intygMakulera.dialog.html',
                    model: dialogMakuleraModel,
                    button1click: function() {
                        $log.debug('revoking intyg from dialog' + intyg);
                        _revokeSigneratIntyg(IntygProxy.makuleraIntyg, intyg, dialogMakuleraModel, makuleraDialog, onSuccess);
                    },
                    button2click: function() {
                        $log.debug('revoking and replacing intyg from dialog' + intyg);
                        _revokeSigneratIntyg(IntygProxy.makuleraErsattIntyg, intyg, dialogMakuleraModel, makuleraDialog, onSuccess);
                    },

                    button1text: 'common.revoke',
                    button1id: 'button1makulera-dialog',
                    button2text: 'common.revokeandreplace',
                    button2id: 'button2makulera-dialog',
                    button3text: 'common.canceldontrevoke',
                    button3id: 'button3makulera-dialog',
                    bodyTextId: 'label.makulera.body',
                    autoClose: false
                });

                return makuleraDialog;
            }

            // Return public API for the service
            return {
                makulera: _makulera
            };
        }]);
