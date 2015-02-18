/**
 * Common certificate management methods between certificate modules
 */
angular.module('common').factory('common.ManageCertView',
    ['$rootScope', '$document', '$log', '$location', '$route', '$routeParams', '$timeout', '$window',
        'common.CertificateService',
        'common.dialogService', 'common.messageService', 'common.statService', 'common.User', 'common.featureService',
        function($rootScope, $document, $log, $location, $route, $routeParams, $timeout, $window, CertificateService,
            dialogService,
            messageService, statService, User, featureService) {
            'use strict';

            /**
             * Load draft to webcert
             * @param $scope
             * @private
             */
            function _load($scope, intygsTyp, onSuccess) {
                $scope.widgetState.doneLoading = false;
                CertificateService.getDraft($routeParams.certificateId, intygsTyp, function(data) {
                    $scope.widgetState.doneLoading = true;
                    $scope.widgetState.activeErrorMessageKey = null;
                    $scope.cert = data.content;
                    $scope.certMeta.intygId = data.content.id;
                    $scope.certMeta.vidarebefordrad = data.vidarebefordrad;
                    $scope.isSigned = data.status === 'SIGNED';
                    $scope.isComplete = $scope.isSigned || data.status === 'DRAFT_COMPLETE';
                    if (onSuccess !== undefined) {
                        onSuccess(data.content);
                    }
                }, function(error) {
                    $scope.widgetState.doneLoading = true;
                    $scope.widgetState.activeErrorMessageKey = checkSetError(error.errorCode);
                });
            }

            /**
             * Save draft to webcert
             * @param $scope
             * @param intygsTyp
             * @private
             */
            function _save($scope, intygsTyp, autoSave) {
                if (autoSave && CertificateService.isSaveDraftInProgress()) {
                    return false;
                }
                $scope.certForm.$setPristine();
                CertificateService.saveDraft($routeParams.certificateId, intygsTyp, $scope.cert, autoSave,
                    function(data) {

                        $scope.widgetState.saveErrorMessageKey = null;

                        if (!autoSave) {
                            $scope.validationMessagesGrouped = {};
                            $scope.validationMessages = [];
                        }

                        if (data.status === 'COMPLETE') {
                            $scope.isComplete = true;
                        } else {
                            $scope.isComplete = false;
                            if (!autoSave) {
                                $scope.validationMessages = data.messages;

                                angular.forEach(data.messages, function(message) {
                                    var field = message.field;
                                    var parts = field.split('.');
                                    var section;
                                    if (parts.length > 0) {
                                        section = parts[0].toLowerCase();

                                        if ($scope.validationMessagesGrouped[section]) {
                                            $scope.validationMessagesGrouped[section].push(message);
                                        } else {
                                            $scope.validationMessagesGrouped[section] = [message];
                                        }
                                    }
                                });
                            }
                        }
                    }, function(error) {
                        $scope.certForm.$setDirty();
                        // Show error message if save fails
                        $scope.widgetState.saveErrorMessageKey = checkSetErrorSave(error.errorCode);
                    }
                );
                return true;
            }

            /**
             * Discard a certificate draft
             */
            function _discard($scope, intygsTyp) {

                var bodyText = 'När du raderar utkastet tas det bort från Webcert.';
                $scope.dialog = {
                    acceptprogressdone: false,
                    errormessageid: 'Error',
                    showerror: false
                };

                var draftDeleteDialog = {};
                draftDeleteDialog = dialogService.showDialog($scope, {
                    dialogId: 'confirm-draft-delete',
                    titleId: 'common.modal.label.discard_draft',
                    bodyText: bodyText,
                    button1id: 'confirm-draft-delete-button',

                    button1click: function() {
                        $log.debug('delete draft ');
                        $scope.dialog.acceptprogressdone = false;
                        CertificateService.discardDraft($routeParams.certificateId, intygsTyp, function() {
                            $scope.dialog.acceptprogressdone = true;
                            statService.refreshStat(); // Update statistics to reflect change

                            if (featureService.isFeatureActive('franJournalsystem')) {
                                $rootScope.$broadcast('intyg.deleted', $routeParams.certificateId);
                            } else {
                                $location.path('/unsigned');
                            }
                            draftDeleteDialog.close();
                        }, function(error) {
                            $scope.dialog.acceptprogressdone = true;
                            if (error.errorCode === 'DATA_NOT_FOUND') { // Godtagbart, intyget var redan borta.
                                statService.refreshStat(); // Update statistics to reflect change
                                draftDeleteDialog.close();
                                $location.path('/unsigned');
                            } else {
                                $scope.dialog.showerror = true;
                                if (error === '') {
                                    $scope.dialog.errormessageid = 'common.error.cantconnect';
                                } else {
                                    $scope.dialog.errormessageid =
                                        ('error.message.' + error.errorCode).toLowerCase();
                                }
                            }
                        });
                    },
                    button1text: 'common.delete',
                    button2text: 'common.cancel',
                    autoClose: false
                });
            }

            function checkSetError(errorCode) {
                var model = 'common.error.unknown';
                if (errorCode !== undefined && errorCode !== null) {
                    model = ('common.error.' + errorCode).toLowerCase();
                }

                return model;
            }

            function checkSetErrorSave(errorCode) {
                var model = 'common.error.save.unknown';
                if (errorCode !== undefined && errorCode !== null) {
                    model = ('common.error.save.' + errorCode).toLowerCase();
                }

                return model;
            }

            function signera($scope, intygsTyp) {
                if (User.userContext.authenticationScheme === 'urn:inera:webcert:fake') {
                    return _signeraServer($scope, intygsTyp, $routeParams.certificateId);
                } else {
                    return _signeraKlient($scope, intygsTyp, $routeParams.certificateId);
                }
            }

            function _signeraServer($scope, intygsTyp, intygsId) {
                var bodyText = 'Är du säker på att du vill signera intyget?';
                var confirmDialog = dialogService.showDialog($scope, {
                    dialogId: 'confirm-sign',
                    titleId: 'common.modal.label.confirm_sign',
                    bodyText: bodyText,
                    autoClose: false,
                    button1id: 'confirm-signera-utkast-button',

                    button1click: function() {
                        _confirmSignera($scope, intygsTyp, intygsId, confirmDialog);
                    },
                    button1text: 'common.sign',
                    button2click: function() {
                        if ($scope._timer) {
                            $timeout.cancel($scope._timer);
                        }
                        $scope.dialog.acceptprogressdone = true;
                    },
                    button2text: 'common.cancel'
                });
            }

            function _signeraKlient($scope, intygsTyp, intygsId) {
                    $scope.signingWithSITHSInProgress = true;
                    CertificateService.getSigneringshash(intygsId, intygsTyp, function(ticket) {
                    _openNetIdPlugin(ticket.hash, function(signatur) {
                        CertificateService.signeraUtkastWithSignatur(ticket.id, intygsTyp, signatur, function(ticket) {

                            if (ticket.status === 'SIGNERAD') {
                                _showIntygAfterSignering($scope, intygsTyp, intygsId);
                            } else {
                                _waitForSigneringsstatusSigneradAndClose($scope, intygsTyp, intygsId, ticket);
                            }

                        }, function(error) {
                            _showSigneringsError($scope, error);
                        });
                    }, function(error) {
                        _showSigneringsError($scope, error);
                    });
                }, function(error) {
                    _showSigneringsError($scope, error);
                });
            }

            function _confirmSignera($scope, intygsTyp, intygsId, confirmDialog) {
                $scope.dialog.acceptprogressdone = false;
                $scope.dialog.showerror = false;
                CertificateService.signeraUtkast(intygsId, intygsTyp, function(ticket) {
                    _waitForSigneringsstatusSigneradAndClose($scope, intygsTyp, intygsId, ticket,
                        confirmDialog);
                }, function(error) {
                    _showSigneringsError($scope, error);
                });
            }

            function _openNetIdPlugin(hash, onSuccess, onError) {

                iid_SetProperty('Base64', 'true');
                iid_SetProperty('DataToBeSigned', hash);
                iid_SetProperty('URLEncode', 'false');
                var resultCode = iid_Invoke('Sign');

                if (resultCode === 0) {
                    onSuccess(iid_GetProperty('Signature'));
                } else {
                    var message = 'Signeringen avbröts med kod: ' + resultCode;
                    $log.info(message);
                    onError({ errorCode: 'SIGN_NETID_ERROR'});
                }
            }

            function _waitForSigneringsstatusSigneradAndClose($scope, intygsTyp, intygsId, ticket, dialog) {

                function getSigneringsstatus() {
                    CertificateService.getSigneringsstatus(ticket.id, intygsTyp, function(ticket) {
                        if ('BEARBETAR' === ticket.status) {
                            $scope._timer = $timeout(getSigneringsstatus, 1000);
                        } else if ('SIGNERAD' === ticket.status) {
                            _showIntygAfterSignering($scope, intygsTyp, intygsId, dialog);
                        } else {
                            _showSigneringsError($scope, {errorCode: 'SIGNERROR'});
                        }
                    });
                }

                getSigneringsstatus();
            }

            function _showIntygAfterSignering($scope, intygsTyp, intygsId, dialog) {
                if (dialog) {
                    dialog.close();
                }
                $scope.signingWithSITHSInProgress = false;

                $location.replace();
                $location.path('/intyg/' + intygsTyp + '/' + intygsId);
                statService.refreshStat();
            }

            function _setErrorMessageId(error) {
                var messageId = '';

                if (error === undefined) {
                    $log.debug('_setErrorMessageId: Error is not defined.');
                    messageId = 'common.error.signerror';
                }
                else {
                    if (error.errorCode === 'DATA_NOT_FOUND') {
                        messageId = 'common.error.certificatenotfound';
                    } else if (error.errorCode === 'INVALID_STATE') {
                        messageId = 'common.error.certificateinvalidstate';
                    } else if (error.errorCode === 'SIGN_NETID_ERROR') {
                        messageId = 'common.error.signerrornetid';
                    } else if (error === '') {
                        messageId = 'common.error.cantconnect';
                    } else {
                        messageId = 'common.error.signerror';
                    }
                }
                return messageId;
            }

            function _showSigneringsError($scope, error) {
                if ($scope.dialog) {
                    $scope.dialog.acceptprogressdone = true;
                    $scope.dialog.showerror = true;
                    $scope.dialog.errormessageid = _setErrorMessageId(error);
                } else {
                    var sithssignerrormessageid = _setErrorMessageId(error);
                    var errorMessage = messageService.getProperty(sithssignerrormessageid, null, sithssignerrormessageid);
                    dialogService.showErrorMessageDialog(errorMessage);
                    $scope.signingWithSITHSInProgress = false;
                }
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

            function _printDraft(intygsId, intygsTyp) {
                $window.print();
                CertificateService.logPrint(intygsId, intygsTyp, function(data) {
                    $log.debug('_logPrint, success: ' + data);
                });
            }


            // Return public API for the service
            return {
                load: _load,
                save: _save,
                discard: _discard,
                signera: signera,
                isRevoked: _isRevoked,
                isSentToTarget: _isSentToTarget,
                printDraft: _printDraft,

                __test__: {
                    confirmSignera: _confirmSignera
                }
            };
        }]);
