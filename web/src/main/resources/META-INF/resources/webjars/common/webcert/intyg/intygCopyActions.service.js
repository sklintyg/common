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

angular.module('common').factory('common.IntygCopyActions',
    [ '$log', '$stateParams',
        'common.dialogService', 'common.IntygProxy', 'common.authorityService', 'common.UserModel', 'common.User', 'common.IntygHelper', 'common.PersonIdValidatorService',
        function($log, $stateParams, dialogService, IntygProxy, authorityService, UserModel, userService, IntygHelper, PersonIdValidatorService) {
            'use strict';

            var _COPY_DIALOG_PREFERENCE = 'wc.dontShowCopyDialog';
            var _FORNYA_DIALOG_PREFERENCE = 'wc.dontShowFornyaDialog';
            var copyDialogModel = {
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
            var fornyaDialogModel = angular.copy(copyDialogModel);
            fornyaDialogModel.errormessageid = 'error.failedtofornyaintyg';

            var ersattDialogModel = angular.copy(copyDialogModel);
            ersattDialogModel.errormessageid = 'error.failedtoersattintyg';


            function resetViewStateErrorKeys (viewState) {
                viewState.activeErrorMessageKey = null;
                viewState.inlineErrorMessageKey = null;
            }

            function hideCopyDialogError () {
                copyDialogModel.showerror = null;
            }

            function hideFornyaDialogError () {
                fornyaDialogModel.showerror = null;
            }
            
            function hideErsattDialogError () {
                ersattDialogModel.showerror = null;
            }

            function dialogButton1Click (options) {
                var requestType = options.requestType;
                var requestData = options.requestData;
                var requestFn = options.requestFn;
                var viewState = options.viewState;
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
                    if(viewState && viewState.inlineErrorMessageKey) {
                        viewState.inlineErrorMessageKey = null;
                    }

                    var end = function() {
                        IntygHelper.goToDraft(draftResponse.intygsTyp, draftResponse.intygsUtkastId);
                    };

                    closeDialog({direct:end});

                }, function(errorCode) {
                    if (errorCode === 'DATA_NOT_FOUND') {
                        dialogModel.errormessageid = 'error.failedto' + requestType + 'intyg.personidnotfound';
                    }
                    else {
                        dialogModel.errormessageid = 'error.failedto' + requestType + 'intyg';
                    }
                    dialogModel.acceptprogressdone = true;
                    dialogModel.showerror = true;
                });
            }

            function _copyMessageKey(intygsTyp) {
                return (intygsTyp && intygsTyp.toLowerCase() === 'fk7263' ? intygsTyp : 'common')  + '.modal.copy.text';
            }

            function _copy(viewState, intygCopyRequest, isOtherCareUnit) {
                var copyDialog;
                // Create preference and model representative
                copyDialogModel.dontShowInfo = false;

                if (UserModel.getAnvandarPreference(_COPY_DIALOG_PREFERENCE) === undefined) {
                    UserModel.setAnvandarPreference(_COPY_DIALOG_PREFERENCE, copyDialogModel.dontShowInfo);
                }


                if (UserModel.getAnvandarPreference(_COPY_DIALOG_PREFERENCE) === true || UserModel.getAnvandarPreference(_COPY_DIALOG_PREFERENCE) === 'true') {
                    $log.debug('copy intyg without dialog' + intygCopyRequest);
                    resetViewStateErrorKeys(viewState);
                    _createCopyDraft(intygCopyRequest, function(draftResponse) {
                        IntygHelper.goToDraft(draftResponse.intygsTyp, draftResponse.intygsUtkastId);
                    }, function(errorCode) {
                        if (errorCode === 'DATA_NOT_FOUND') {
                            viewState.inlineErrorMessageKey = 'error.failedtocopyintyg.personidnotfound';
                        }
                        else {
                            viewState.inlineErrorMessageKey = 'error.failedtocopyintyg';
                        }
                    });
                } else {

                    copyDialogModel.otherCareUnit = isOtherCareUnit;
                    copyDialogModel.patientId = PersonIdValidatorService.validate($stateParams.patientId);
                    copyDialogModel.deepIntegration = !authorityService.isAuthorityActive({authority: 'HANTERA_PERSONUPPGIFTER'});
                    copyDialogModel.intygTyp = intygCopyRequest.intygType;

                    copyDialog = dialogService.showDialog({
                        dialogId: 'copy-dialog',
                        titleId: 'label.copyintyg',
                        templateUrl: '/app/partials/copy-dialog.html',
                        model: copyDialogModel,
                        button1click: function () {
                            dialogButton1Click({
                                requestType: 'copy',
                                requestData: intygCopyRequest,
                                requestFn: _createCopyDraft,
                                viewState: viewState,
                                dialogModel: copyDialogModel,
                                dialogPreferenceKey: _COPY_DIALOG_PREFERENCE,
                                closeDialog: function (result) {
                                    copyDialog.close(result);
                                }
                            });
                        },
                        button2click: function(modalInstance){
                            modalInstance.close();
                        },
                        button1text: 'common.copy',
                        button2text: 'common.cancel',
                        bodyText: _copyMessageKey(copyDialogModel.intygTyp),
                        autoClose: false
                    });

                    copyDialog.opened.then(function() {
                        copyDialogModel.isOpen = true;
                    }, function() {
                        copyDialogModel.isOpen = false;
                    });

                    copyDialog.result.then(
                        hideCopyDialogError,
                        hideCopyDialogError
                    );

                    return copyDialog;
                }

                return null;
            }

            function _fornyaMessageKey(intygsTyp) {
                return (intygsTyp && intygsTyp.toLowerCase() === 'fk7263' ? intygsTyp : 'common')  + '.modal.fornya.text';
            }

            function _fornya(viewState, intygFornyaRequest, isOtherCareUnit) {
                var fornyaDialog;
                // Create preference and model representative
                fornyaDialogModel.dontShowInfo = false;

                if (UserModel.getAnvandarPreference(_FORNYA_DIALOG_PREFERENCE) === undefined) {
                    UserModel.setAnvandarPreference(_FORNYA_DIALOG_PREFERENCE, fornyaDialogModel.dontShowInfo);
                }

                if (UserModel.getAnvandarPreference(_FORNYA_DIALOG_PREFERENCE) === true || UserModel.getAnvandarPreference(_FORNYA_DIALOG_PREFERENCE) === 'true') {
                    $log.debug('copy intyg without dialog' + intygFornyaRequest);
                    resetViewStateErrorKeys(viewState);
                    _createFornyaDraft(intygFornyaRequest, function(draftResponse) {
                        IntygHelper.goToDraft(draftResponse.intygsTyp, draftResponse.intygsUtkastId);
                    }, function(errorCode) {
                        if (errorCode === 'DATA_NOT_FOUND') {
                            viewState.inlineErrorMessageKey = 'error.failedtofornyaintyg.personidnotfound';
                        }
                        else {
                            viewState.inlineErrorMessageKey = 'error.failedtofornyaintyg';
                        }
                    });
                } else {

                    fornyaDialogModel.otherCareUnit = isOtherCareUnit;
                    fornyaDialogModel.patientId = PersonIdValidatorService.validate($stateParams.patientId);
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
                        button2click: function(modalInstance){
                            modalInstance.close();
                        },
                        button1text: 'common.ersatt',
                        button2text: 'common.ersatt.cancel',
                        bodyText: ersattDialogModel.intygTyp + '.modal.ersatt.text',
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

            function _createCopyDraft(intygCopyRequest, onSuccess, onError) {
                IntygProxy.copyIntyg(intygCopyRequest, function(data) {
                    $log.debug('Successfully requested copy draft');
                    if(onSuccess) {
                        onSuccess(data);
                    }
                }, function(error) {
                    $log.debug('Create copy failed: ' + error.message);
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

            // Return public API for the service
            return {
                COPY_DIALOG_PREFERENCE: _COPY_DIALOG_PREFERENCE,
                FORNYA_DIALOG_PREFERENCE: _FORNYA_DIALOG_PREFERENCE,
                copy: _copy,
                fornya: _fornya,
                ersatt: _ersatt,

                __test__: {
                    createCopyDraft: _createCopyDraft
                }
            };
        }]);
