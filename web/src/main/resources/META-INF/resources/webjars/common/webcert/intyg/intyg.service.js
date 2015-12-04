angular.module('common').factory('common.IntygService',
    [ '$log', '$timeout', '$location', '$cookies', '$stateParams', 'common.dialogService', 'common.IntygProxy', 'common.authorityService',
        function($log, $timeout, $location, $cookies, $stateParams, dialogService, IntygProxy, authorityService) {
            'use strict';

            var _COPY_DIALOG_COOKIE = 'wc.dontShowCopyDialog';
            var copyDialogModel = {
                isOpen: false
            };

            function _initCopyDialog() {
                copyDialogModel = {
                    isOpen: false,
                    errormessageid: 'error.failedtocopyintyg'
                };
            }

            function _copy(viewState, intygCopyRequest, isOtherCareUnit) {

                _initCopyDialog();

                function goToDraft(type, intygId) {

                    /**
                     * IMPORTANT TIMEOUT. Since location doesn't change anything in the view, apply or digest is not called with just the call to location.path.
                     * Therefore we need a call to angular timeout to force a digest and let angular change the path correctly.
                     */
                    $timeout(function() {
                        // anything you want can go here and will safely be run on the next digest.
                        $location.path('/' + type + '/edit/' + intygId, true);
                    });
                }

                // Create cookie and model representative
                copyDialogModel.dontShowCopyInfo = false;

                if($cookies.getObject(_COPY_DIALOG_COOKIE) === undefined) {
                    $cookies.putObject(_COPY_DIALOG_COOKIE, copyDialogModel.dontShowCopyInfo);
                }

                if ($cookies.getObject(_COPY_DIALOG_COOKIE)) {
                    $log.debug('copy cert without dialog' + intygCopyRequest);
                    viewState.activeErrorMessageKey = null;
                    viewState.inlineErrorMessageKey = null;
                    _createCopyDraft(intygCopyRequest, function(draftResponse) {
                        goToDraft(draftResponse.intygsTyp, draftResponse.intygsUtkastId);
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
                    copyDialogModel.patientId = $stateParams.patientId;
                    copyDialogModel.deepIntegration = !authorityService.isAuthorityActive({authority: 'HANTERA_PERSONUPPGIFTER'});
                    copyDialogModel.intygTyp = intygCopyRequest.intygType;

                    var copyDialog = dialogService.showDialog({
                        dialogId: 'copy-dialog',
                        titleId: 'label.copycert',
                        templateUrl: '/app/partials/copy-dialog.html',
                        model: copyDialogModel,
                        button1click: function() {
                            $log.debug('copy cert from dialog' + intygCopyRequest);
                            if (copyDialogModel.dontShowCopyInfo) {
                                $cookies.putObject(_COPY_DIALOG_COOKIE, copyDialogModel.dontShowCopyInfo);
                            }

                            copyDialogModel.showerror = false;
                            copyDialogModel.acceptprogressdone = false;
                            _createCopyDraft(intygCopyRequest, function(draftResponse) {
                                copyDialogModel.acceptprogressdone = true;
                                if(viewState && viewState.inlineErrorMessageKey) {
                                    viewState.inlineErrorMessageKey = null;
                                }
                                var end = function() {
                                    goToDraft(draftResponse.intygsTyp, draftResponse.intygsUtkastId);
                                };
                                copyDialog.close({direct:end});

                            }, function(errorCode) {
                                if (errorCode === 'DATA_NOT_FOUND') {
                                    copyDialogModel.errormessageid = 'error.failedtocopyintyg.personidnotfound';
                                }
                                else {
                                    copyDialogModel.errormessageid = 'error.failedtocopyintyg';
                                }
                                copyDialogModel.acceptprogressdone = true;
                                copyDialogModel.showerror = true;
                            });
                        },
                        button1text: 'common.copy',
                        button2text: 'common.cancel',
                        autoClose: false
                    });

                    copyDialog.opened.then(function() {
                        copyDialogModel.isOpen = true;
                    }, function() {
                        copyDialogModel.isOpen = false;
                    });
                    return copyDialog;
                }

                return null;
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
                    $log.debug('Send cert failed: ' + error);
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
                    templateUrl: '/web/webjars/common/webcert/intyg/intyg.send.dialog.html',
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

            // Makulera dialog setup
            var makuleraDialog = {
                isOpen: false
            };

            function _revokeSigneratIntyg(cert, dialogModel, makuleraDialog, onSuccess) {
                dialogModel.showerror = false;
                dialogModel.acceptprogressdone = false;
                IntygProxy.makuleraIntyg(cert.id, cert.intygType, function() {
                    dialogModel.acceptprogressdone = true;
                    makuleraDialog.close();
                    onSuccess();
                }, function(error) {
                    $log.debug('Revoke failed: ' + error);
                    dialogModel.acceptprogressdone = true;
                    dialogModel.showerror = true;
                });
            }

            function _makulera( cert, confirmationMessage, onSuccess) {

                var dialogMakuleraModel = {
                    acceptprogressdone: true,
                    focus: false,
                    errormessageid: 'error.failedtomakuleraintyg',
                    showerror: false
                };

                var successCallback = function() {
                    dialogService.showMessageDialog('label.makulera.confirmation', confirmationMessage,
                        function() {
                            onSuccess();
                        });
                };

                makuleraDialog = dialogService.showDialog({
                    dialogId: 'makulera-dialog',
                    titleId: 'label.makulera',
                    templateUrl: '/app/partials/makulera-dialog.html',
                    model: dialogMakuleraModel,
                    button1click: function() {
                        $log.debug('revoking cert from dialog' + cert);
                        _revokeSigneratIntyg(cert, dialogMakuleraModel, makuleraDialog, successCallback);
                    },

                    button1text: 'common.revoke',
                    button1id: 'button1makulera-dialog',
                    button2text: 'common.cancel',
                    bodyTextId: 'label.makulera.body',
                    autoClose: false
                });

                return makuleraDialog;
            }

            function _isSentToTarget(statusArr, target) {
                if (statusArr) {
                    for (var i = 0; i < statusArr.length; i++) {
                        if (statusArr[i].target === target && statusArr[i].type === 'SENT') {
                            return true;
                        }
                    }
                }
                return false;
            }

            function _isRevoked(statusArr) {
                if (statusArr) {
                    for (var i = 0; i < statusArr.length; i++) {
                        if (statusArr[i].type === 'CANCELLED') {
                            return true;
                        }
                    }
                }
                return false;
            }

             // Return public API for the service
            return {
                makulera: _makulera,
                send: _send,
                COPY_DIALOG_COOKIE: _COPY_DIALOG_COOKIE,
                copy: _copy,
                isRevoked: _isRevoked,
                isSentToTarget: _isSentToTarget,

                __test__: {
                    createCopyDraft: _createCopyDraft
                }
            };
        }]);
