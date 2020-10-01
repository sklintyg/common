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
angular.module('common').factory('common.IntygMakulera',
    [ '$log', '$rootScope', '$stateParams', 'common.dialogService', 'common.IntygProxy', 'common.UtkastProxy', 'common.ObjectHelper', 'common.IntygCopyRequestModel',
        'common.IntygHelper', 'common.IntygViewStateService', 'common.ArendeListViewStateService', 'common.moduleService', 'common.featureService',
        'common.messageService',
        function($log, $rootScope, $stateParams, dialogService, IntygProxy, UtkastProxy, ObjectHelper, IntygCopyRequestModel, IntygHelper,
            CommonViewState, ArendeListViewStateService, moduleService, featureService,
            messageService) {
            'use strict';

            // Makulera dialog setup
            var makuleraDialog = {
                isOpen: false
            };

            function _revokeSigneratIntyg(intygMakuleraMethod, intyg, dialogModel, makuleraDialog, onSuccess) {

                dialogModel.showerror = false;

                var revokeMessage = {
                    message : '',
                    reason : dialogModel.makuleraModel.reason
                };
                if (dialogModel.makuleraModel.reason) {
                    revokeMessage.message += dialogModel.labels[dialogModel.makuleraModel.reason];
                    if (dialogModel.makuleraModel.clarification[dialogModel.makuleraModel.reason]) {
                        revokeMessage.message += ' ' + dialogModel.makuleraModel.clarification[dialogModel.makuleraModel.reason];
                    }
                }
                revokeMessage.message.trim();

                function onMakuleraComplete() {
                    dialogModel.makuleraProgressDone = true;
                    makuleraDialog.close();
                    $rootScope.$broadcast('intygstatus.updated');
                    onSuccess();
                }

                function onMakuleraFail(error) {
                    $log.debug('Revoke failed: ' + error);
                    dialogModel.makuleraProgressDone = true;
                    dialogModel.ersattProgressDone = true;
                    dialogModel.showerror = true;
                }

                if(intygMakuleraMethod === 'REVOKE') {
                    dialogModel.makuleraProgressDone = false;

                    if (intyg.utkast) {
                        UtkastProxy.makuleraUtkast(intyg.id, intyg.intygType, revokeMessage,
                            onMakuleraComplete, onMakuleraFail);
                    } else {
                        IntygProxy.makuleraIntyg(intyg.id, intyg.intygType, revokeMessage,
                            onMakuleraComplete, onMakuleraFail);
                    }
                }
            }

            function _makulera(intyg, confirmationMessage, onSuccess) {
                function isMakuleraEnabled(model) {
                    return model.makuleraProgressDone && // model.ersattProgressDone &&
                        (
                            model.choices.length === 0 ||
                            (ObjectHelper.isDefined(model.makuleraModel.reason) &&
                                model.makuleraModel.reason !== 'ANNAT_ALLVARLIGT_FEL') ||
                            (model.makuleraModel.reason === 'ANNAT_ALLVARLIGT_FEL' &&
                                !ObjectHelper.isEmpty(model.makuleraModel.clarification[model.makuleraModel.reason]))
                        );
                }

                function getMakuleraText() {
                    var myText;
                    if(intyg.isLocked){
                        myText = '.makulera.locked.body.common-header';
                    } else {
                        myText = '.makulera.body.common-header';
                    }
                    var textId = intyg.intygType + myText;
                    if (!messageService.propertyExists(textId)) {
                        // If intyg hasn't specified a text, fall back to common text
                        textId = 'label' + myText;
                    }
                    return textId;
                }

                function getRadioTitle(recipient){
                    if(recipient === 'common.recipient.'){
                        return messageService.getProperty('label.makulera.no.recipient.title.radio');
                    } else if(intyg.isLocked){
                        return messageService.getProperty('label.makulera.locked.title.radio');
                    } else {
                        var myRecipient = messageService.getProperty(recipient);
                        return messageService.getProperty('label.makulera.title.radio', {recipient: myRecipient});
                    }
                }

                function getAlertMessage(){
                    if(intyg.isLocked){
                        return 'label.makulera.locked.body.common-footer';
                    } else {
                        return 'label.makulera.body.common-footer';
                    }
                }

                function getFelPatientText(){
                    if(intyg.isLocked){
                        return 'Utkastet har skapats på fel patient';
                    } else {
                        return 'Intyget har utfärdats på fel patient';
                    }
                }

                function getTitle(){
                    var myTitle = 'label.makulera';
                    if(intyg.isLocked){
                        return myTitle + '.locked';
                    } else {
                        return myTitle;
                    }
                }

                var recipient = 'common.recipient.' + moduleService.getModule(intyg.intygType).defaultRecipient.toLowerCase();
                var dialogMakuleraModel = {
                    hasUnhandledArenden: ArendeListViewStateService.hasUnhandledItems(),
                    isMakuleraEnabled: isMakuleraEnabled,
                    makuleraProgressDone: true,
                    focus: false,
                    bodyTextId: getMakuleraText(),
                    errormessageid: 'error.failedtomakuleraintyg',
                    showerror: false,
                    alertMessage: getAlertMessage(),

                    isLocked: intyg.isLocked,
                labels: {},
                    choices: [],
                    makuleraModel: {
                        reason: undefined,
                        clarification: []
                    },
                    recipient: recipient,
                    radioTitle: getRadioTitle(recipient)
                };

                if (featureService.isFeatureActive(featureService.features.MAKULERA_INTYG_KRAVER_ANLEDNING, intyg.intygType)) {
                    dialogMakuleraModel.labels = {
                        'FEL_PATIENT': getFelPatientText(),
                        'ANNAT_ALLVARLIGT_FEL': 'Annat allvarligt fel'
                    };
                }

                // Fill dialogMakuleraModel.choices array with choices based on labels
                angular.forEach(dialogMakuleraModel.labels, function(label, key) {

                    this.push({
                        label: label,
                        value: key,
                        textAreaLabel: key === 'FEL_PATIENT' ? 'Förtydliga vid behov' : 'Ange orsaken till felet.',
                        required: key !== 'FEL_PATIENT'
                    });
                }, dialogMakuleraModel.choices);

                makuleraDialog = dialogService.showDialog({
                    dialogId: 'makulera-dialog',
                    titleId: getTitle(),
                    templateUrl: '/web/webjars/common/webcert/intyg/intygMakulera.dialog.html',
                    model: dialogMakuleraModel,
                    button1click: function() {
                        $log.debug('revoking intyg from dialog' + intyg);
                        _revokeSigneratIntyg('REVOKE', intyg, dialogMakuleraModel, makuleraDialog, onSuccess);
                    },
                    button1text: 'common.makulera',
                    button1id: 'button1makulera-dialog',
                    button3text: 'common.cancel',
                    button3id: 'button3makulera-dialog',
                    autoClose: false
                });

                return makuleraDialog;
            }

            // Return public API for the service
            return {
                makulera: _makulera
            };
        }]);
