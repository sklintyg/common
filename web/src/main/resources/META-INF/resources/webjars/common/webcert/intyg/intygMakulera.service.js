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
    [ '$log', '$stateParams', 'common.dialogService', 'common.IntygProxy', 'common.ObjectHelper', 'common.IntygCopyRequestModel', 'common.IntygHelper',
        'common.IntygViewStateService', 'common.ArendeListViewStateService',
        function($log, $stateParams, dialogService, IntygProxy, ObjectHelper, IntygCopyRequestModel, IntygHelper, CommonViewState, ArendeListViewStateService) {
            'use strict';

            // Makulera dialog setup
            var makuleraDialog = {
                isOpen: false
            };

            function _revokeSigneratIntyg(intygMakuleraMethod, intyg, dialogModel, makuleraDialog, onSuccess) {

                dialogModel.showerror = false;

                var revokeMessage = {
                    message : dialogModel.labels[dialogModel.makuleraModel.reason] + '. ' + dialogModel.makuleraModel.clarification[dialogModel.makuleraModel.reason],
                    reason : dialogModel.makuleraModel.reason
                };
                revokeMessage.message.trim();

                function onMakuleraComplete() {
                    dialogModel.makuleraProgressDone = true;
                    makuleraDialog.close();
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
                    IntygProxy.makuleraIntyg(intyg.id, intyg.intygType, revokeMessage,
                        onMakuleraComplete, onMakuleraFail);
                }
            }

            function _makulera(intyg, confirmationMessage, onSuccess) {
                // Only show tooltip for FK-intyg
                var isFkIntyg = CommonViewState.defaultRecipient === 'FK' ? true : false;

                function isMakuleraEnabled(model) {
                    return model.makuleraProgressDone && // model.ersattProgressDone &&
                        (
                            (ObjectHelper.isDefined(model.makuleraModel.reason) &&
                                model.makuleraModel.reason !== 'ANNAT_ALLVARLIGT_FEL') ||
                            (model.makuleraModel.reason === 'ANNAT_ALLVARLIGT_FEL' &&
                                !ObjectHelper.isEmpty(model.makuleraModel.clarification[model.makuleraModel.reason]))
                        );
                }

                var dialogMakuleraModel = {
                    isFkIntyg: isFkIntyg,
                    hasUnhandledArenden: ArendeListViewStateService.hasUnhandledItems(),
                    isMakuleraEnabled: isMakuleraEnabled,
                    makuleraProgressDone: true,
                    focus: false,
                    errormessageid: 'error.failedtomakuleraintyg',
                    showerror: false,
                    labels: {
                        'FEL_PATIENT': 'Intyget har utfärdats på fel patient.',
                        'ANNAT_ALLVARLIGT_FEL': 'Annat allvarligt fel.'
                    },
                    choices: [],
                    makuleraModel: {
                        reason: undefined,
                        clarification: []
                    }
                };

                // Fill dialogMakuleraModel.choices array with choices based on labels
                angular.forEach(dialogMakuleraModel.labels, function(label, key) {

                    this.push({
                        label: label,
                        value: key,
                        placeholder: key === 'FEL_PATIENT' ? 'Förtydliga vid behov...' : 'Ange orsak (obligatoriskt)...'
                    });
                }, dialogMakuleraModel.choices);

                makuleraDialog = dialogService.showDialog({
                    dialogId: 'makulera-dialog',
                    titleId: 'label.makulera',
                    templateUrl: '/web/webjars/common/webcert/intyg/intygMakulera.dialog.html',
                    model: dialogMakuleraModel,
                    button1click: function() {
                        $log.debug('revoking intyg from dialog' + intyg);
                        _revokeSigneratIntyg('REVOKE', intyg, dialogMakuleraModel, makuleraDialog, onSuccess);
                    },
                    button1text: 'common.revoke',
                    button1id: 'button1makulera-dialog',
                    button3text: 'common.canceldontrevoke',
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
