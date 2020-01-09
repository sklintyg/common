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
angular.module('common').factory('common.IntygCopyActions',
    [ '$log', '$stateParams', '$state', 'common.messageService',
        'common.dialogService', 'common.IntygProxy', 'common.authorityService', 'common.UserModel', 'common.User', 'common.IntygHelper', 'common.PersonIdValidatorService',
        function($log, $stateParams, $state, messageService,
            dialogService, IntygProxy, authorityService, UserModel, userService, IntygHelper, PersonIdValidatorService) {
            'use strict';

            var _FORNYA_DIALOG_PREFERENCE = 'wc.dontShowFornyaDialog';
            var baseDialogModel = {
                isOpen: false,
                dontShowInfo: null,
                otherCareUnit: null,
                patientId: null,
                deepIntegration: null,
                intygTyp: null,
                showerror: null,
                acceptprogressdone: null,
                errormessageid: 'error.failedtocopyintyg'
            };
            var fornyaDialogModel = angular.copy(baseDialogModel);
            fornyaDialogModel.errormessageid = 'error.failedtofornyaintyg';

            var ersattDialogModel = angular.copy(baseDialogModel);
            ersattDialogModel.errormessageid = 'error.failedtoersattintyg';

            var createFromTemplateDialogModel = angular.copy(baseDialogModel);
            createFromTemplateDialogModel.errormessageid = 'error.failedtocreateintyg';

            function resetViewStateErrorKeys (viewState) {
                //The copy actions service is used both in an viewIntyg context as well as in patient list view.
                //Their viewStates are actually 2 different structures. We should look at refactoring this..
                if (viewState.common) {
                    viewState.common.activeErrorMessageKey = null;
                    viewState.common.inlineErrorMessageKey = null;
                } else {
                    viewState.activeErrorMessageKey = null;
                    viewState.inlineErrorMessageKey = null;
                }
            }

            function hideFornyaDialogError () {
                fornyaDialogModel.showerror = null;
            }

            function hideErsattDialogError () {
                ersattDialogModel.showerror = null;
            }

            function hideCreateFromTemplateDialogError () {
                createFromTemplateDialogModel.showerror = null;
            }

            function dialogButton1Click (options) {
                var requestType = options.requestType;
                var requestData = options.requestData;
                var requestFn = options.requestFn;
                var closeDialog = options.closeDialog;
                var dialogModel = options.dialogModel;
                var dialogPreferenceKey = options.dialogPreferenceKey;

                $log.debug(requestType + ' intyg from dialog' + requestData);

                // Can't check directly on dialogModel.dontShowInfo, it may have false as its value...
                if (dialogPreferenceKey && (typeof dialogModel.dontShowInfo !== undefined) && dialogModel.dontShowInfo !== null) {
                    userService.storeAnvandarPreference(dialogPreferenceKey, dialogModel.dontShowInfo);
                }


                dialogModel.showerror = false;
                dialogModel.acceptprogressdone = false;
                requestFn(requestData, function(draftResponse) {
                    dialogModel.acceptprogressdone = true;
                    //hide any previously shown error (nothing more needed, as we are about to close the dialog anyway)
                    dialogModel.showerror = false;

                    var end = function() {
                        IntygHelper.goToDraft(draftResponse.intygsTyp, draftResponse.intygTypeVersion, draftResponse.intygsUtkastId);
                    };

                    closeDialog({direct:end});

                }, function(errorCode) {
                    if (errorCode === 'DATA_NOT_FOUND') {
                        dialogModel.errormessageid = 'error.failedto' + requestType + 'intyg.personidnotfound';
                    } else if (errorCode === 'INVALID_STATE_REPLACED') {
                        dialogModel.errormessageid = 'error.failedto' + requestType + 'intyg.replaced';
                    } else if (errorCode === 'COMPLEMENT_INTYG_EXISTS') {
                        dialogModel.errormessageid = 'error.failedto' + requestType + 'intyg.complemented';
                    } else if (errorCode === 'PU_PROBLEM') {
                        dialogModel.errormessageid = 'error.pu_problem';
                    } else {
                        dialogModel.errormessageid = 'error.failedto' + requestType + 'intyg';
                    }
                    dialogModel.acceptprogressdone = true;
                    dialogModel.showerror = true;
                });
            }

            function _fornyaMessageKey(intygsTyp) {
                return intygsTyp.toLowerCase() + '.modal.fornya.text';
            }

            function _fornya(viewState, intygFornyaRequest, isOtherCareUnit) {
                var fornyaDialog;
                // Create preference and model representative
                fornyaDialogModel.dontShowInfo = false;

                if (UserModel.getAnvandarPreference(_FORNYA_DIALOG_PREFERENCE) === undefined) {
                    UserModel.setAnvandarPreference(_FORNYA_DIALOG_PREFERENCE, fornyaDialogModel.dontShowInfo);
                }

                if (UserModel.getAnvandarPreference(_FORNYA_DIALOG_PREFERENCE) === true || UserModel.getAnvandarPreference(_FORNYA_DIALOG_PREFERENCE) === 'true') {
                    $log.debug('fornya intyg without dialog' + intygFornyaRequest);
                    resetViewStateErrorKeys(viewState);
                    _createFornyaDraft(intygFornyaRequest, function(draftResponse) {
                        IntygHelper.goToDraft(draftResponse.intygsTyp, draftResponse.intygTypeVersion, draftResponse.intygsUtkastId);
                    }, function(errorCode) {
                        var errorMessageKey = 'error.failedtofornyaintyg';
                        if (errorCode === 'DATA_NOT_FOUND') {
                            errorMessageKey = 'error.failedtofornyaintyg.personidnotfound';
                        } else if (errorCode === 'INVALID_STATE_REPLACED') {
                            errorMessageKey = 'error.failedtofornyaintyg.replaced';
                        } else if (errorCode === 'PU_PROBLEM') {
                            errorMessageKey = 'error.pu_problem';
                        }
                        dialogService.showErrorMessageDialog(messageService.getProperty(errorMessageKey));
                    });
                } else {

                    fornyaDialogModel.otherCareUnit = isOtherCareUnit;
                    fornyaDialogModel.patientId = PersonIdValidatorService.validate(UserModel.getIntegrationParam('alternateSsn'));
                    fornyaDialogModel.deepIntegration = !authorityService.isAuthorityActive({authority: 'HANTERA_PERSONUPPGIFTER'});
                    fornyaDialogModel.intygTyp = intygFornyaRequest.intygType;

                    fornyaDialog = dialogService.showDialog({
                        dialogId: 'fornya-dialog',
                        titleId: 'label.fornyaintyg',
                        templateUrl: '/app/partials/fornya-dialog.html',
                        model: fornyaDialogModel,
                        button1click: function () {
                            dialogButton1Click({
                                requestType: 'fornya',
                                requestData: intygFornyaRequest,
                                requestFn: _createFornyaDraft,
                                viewState: viewState,
                                dialogModel: fornyaDialogModel,
                                dialogPreferenceKey: _FORNYA_DIALOG_PREFERENCE,
                                closeDialog: function (result) {
                                    fornyaDialog.close(result);
                                }
                            });
                        },
                        button2click: function(modalInstance){
                            modalInstance.close();
                        },
                        button1text: 'common.fornya',
                        button2text: 'common.cancel',
                        bodyText: _fornyaMessageKey(fornyaDialogModel.intygTyp),
                        autoClose: false
                    });

                    fornyaDialog.opened.then(function() {
                        fornyaDialogModel.isOpen = true;
                    }, function() {
                        fornyaDialogModel.isOpen = false;
                    });

                    fornyaDialog.result.then(
                        hideFornyaDialogError,
                        hideFornyaDialogError
                    );

                    return fornyaDialog;
                }

                return null;
            }

            function _ersatt(viewState, intygErsattRequest, isOtherCareUnit) {

                ersattDialogModel.otherCareUnit = isOtherCareUnit;
                ersattDialogModel.patientId = PersonIdValidatorService.validate($stateParams.patientId);
                ersattDialogModel.deepIntegration = !authorityService.isAuthorityActive({authority: 'HANTERA_PERSONUPPGIFTER'});
                ersattDialogModel.intygTyp = intygErsattRequest.intygType;
                ersattDialogModel.ersattningsUtkastFinns = !!viewState.common.intygProperties.latestChildRelations.replacedByUtkast;

                var ersattDialogText = 'intyg.modal.ersatt.utkast.finns.text';
                var infoMessageKey = 'intyg.modal.ersatt.utkast.finns.text.info';

                if (!ersattDialogModel.ersattningsUtkastFinns &&
                    (ersattDialogModel.intygTyp === 'db' || ersattDialogModel.intygTyp === 'doi')) {
                    ersattDialogText = ersattDialogModel.intygTyp + '.modal.ersatt.text';
                    infoMessageKey = ersattDialogModel.intygTyp + '.modal.ersatt.text.info';
                }


                ersattDialogModel.infoMessage = undefined;

                if (messageService.propertyExists(infoMessageKey)) {
                    ersattDialogModel.infoMessage = infoMessageKey;
                }

               var ersattDialog = dialogService.showDialog({
                        dialogId: 'ersatt-dialog',
                        titleId: 'label.ersattintyg',
                        templateUrl: '/app/partials/ersatt-dialog.html',
                        model: ersattDialogModel,
                        button1click: function () {
                            dialogButton1Click({
                                requestType: 'ersatt',
                                requestData: intygErsattRequest,
                                requestFn: _createErsattDraft,
                                viewState: viewState,
                                dialogModel: ersattDialogModel,
                                closeDialog: function (result) {
                                    ersattDialog.close(result);
                                }
                            });
                        },
                        button2click: function (modalInstance) {
                            modalInstance.close();
                            IntygHelper.goToDraft(viewState.intygModel.typ, viewState.intygModel.textVersion, viewState.common.intygProperties.latestChildRelations.replacedByUtkast.intygsId);
                        },
                        button3click: function(modalInstance){
                            modalInstance.close();
                        },
                        button1text: 'common.ersatt',
                        button2text: 'common.ersatt.resume',
                        button3text: 'common.ersatt.cancel',
                        bodyText: ersattDialogText,
                        autoClose: false
                    });

                ersattDialog.opened.then(function() {
                    ersattDialog.isOpen = true;
                    }, function() {
                    ersattDialog.isOpen = false;
                    });

                ersattDialog.result.then(
                        hideErsattDialogError,
                        hideErsattDialogError
                    );

                return ersattDialog;

            }

            function _createFromTemplate(viewState, intygCreateFromTemplateRequest, isOtherCareUnit, previousIntyg) {

                var newIntygType = intygCreateFromTemplateRequest.newIntygType;

                createFromTemplateDialogModel.infoMessage = undefined;
                createFromTemplateDialogModel.notSentInfoMessageKey = undefined;

                if (previousIntyg !== undefined && previousIntyg[newIntygType] && !previousIntyg[newIntygType].sameVardgivare) {
                    var infoMessageKey = intygCreateFromTemplateRequest.intygType + '.createfromtemplate.' + newIntygType + '.modal.text.info';

                    if (messageService.propertyExists(infoMessageKey)) {
                        createFromTemplateDialogModel.infoMessage = infoMessageKey;
                    }
                }
                if (!viewState.common.isSentIntyg()) {
                    var notSentInfoMessageKey = intygCreateFromTemplateRequest.intygType + '.createfromtemplate.' + newIntygType + '.modal.text.info.notsent';
                    if (messageService.propertyExists(notSentInfoMessageKey)) {
                        createFromTemplateDialogModel.notSentInfoMessageKey = notSentInfoMessageKey;
                    }
                }

                var confirmCreateButtonMessageKey = intygCreateFromTemplateRequest.intygType + '.createfromtemplate.' + newIntygType + '.modal.button.continue';
                if (!messageService.propertyExists(confirmCreateButtonMessageKey)) {
                    confirmCreateButtonMessageKey = 'common.createfromtemplate.continue';
                }
                createFromTemplateDialogModel.confirmCreateButtonMessageKey = confirmCreateButtonMessageKey;

                var createDialog = dialogService.showDialog({
                    dialogId: 'ersatt-dialog',
                    titleId: intygCreateFromTemplateRequest.intygType + '.createfromtemplate.' + newIntygType + '.modal.header',
                    templateUrl: '/app/partials/createfromtemplate-dialog.html',
                    model: createFromTemplateDialogModel,
                    button1click: function () {
                        dialogButton1Click({
                            requestType: 'fornya',
                            requestData: intygCreateFromTemplateRequest,
                            requestFn: _createFromTemplateDraft,
                            viewState: viewState,
                            dialogModel: createFromTemplateDialogModel,
                            closeDialog: function (result) {
                                createDialog.close(result);
                            }
                        });
                    },
                    button2click: function (modalInstance) {
                        modalInstance.close();
                    },
                    button1text: createFromTemplateDialogModel.confirmCreateButtonMessageKey,
                    button2text: 'common.createfromtemplate.cancel',
                    bodyText: intygCreateFromTemplateRequest.intygType + '.createfromtemplate.' + newIntygType + '.modal.text',
                    autoClose: false
                });

                createDialog.opened.then(function() {
                    createDialog.isOpen = true;
                }, function() {
                    createDialog.isOpen = false;
                });

                createDialog.result.then(
                    hideCreateFromTemplateDialogError,
                    hideCreateFromTemplateDialogError
                );

                return createDialog;
            }

            function _createFornyaDraft(intygFornyaRequest, onSuccess, onError) {
                IntygProxy.fornyaIntyg(intygFornyaRequest, function(data) {
                    $log.debug('Successfully requested fornyad draft');
                    if(onSuccess) {
                        onSuccess(data);
                    }
                }, function(error) {
                    $log.debug('Create fornyad draft failed: ' + error.message);
                    if (onError) {
                        onError(error.errorCode);
                    }
                });
            }

            function _createErsattDraft(intygErsattRequest, onSuccess, onError) {
                IntygProxy.ersattIntyg(intygErsattRequest, function(data) {
                    $log.debug('Successfully requested ersatt copy draft');
                    if(onSuccess) {
                        onSuccess(data);
                    }
                }, function(error) {
                    $log.debug('Create ersatt copy failed: ' + error.message);
                    if (onError) {
                        onError(error.errorCode);
                    }
                });
            }

            function _createFromTemplateDraft(intygCreateFromTemplateRequest, onSuccess, onError) {
                IntygProxy.create(intygCreateFromTemplateRequest, function(data) {
                    $log.debug('Successfully requested create draft of new intyg type');
                    if(onSuccess) {
                        onSuccess(data);
                    }
                }, function(error) {
                    $log.debug('Create draft of new intyg type failed: ' + error.message);
                    if (onError) {
                        onError(error.errorCode);
                    }
                });
            }

            // Return public API for the service
            return {
                FORNYA_DIALOG_PREFERENCE: _FORNYA_DIALOG_PREFERENCE,
                fornya: _fornya,
                ersatt: _ersatt,
                createFromTemplate: _createFromTemplate,
                __test__: {
                    createFornyaDraft: _createFornyaDraft
                }
            };
        }]);
